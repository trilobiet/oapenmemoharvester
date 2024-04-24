package org.oapen.memoproject.dataingestion.harvest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Contribution;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.GrantData;
import org.oapen.memoproject.dataingestion.jpa.entities.Identifier;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.oapen.memoproject.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parses an XOAI Record Element as a Document and produces JPA Entities 
 * for Classifications, Contributors, Funders, Publisher and Title
 *  
 * @author acdhirr
 *
 */
public final class XOAIDocumentParser implements EntitiesSource {
	
	private final Element element;
	private final XPath xpath;

	
	public XOAIDocumentParser(Element element) {
		
		this.element = element;
		xpath = XPathFactory.newInstance().newXPath();
	}
	
	private static final Logger logger = 
			LoggerFactory.getLogger(XOAIDocumentParser.class);
	
	
	@Override
	/**
	 * Extract Set of Classifications (0..n) from this document 
	 */
	public Set<Classification> getClassifications() {
		
		final String path = ".//element[@name='classification']//field/text()";
		Set<Classification> classifications = new HashSet<>();
		
		getNodeList(path).ifPresent(nodes -> {
			
			List<String> lines = new ArrayList<>(); 

			for (int i=0; i<nodes.getLength(); i++) 
				lines.add(
					StringUtils.trimAllSpace(nodes.item(i).getTextContent())
				);
			
			classifications.addAll(XOAIDocumentParserUtils.parseClassifications(lines));
		}); 
		
		return classifications;
	}
	
	
	@Override
	/**
	 * Extract Set of Contributors (0..n) from this document 
	 */
	public Set<Contributor> getContributors() {
		
		final String path = ".//element[@name='contributor']//field[@name='value']";
		Set<Contributor> contributors = new HashSet<>();
		
		getNodeList(path).ifPresent(nodes -> {
			for (int i=0; i<nodes.getLength(); i++) 
				contributors.add( 
					new Contributor(
						StringUtils.trimAllSpace(nodes.item(i).getTextContent())
					)
				);
		});

		return contributors;
	}
		
	
	@Override
	/**
	 * Extract Set of Funders from (0..n) this document 
	 */
	public Set<Funder> getFunders() {
		
		final String path = ".//element[@name='oapen.relation.isFundedBy']"; 
		Set<Funder> funders = new HashSet<>();
		
		getNodeList(path).ifPresent(nodes -> {
		
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Funder.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				
				for (int i=0; i < nodes.getLength(); i++) {
		        	
		        	Node node = nodes.item(i);
		        	Funder funder = (Funder) unmarshaller.unmarshal(node);
		        	if (funder.isComplete()) funders.add(funder);
		        }
			} catch (JAXBException e) {
				logger.error("Could not unmarshall funder(s). " + e.getMessage());
			}
				
		});
		
		return funders;
	}	
	
	
	@Override
	/**
	 * Extract Optional Publisher (0..1) from this document 
	 */
	public Optional<Publisher> getPublisher() {
		
		final String path = ".//element[@name='oapen.relation.isPublishedBy']";
		
		Optional<Publisher> p = Optional.empty();
		Optional<Node> q = getNode(path);
		
		try {
			if (q.isPresent()) {
				Node node = q.get();  
				JAXBContext jaxbContext = JAXBContext.newInstance(Publisher.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				Publisher publisher = (Publisher) unmarshaller.unmarshal(node);
				if (publisher.isComplete()) p = Optional.of(publisher);
			}
		}
		catch (Exception e) {
			logger.error("Could not unmarshall publisher. " + e.getMessage());
		}
		
		return p;
	}
	
	
	@Override
	/**
	 * Extract Optional Title (0..1) from this document. Optional, because theoretically
	 * a handle could not be unfindable or unparsable.  
	 */
	public Optional<Title> getTitle() {
	
		Optional<Title> r = Optional.empty();

		Optional<String> handle = getHandle();
		Optional<String> status = getStatus();
		
		if (handle.isPresent()) {
		
			Title title = new Title(getHandle().get());
			
			// Skip all fields except status for elements that have status "deleted" 
			if (status.isPresent() && status.get().equals("deleted")) {
				
				title.setStatus("deleted");
			}	
			else {	
				
				getAbstractOtherLanguage().ifPresent(title::setAbstractOtherLanguage);
				getChapterNumber().ifPresent(title::setChapterNumber);
				//getCollection().ifPresent(title::setCollection);
				getYearAvailable().ifPresent(title::setYearAvailable);
				getDescriptionAbstract().ifPresent(title::setDescriptionAbstract);
				getDescriptionOtherLanguage().ifPresent(title::setDescriptionOtherLanguage);
				getDownloadUrl().ifPresent(title::setDownloadUrl);
				getImprint().ifPresent(title::setImprint);
				getLicense().ifPresent(title::setLicense);
				getPages().ifPresent(title::setPages);
				getPartOfBook().ifPresent(title::setPartOfBook);
				getPartOfSeries().ifPresent(title::setPartOfSeries);
				getPlacePublication().ifPresent(title::setPlacePublication);
				getSeriesNumber().ifPresent(title::setSeriesNumber);
				getSysId().ifPresent(title::setSysId);
				getTermsAbstract().ifPresent(title::setTermsAbstract);
				getThumbnail().ifPresent(title::setThumbnail);
				getTitleTitle().ifPresent(title::setTitle);
				getTitleAlternative().ifPresent(title::setTitleAlternative);
				getType().ifPresent(title::setType);
				getWebshopUrl().ifPresent(title::setWebshopUrl);
				
				getPublisher().ifPresent(title::setPublisher);
				getYearAvailable().ifPresent(title::setYearAvailable);
				
				title.setClassifications(getClassifications());
				title.setContributions(getContributions());
				title.setExportChunks(getExportChunks());
				title.setFunders(getFunders());
				title.setGrantData(getGrantData());
				title.setIdentifiers(getIdentifiers());
				title.setLanguages(getLanguages());
				title.setSubjectsOther(getSubjectsOther());
				title.setCollections(getCollections());
			}	
			
			r = Optional.of(title);
		}	
		
		return r;
	}
		
	
	private Optional<Node> getNode(String xpathQuery) {
		
		try {
			Node n = (Node) xpath.evaluate(xpathQuery, element, XPathConstants.NODE);
			if (n != null) return Optional.of(n);
			else return Optional.empty();
		} catch (XPathExpressionException e) {
			// XPATH expressions are hard coded, so if they erred we would already know
			return Optional.empty();
		}
	}
	
	
	private Optional<NodeList> getNodeList(String xpathQuery) {
		
		try {
			return Optional.of( (NodeList) xpath.evaluate(xpathQuery, element, XPathConstants.NODESET) );
		} catch (XPathExpressionException e) {
			// XPATH expressions are hard coded, so if they erred we would already know
			return Optional.empty();
		}
	}
	
	
	private Set<String> getTextValueSet(String xpathQuery) {
		
		Set<String> values = new HashSet<>();
		
		getNodeList(xpathQuery).ifPresent(nodes -> {

			for (int i=0; i < nodes.getLength(); i++) {
	        	
	        	Node node = nodes.item(i);
	        	values.add(
	        		StringUtils.trimAllSpace(node.getTextContent())
	        	);
	        }
		});
		
		return values;
	}
	
	
	private Optional<String> getTextValue(String xpathQuery) {

		Optional<Node> node = getNode(xpathQuery);
		
		if (node.isPresent())
			return Optional.of(
				StringUtils.trimAllSpace(node.get().getTextContent())
			);
		else 
			return Optional.empty();
	}
	
	
	private Set<Contribution> nodeListToContributionSet(NodeList nodes, String role) {
		
		Set<Contribution> set = new HashSet<>();
		
		for (int i=0; i < nodes.getLength(); i++) {
        	
        	Node node = nodes.item(i);
        	Contribution member = new Contribution(
        		StringUtils.trimAllSpace(node.getTextContent().trim())
        		,role
        	);
        	set.add(member);
        }
		
		return set;
	}
	
	
	private Set<Contribution> getContributions() {
		
		final String authorPath = ".//element[@name='contributor']/element[@name='author']//field[@name='value']";
		final String editorPath = ".//element[@name='contributor']/element[@name='editor']//field[@name='value']";
		final String advisorPath = ".//element[@name='contributor']/element[@name='advisor']//field[@name='value']";
		final String otherPath = ".//element[@name='contributor']/element[@name='other']//field[@name='value']";

		Set<Contribution> contributions = new HashSet<>();
		
		getNodeList(authorPath).ifPresent(nodes -> contributions.addAll(nodeListToContributionSet(nodes, "author")));
		getNodeList(editorPath).ifPresent(nodes -> contributions.addAll(nodeListToContributionSet(nodes, "editor")));
		getNodeList(advisorPath).ifPresent(nodes -> contributions.addAll(nodeListToContributionSet(nodes, "advisor")));
		getNodeList(otherPath).ifPresent(nodes -> contributions.addAll(nodeListToContributionSet(nodes, "other")));
		
		return contributions;
	}
	

	private Set<GrantData> nodeListToGrantDataSet(NodeList nodes, String property) {
		
		Set<GrantData> set = new HashSet<>();
		
		for (int i=0; i < nodes.getLength(); i++) {
        	
        	Node node = nodes.item(i);
        	String value = node.getTextContent();
        	if (!value.isBlank() && !value.equals("[...]")) {  // ignore incomplete data
        		GrantData member = new GrantData(
        			property, 
        			StringUtils.cutOff(StringUtils.trimAllSpace(node.getTextContent().trim()), 255) // max length 255
        		);
        		set.add(member);
        	}	
        }
		
		return set;
	}
	
	
	// Grant data: grant.number, grant.program, grant.project and grant.acronym
	private Set<GrantData> getGrantData() {
		
		final String grantNumberPath1	= ".//element[@name='oapen.grant.number']//field[@name='originalValue']";
		final String grantNumberPath2	= ".//element[@name='grant']/element[@name='number']//field[@name='value']";
		final String grantProgramPath1	= ".//element[@name='oapen.grant.program']//field[@name='originalValue']";
		final String grantProgramPath2	= ".//element[@name='grant']/element[@name='program']//field[@name='value']";
		final String grantProjectPath	= ".//element[@name='grant']/element[@name='project']//field[@name='value']";
		final String grantAcronymPath	= ".//element[@name='grant']/element[@name='acronym']//field[@name='value']";
		
		Set<GrantData> grantdata = new HashSet<>();
		
		getNodeList(grantNumberPath1).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "number")));
		getNodeList(grantNumberPath2).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "number")));
		getNodeList(grantProgramPath1).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "program")));
		getNodeList(grantProgramPath2).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "program")));
		getNodeList(grantProjectPath).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "project")));
		getNodeList(grantAcronymPath).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "acronym")));

		return grantdata;
	}
	
	
	private Set<Identifier> nodeListToIdentifierSet(NodeList nodes, String type, Set<String> ignoreIds) {
		
		Set<Identifier> set = new HashSet<>();
		
		for (int i=0; i < nodes.getLength(); i++) {
        	
        	Node node = nodes.item(i);
        	String id = node.getTextContent().trim();
        	
        	if (!ignoreIds.contains(id)) {
	        	Identifier member = new Identifier(
	        		StringUtils.cutOff(StringUtils.trimAllSpace(node.getTextContent().trim()),100)
	        		,type
	        	);
	        	set.add(member);
        	}
        }
		
		return set;
	}
	
	private Set<Identifier> nodeListToIdentifierSet(NodeList nodes, String type) {
		
		return nodeListToIdentifierSet(nodes, type, new HashSet<>());
	}
	
 	
	private Set<Identifier> getIdentifiers() {
		
		final String pathONIX		= ".//element[@name='identifier']//field[starts-with(text(),'ONIX')]";
		final String pathDOI		= ".//element[@name='doi']//field[@name='value']";
		final String pathURI		= ".//element[@name='uri']//field[@name='value']";
		final String pathOCN1		= ".//element[@name='identifier']//field[starts-with(text(),'OCN')]";
		final String pathOCN2		= ".//element[@name='ocn']//field[@name='value']";
		final String pathISBN1		= ".//element[@name='isbn']//field[@name='value']";
		final String pathISBN2		= ".//element[@name='bitstream']//field[@name='oapenrelationisbn']";
		final String pathISSN		= ".//element[@name='issn']//field[@name='value']";
		final String pathUNKNOWN1	= ".//element[@name='identifier']/element[@name='none']//field[@name='value']";
		final String pathUNKNOWN2	= ".//element[@name='identifier']//field[@name='value']";
		
		Set<Identifier> identifiers = new HashSet<>();
		Set<String> usedIds = new HashSet<>();
		
		getNodeList(pathONIX).stream().forEach(nodes -> {
			Set<String> ids = getTextValueSet(pathONIX);
			usedIds.addAll(ids);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"ONIX"));
		});
		
		getNodeList(pathDOI).stream().forEach(nodes -> {
			Set<String> ids = getTextValueSet(pathDOI);
			usedIds.addAll(ids);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"DOI"));
		});
		
		getNodeList(pathURI).stream().forEach(nodes -> {
			Set<String> ids = getTextValueSet(pathURI);
			usedIds.addAll(ids);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"URI"));
		});
		
		getNodeList(pathOCN1).stream().forEach(nodes -> {
			Set<String> ocns = getTextValueSet(pathOCN1);
			usedIds.addAll(ocns);
			ocns.forEach(ocn -> identifiers.add(new Identifier(XOAIDocumentParserUtils.parseOCN(ocn),"OCN")));
		});
		
		getNodeList(pathOCN2).stream().forEach(nodes -> {
			Set<String> ocns = getTextValueSet(pathOCN2);
			usedIds.addAll(ocns);
			ocns.forEach(ocn -> identifiers.add(new Identifier(XOAIDocumentParserUtils.parseOCN(ocn),"OCN")));
		});

		getNodeList(pathISBN1).stream().forEach(nodes -> {
			Set<String> isbns = XOAIDocumentParserUtils.parseISBNOrISSN(getTextValueSet(pathISBN1));
			usedIds.addAll(isbns);
			isbns.forEach(isbn -> identifiers.add(new Identifier(isbn,"ISBN")));
		});
		
		getNodeList(pathISBN2).stream().forEach(nodes -> {
			Set<String> isbns = XOAIDocumentParserUtils.parseISBNOrISSN(getTextValueSet(pathISBN2));
			usedIds.addAll(isbns);
			isbns.forEach(isbn -> identifiers.add(new Identifier(isbn,"ISBN")));
		});

		getNodeList(pathISSN).stream().forEach(nodes -> {
			Set<String> issns = XOAIDocumentParserUtils.parseISBNOrISSN(getTextValueSet(pathISSN));
			usedIds.addAll(issns);
			issns.forEach(issn -> identifiers.add(new Identifier(issn,"ISSN")));
		});
		
		/* pathUNKNOWN1 and pathUNKNOWN2 will return ids that we already have parsed succesfully:
		 * they are stored in usedIds. Ignore these and what remains will be in UNKNOWN category
		 */
		
		getNodeList(pathUNKNOWN1).stream().forEach(nodes -> {
			Set<String> ids = getTextValueSet(pathUNKNOWN1);
			usedIds.addAll(ids);
			// This is an old OAPEN internal id: ignore
		});
		
		// Now what is left goes in UNKNOWN
		getNodeList(pathUNKNOWN2).stream().forEach(nodes -> identifiers.addAll(nodeListToIdentifierSet(nodes,"UNKNOWN", usedIds)));
		
		return identifiers;
	}

	
	private Set<ExportChunk> getExportChunks() {
		
		String path = ".//*[.='EXPORT']/..//element[@name='bitstream']/field[@name='url']";
		
		Set<ExportChunk> set = new HashSet<>();
		
		getNodeList(path).ifPresent(nodes -> {
			
			for (int i=0; i < nodes.getLength(); i++) {
	        	
	        	Node node = nodes.item(i);
	        	String url = StringUtils.trimAllSpace(node.getTextContent());
	        	ExportChunk member = new ExportChunk(XOAIDocumentParserUtils.exportChunkType(url), url);
	        	set.add(member);
	        }
		});
		
		return set;
	}

	
	private Set<String> getLanguages() {
		
		final String path = ".//element[@name='language']//field[@name='value']";
		return XOAIDocumentParserUtils.parseLanguages(getTextValueSet(path));
	}


	private Set<String> getCollections() {
		
		final String path = ".//element[@name='oapen']//element[@name='collection']//field[@name='value']";
		return getTextValueSet(path);
	}
	
	
	private Set<String> getSubjectsOther() {
		
		final String path = ".//element[@name='subject']//element[@name='other']//field[@name='value']";
		
		Set<String> q = getTextValueSet(path);
		
		if (!q.isEmpty()) return XOAIDocumentParserUtils.parseSubjects(q);
		else return q;
	}

	
	private Optional<Integer> getYearAvailable() {
		
		final String pathDateAccessioned = ".//element[@name='dc']/element[@name='date']/element[@name='accessioned']//field[@name='value']";
		final String pathDateAvailable = ".//element[@name='dc']/element[@name='date']/element[@name='available']//field[@name='value']";
		final String pathDateIssued = ".//element[@name='dc']/element[@name='date']/element[@name='issued']//field[@name='value']";
		
		List<String> paths = new ArrayList<>(Arrays.asList(pathDateAccessioned,pathDateAvailable,pathDateIssued));
		Set<Integer> years = new HashSet<>();
		
		for (String path: paths) {
			
			getNodeList(path).ifPresent(nodes -> {
				
				for (int i=0; i < nodes.getLength(); i++) {
					String datetext = StringUtils.trimAllSpace(nodes.item(i).getTextContent()); 
					XOAIDocumentParserUtils.yearFromString(datetext).ifPresent(years::add);
				}	
			});
		}

		// Get smallest (earliest year)
		if (years.isEmpty()) return Optional.empty();
		else return Optional.of( Collections.min(years) );
	}
	
	
	private Optional<String> getStatus() {
		
		final String path = ".//header/@status";
		return getTextValue(path);
	}
	

	private Optional<String> getHandle() {
		
		final String path = "./header/identifier";
		// Monadish isn't it?
		return getTextValue(path).flatMap(n -> XOAIDocumentParserUtils.extractHandleFromIdentifier(n));
	}

	
	private Optional<String> getSysId() {
		
		final String path = ".//element[@name='others']/field[@name='uuid']";
		return getTextValue(path);
	}

	
	private Optional<String> getDownloadUrl() {
		
		final String path = ".//*[.='ORIGINAL']/..//element[@name='bitstream']/field[@name='url']";
		return getTextValue(path);
	}

	
	private Optional<String> getThumbnail() {
		
		final String path = ".//*[.='THUMBNAIL']/..//element[@name='bitstream']/field[@name='url']";
		return getTextValue(path);
	}

	
	private Optional<String> getLicense() {
		
		final String path = ".//*[.='ORIGINAL']/..//element[@name='bitstream']/field[@name='rightsuri']";
		return getTextValue(path);
	}

	
	private Optional<String> getWebshopUrl() {
		
		final String path = ".//*[.='ORIGINAL']/..//element[@name='bitstream']/field[@name='dcidentifierurlwebshop']";
		return getTextValue(path);
	}

	
	private Optional<String> getDescriptionOtherLanguage() {
		
		final String path = ".//element[@name='oapen']/element[@name='description']/element[@name='otherlanguage']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getDescriptionAbstract() {
		
		final String path = ".//element[@name='dc']/element[@name='description']/element[@name='abstract']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getTermsAbstract() {
		
		final String path = ".//element[@name='dcterms']/element[@name='abstract']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getAbstractOtherLanguage() {
		
		final String path = ".//element[@name='oapen']/element[@name='abstract']/element[@name='otherlanguage']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getPartOfSeries() {
		
		final String path = ".//element[@name='ispartofseries']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getTitleTitle() {
		
		final String path = ".//element[@name='dc']/element[@name='title']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getTitleAlternative() {
		
		final String path = ".//element[@name='dc']/element[@name='title']/element[@name='alternative']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getType() {
		
		final String path = ".//element[@name='dc']/element[@name='type']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getChapterNumber() {
		
		final String path = ".//element[@name='chapternumber']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getImprint() {
		
		final String path = ".//element[@name='imprint']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getPages() {
		
		final String path = ".//element[@name='pages']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getPlacePublication() {
		
		final String path = ".//element[@name='place']/element[@name='publication']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getSeriesNumber() {
		
		final String path = ".//element[@name='series']/element[@name='number']//field[@name='value']";
		return getTextValue(path);
	}

	
	private Optional<String> getPartOfBook() {
		
		final String path = ".//element[@name='oapen.relation.isPartOfBook']/field[@name='handle']";
		return getTextValue(path);
	}

}
