package org.resthub.core.context.config.persistence;

import org.resthub.core.context.config.AbstractResthubScanExecutor;
import org.resthub.core.context.config.ResthubScanSpec;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public final class EntityScanSpec extends ResthubScanSpec<EntityScanSpec> {
    
    private static final String DEFAULT_PERSISTENCE_UNIT_NAME = "resthub";

    private static final Class<? extends AbstractResthubScanExecutor> EXECUTOR_TYPE = EntityScanExecutor.class;
    private String persistenceUnitName;
    
    public EntityScanSpec(String... basePackages) {
        super(EXECUTOR_TYPE, basePackages);
        persistenceUnitName = DEFAULT_PERSISTENCE_UNIT_NAME;
    }
    
    public EntityScanSpec persistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
        return this;
    }

    String persistenceUnitName() {
        return persistenceUnitName;
    }
    
}
