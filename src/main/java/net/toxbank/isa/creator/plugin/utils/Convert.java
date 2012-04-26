package net.toxbank.isa.creator.plugin.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.toxbank.client.resource.User;
import net.toxbank.isa.creator.plugin.ToxBankRESTClient;

import org.isatools.isacreator.ontologymanager.OntologySourceRefObject;
import org.isatools.isacreator.ontologymanager.common.OntologyTerm;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 03/10/2011
 *         Time: 17:04
 */
public class Convert {

    public static Map<OntologySourceRefObject, List<OntologyTerm>> convertUsersResult(List<User> users) {
    	
    	
        Map<OntologySourceRefObject, List<OntologyTerm>> convertedResult = new HashMap<OntologySourceRefObject, List<OntologyTerm>>();

        OntologySourceRefObject source = new OntologySourceRefObject(
                ToxBankRESTClient.resourceInformation.getResourceAbbreviation(), "", "", ToxBankRESTClient.resourceInformation.getResourceName());

        convertedResult.put(source, new ArrayList<OntologyTerm>());

        for(User user: users) {
            OntologyTerm ontologyTerm = new OntologyTerm(
            			String.format("%s %s %s",user.getTitle(),user.getFirstname(),user.getLastname()),
            			user.getResourceURL().toString(), source);
            ontologyTerm.addToComments("Organisation", user.getOrganisations().toString());
            convertedResult.get(source).add(ontologyTerm);
        }

        return convertedResult;
    }
}
