package me.lin.amn.repository.model.artifact;

import me.lin.amn.repository.dao.ArtifactRepository;
import me.lin.amn.repository.model.Artifact;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;

/**
 * Created by Lin on 6/20/16.
 */
public class ArtifactUpload implements Uploadable, Runnable{

    private String fileName;
    private long len;
    private InputStream is;

    @Autowired
    private ArtifactRepository artRepo;

    public ArtifactUpload(String fileName, long len, InputStream is){
        this.is = is;
        this.len = len;
        this.fileName = fileName;
    }

    public void upload() {

        //build artifact out of inputStream
        ArtifactBuilder ab =  Artifact.create(fileName);
        ab.portArtifactFromInputStream(is, len);
        Artifact artifact = ab.build();

        //persist in repository
        artifact = artRepo.save(artifact);
    }

    @Override
    public void run() {
        upload();
    }
}
