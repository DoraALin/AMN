package me.lin.amn.repository.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Asset model for ASSET table
 * Created by Lin on 5/27/16.
 */
@Entity
@Table(name="ASSETS")
public class Asset {
    private String guid;            //guid for asset, user is able to specify guid for variable value
    private String version;         //version for versioning control
    private String name;            //asset name
    private String shortDescription;//abstract for asset
    private String description;     //detailed description for asset

    private long id;                //id to identify asset in serialization tools(db, file system)
    private Date lastModified;      //last modified
    private String stageID;         //stage id for current asset

    private ArtifactManifest artifactManifest;      //artifact ID reference to artifact

    //TODO: custome asset attributes, and the ID for access control(like Community, asset type), asset type

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    //use standard UUID for asset identification
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED")
    public Date getDate() {
        return lastModified;
    }

    public void setDate(Date lastModified) {
        this.lastModified = lastModified;
    }

    @OneToOne
    public ArtifactManifest getArtifactManifest() {
        return this.artifactManifest;
    }

    @OneToOne
    public void setArtifactManifest(ArtifactManifest artifactManifest) {
        this.artifactManifest = artifactManifest;
    }

}
