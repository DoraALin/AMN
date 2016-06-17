package me.lin.amn.common.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * Created by Lin on 6/17/16.
 */
@Aspect
public class Tracing {

    private static Log LOG = LogFactory.getLog(Tracing.class.getName());

    @Pointcut("execution(** me.lin.amn.repository.dao.ArtifactManifestArtifactRelation+.addArtifact(..))")
    public void trace() {
    }

    @Before(value="trace()", argNames="joinPoint")
    public void enter(JoinPoint joinPoint) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Enter: " + joinPoint.getTarget().getClass().getName() +'.'
                    + joinPoint.getSignature().getName());
        }
    }

    @After(value="trace()", argNames = "joinPoint")
    public void exit(JoinPoint joinPoint) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Exit: " + joinPoint.getTarget().getClass().getName() +'.'
                    + joinPoint.getSignature().getName());
        }
    }
}
