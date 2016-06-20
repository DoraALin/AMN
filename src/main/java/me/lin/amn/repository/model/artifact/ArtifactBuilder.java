package me.lin.amn.repository.model.artifact;

import me.lin.amn.repository.model.Artifact;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.sql.Blob;

/**
 * Created by Lin on 6/20/16.
 */
public class ArtifactBuilder {

    @Autowired
    private EntityManager em;

    private Artifact artifact;

    private String fileName;

    public ArtifactBuilder(String fileName) {
        this.fileName = fileName;
        artifact = new Artifact();
    }

    public ArtifactBuilder setArtifactName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ArtifactBuilder portArtifactFromInputStream(InputStream is, long len) {
        Session session = (Session) em.getDelegate();
        Blob blob = session.getLobHelper().createBlob(is, len);
        artifact.setArtifact(blob);
        return this;
    }

    public Artifact build() {
        return this.artifact;
    }
}
