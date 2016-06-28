package me.lin.amn.repository.controller;

import me.lin.amn.common.logging.TracingConfig;
import me.lin.amn.repository.ServerConfig;
import me.lin.amn.repository.dao.ArtifactRepository;
import me.lin.amn.repository.model.Artifact;
import me.lin.amn.repository.model.artifact.ArtifactUpload;
import me.lin.amn.repository.model.artifact.Uploadable;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Lin on 6/20/16.
 */
@RestController
@RequestMapping("/artifacts")
public class ArtifactController {

    private static Log LOG = LogFactory.getLog(ArtifactController.class.getName());

    private ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());

    @RequestMapping(value = "/new", method = POST )
    public List<String> upload(WebRequest req,
                               NativeWebRequest naReq,
                               RedirectAttributes redirectAttributes) {
        HttpServletRequest httpReq = naReq.getNativeRequest(HttpServletRequest.class);
        FileItemIterator iter = null;
        final Object LOCK = new Object();
        List<String> artifactIDs = null;
        try {
            List<FileItem> fileItems = fileUpload.parseRequest(httpReq);
            //list for persist artifact
            List<ArtifactUpload> artifactUploadTasks = new ArrayList(fileItems.size());
            ArtifactCollector collector = new ArtifactCollector(artifactUploadTasks, LOCK);
            CyclicBarrier uploadBarrier = new CyclicBarrier(fileItems.size(), collector);
            //cached thread pool for multi-upload
            ExecutorService exec = Executors.newCachedThreadPool();
            //needs to be class, not interface
            ApplicationContext context =
                    new AnnotationConfigApplicationContext(ServerConfig.class, TracingConfig.class);
            for (int idx = 0; idx < fileItems.size(); idx++) {
                FileItem fi = fileItems.get(idx);
                ArtifactUpload uploadTask = (ArtifactUpload) context.getBean(Uploadable.class);
                uploadTask.setFileInfo(fi.getName(), fi.getSize(), fi.getInputStream(), uploadBarrier);
                exec.submit(uploadTask);
                artifactUploadTasks.add(uploadTask);
            }
            exec.shutdown();
            try {
                //wait for artifactIDCollector to finish
                synchronized (LOCK) {
                    if (!collector.isUploadFinish())
                        LOCK.wait();
                }
            } catch (InterruptedException e) {
                LOG.error("Artifact Controller Thread is interrupted while waiting for artifactIDCollector to finish.", e);
                collector.clearUpload();
                artifactIDs = new ArrayList<>(0);
            }
            artifactIDs = collector.getArtifactIDs();
        } catch (FileUploadException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        }
        return artifactIDs;
    }

    class ArtifactCollector implements Runnable {

        private boolean uploadFinish = false;
        private boolean clearUpload = false;
        //Artifact in Future list for collecting ID
        private List<ArtifactUpload> artifactUploadTasks;
        private List<String> artifactIDs;
        private Object LOCK;

        ArtifactRepository artRepo;

        ArtifactCollector(List<ArtifactUpload> artifactUploadTasks, Object LOCK){
            this.artifactUploadTasks = artifactUploadTasks;
            this.LOCK = LOCK;
            ApplicationContext context =
                    new AnnotationConfigApplicationContext(ServerConfig.class, TracingConfig.class);
            artRepo = context.getBean(ArtifactRepository.class);
        }

        public boolean isUploadFinish(){
            return uploadFinish;
        }

        public final List<String> getArtifactIDs() {
            return artifactIDs;
        }

        /**
         * clear artifacts in ArtifactUpload
         */
        public void clearUpload(){
            synchronized (LOCK) {
                //clear upload only when upload not finish
                clearUpload = true;
                if(isUploadFinish()) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    exec.execute(new Runnable() {
                        @Override
                        public void run() {
                            Artifact art;
                            for (ArtifactUpload artifact : artifactUploadTasks) {
                                art = artifact.getArtifact();
                                artRepo.delete(art.getArtifactID());
                            }
                        }
                    });
                    exec.shutdown();
                }
            }
        }

        @Override
        public void run() {
            try {
                Artifact art;
                artifactIDs = new ArrayList(this.artifactUploadTasks.size());
                for (ArtifactUpload artifact : this.artifactUploadTasks) {
                    art = artifact.getArtifact();
                    artifactIDs.add(art.getArtifactID());
                }
                //wakeup Artifact controller to continue
                synchronized (LOCK) {
                    uploadFinish = true;
                    if(clearUpload == Boolean.TRUE){
                        //this happen when ArtifactController is interruptted during LOCK.wait()
                        for (ArtifactUpload artifact : this.artifactUploadTasks) {
                            art = artifact.getArtifact();
                            artRepo.delete(art.getArtifactID());
                        }
                    }
                    LOCK.notify();
                }
            }finally {

            }
        }
    }
}
