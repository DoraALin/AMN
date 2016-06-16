package me.lin.amn.repository.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Lin on 5/30/16.
 */
@Entity(name="ArtifactManifest")
@Table(name = "ARTIFACTS_MANIFEST")
public class ArtifactManifest implements Serializable{
    private String manifestID;      //guid for asset, user is able to specify guid for variable value
    private Date lastModified;      //last modified
    private String manifest;        //artifact manifest


    private List<ArtifactManifestArtifact> artifacts = new ArrayList<>();

    public ArtifactManifest() {

    }

    public ArtifactManifest(String manifest) {
        this.manifest = manifest;
    }

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",
            strategy = "me.lin.amn.repository.model.AMIdentifierGenerator")
    @Column(name="MANIFEST_ID")
    @Id
    public String getManifestID() {
        return manifestID;
    }

    public void setManifestID(String manifest_id) {
        this.manifestID = manifest_id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getManifest() {
        return this.manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    @OneToMany(mappedBy = "artifactManifest", cascade = CascadeType.ALL/*, orphanRemoval = true*/)
    public List<ArtifactManifestArtifact> getArtifacts() {
        return this.artifacts;
    }

    public void setArtifacts(List<ArtifactManifestArtifact> artifacts) {
        this.artifacts = artifacts;
    }

    public ArtifactManifestArtifact addArtifact(Artifact artifact) {
        ArtifactManifestArtifact ama = new ArtifactManifestArtifact(this, artifact);
        artifacts.add(ama);
        artifact.getArtifactManifest().add(ama);
        return ama;
    }

    public ArtifactManifestArtifact removeArtifact(Artifact artifact) {
        ArtifactManifestArtifact ama = new ArtifactManifestArtifact(this, artifact);
        artifact.getArtifactManifest().remove(ama);
        artifacts.remove(ama);
        return ama;
//        ama.setArtifactManifest(null);
//        ama.setArtifact(null);
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        ArtifactManifest am = (ArtifactManifest) o;
        return Objects.equals( this.manifestID, am.getManifestID() );
    }

    @Override
    public int hashCode() {
        return Objects.hash( this.manifestID );
    }
}
