package org.isatools.novartismetastore;

import java.util.List;
import java.util.Map;

import net.toxbank.isa.creator.plugin.ToxBankRESTClient;

import org.isatools.isacreator.ontologymanager.OntologySourceRefObject;
import org.isatools.isacreator.ontologymanager.common.OntologyTerm;
import org.junit.Test;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 12/09/2011
 *         Time: 16:55
 */
public class ToxBankRESTClientTest {

    @Test
    public void getTermsByPartialNameFromSource() {
        ToxBankRESTClient client = new ToxBankRESTClient();
        Map<OntologySourceRefObject, List<OntologyTerm>> result = client.searchRepository("Nina");

        System.out.println("There are " + result.size() + " results");
        for (OntologySourceRefObject source : result.keySet()) {
            System.out.println("For " + source.getSourceName());

            for(OntologyTerm term : result.get(source)) {
                System.out.println("\t" + term.getUniqueId() + " term name is: " + term.getOntologyTermName());
            }
        }
    }

}
