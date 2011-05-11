package org.resthub.core.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.dao.GenericDao;
import org.resthub.core.test.AbstractResthubTransactionalTest;
import org.resthub.core.util.ClassUtils;
import org.resthub.core.util.MetamodelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDaoTest<T, PK extends Serializable, D extends GenericDao<T, PK>>
        extends AbstractResthubTransactionalTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The tested DAO
     */
    protected D dao;
    /**
     * Id of the tested POJO
     */
    protected PK id;

    @PersistenceContext
    private EntityManager em;

    /**
     * Injection of DAO.
     */
    public void setDao(D dao) {
        this.dao = dao;
    }

    public final Logger getLogger() {
        return logger;
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
                (Class<T>) ClassUtils.getGenericTypeFromBean(this.dao),
                em.getMetamodel());
        return (PK) utils.getIdFromEntity(obj);
    }

    /**
     * Instantiate a new instance for this test entity
     * 
     * @return the concrete created instance of T
     */
    @SuppressWarnings("unchecked")
    protected T createTestEntity() {
        try {
            return (T) ClassUtils.getGenericTypeFromBean(this.dao)
                    .newInstance();
        } catch (InstantiationException e) {
            logger.error("Cannot instantiate instance of "
                    + ClassUtils.getGenericTypeFromBean(this.dao));
        } catch (IllegalAccessException e) {
            logger.error("Cannot access nullarity constructor for class "
                    + ClassUtils.getGenericTypeFromBean(this.dao));
        }
        return null;
    }

    /**
     * Initialize current test entity, save it and store generated id.
     */
    @Before
    public void setUpTestEntity() {
        T resource = this.createTestEntity();
        resource = dao.save(resource);
        this.id = getIdFromEntity(resource);
    }

    /**
     * Clean created entities after testing
     */
    @After
    public void tearDown() {
        // Don't use deleteAll because it does not achieve cascade delete
        for (T resource : dao.readAll()) {
            dao.delete(resource);
        }
    }

    @Test
    public abstract void testUpdate();

    @Test
    public void testSave() {
        T resource = this.createTestEntity();
        resource = dao.save(resource);

        T foundResource = dao.readByPrimaryKey(getIdFromEntity(resource));
        assertNotNull("Resource not found!", foundResource);
    }

    @Test
    public void testDelete() {
        T resource = dao.readByPrimaryKey(this.id);
        dao.delete(resource);

        T foundResource = dao.readByPrimaryKey(this.id);
        assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testDeleteById() {
        dao.delete(this.id);

        T foundResource = dao.readByPrimaryKey(this.id);
        assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testFindAll() {
        List<T> resourceList = dao.readAll();
        assertTrue("No resources found!", resourceList.size() >= 1);
    }

    @Test
    public void testCount() {
        Long nb = dao.count();
        assertTrue("No resources found!", nb >= 1);
    }

    @Test
    public void testReadByPrimaryKey() {
        T foundResource = dao.readByPrimaryKey(this.id);
        assertNotNull("Resource not found!", foundResource);
        assertEquals("Resource does not contain the correct Id!", this.id,
                getIdFromEntity(foundResource));
    }
}
