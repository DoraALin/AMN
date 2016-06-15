package me.lin.amn.repository.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * table mapping Artifacts_Manifest anf Artifacts table
 * Created by Lin on 5/30/16.
 */
@Entity(name="ArtifactManifestArtifact")
@Table(name = "ARTIFACTS_MANIFEST_ARTIFACTS")
public class ArtifactManifestArtifact implements Serializable{
    private ArtifactManifest artifactManifest;
    private Artifact artifact;

    public ArtifactManifestArtifact() {

    }

    public ArtifactManifestArtifact(ArtifactManifest manifest, Artifact artifact) {
        this.artifactManifest = manifest;
        this.artifact = artifact;
    }

    @ManyToOne
    @Id
    public ArtifactManifest getArtifactManifest() {
        return this.artifactManifest;
    }

    public void setArtifactManifest(ArtifactManifest manifest) {
        this.artifactManifest = manifest;
    }

    @ManyToOne
    @Id
    public Artifact getArtifact() {
        return this.artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        ArtifactManifestArtifact that = (ArtifactManifestArtifact) o;
        return Objects.equals( this.artifact, that.getArtifact() ) &&
                Objects.equals( this.getArtifactManifest(), that.getArtifactManifest() );
    }

    @Override
    public int hashCode() {
        return Objects.hash( this.artifactManifest, this.artifact );
    }
}
