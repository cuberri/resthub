package org.resthub.core.context.config.jaxb;

import java.lang.annotation.Annotation;

import javax.xml.bind.annotation.XmlRootElement;

import org.resthub.core.context.config.ResthubComponentProvider;
import org.resthub.core.context.config.persistence.EntityComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * This class defines and perform scanning for XML binding specified
 * configurations. It reads options, parameters, filters and apply them to
 * filtering
 * 
 * This class inherit {@link ResthubComponentProvider}
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author Loïc Frering <loic.frering@gmail.com>
 * 
 */
public class JAXBElementComponentProvider extends ResthubComponentProvider {

	public JAXBElementComponentProvider(boolean useDefaultFilters) {
		super(useDefaultFilters);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void registerDefaultFilters() {
		ClassLoader cl = EntityComponentProvider.class
		.getClassLoader();
		try {
			this.addIncludeFilter(new AnnotationTypeFilter(
					((Class<? extends Annotation>) cl
							.loadClass("javax.xml.bind.annotation.XmlRootElement")),
					false));
			logger.info("javax.xml.bind.annotation.XmlRootElement found and supported for entity scanning");
		} catch (ClassNotFoundException ex) {
			// javax.xml.bind.annotation.XmlRootElement not available - simply skip.
		}
	}
}
