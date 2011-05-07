package org.resthub.core.context.config.jaxb;


import org.resthub.core.context.config.AbstractResthubParser;
import org.resthub.core.context.config.JAXBElementScanSpec;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.config.FeatureSpecification;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class provide utilities for xml binding resources scanning defined by a scanning
 * configuration in application context. Define an abstract handling method to
 * manipulate all found resources, matching with specified configuration options.
 * Concrete implementations should be provided
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public abstract class AbstractJAXBElementsParser extends AbstractResthubParser {

    @Override
    protected FeatureSpecification doParse(Element element, ParserContext parserContext) {
        ClassLoader classLoader = parserContext.getReaderContext().getResourceLoader().getClassLoader();

		JAXBElementScanSpec spec = new JAXBElementScanSpec(element.getAttribute("base-package"));
        
		// Parse exclude and include filter elements.
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String localName = parserContext.getDelegate().getLocalName(node);
				String filterType = ((Element)node).getAttribute("type");
				String expression = ((Element)node).getAttribute("expression");
				if ("include-filter".equals(localName)) {
					spec.addIncludeFilter(filterType, expression, classLoader);
				}
				else if ("exclude-filter".equals(localName)) {
					spec.addExcludeFilter(filterType, expression, classLoader);
				}
			}
		}

		return spec;
    }


}
