package net.toxbank.isa.creator.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.toxbank.client.TBClient;
import net.toxbank.client.resource.User;
import net.toxbank.client.resource.UserClient;
import net.toxbank.isa.creator.plugin.resource.ResourceDescription;
import net.toxbank.isa.creator.plugin.utils.Convert;
import net.toxbank.isa.creator.plugin.xml.ResourceXMLHandler;

import org.isatools.isacreator.ontologymanager.OntologySourceRefObject;
import org.isatools.isacreator.ontologymanager.common.OntologyTerm;
import org.isatools.isacreator.plugins.host.service.PluginOntologyCVSearch;
import org.isatools.isacreator.plugins.registries.OntologySearchPluginRegistry;

/**
 * Created by the ISA team
 *
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
    		boolean ok = tbclient.login("user","pass");
    		UserClient cli = tbclient.getUserClient();
    		
    		List<User> users = cli.searchRDF_XML(new URL(String.format("%suser", queryURL)),term);	
    		
            return Convert.convertUsersResult(users);
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

    public void registerSearch() {
        OntologySearchPluginRegistry.registerPlugin(this);
    }

    public void deregisterSearch() {
        OntologySearchPluginRegistry.deregisterPlugin(this);
    }

}
