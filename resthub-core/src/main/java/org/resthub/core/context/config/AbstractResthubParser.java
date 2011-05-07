package org.resthub.core.context.config;


import org.springframework.context.config.AbstractSpecificationBeanDefinitionParser;

/**
 * This class provide utilities for resources scanning defined by a scanning
 * configuration in application context. Define an abstract handling method to
 * manipulate all found resources, matching with specified configuration
 * options. Concrete implementations should be provided
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public abstract class AbstractResthubParser extends AbstractSpecificationBeanDefinitionParser implements ResthubBeanDefinitionParser {

}
