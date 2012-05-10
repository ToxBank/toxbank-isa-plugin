package net.toxbank.isa.creator.plugin.xml;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import org.isatools.isacreator.ontologymanager.OntologySourceRefObject;
import org.isatools.isacreator.ontologymanager.common.OntologyTerm;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.utils.xml.XPathReader;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 12/09/2011
 *         Time: 16:44
 */
public class KeywordsXMLHandler {

    public final static OntologySourceRefObject source = new OntologySourceRefObject("TBK", "", "", "Toxbank keywords");

    public List<OntologyTerm> parseXML(String xml,String term) throws FileNotFoundException {
        XPathReader reader = new XPathReader(new FileInputStream(xml));

        List<OntologyTerm> terms = new ArrayList<OntologyTerm>();

        NodeList preferredTerms = (NodeList) reader.read("//section", XPathConstants.NODESET);

        if (preferredTerms.getLength() > 0) {
        	String uniqueName = null;
            for (int sectionIndex = 0; sectionIndex <= preferredTerms.getLength(); sectionIndex++) {
            	Node node = preferredTerms.item(sectionIndex);
            	if (node==null) continue;
            	NodeList children = node.getChildNodes();
            	uniqueName = null;
            	for (int i=0; i < children.getLength();i++) {
            		if ("unique_name".equals(children.item(i).getNodeName())) {
            			uniqueName = children.item(i).getTextContent();
            			break;
            		}
            	}
            	if (uniqueName==null) continue;
            	else uniqueName = uniqueName.toLowerCase(); 
            		
            	String id = String.format("%s",((Element) node).getAttribute("number").trim());
            	String token = ((Element) node).getAttribute("title").trim();
                if (!id.equalsIgnoreCase("") && token.toLowerCase().indexOf(term)>=0) {
                	OntologyTerm oterm = new OntologyTerm(null,null,source);
                	oterm.setOntologyTermName(uniqueName);
                	oterm.setOntologySourceAccession(token);
                    terms.add(oterm);
                }
            }

            return terms;
        }
        return new ArrayList<OntologyTerm>();
    }

}
