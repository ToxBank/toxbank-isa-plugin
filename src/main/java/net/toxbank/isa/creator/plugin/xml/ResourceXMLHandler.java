package net.toxbank.isa.creator.plugin.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.xml.xpath.XPathConstants;

import org.apache.log4j.Logger;

import net.toxbank.isa.creator.plugin.resource.ResourceDescription;
import uk.ac.ebi.utils.xml.XPathReader;

public class ResourceXMLHandler {

    public static final String resourceFileLocation = "config/tb-resource-description.xml";

    public ResourceDescription parseXML() {
        try {
            XPathReader reader = new XPathReader(new FileInputStream(resourceFileLocation));

            String name = (String) reader.read("/resource/name", XPathConstants.STRING);
            String abbreviation = (String) reader.read("/resource/abbreviation", XPathConstants.STRING);
            String queryURL = (String) reader.read("/resource/queryURL", XPathConstants.STRING);
            String username = (String) reader.read("/resource/username", XPathConstants.STRING);
            String password = (String) reader.read("/resource/password", XPathConstants.STRING);
            String keywords = (String) reader.read("/resource/keywords", XPathConstants.STRING);
            ResourceDescription resourceDescription = new ResourceDescription(name, abbreviation, queryURL);
            resourceDescription.setUsername(username);
            resourceDescription.setPassword(password);
            try { 
            	resourceDescription.setKeywords(new URL(keywords)); 
            } catch (Exception x) {
            	Logger.getLogger(getClass()).error("Invalid URL "+keywords, x);
            	resourceDescription.setKeywords(null);
            }
            return resourceDescription;

        } catch (FileNotFoundException e) {
            return new ResourceDescription("Repository", "REPO", "URL is unknown");
        }
    }

    public static void main(String[] args) {
        ResourceXMLHandler handler = new ResourceXMLHandler();
        ResourceDescription desc = handler.parseXML();

        System.out.println(desc.getResourceName());
    }
}
