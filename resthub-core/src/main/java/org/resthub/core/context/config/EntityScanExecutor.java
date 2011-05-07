package org.resthub.core.context.config;

import java.util.Set;
import org.resthub.core.context.config.persistence.EntityComponentProvider;
import org.resthub.core.context.persistence.EntityListExcluderBean;
import org.resthub.core.context.persistence.EntityListIncluderBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
class EntityScanExecutor extends AbstractResthubScanExecutor<EntityScanSpec> {

    @Override
    protected ResthubComponentProvider createScanner(EntityScanSpec spec) {
        return new EntityComponentProvider(spec.useDefaultFilters());
    }

    @Override
    protected BeanDefinition createBeanDefinition(Set<String> entities, EntityScanSpec spec) {
        Class<?> beanClass;
        switch (spec.type()) {
            case EXCLUDER:
                beanClass = EntityListExcluderBean.class;
                break;
                
            default:
                beanClass = EntityListIncluderBean.class;
        }
        
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
		builder.getRawBeanDefinition().setBeanClass(beanClass);
		builder.addPropertyValue("entities", entities);
		builder.addPropertyValue("persistenceUnitName", spec.persistenceUnitName());
		return builder.getRawBeanDefinition();
    }
    
}
