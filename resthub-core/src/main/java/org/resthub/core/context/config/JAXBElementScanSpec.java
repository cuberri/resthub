package org.resthub.core.context.config;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public class JAXBElementScanSpec extends ResthubScanSpec {
    
    private static final Class<? extends AbstractResthubScanExecutor> EXECUTOR_TYPE = JAXBElementScanExecutor.class;
    
    public JAXBElementScanSpec(String... basePackages) {
        super(EXECUTOR_TYPE, basePackages);
    }
    
}
