package me.lin.amn.repository.model;

import me.lin.amn.repository.model.artifact.ArtifactBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Lin on 5/30/16.
 */
@Entity(name="artifact")
@Table(name="ARTIFACTS")
public class Artifact implements Serializable {
    private String artifactID;
    private Date lastModified;      //last modified
    private int refCount;           //reference count from asset to this artifact
    private Blob artifact;


    private List<ArtifactManifestArtifact> artifactManifest = new ArrayList<>();

    public Artifact() {

    }

    public static ArtifactBuilder create(String filename){
        return new ArtifactBuilder(filename);
    }

    public Artifact(Blob artifactBlob) {
        this.artifact = artifactBlob;
    }

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",
            strategy = "me.lin.amn.repository.model.AMIdentifierGenerator")
    @Column(name="ARTIFACT_ID")
    @Id
    public String getArtifactID() {
        return artifactID;
    }

    public void setArtifactID(String artifact_id) {
        this.artifactID = artifact_id;
    }

    @Column(name = "REF_CNT")
    public int getRefCount() {
        return this.refCount;
    }

    public void setRefCount(int refCount) {
        this.refCount = refCount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Blob getArtifact() {
        return this.artifact;
    }

    public void setArtifact(Blob artifact) {
        this.artifact = artifact;
    }

    @OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ArtifactManifestArtifact> getArtifactManifest() {
        return this.artifactManifest;
    }

    public void setArtifactManifest(List<ArtifactManifestArtifact> artifactManifest) {
        this.artifactManifest = artifactManifest;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Artifact art = (Artifact) o;
        return Objects.equals(this.artifactID, art.getArtifactID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.artifactID);
    }
}