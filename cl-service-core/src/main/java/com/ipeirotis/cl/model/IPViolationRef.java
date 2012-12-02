package com.ipeirotis.cl.model;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class IPViolationRef extends Entity implements Comparable<IPViolationRef> {
	private static final long serialVersionUID = -8429919264191012276L;
	
	String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public int compareTo(IPViolationRef o) {
		if (null == o)
			return -1;
		
		if (this == o)
			return 0;
		
		return new CompareToBuilder().append(this.id, o.id).append(this.url, o.url).toComparison();
	}
}
