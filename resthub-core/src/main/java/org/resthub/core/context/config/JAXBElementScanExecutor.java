package org.resthub.core.context.config;

import java.util.Set;
import org.resthub.core.context.config.jaxb.JAXBElementComponentProvider;
import org.resthub.core.context.jaxb.JAXBElementListExcluderBean;
import org.resthub.core.context.jaxb.JAXBElementListIncluderBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
class JAXBElementScanExecutor extends AbstractResthubScanExecutor<JAXBElementScanSpec> {

    @Override
    protected ResthubComponentProvider createScanner(JAXBElementScanSpec spec) {
        return new JAXBElementComponentProvider(spec.useDefaultFilters());
    }

    @Override
    protected BeanDefinition createBeanDefinition(Set<String> elements, JAXBElementScanSpec spec) {
        Class<?> beanClass;
        switch (spec.type()) {
            case EXCLUDER:
                beanClass = JAXBElementListExcluderBean.class;
                break;
                
            default:
                beanClass = JAXBElementListIncluderBean.class;
        }
        
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
		builder.getRawBeanDefinition().setBeanClass(beanClass);
		builder.addPropertyValue("elements", elements);
		return builder.getRawBeanDefinition();
    }
    
}
