package me.lin.amn.repository.dao.interfaces;

import me.lin.amn.repository.model.Artifact;
import me.lin.amn.repository.model.ArtifactManifest;

/**
 * Created by Lin on 6/16/16.
 */
public interface IArtifactManifest {
    void addArtifact(ArtifactManifest artManifest, Artifact art);
    void removeArtifact(ArtifactManifest artManifest, Artifact art);
}
