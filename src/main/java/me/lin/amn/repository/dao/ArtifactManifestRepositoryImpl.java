package me.lin.amn.repository.dao;

import me.lin.amn.repository.model.Artifact;
import me.lin.amn.repository.model.ArtifactManifest;
import me.lin.amn.repository.model.ArtifactManifestArtifact;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Lin on 6/16/16.
 */
@Repository
public class ArtifactManifestRepositoryImpl implements ArtifactManifestArtifactRelation{
    EntityManager em;

    @PersistenceContext
    void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void addArtifact(ArtifactManifest artManifest, Artifact art) {
        ArtifactManifestArtifact ama = artManifest.addArtifact(art);
        em.persist(ama);
//        em.merge(artManifest);
    }

    @Override
    @Transactional
    public void removeArtifact(ArtifactManifest artManifest, Artifact art) {
        ArtifactManifestArtifact ama = artManifest.removeArtifact(art);
        ama = em.merge(ama);
        em.remove(ama);
    }


}
