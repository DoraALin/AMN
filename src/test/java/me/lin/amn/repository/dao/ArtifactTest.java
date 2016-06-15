package me.lin.amn.repository.dao;

import junit.framework.TestCase;
import me.lin.amn.repository.model.Artifact;
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
@ContextConfiguration(classes = RepositoryConfig.class)
public class ArtifactTest extends TestCase {
    private static Log LOG = LogFactory.getLog(ArtifactTest.class.getName());

    @Autowired
    ArtifactRepository artRepo;

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
}
