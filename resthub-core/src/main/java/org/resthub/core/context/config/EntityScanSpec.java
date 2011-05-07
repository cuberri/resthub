package org.resthub.core.context.config;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public class EntityScanSpec extends ResthubScanSpec {

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
