package org.resthub.web.test.controller;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.resthub.core.service.GenericService;
import org.resthub.core.util.ClassUtils;
import org.resthub.web.controller.GenericController;
import org.resthub.web.test.AbstractWebResthubTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.resthub.core.util.MetamodelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for your generic controller tests
 */
public abstract class AbstractControllerTest<T, PK extends Serializable, S extends GenericService<T, PK>, C extends GenericController<T, S, PK>>
        extends AbstractWebResthubTest {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * The tested controller
     */
    protected C controller;

    @PersistenceContext
    private EntityManager em;

    /**
     * Injection of controller
     */
    public void setController(C controller) {
        this.controller = controller;
    }

    /**
     * Returns the resource path of controller class
     */
    public String getResourcePath() {
        Class<? extends GenericController> classInstance = controller
                .getClass();
        return classInstance.getAnnotation(Path.class).value();
    }

    /**
     * Returns the resource class
     */
    public Class<?> getResourceClass() {
        return this.createTestResource().getClass();
    }

    /**
     * Creates a generic instance for tests
     * 
     * @return T
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected T createTestResource() {
        try {
            return (T) ClassUtils.getGenericTypeFromBean(this.controller)
                    .newInstance();
        } catch (InstantiationException e) {
            logger.error("Cannot instantiate instance of "
                    + ClassUtils.getGenericTypeFromBean(this.controller));
        } catch (IllegalAccessException e) {
            logger.error("Cannot access nullarity constructor for class "
                    + ClassUtils.getGenericTypeFromBean(this.controller));
        }
        return null;
    }

    /**
     * Cleans all persisted objects to simulate transactionnal tests
     */
    @After
    public void cleanAll() {
        List<T> list = controller.getService().findAll();
        for (T entity : list) {
            controller.delete(getIdFromEntity(entity));
        }
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
                (Class<T>) ClassUtils.getGenericTypeFromBean(this.controller),
                em.getMetamodel());
        return (PK) utils.getIdFromEntity(obj);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateResource() {
        WebResource r = resource().path(getResourcePath());
        T res = (T) r.type(MediaType.APPLICATION_XML).post(getResourceClass(),
                createTestResource());
        Assert.assertNotNull("Resource not created", res);
    }

    @Test
    public void testFindAllResourcesJson() {
        WebResource r = resource().path(getResourcePath());
        r.type(MediaType.APPLICATION_XML).post(String.class,
                createTestResource());
        r.type(MediaType.APPLICATION_XML).post(String.class,
                createTestResource());
        String response = r.accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        Assert.assertTrue("Unable to find all resources or bad-formed JSON",
                response.contains("\"totalElements\" : 2"));
    }

    @Test
    public void testFindAllResourcesXml() {
        WebResource r = resource().path(getResourcePath());
        r.type(MediaType.APPLICATION_XML).post(String.class,
                createTestResource());
        r.type(MediaType.APPLICATION_XML).post(String.class,
                createTestResource());
        String response = r.accept(MediaType.APPLICATION_XML).get(String.class);

        Assert.assertTrue("Unable to find all resources or bad-formed XML",
                response.contains("<totalElements>2</totalElements>"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteResource() {
        WebResource r = resource().path(getResourcePath());
        T res = (T) r.type(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_JSON)
                .post(getResourceClass(), createTestResource());
        Assert.assertNotNull("Resource not created", res);

        r = resource().path(getResourcePath() + "/" + getIdFromEntity(res));
        ClientResponse response = r.delete(ClientResponse.class);
        Assert.assertEquals(Status.NO_CONTENT.getStatusCode(),
                response.getStatus());
        response = r.accept(MediaType.APPLICATION_XML)
                .get(ClientResponse.class);
        Assert.assertEquals(Status.NOT_FOUND.getStatusCode(),
                response.getStatus());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindResource() {
        WebResource r = resource().path(getResourcePath());
        T res = (T) r.type(MediaType.APPLICATION_XML).post(getResourceClass(),
                createTestResource());
        Assert.assertNotNull("Resource not created", res);

        r = resource().path(getResourcePath() + "/" + getIdFromEntity(res));
        ClientResponse cr = r.get(ClientResponse.class);
        Assert.assertEquals("Unable to find resource",
                Status.OK.getStatusCode(), cr.getStatus());
    }

    @Test
    public abstract void testUpdate();
}
