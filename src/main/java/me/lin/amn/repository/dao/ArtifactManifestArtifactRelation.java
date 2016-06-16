package me.lin.amn.repository.dao;

import me.lin.amn.repository.model.Artifact;
import me.lin.amn.repository.model.ArtifactManifest;

/**
 * Created by Lin on 6/16/16.
 */
public interface ArtifactManifestArtifactRelation {
    void addArtifact(ArtifactManifest artManifest, Artifact art);
    void removeArtifact(ArtifactManifest artManifest, Artifact art);
}
