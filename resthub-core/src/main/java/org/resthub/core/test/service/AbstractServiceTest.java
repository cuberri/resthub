package org.resthub.core.test.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.service.GenericService;
import org.resthub.core.test.AbstractResthubTransactionAwareTest;
import org.resthub.core.util.ClassUtils;
import org.resthub.core.util.MetamodelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServiceTest<T, PK extends Serializable, D extends GenericService<T, PK>>
        extends AbstractResthubTransactionAwareTest {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * The tested Service
     */
    protected D service;
    /**
     * The tested POJO
     */
    protected PK id;

    @PersistenceContext
    private EntityManager em;

    /**
     * Injection of Service.
     */
    public void setService(D service) {
        this.service = service;
    }
    
    /**
     * Automatically retrieve ID from entity instance.
     * 
     * @param obj
     *            The object from whom we need primary key
     * @return The corresponding primary key.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected PK getIdFromEntity(T obj) {
        MetamodelUtils utils = new MetamodelUtils<T, PK>(
                (Class<T>) ClassUtils.getGenericTypeFromBean(this.service),
                em.getMetamodel());
        return (PK) utils.getIdFromEntity(obj);
    }

    @SuppressWarnings("unchecked")
    protected T createTestEntity() {
        try {
            return (T) ClassUtils.getGenericTypeFromBean(this.service)
                    .newInstance();
        } catch (InstantiationException e) {
            logger.error("Cannot instantiate instance of "
                    + ClassUtils.getGenericTypeFromBean(this.service));
        } catch (IllegalAccessException e) {
            logger.error("Cannot access nullarity constructor for class "
                    + ClassUtils.getGenericTypeFromBean(this.service));
        }
        
        return null;
    }

    @Before
    public void setUp() {
        T resource = service.create(this.createTestEntity());
        this.id = getIdFromEntity(resource);
    }

    @After
    public void tearDown() {
        // Don't use deleteAll because it does not acheive cascade delete
        for (T resource : service.findAll()) {
            service.delete(resource);
        }

    }

    @Test
    public void testCreate() {
        T resource = service.create(this.createTestEntity());

        T foundResource = service.findById(getIdFromEntity(resource));
        Assert.assertNotNull("Resource not created!", foundResource);
    }

    @Test
    public abstract void testUpdate();

    @Test
    public void testDelete() {
        T resource = service.findById(this.id);
        service.delete(resource);

        T foundResource = service.findById(this.id);
        Assert.assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testDeleteById() {
        T resource = service.findById(this.id);
        service.delete(getIdFromEntity(resource));

        T foundResource = service.findById(this.id);
        Assert.assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testFindById() {
        T resource = service.findById(this.id);
        Assert.assertNotNull("Resource should not be null!", resource);
        Assert.assertEquals("Resource id and resourceId should be equals!",
                this.id, this.getIdFromEntity(resource));
    }

    @Test
    public void testFindAll() {
        List<T> resourceList = service.findAll(null).asList();
        Assert.assertTrue("No resources found!", resourceList.size() >= 1);
    }

    @Test
    public void testCount() {
        Long nb = service.count();
        Assert.assertTrue("No resources found!", nb >= 1);
    }
}
