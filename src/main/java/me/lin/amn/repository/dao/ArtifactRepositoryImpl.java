package me.lin.amn.repository.dao;

import me.lin.amn.repository.dao.interfaces.IArtifactRepository;
import me.lin.amn.repository.model.Artifact;
import me.lin.amn.repository.model.Artifact_;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Lin on 6/30/16.
 */
@Repository
@Transactional
public class ArtifactRepositoryImpl implements IArtifactRepository {

    @PersistenceUnit
    @Autowired
    EntityManagerFactory emf;

    @Override
    public Artifact findArtifactWithContentById(String artifactId) {
        CriteriaBuilder builder = emf.getCriteriaBuilder();
        CriteriaQuery<Artifact> criteria = builder.createQuery(Artifact.class);
        Root<Artifact> artifacts = criteria.from(Artifact.class);
        criteria.select(artifacts);
        criteria.where(builder.equal(artifacts.get(Artifact_.artifactID), artifactId));
        EntityManager em = emf.createEntityManager();
        List<Artifact> cats = em.createQuery(criteria).getResultList();
        return cats.get(0);
    }

    @Override
    public void getBinaryStream(Artifact artifact, OutputStream os) throws SQLException, IOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        InputStream is;
        tx.begin();
        is = artifact.getArtifact().getBinaryStream();
        IOUtils.copy(is, os);
        tx.commit();
    }
}
