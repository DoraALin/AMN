package me.lin.amn.repository.model.artifact;

import me.lin.amn.common.logging.TracingConfig;
import me.lin.amn.repository.ServerConfig;
import me.lin.amn.repository.model.Artifact;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.io.InputStream;
import java.sql.Blob;

/**
 * Created by Lin on 6/20/16.
 */
@Component
@Scope("prototype")
public class ArtifactBuilder {

    @Autowired
    private EntityManagerFactory emf;

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
        ApplicationContext context =
                new AnnotationConfigApplicationContext(TracingConfig.class, ServerConfig.class);
        EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
        Session session = (Session) emf.createEntityManager().getDelegate();
        Blob blob = session.getLobHelper().createBlob(is, len);
        artifact.setArtifact(blob);
        return this;
    }

    public Artifact build() {
        return this.artifact;
    }
}
