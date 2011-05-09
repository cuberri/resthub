package org.resthub.core.context.config.persistence;

import org.resthub.core.context.config.ResthubScanSpec.ScanType;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.config.FeatureSpecification;
import org.w3c.dom.Element;


/**
 * This class scan entities defined by a scanning configuration in application
 * context and add all found entities, matching with specified configuration
 * options, to the include list of persistence context in order to be managed
 * later (on initialization phasis)
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public class IncludeEntitiesParser extends AbstractEntitiesParser {

	@Override
    protected FeatureSpecification doParse(Element element, ParserContext parserContext) {
        EntityScanSpec spec = (EntityScanSpec) super.doParse(element, parserContext);
        spec.type(ScanType.INCLUDER);
        return spec;
    }

	public String getElementName() {
		return "include-entities";
	}
	
}
