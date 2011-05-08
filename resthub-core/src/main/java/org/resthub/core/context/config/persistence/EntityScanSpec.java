package org.resthub.core.context.config.persistence;

import org.resthub.core.context.config.AbstractResthubScanExecutor;
import org.resthub.core.context.config.ResthubScanSpec;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public final class EntityScanSpec extends ResthubScanSpec {

    private static final Class<? extends AbstractResthubScanExecutor> EXECUTOR_TYPE = EntityScanExecutor.class;
    private String persistenceUnitName;
    
    public EntityScanSpec(String... basePackages) {
        super(EXECUTOR_TYPE, basePackages);
    }
    
    public EntityScanSpec setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
        return this;
    }

    String persistenceUnitName() {
        return persistenceUnitName;
    }
    
}
