package me.lin.amn.repository.model.artifact;

import me.lin.amn.repository.dao.ArtifactRepository;
import me.lin.amn.repository.model.Artifact;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Lin on 6/20/16.
 */
@Component
@Scope("prototype")
public class ArtifactUpload implements Uploadable, Runnable{

    private String fileName;
    private long len;
    private InputStream is;
    private Artifact artifact;


    private CyclicBarrier barrier;

    @Autowired
    private ArtifactRepository artRepo;

    /**
     * constructor for Spring
     */
    public ArtifactUpload(){

    }

    public ArtifactUpload(String fileName, long len, InputStream is, CyclicBarrier barrier){
        this.is = is;
        this.len = len;
        this.fileName = fileName;
        this.barrier = barrier;
    }

    /**
     * update artifact file info for uploading
     * @param fileName
     * @param len
     * @param is
     * @param barrier
     */
    @Override
    public void setFileInfo(String fileName, long len, InputStream is, CyclicBarrier barrier){
        this.is = is;
        this.len = len;
        this.fileName = fileName;
        this.barrier = barrier;
    }

    public void upload(){
        //build artifact out of inputStream
        ArtifactBuilder ab =  Artifact.create(fileName);
        ab.portArtifactFromInputStream(is, len);
        artifact = ab.build();
        //persist in repository
        artifact = artRepo.save(artifact);
    }

    public Artifact getArtifact() {
        return this.artifact;
    }

    @Override
    public void run() {
        try {
            upload();
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
