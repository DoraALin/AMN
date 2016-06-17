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
@Transactional
public class ArtifactManifestRepositoryImpl implements ArtifactManifestArtifactRelation{

    @PersistenceContext
    EntityManager em;

    @Override
    public void addArtifact(ArtifactManifest artManifest, Artifact art) {
        ArtifactManifestArtifact ama = artManifest.addArtifact(art);
        em.persist(ama);
    }

    @Override
    public void removeArtifact(ArtifactManifest artManifest, Artifact art) {
        ArtifactManifestArtifact ama = artManifest.removeArtifact(art);
        em.merge(ama);
        em.remove(ama);
    }


}
