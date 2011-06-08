package org.resthub.web.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

import org.resthub.web.model.WebSampleResource;
import org.resthub.web.service.WebSampleResourceService;
import org.springframework.transaction.annotation.Transactional;

@Path("/resources")
@Named("webSampleResourceController")
@Transactional(readOnly=false)
public class WebSampleResourceControllerImpl extends GenericControllerImpl<WebSampleResource, Long, WebSampleResourceService> implements WebSampleResourceController {
	
    @Inject
    @Named("webSampleResourceService")
    @Override
    public void setService(WebSampleResourceService service) {
        this.service = service;
    }
	
}
