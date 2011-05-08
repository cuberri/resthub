package org.resthub.core.context.config;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.config.AbstractSpecificationExecutor;
import org.springframework.context.config.SpecificationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.TypeFilter;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public abstract class AbstractResthubScanExecutor<S extends ResthubScanSpec> extends AbstractSpecificationExecutor<S> {

    @Override
    protected void doExecute(S spec, SpecificationContext specificationContext) {
        BeanDefinitionRegistry registry = specificationContext.getRegistry();
        ResourceLoader resourceLoader = specificationContext.getResourceLoader();
        Environment environment = specificationContext.getEnvironment();

        ClassPathScanningCandidateComponentProvider scanner = createScanner(spec);

        scanner.setResourceLoader(resourceLoader);
        scanner.setEnvironment(environment);

        for (TypeFilter filter : spec.includeFilters()) {
            scanner.addIncludeFilter(filter);
        }
        for (TypeFilter filter : spec.excludeFilters()) {
            scanner.addExcludeFilter(filter);
        }

        Set<String> resources = new HashSet<String>();

        for (String basePackage : spec.basePackages()) {
            for (BeanDefinition beanDefinition : scanner.findCandidateComponents(basePackage)) {
                resources.add(beanDefinition.getBeanClassName());
            }
        }

        registerBeanDefinition(resources, spec, registry);

    }

    protected void registerBeanDefinition(Set<String> resources, S spec, BeanDefinitionRegistry registry) {

        BeanDefinition beanDefinition = createBeanDefinition(resources, spec);
        String beanName = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, registry);

        registry.registerBeanDefinition(beanName, beanDefinition);
    }
    
    protected abstract ResthubComponentProvider createScanner(S spec);
    
    protected abstract BeanDefinition createBeanDefinition(Set<String> resources, S spec);
}
