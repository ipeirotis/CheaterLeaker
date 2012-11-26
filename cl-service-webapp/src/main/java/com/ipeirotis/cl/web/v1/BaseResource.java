package com.ipeirotis.cl.web.v1;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
@Produces(MediaType.APPLICATION_JSON_VALUE)
abstract class BaseResource {
	@Context
	HttpHeaders requestHeaders;

}
