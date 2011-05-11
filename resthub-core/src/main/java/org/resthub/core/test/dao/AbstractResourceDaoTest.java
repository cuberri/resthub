package org.resthub.core.test.dao;

import org.resthub.core.dao.GenericResourceDao;
import org.resthub.core.model.Resource;

public abstract class AbstractResourceDaoTest<T extends Resource, D extends GenericResourceDao<T>>
		extends AbstractDaoTest<T, Long, D> {


    /**
     * {@InheritDoc}
     */
    @Override
    protected Long getIdFromEntity(T resource) {
        return resource.getId();
    }
    
}
