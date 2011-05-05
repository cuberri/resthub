package org.resthub.core.context.config.jaxb;

import java.util.Set;

import org.resthub.core.context.config.AbstractParser;
import org.resthub.core.context.config.ResthubComponentProvider;
import org.resthub.core.context.jaxb.JAXBElementListBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * This class provide utilities for xml binding resources scanning defined by a scanning
 * configuration in application context. Define an abstract handling method to
 * manipulate all found resources, matching with specified configuration options.
 * Concrete implementations should be provided
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public abstract class AbstractJAXBElementsParser extends
		AbstractParser {

	protected abstract Class<? extends JAXBElementListBean> getBeanClass();
	
	/**
	 * {@InheritDoc}
	 */
	protected ResthubComponentProvider createProvider() {
		return new JAXBElementComponentProvider();
	}
	
	protected BeanDefinition createBeanDefinition(Set<String> elements) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.genericBeanDefinition();
		builder.getRawBeanDefinition().setBeanClass(this.getBeanClass());
		builder.addPropertyValue("elements", elements);
		return builder.getRawBeanDefinition();
	}

}
