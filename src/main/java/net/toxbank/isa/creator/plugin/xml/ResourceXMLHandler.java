package net.toxbank.isa.creator.plugin.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import net.toxbank.isa.creator.plugin.resource.ResourceDescription;
import net.toxbank.isa.creator.plugin.resource.ResourceField;

import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;
import org.w3c.dom.NodeList;

import uk.ac.ebi.utils.xml.XPathReader;

public class ResourceXMLHandler {
	public static final String pluginLocation = "Plugins/ToxbankRESTPlugin";
    public static final String resourceFileLocation = String.format("%s/config/resource-description.xml",pluginLocation);

    public List<ResourceDescription> parseXML() {
        try {
        	
            XPathReader reader = new XPathReader(new FileInputStream(resourceFileLocation));
            String username = (String) reader.read("/resources/username", XPathConstants.STRING);
            String password = (String) reader.read("/resources/password", XPathConstants.STRING);
            //one could have different credentials for different reosurces, but for now use the same
            
            NodeList resources = (NodeList) reader.read("/resources/resource", XPathConstants.NODESET);

            if (resources.getLength() > 0) {

                List<ResourceDescription> resourceDescriptions = new ArrayList<ResourceDescription>();

                for (int resourceIndex = 0; resourceIndex <= resources.getLength(); resourceIndex++) {


                    String name = (String) reader.read("resources/resource[" + resourceIndex + "]/@name", XPathConstants.STRING);
                    String abbreviation = (String) reader.read("resources/resource[" + resourceIndex + "]/@abbreviation", XPathConstants.STRING);
                    String resourceVersion = (String) reader.read("resources/resource[" + resourceIndex + "]/@version", XPathConstants.STRING);
                    String queryURL = (String) reader.read("resources/resource[" + resourceIndex + "]/@queryURL", XPathConstants.STRING);

                    if (!name.isEmpty()) {
                        ResourceDescription newDescription = new ResourceDescription(name, abbreviation, resourceVersion, queryURL);
                        newDescription.setUsername(username);
                        newDescription.setPassword(password);

                        NodeList fields = (NodeList) reader.read("/resources/resource[" + resourceIndex + "]/fields/field", XPathConstants.NODESET);

                        if (fields.getLength() > 0) {
                            MultiMap<String, ResourceField> resourceFields = new MultiHashMap<String, ResourceField>();

                            for (int fieldIndex = 0; fieldIndex <= fields.getLength(); fieldIndex++) {
                                String fieldName = (String) reader.read("resources/resource[" + resourceIndex + "]/fields/field[" + fieldIndex + "]/@name", XPathConstants.STRING);
                                String assayMeasurement = (String) reader.read("resources/resource[" + resourceIndex + "]/fields/field[" + fieldIndex + "]/@assay-measurement", XPathConstants.STRING);
                                String assayTechnology = (String) reader.read("resources/resource[" + resourceIndex + "]/fields/field[" + fieldIndex + "]/@assay-technology", XPathConstants.STRING);

                                if (!fieldName.isEmpty()) {
                                    resourceFields.put(fieldName, new ResourceField(fieldName,
                                            assayTechnology == null ? "" : assayTechnology,
                                            assayMeasurement == null ? "" : assayMeasurement));
                                }
                            }

                            newDescription.setResourceFields(resourceFields);
                        }

                        resourceDescriptions.add(newDescription);
                    }
                }

                return resourceDescriptions;

            }
            return new ArrayList<ResourceDescription>();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<ResourceDescription>();
        }
    }
}
