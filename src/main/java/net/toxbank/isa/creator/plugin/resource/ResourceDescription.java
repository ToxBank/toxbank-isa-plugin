package net.toxbank.isa.creator.plugin.resource;

public class ResourceDescription {

    private String resourceName;
    private String resourceAbbreviation;
    private String queryURL;

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

