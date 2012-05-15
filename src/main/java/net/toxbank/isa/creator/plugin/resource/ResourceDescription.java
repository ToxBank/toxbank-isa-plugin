package net.toxbank.isa.creator.plugin.resource;

import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;

public class ResourceDescription {
    private String username;
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String password;
    private String resourceName;
    private String resourceAbbreviation;
    private String resourceVersion;
    private String queryURL;

    private MultiMap<String, ResourceField> resourceFields;

    public ResourceDescription(String resourceName, String resourceAbbreviation, String resourceVersion, String queryURL) {
        this(resourceName, resourceAbbreviation, resourceVersion, queryURL, new MultiHashMap<String, ResourceField>());
    }

    public ResourceDescription(String resourceName, String resourceAbbreviation, String resourceVersion, String queryURL, MultiMap<String, ResourceField> resourceFields) {
        this.resourceName = resourceName;
        this.resourceAbbreviation = resourceAbbreviation;
        this.resourceVersion = resourceVersion;
        this.queryURL = queryURL;
        this.resourceFields = resourceFields;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getResourceAbbreviation() {
        return resourceAbbreviation;
    }

    public String getQueryURL() {
        return queryURL;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public MultiMap<String, ResourceField> getResourceFields() {
        return resourceFields;
    }

    public void setResourceFields(MultiMap<String, ResourceField> resourceFields) {
        this.resourceFields = resourceFields;
    }
}

