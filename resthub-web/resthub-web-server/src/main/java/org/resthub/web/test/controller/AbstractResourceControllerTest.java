package org.resthub.web.test.controller;

import org.resthub.core.model.Resource;
import org.resthub.core.service.GenericResourceService;
import org.resthub.core.util.ClassUtils;
import org.resthub.web.controller.GenericResourceController;

/**
 * Base class for your generic resource controller tests
 */
public abstract class AbstractResourceControllerTest<T extends Resource, S extends GenericResourceService<T>, C extends GenericResourceController<T, S>>
						extends AbstractControllerTest<T, Long, S, C> {

	/**
	 * Returns the resource class
	 */
	@Override
	public Class<? extends Resource> getResourceClass() {
		return createTestResource().getClass();
	}

	/**
	 * Creates a generic instance for tests
	 * @return T
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
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
}
