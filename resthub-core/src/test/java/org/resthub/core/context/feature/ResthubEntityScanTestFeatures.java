package org.resthub.core.context.feature;

import javax.persistence.Entity;
import org.resthub.core.context.config.ResthubScanSpec.ScanType;
import org.resthub.core.context.config.persistence.EntityScanSpec;
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
public class ResthubEntityScanTestFeatures {
    
    @Feature
    public ComponentScanSpec componentScan() {
        return new ComponentScanSpec("org.resthub.core.context.persistence");
    }

    @Feature
    public EntityScanSpec includeEntityScan() {
        return new EntityScanSpec("org.resthub.core.context.model")
                .useDefaultFilters(false)
                .type(ScanType.INCLUDER)
                .addIncludeFilter(new AnnotationTypeFilter(Entity.class))
                .addExcludeFilter(new AssignableTypeFilter(ConfigAbstractResource.class));
    }

}
