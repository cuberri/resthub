package org.resthub.core.context.feature;

import javax.xml.bind.annotation.XmlRootElement;
import org.resthub.core.context.config.ResthubScanSpec.ScanType;
import org.resthub.core.context.config.jaxb.JAXBElementScanSpec;
import org.resthub.core.context.model.ConfigAbstractResource;
import org.springframework.context.annotation.ComponentScanSpec;
import org.springframework.context.annotation.Feature;
import org.springframework.context.annotation.FeatureConfiguration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
@FeatureConfiguration
public class ResthubJAXBElementScanTestFeatures {
    
    @Feature
    public ComponentScanSpec componentScan() {
        return new ComponentScanSpec("org.resthub.core.context.jaxb");
    }

    @Feature
    public JAXBElementScanSpec includeEntityScan() {
        return new JAXBElementScanSpec("org.resthub.core.context.model")
                .useDefaultFilters(false)
                .type(ScanType.INCLUDER)
                .addIncludeFilter(new AnnotationTypeFilter(XmlRootElement.class))
                .addExcludeFilter(new AssignableTypeFilter(ConfigAbstractResource.class));
    }

}
