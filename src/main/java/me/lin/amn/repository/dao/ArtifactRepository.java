package me.lin.amn.repository.dao;

import me.lin.amn.repository.model.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Lin on 6/1/16.
 */
@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, String> {
    //Repository methods are composed of:
    // a verb,
    // an optional subject, if not specified, imply to Artifact
    // the word By,
    // and a predicate.
    Artifact findByArtifactID(String artifactID);
}
