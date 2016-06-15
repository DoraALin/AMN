package me.lin.amn.repository.model;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.persister.entity.AbstractEntityPersister;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Lin on 5/30/16.
 */
public class AMIdentifierGenerator implements IdentifierGenerator {
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException{
        AbstractEntityPersister classMetadata =
                (AbstractEntityPersister)session.getFactory()
                        .getClassMetadata(object.getClass());

        String identifierPropertyName = classMetadata.getIdentifierPropertyName();
        return identifierPropertyName + '_' + UUID.randomUUID().toString();
    }
}
