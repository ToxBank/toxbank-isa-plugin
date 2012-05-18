package net.toxbank.isa.creator.plugin.xml;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import net.toxbank.client.io.rdf.TOXBANK;

import org.isa2rdf.model.ISA;
import org.isatools.isacreator.ontologymanager.OntologySourceRefObject;
import org.isatools.isacreator.ontologymanager.common.OntologyTerm;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class KeywordsRDFHandler {
    public final static OntologySourceRefObject source = new OntologySourceRefObject("TBK", "", "", "Toxbank keywords");

    public List<OntologyTerm> parse(String xml,String term) throws Exception {
    	Model model = ModelFactory.createDefaultModel();    	
    	if (!(xml.startsWith("http") && !xml.startsWith("file"))) {
    		FileInputStream in = new FileInputStream(new File(xml));
    		try {
    			model.read(in, null);
    		} finally {
    			try {in.close();} catch (Exception x) {}
    		}
    	} else {
        	model.read(xml);    		
    	}
    	
    	return query(model,term);
    
       
    }
    
	final static String sparqlQuery = 
			"PREFIX tb:<%s>\n"+
			"PREFIX isa:<%s>\n"+
			"PREFIX skos:<http://www.w3.org/2004/02/skos/core#>\n"+
			"PREFIX dcterms:<http://purl.org/dc/terms/>\n"+
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
			"PREFIX owl:<http://www.w3.org/2002/07/owl#>\n"+
			"SELECT ?keyword ?prefLabel ?definition ?seeAlso ?sameAs where {\n" +
			" ?keyword rdf:type skos:Concept.\n" +
			"OPTIONAL { " +
			"	?keyword skos:prefLabel ?prefLabel." +
			"}\n" +
			"OPTIONAL { " +
			"	?keyword skos:definition ?definition." +
			"}\n" +
		//	"OPTIONAL { ?keyword skos:altLabel ?altLabel.}\n" +
			"OPTIONAL { ?keyword rdfs:seeAlso ?seeAlso}.\n" +
			"OPTIONAL { ?keyword owl:sameAs ?sameAs}.\n" +
			"FILTER regex(?definition,\"%s\",\"i\")\n" +
		//	"FILTER regex(?prefLabel,\"%s\",\"i\")\n" +
			"} order by ?prefLabel \n"
			;
	
	protected List<OntologyTerm> query(Model model,String term) throws Exception {
		List<OntologyTerm> terms = new ArrayList<OntologyTerm>();

		Query query = QueryFactory.create(String.format(sparqlQuery,TOXBANK.URI,ISA.URI,term));
		QueryExecution qe = QueryExecutionFactory.create(query,model);
		ResultSet rs = qe.execSelect();
		int n = 0;
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			RDFNode a = qs.get("keyword");

			RDFNode b = qs.get("prefLabel");
			RDFNode c = qs.get("definition");
			//RDFNode d = qs.get("altLabel");
			RDFNode e = qs.get("seeAlso");
			RDFNode f = qs.get("sameAs");

			
        	OntologyTerm oterm = new OntologyTerm(b==null?null:b.isLiteral()?((Literal)b).getString():"",null,source);
        	
        	if (a!=null) {
        		oterm.setOntologySourceAccession(a.isURIResource()?((Resource)a).getURI():"");
        		oterm.setOntologyPurl(source.getSourceFile()+"/");
        		oterm.setOntologySourceAccession(a.isURIResource()?((Resource)a).getURI():"");
        		oterm.setOntologyTermName(String.format("%s:%s",source.getSourceName(),oterm.getOntologySourceAccession()));
                
        	}
        	if (b!=null) {
        		oterm.addToComments("Label", b.isLiteral()?((Literal)b).getString():"");
        		oterm.setOntologySourceAccession( b.isLiteral()?((Literal)b).getString():"");
        	}
        	if (c!=null) oterm.addToComments("Definition", c.isLiteral()?((Literal)c).getString():"");
        	//if (d!=null) oterm.addToComments("Alt label", d.isLiteral()?((Literal)d).getString():"");
        	if (e!=null) oterm.addToComments("See also", 
        			String.format("<html><a href='%s'>%s</a></html>", e.toString(),e.toString()));
        	if (f!=null) oterm.addToComments("Same as", 
        			String.format("<html><a href='%s'>%s</a></html>", f.toString(),f.toString()));        	
            terms.add(oterm);
			n++;
		}
		qe.close();
		return terms;	
	}
}
