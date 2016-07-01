package me.lin.amn.repository.dao.interfaces;

import me.lin.amn.repository.model.Artifact;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Lin on 6/30/16.
 */
public interface IArtifactRepository {
    Artifact findArtifactWithContentById(String artifactId);
    void getBinaryStream(Artifact artifact, OutputStream os) throws SQLException, IOException;
}
