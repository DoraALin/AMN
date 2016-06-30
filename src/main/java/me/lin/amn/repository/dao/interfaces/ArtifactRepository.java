package me.lin.amn.repository.dao.interfaces;

import me.lin.amn.repository.dao.interfaces.IArtifactRepository;
import me.lin.amn.repository.model.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Lin on 6/1/16.
 */
@Repository
@Transactional
public interface ArtifactRepository extends JpaRepository<Artifact, String>, IArtifactRepository {
}
