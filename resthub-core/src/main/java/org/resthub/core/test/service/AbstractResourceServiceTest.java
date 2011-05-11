package org.resthub.core.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.resthub.core.model.Resource;
import org.resthub.core.service.GenericResourceService;

public abstract class AbstractResourceServiceTest<T extends Resource, D extends GenericResourceService<T>>
        extends AbstractServiceTest<T, Long, D> {

    @Test
    public void testCreate() {
        T resource = service.create(this.createTestRessource());

        T foundResource = service.findById(resource.getId());
        Assert.assertNotNull("Resource not created!", foundResource);
    }

}
