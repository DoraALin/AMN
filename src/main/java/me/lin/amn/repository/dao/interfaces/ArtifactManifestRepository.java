package me.lin.amn.repository.dao.interfaces;

import me.lin.amn.repository.model.ArtifactManifest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Lin on 6/16/16.
 */
@Repository
public interface ArtifactManifestRepository extends JpaRepository<ArtifactManifest, String>, IArtifactManifest {
}
