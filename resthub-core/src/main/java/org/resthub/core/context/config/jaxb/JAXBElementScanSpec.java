package org.resthub.core.context.config.jaxb;

import org.resthub.core.context.config.AbstractResthubScanExecutor;
import org.resthub.core.context.config.ResthubScanSpec;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public final class JAXBElementScanSpec extends ResthubScanSpec<JAXBElementScanSpec> {
    
    private static final Class<? extends AbstractResthubScanExecutor> EXECUTOR_TYPE = JAXBElementScanExecutor.class;
    
    public JAXBElementScanSpec(String... basePackages) {
        super(EXECUTOR_TYPE, basePackages);
    }
    
}
