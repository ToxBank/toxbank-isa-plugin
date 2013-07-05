package net.toxbank.isa.creator.plugin.xml;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import net.toxbank.client.io.rdf.TOXBANK;

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
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class KeywordsRDFHandler {
    public final static OntologySourceRefObject source = new OntologySourceRefObject("TBK", "", "", "Toxbank keywords");
    protected Model model = null;
    public List<OntologyTerm> parse(String xml,String term) throws Exception {
    	if (model==null) {
    		model = ModelFactory.createDefaultModel();    	
	    	if (!(xml.startsWith("http") && !xml.startsWith("file"))) {
	    		File file = new File(xml);
	    		//System.out.println(file.getAbsolutePath());
	    		FileInputStream in = new FileInputStream(file);
	    		try {
	    			model.read(in, null);
	    		} finally {
	    			try {in.close();} catch (Exception x) {}
	    		}
	    	} else {
	        	model.read(xml);    		
	    	}
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
			"SELECT distinct ?categoryLabel ?keyword ?prefLabel ?definition ?seeAlso ?sameAs where {\n" +
			" ?keyword rdf:type skos:Concept.\n" +
			"OPTIONAL { " +
			"	?keyword skos:prefLabel ?prefLabel." +
			"}\n" +			
			"OPTIONAL { " +
			"	?keyword skos:broader ?category." +
			"	?category skos:prefLabel ?categoryLabel." +
			"}\n" +
			"OPTIONAL { " +
			"	?keyword skos:definition ?definition." +
			"}\n" +
			"OPTIONAL { ?keyword skos:altLabel ?altLabel.}\n" +
			"OPTIONAL { ?keyword rdfs:seeAlso ?seeAlso}.\n" +
			"OPTIONAL { ?keyword owl:sameAs ?sameAs}.\n" +
			"FILTER (" +
			"    regex(?prefLabel,\"%s\",\"i\")\n" +
			" || regex(?definition,\"%s\",\"i\")\n " +
			" || regex(?sameAs,\"%s\",\"i\")\n " +
			" || regex(?seeAlso,\"%s\",\"i\")\n " +
			" || regex(?categoryLabel,\"%s\",\"i\")\n " +
			" || regex(?altLabel, \"%s\",\"i\")\n " +
			")\n" +
			"} order by ?categoryLabel \n"
			;
	public static final String ISA_URI ="http://onto.toxbank.net/isa/";
	protected List<OntologyTerm> query(Model model,String term) throws Exception {
		List<OntologyTerm> terms = new ArrayList<OntologyTerm>();

		//term = term.toLowerCase();
		String sparql = String.format(sparqlQuery,TOXBANK.URI,ISA_URI,term,term,term,term,term,term);
		//System.out.println(sparql);
		Query query = QueryFactory.create(sparql);
		QueryExecution qe = QueryExecutionFactory.create(query,model);
		ResultSet rs = qe.execSelect();
		int n = 0;
		Property altLabel = model.createProperty("http://www.w3.org/2004/02/skos/core#","altLabel");
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			RDFNode a = qs.get("keyword");
			
			RDFNode b = qs.get("prefLabel");
			RDFNode c = qs.get("definition");
			//RDFNode d = qs.get("altLabel");
			RDFNode e = qs.get("seeAlso");
			RDFNode f = qs.get("sameAs");
			
			RDFNode category = qs.get("categoryLabel");

			
        	OntologyTerm oterm = new OntologyTerm(b==null?null:b.isLiteral()?((Literal)b).getString():"",null,"",source);
        	
        	String categoryLabel = category==null?null:category.isResource()?((Resource)category).getURI():
        			category.isLiteral()?((Literal)category).getString():category.toString();
        	
        	if (categoryLabel!=null) oterm.addToComments("Category", categoryLabel);
        	if (a!=null) {
        		oterm.setOntologyTermAccession(a.isURIResource()?((Resource)a).getURI():"");
        		oterm.setOntologyPurl(source.getSourceFile()+"/");
        		oterm.setOntologyTermName(null);
        		oterm.setOntologyTermAccession(a.isURIResource()?((Resource)a).getURI():"");
        		
        		
        		StringBuilder labels = new StringBuilder();
        		StmtIterator sti = model.listStatements((Resource)a,altLabel,(Literal)null);
        		int i=0;
    			while (sti.hasNext()) {
    				Statement st = sti.next();
    				RDFNode label = st.getObject();
    				if (label.isLiteral()) {
    					if (i>0) labels.append(",");
    					labels.append(((Literal)label).getString());
    					i++;
    				}
    			}
				if (i>0) oterm.addToComments("Alias",labels.toString());
				sti.close();
                
        	}
        	if (b!=null) {
        		oterm.addToComments("Label", b.isLiteral()?((Literal)b).getString():"");
        		oterm.setOntologyTermAccession(String.format("[%s] %s",
        						categoryLabel==null?"":categoryLabel, 
        						b.isLiteral()?((Literal)b).getString():""));
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
