package me.lin.amn.repository.model;

import junit.framework.TestCase;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Logger;

/**
 * Created by Lin on 5/30/16.
 */
public class HibernateArtifactTest extends TestCase {
    private EntityManagerFactory entityManagerFactory;

    private static Logger LOG = Logger.getLogger(HibernateArtifactTest.class.getName());

    @Override
    protected void setUp() throws Exception {
        // like discussed with regards to SessionFactory, an EntityManagerFactory is set up once for an application
        // 		IMPORTANT: notice how the name here matches the name we gave the persistence-unit in persistence.xml!
        entityManagerFactory = Persistence.createEntityManagerFactory( "me.lin.amn.jpa" );
    }

    @Override
    protected void tearDown() throws Exception {
        entityManagerFactory.close();
    }

    private void commit(Session session) {
        LOG.info("Commit");
        session.getTransaction().commit();
        LOG.info("Committed");
    }

    private void commit(EntityManager entityMgr) {
        LOG.info("Commit");
        entityMgr.getTransaction().commit();
        LOG.info("Committed");
    }

    public void testBasicUsage() {
        // create a couple of events...
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Artifact art1 = new Artifact(null);
        Artifact art2 = new Artifact(null);
        entityManager.persist(art1);
        entityManager.persist(art2);

        // create artifact manifest
        LOG.info("create artifact manifest 1");
        ArtifactManifest am1 = new ArtifactManifest("test mainfest 1");
        LOG.info("create artifact manifest 2");
        ArtifactManifest am2 = new ArtifactManifest("test mainfest 2");
        entityManager.persist(am1);
        entityManager.persist(am2);

        LOG.info("add artifact 1 to artifact mainfest 1");
        am1.addArtifact(art1);
        LOG.info("add artifact 2 to artifact mainfest 1");
        am1.addArtifact(art2);
        LOG.info("remove artifact 1 to artifact mainfest 2");
        am2.addArtifact(art1);
        entityManager.flush();


        LOG.info("remove artifact 1 from artifact manifest 1");
        am1.removeArtifact(art1);
        LOG.info("Call flush");
        entityManager.flush();
        commit(entityManager);
        entityManager.close();
    }
}
