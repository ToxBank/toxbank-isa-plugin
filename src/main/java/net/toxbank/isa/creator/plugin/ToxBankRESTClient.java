package net.toxbank.isa.creator.plugin;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.toxbank.client.Resources;
import net.toxbank.client.TBClient;
import net.toxbank.client.resource.IToxBankResource;
import net.toxbank.client.resource.Organisation;
import net.toxbank.client.resource.Project;
import net.toxbank.client.resource.Protocol;
import net.toxbank.client.resource.User;
import net.toxbank.isa.creator.plugin.resource.ResourceDescription;
import net.toxbank.isa.creator.plugin.xml.KeywordsXMLHandler;
import net.toxbank.isa.creator.plugin.xml.ResourceXMLHandler;

import org.isatools.isacreator.ontologymanager.OntologySourceRefObject;
import org.isatools.isacreator.ontologymanager.common.OntologyTerm;
import org.isatools.isacreator.plugins.host.service.PluginOntologyCVSearch;
import org.isatools.isacreator.plugins.registries.OntologySearchPluginRegistry;

/**
 * Created by the Toxbank
 *
 * @author Nina Jeliazkova (jeliazkova.nina@gmail.com)
 *         <p/>
 *         Date: 27/04/2012
 *         Time: 13:28
 * 
 * Based on https://github.com/ISA-tools/NovartisMetastore by       
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 12/09/2011
 *         Time: 16:51
 */
public class ToxBankRESTClient implements PluginOntologyCVSearch {

    public static ResourceDescription resourceInformation;
    public static String queryURL;

    static {
        ResourceXMLHandler xmlHandler = new ResourceXMLHandler();
        resourceInformation = xmlHandler.parseXML();
        queryURL = resourceInformation.getQueryURL();
    }


    public Map<OntologySourceRefObject, List<OntologyTerm>> searchRepository(String term) {
    	TBClient tbclient = new TBClient();
        try {
    		boolean ok = tbclient.login(resourceInformation.getUsername(),resourceInformation.getPassword());
    		List<User> users = tbclient.getUserClient().searchRDF_XML(new URL(String.format("%s%s", queryURL,Resources.user)),term);
    		List<Protocol> protocols = tbclient.getProtocolClient().searchRDF_XML(new URL(String.format("%s%s", queryURL,Resources.protocol)),term);
    		List<Project> projects = tbclient.getProjectClient().searchRDF_XML(new URL(String.format("%s%s", queryURL,Resources.project)),term);
    		List<Organisation> organisations = tbclient.getOrganisationClient().searchRDF_XML(new URL(String.format("%s%s", queryURL,Resources.organisation)),term);
    		
    		Map<OntologySourceRefObject, List<OntologyTerm>> results = new HashMap<OntologySourceRefObject, List<OntologyTerm>>();
    		if (users!=null && users.size()>0) {
    			OntologySourceRefObject source = new OntologySourceRefObject("TBU",  String.format("%s%s", queryURL,Resources.user), "", "Toxbank users");
    			convertResourceResult(users,source,results);
    		}
    		if (protocols!=null && protocols.size()>0) {
    	    	OntologySourceRefObject source = new OntologySourceRefObject("TBP",  String.format("%s%s", queryURL,Resources.protocol), "", "Toxbank protocols");
    	    	convertResourceResult(protocols,source,results);
    		}
    		if (projects!=null && projects.size()>0) {
    	    	OntologySourceRefObject source = new OntologySourceRefObject("TBC",  String.format("%s%s", queryURL,Resources.project), "", "Toxbank projects");
    	    	convertResourceResult(projects,source,results);
    		}
    		if (organisations!=null && organisations.size()>0) {
    	    	OntologySourceRefObject source = new OntologySourceRefObject("TBO", String.format("%s%s", queryURL,Resources.organisation), "", "Toxbank organisations");
    	    	convertResourceResult(organisations,source,results);
    		}
    		
    		List<OntologyTerm> terms = searchKeywords(term);
    		if ((terms!=null) && terms.size()>0)  results.put(KeywordsXMLHandler.source,terms);
    		
            return results;
        } catch (MalformedURLException e) {
            System.out.println("Wrong URL ...");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("No file found. Assuming connection is down...");
            e.printStackTrace();
        } finally {
        	try { tbclient.logout(); } catch (Exception x) {}
        }
        return new HashMap<OntologySourceRefObject, List<OntologyTerm>>();
    }
    
    protected List<OntologyTerm> searchKeywords(String term)  {
    	KeywordsXMLHandler xmlHandler = new KeywordsXMLHandler();

        try {
            return xmlHandler.parseXML(resourceInformation.getKeywords().getFile(),term);

        } catch (FileNotFoundException e) {
            System.out.println("No file found. Assuming connection is down...");
            e.printStackTrace();
            return null;
        }
    }

    public void registerSearch() {
        OntologySearchPluginRegistry.registerPlugin(this);
    }

    public void deregisterSearch() {
        OntologySearchPluginRegistry.deregisterPlugin(this);
    }

  
    protected void convertResourceResult(List<? extends IToxBankResource> resources,OntologySourceRefObject source,Map<OntologySourceRefObject, List<OntologyTerm>> results) {

    	ArrayList<OntologyTerm> terms = new ArrayList<OntologyTerm>();
    	 for(IToxBankResource resource:resources) {
             OntologyTerm ontologyTerm = new OntologyTerm(resource.getTitle(),null, source);
             ontologyTerm.setOntologyPurl(source.getSourceFile()+"/");
             ontologyTerm.setOntologySourceAccession(resource.getResourceURL().toExternalForm());
            // ontologyTerm.addToComments("Organisation", user.getOrganisations().toString());
             ontologyTerm.setOntologyTermName(String.format("%s:%s",source.getSourceName(),ontologyTerm.getOntologySourceAccession()));
             if (resource instanceof User) {
            	 User user = (User) resource;
            	 if (user.getOrganisations()!=null) ontologyTerm.addToComments("Organisation", user.getOrganisations().toString());
            	 ontologyTerm.setOntologySourceAccession(String.format("%s %s %s",
            			 user.getTitle()==null?"":user.getTitle(),
            			 user.getFirstname()==null?"":user.getFirstname(),
            			 user.getLastname()==null?"":user.getLastname()));
            	 
             } else if (resource instanceof Protocol) {
            	 ontologyTerm.setOntologySourceAccession(resource.getTitle());
             } else if (resource instanceof Project) {

            	 ontologyTerm.setOntologySourceAccession(resource.getTitle());
             } else if (resource instanceof Organisation) {
            	 ontologyTerm.setOntologySourceAccession(resource.getTitle());
             }
             terms.add(ontologyTerm);
         }
    	 if (terms!=null && (terms.size()>0)) results.put(source, terms);
    }
}
