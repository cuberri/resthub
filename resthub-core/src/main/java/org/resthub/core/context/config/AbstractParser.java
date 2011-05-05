package org.resthub.core.context.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * This class provide utilities for resources scanning defined by a scanning
 * configuration in application context. Define an abstract handling method to
 * manipulate all found resources, matching with specified configuration
 * options. Concrete implementations should be provided
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public abstract class AbstractParser implements ResthubBeanDefinitionParser {

    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    protected Element element;

    /**
     * {@InheritDoc}
     */
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        this.element = element;

        String[] basePackages = StringUtils.tokenizeToStringArray(
                element.getAttribute(BASE_PACKAGE_ATTRIBUTE),
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

        // Actually scan for entities definitions and register them.
        ResthubComponentProvider provider = configureScanner(parserContext, element);

        Set<String> resources = new HashSet<String>();

        for (String basePackage : basePackages) {

            for (BeanDefinition beanDefinition : provider.findCandidateComponents(basePackage)) {
                resources.add(beanDefinition.getBeanClassName());
            }

        }

        registerBeanDefinition(resources, parserContext);

        return null;
    }

    /**
     * {@InheritDoc}
     * 
     */
    protected void registerBeanDefinition(Set<String> resources,
            ParserContext parserContext) {

        BeanDefinition beanDefinition = createBeanDefinition(resources);
        String beanName = BeanDefinitionReaderUtils.generateBeanName(
                beanDefinition, parserContext.getRegistry());

        parserContext.getRegistry().registerBeanDefinition(beanName,
                beanDefinition);
    }

    /**
     * {@InheritDoc}
     */
    protected ResthubComponentProvider configureScanner(
            ParserContext parserContext, Element element) {
        XmlReaderContext readerContext = parserContext.getReaderContext();

        // Delegate bean definition registration to provider class.
        ResthubComponentProvider provider = createProvider();
        ResourceLoader resourceLoader = readerContext.getResourceLoader();
        provider.setResourceLoader(resourceLoader);
        
        TypeFilterParser parser = new TypeFilterParser(resourceLoader.getClassLoader(), readerContext);
        parser.parseFilters(element, provider);

        return provider;
    }

    /**
     * {@InheritDoc}
     */
    protected abstract ResthubComponentProvider createProvider();

    protected abstract Class<?> getBeanClass();

    protected abstract BeanDefinition createBeanDefinition(Set<String> resources);
}
