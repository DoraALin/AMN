package me.lin.amn.repository.controller;

import me.lin.amn.repository.ArtifactManager;
import me.lin.amn.repository.dao.interfaces.ArtifactRepository;
import me.lin.amn.repository.model.Artifact;
import me.lin.amn.repository.model.artifact.ArtifactUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Lin on 6/20/16.
 */
@RestController
@RequestMapping("/artifacts")
public class ArtifactController {

    private static Log LOG = LogFactory.getLog(ArtifactController.class.getName());

    @Autowired
    private ServletFileUpload fileUpload;

    @Autowired
    private ArtifactManager artifactManager;

    @RequestMapping(value = "/one", method = GET )
    public void download(@RequestParam("artifactID") String artifactID, OutputStream os) {
        if((null == artifactID ||
                (null != artifactID && artifactID.isEmpty()))){
            return;
        }
        Artifact artifact =  artifactManager.getArtifactById(artifactID);
        try {
            artifactManager.getBinaryStream(artifact, os);
        } catch (SQLException e) {
            throw new ArtifactControllerException("Could not retrieve artifact with id: " + artifactID + '.', e);
        } catch (IOException e) {
            throw new ArtifactControllerException("Error open output stream.", e);
        }
    }


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
            ArtifactCollector collector = new ArtifactCollector(artifactUploadTasks,
                    artifactManager.getArtifactRepository(), LOCK);
            CyclicBarrier uploadBarrier = new CyclicBarrier(fileItems.size(), collector);
            //cached thread pool for multi-upload
            ExecutorService exec = Executors.newCachedThreadPool();
            //needs to be class, not interface
            for (int idx = 0; idx < fileItems.size(); idx++) {
                FileItem fi = fileItems.get(idx);
                ArtifactUpload uploadTask = new ArtifactUpload(fi.getName(), fi.getSize(), fi.getInputStream(), uploadBarrier, artifactManager.getArtifactRepository());
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
                LOG.error("artifact Controller Thread is interrupted while waiting for artifactIDCollector to finish.", e);
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
        //artifact in Future list for collecting ID
        private List<ArtifactUpload> artifactUploadTasks;
        private List<String> artifactIDs;
        private Object LOCK;

        ArtifactRepository artRepo;

        ArtifactCollector(List<ArtifactUpload> artifactUploadTasks, ArtifactRepository artRepo, Object LOCK){
            this.artifactUploadTasks = artifactUploadTasks;
            this.LOCK = LOCK;
            this.artRepo = artRepo;
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
                    exec.execute(()->{
                        Artifact art;
                        for (ArtifactUpload artifact : artifactUploadTasks) {
                            art = artifact.getArtifact();
                            artRepo.delete(art.getArtifactID());
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
                //wakeup artifact controller to continue
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

    class ArtifactControllerException extends RuntimeException {

        public ArtifactControllerException(String msg, Throwable nestedException){
            super(msg, nestedException);
        }
    }
}
