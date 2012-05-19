package net.toxbank.isa.creator.plugin.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.toxbank.isa.creator.plugin.ToxBankRESTClient;

import org.isatools.isacreator.configuration.Ontology;
import org.isatools.isacreator.configuration.RecommendedOntology;
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
        Map<OntologySourceRefObject, List<OntologyTerm>> result = client.searchRepository("Cell",null,true);

        System.out.println("There are " + result.size() + " results");
        for (OntologySourceRefObject source : result.keySet()) {
            System.out.println("For " + source.getSourceName());

            for(OntologyTerm term : result.get(source)) {
                System.out.println(
                		String.format("getUniqueId=%s\ngetOntologyTermName=%s\ngetComments=%s\ngetOntologyPurl=%s\ngetOntologySource=%s\ngetOntologySourceAccession=%s\ngetOntologyVersionId=%s\ngetOntologySourceInformation=%s\n\n",
                		term.getUniqueId(),
                		term.getOntologyTermName(),
                		term.getComments(),
                		term.getOntologyPurl(),
                		term.getOntologySource(),
                		term.getOntologySourceAccession(),
                		term.getOntologyVersionId(),
                		term.getOntologySourceInformation()
                		));
                System.out.println(term.getComments()==null?"":term.getComments());
            }
        }
    }
    
    @Test
    public void getKeywords() {
        ToxBankRESTClient client = new ToxBankRESTClient();
        Map<String, RecommendedOntology> recommendedOntologies = new HashMap<String, RecommendedOntology>();
        recommendedOntologies.put("TBK",new RecommendedOntology(new Ontology("TBK","0.1","TBK","TBK")));
        Map<OntologySourceRefObject, List<OntologyTerm>> result = 
        	
        			client.searchRepository("staining",recommendedOntologies,false);


        for (OntologySourceRefObject source : result.keySet()) {
            System.out.println("For " + source.getSourceName());

            for(OntologyTerm term : result.get(source)) {
            	 System.out.println(String.format("%s\tLabel=%s\t(%s)\t%s",
            			 term.getUniqueId(),
            			 term.getOntologySourceAccession(),
            			 term.getComments().get("Alias"),
            			 term.getComments().get("Definition")
            			 ));
            	/*
                System.out.println(
                		String.format("getUniqueId=%s\ngetOntologyTermName=%s\ngetComments=%s\ngetOntologyPurl=%s\ngetOntologySource=%s\ngetOntologySourceAccession=%s\ngetOntologyVersionId=%s\ngetOntologySourceInformation=%s\n\n",
                		term.getUniqueId(),
                		term.getOntologyTermName(),
                		term.getComments(),
                		term.getOntologyPurl(),
                		term.getOntologySource(),
                		term.getOntologySourceAccession(),
                		term.getOntologyVersionId(),
                		term.getOntologySourceInformation()
                		));
                System.out.println(term.getComments()==null?"":term.getComments());
                */
            }
        }
        System.out.println("There are " + result.size() + " results");
    }

}
