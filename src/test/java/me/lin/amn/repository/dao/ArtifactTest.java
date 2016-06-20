package me.lin.amn.repository.dao;

import junit.framework.TestCase;
import me.lin.amn.common.logging.TracingConfig;
import me.lin.amn.repository.dao.interfaces.ArtifactManifestRepository;
import me.lin.amn.repository.model.Artifact;
import me.lin.amn.repository.model.ArtifactManifest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lin on 6/13/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoryConfig.class, TracingConfig.class} )
public class ArtifactTest extends TestCase {
    private static Log LOG = LogFactory.getLog(ArtifactTest.class.getName());

    @Autowired
    ArtifactRepository artRepo;

    @Autowired
    ArtifactManifestRepository artManifestRepo;

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    @Test
    public void testCreateArtifact() {
        Artifact art = new Artifact(null);
        artRepo.save(art);
    }

    @Test
    public void testCreateArtifactManifest() {
        ArtifactManifest am1 = new ArtifactManifest("test manifest 1");
        ArtifactManifest am2 = new ArtifactManifest("test manifest 2");
        am1 = artManifestRepo.save(am1);
        am2 = artManifestRepo.save(am2);
        long cnt = artManifestRepo.count();
        assertEquals("ArtifactManifest records does not match.", 2, cnt);
    }

    @Test
    public void testRelateArtifactManifestAndArtifact() {
        ArtifactManifest am1 = new ArtifactManifest("test manifest 1");
        ArtifactManifest am2 = new ArtifactManifest("test manifest 2");
        artManifestRepo.save(am1);
        artManifestRepo.save(am2);

        Artifact art1 = new Artifact(null);
        Artifact art2 = new Artifact(null);
        artRepo.save(art1);
        artRepo.save(art2);

        artManifestRepo.addArtifact(am1, art1);
        artManifestRepo.addArtifact(am1, art2);
        artManifestRepo.addArtifact(am2, art1);

    }
}
