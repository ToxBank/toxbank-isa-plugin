package net.toxbank.isa.creator.plugin.resource;

import java.net.URL;

public class ResourceDescription {

    private String resourceName;
    private String resourceAbbreviation;
    private String queryURL;
    private String username;
    private URL keywords;
    
    public URL getKeywords() {
		return keywords;
	}

	public void setKeywords(URL keywords) {
		this.keywords = keywords;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private String password;

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ResourceDescription(String resourceName, String resourceAbbreviation, String queryURL) {
        this.resourceName = resourceName;
        this.resourceAbbreviation = resourceAbbreviation;
        this.queryURL = queryURL;
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
}

