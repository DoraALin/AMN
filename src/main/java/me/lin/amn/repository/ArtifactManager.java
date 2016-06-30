package me.lin.amn.repository;

import me.lin.amn.repository.dao.interfaces.ArtifactRepository;
import me.lin.amn.repository.model.Artifact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Lin on 6/29/16.
 */
@Component
public class ArtifactManager {

    @Autowired
    private ArtifactRepository artRepo;

    public Artifact getArtifactById(String artifactID) {
        Artifact artifact = null;
        if(artRepo.exists(artifactID)){
            artifact = artRepo.findOne(artifactID);
//            artifact = artRepo.findArtifactWithContentById(artifactID);
        }
        return artifact;
    }

    public void getBinaryStream(Artifact artifact, OutputStream os) throws SQLException, IOException {
        artRepo.getBinaryStream(artifact, os);
    }

    public ArtifactRepository getArtifactRepository() {
        return artRepo;
    }
}
