package org.oapen.memoproject.dataingestion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.bind.JAXBContext;
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
import org.springframework.data.mapping.MappingException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XpathElementToEntitiesMapper implements ElementToEntitiesMapper {
	
	private final Element element;
	private final XPath xpath;

	public XpathElementToEntitiesMapper(Element element) {
		
		this.element = element;
		xpath = XPathFactory.newInstance().newXPath();
	}
	
	
	private Set<String> getValueSet(String xpathQuery) throws XPathExpressionException {

		NodeList nodes = (NodeList) xpath.evaluate(xpathQuery, element, XPathConstants.NODESET);
		
		Set<String> values = new HashSet<>();
		
		for (int i=0; i < nodes.getLength(); i++) {
        	
        	Node node = nodes.item(i);
        	values.add(node.getTextContent());
        }
		
		return values;
	}
	
	private Optional<String> getValue(String xpathQuery, String errorMessage) {

		Node node;
		
		try {
			node = (Node) xpath.evaluate(xpathQuery, element, XPathConstants.NODE);
			
			if (node != null) 
				return Optional.of(node.getTextContent());
			else 
				return Optional.empty();
			
		} catch (XPathExpressionException e) {
			
			throw new MappingException(errorMessage);
		}
		
	}
	

	@Override
	public Set<Classification> getClassifications() {
		
		final String path = ".//element[@name='classification']//field/text()";
		
		try {
			NodeList nodes = (NodeList) xpath.evaluate(path, element, XPathConstants.NODESET);
			List<String> lines = new ArrayList<>(); 

			for (int i=0; i<nodes.getLength(); i++) 
				lines.add(nodes.item(i).getTextContent());
			
			return MapperUtils.parseClassifications(lines);
			
		} catch (Exception e) {
			throw new MappingException("Could not parse classifications");
		}
	}
	
	
	@Override
	public Set<Contributor> getContributors() {
		
		final String path = ".//element[@name='contributor']//field[@name='value']";

		try {
			Set<Contributor> contributors = new HashSet<>();
			NodeList nodes = (NodeList) xpath.evaluate(path, element, XPathConstants.NODESET);
			
			for (int i=0; i<nodes.getLength(); i++) 
				contributors.add( new Contributor(nodes.item(i).getTextContent()));
			
			return contributors;
		}	
		catch (Exception e)	{
			throw new MappingException("Could not parse contributors");
		}
	}
	
	
	private Set<Contribution> nodeListToContributionSet(NodeList nodes, String role) {
		
		Set<Contribution> set = new HashSet<>();
		
		for (int i=0; i < nodes.getLength(); i++) {
        	
        	Node node = nodes.item(i);
        	Contribution member = new Contribution(node.getTextContent(),role);
        	set.add(member);
        }
		
		return set;
	}
	
	@Override
	public Set<Contribution> getContributions() {
		
		try {
			Set<Contribution> contributions = new HashSet<>();
			NodeList nodes = (NodeList) xpath.evaluate(".//element[@name='contributor']/element[@name='author']//field[@name='value']", element, XPathConstants.NODESET);
			contributions.addAll(nodeListToContributionSet(nodes, "author"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='contributor']/element[@name='editor']//field[@name='value']", element, XPathConstants.NODESET);
			contributions.addAll(nodeListToContributionSet(nodes, "editor"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='contributor']/element[@name='advisor']//field[@name='value']", element, XPathConstants.NODESET);
			contributions.addAll(nodeListToContributionSet(nodes, "advisor"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='contributor']/element[@name='other']//field[@name='value']", element, XPathConstants.NODESET);
			contributions.addAll(nodeListToContributionSet(nodes, "other"));
			return contributions;
		}	
		catch (Exception e)	{
			throw new MappingException("Could not parse contributors");
		}
	}
	

	@Override
	public Set<Funder> getFunders() {
		
		final String path = ".//element[@name='oapen.relation.isFundedBy']"; 

		try {
			NodeList nodes = (NodeList) xpath.evaluate(path, element, XPathConstants.NODESET);
			JAXBContext jaxbContext = JAXBContext.newInstance(Funder.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			Set<Funder> funders = new HashSet<>();
			
			for (int i=0; i < nodes.getLength(); i++) {
	        	
	        	Node node = nodes.item(i);
	        	Funder funder = (Funder) unmarshaller.unmarshal(node);
	        	funders.add(funder);
	        }
			
			return funders;
		}	
		catch (Exception e)	{
			throw new MappingException("Could not parse funders");
		}
	}
	
	
	private Set<GrantData> nodeListToGrantDataSet(NodeList nodes, String property) {
		
		Set<GrantData> set = new HashSet<>();
		
		for (int i=0; i < nodes.getLength(); i++) {
        	
        	Node node = nodes.item(i);
        	String value = node.getTextContent();
        	if (!value.equals("[...]")) {  // ignore incomplete data
        		GrantData member = new GrantData(property, node.getTextContent());
        		set.add(member);
        	}	
        }
		
		return set;
	}
	
	// TODO Grant data: grant.number, grant.program, grant.project and grant.acronym
	@Override
	public Set<GrantData> getGrantData() {
		
		try {
			Set<GrantData> grantdata = new HashSet<>();
			
			NodeList nodes = (NodeList) xpath.evaluate(".//element[@name='oapen.grant.number']//field[@name='originalValue']", element, XPathConstants.NODESET);
			grantdata.addAll(nodeListToGrantDataSet(nodes, "number"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='grant']/element[@name='number']//field[@name='value']", element, XPathConstants.NODESET);
			grantdata.addAll(nodeListToGrantDataSet(nodes, "number"));
			
			nodes = (NodeList) xpath.evaluate(".//element[@name='oapen.grant.program']//field[@name='originalValue']", element, XPathConstants.NODESET);
			grantdata.addAll(nodeListToGrantDataSet(nodes, "program"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='grant']/element[@name='program']//field[@name='value']", element, XPathConstants.NODESET);
			grantdata.addAll(nodeListToGrantDataSet(nodes, "program"));
			
			nodes = (NodeList) xpath.evaluate(".//element[@name='grant']/element[@name='project']//field[@name='value']", element, XPathConstants.NODESET);
			grantdata.addAll(nodeListToGrantDataSet(nodes, "project"));
			
			nodes = (NodeList) xpath.evaluate(".//element[@name='grant']/element[@name='acronym']//field[@name='value']", element, XPathConstants.NODESET);
			grantdata.addAll(nodeListToGrantDataSet(nodes, "acronym"));

			return grantdata;
		}	
		catch (Exception e)	{
			throw new MappingException("Could not parse grant data");
		}
	}
	
	
	private Set<Identifier> nodeListToIdentifierSet(NodeList nodes, String type) {
		
		Set<Identifier> set = new HashSet<>();
		
		for (int i=0; i < nodes.getLength(); i++) {
        	
        	Node node = nodes.item(i);
        	Identifier member = new Identifier(node.getTextContent(),type);
        	set.add(member);
        }
		
		return set;
	}
 	
	@Override
	public Set<Identifier> getIdentifiers() {
		
		try {
			Set<Identifier> identifiers = new HashSet<>();
			NodeList nodes = (NodeList) xpath.evaluate(".//element[@name='identifier']//field[starts-with(text(),'ONIX')]", element, XPathConstants.NODESET);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"ONIX"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='identifier']//field[starts-with(text(),'OCN')]", element, XPathConstants.NODESET);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"OCN"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='doi']//field[@name='value']", element, XPathConstants.NODESET);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"DOI"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='uri']//field[@name='value']", element, XPathConstants.NODESET);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"URI"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='ocn']//field[@name='value']", element, XPathConstants.NODESET);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"OCN"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='isbn']//field[@name='value']", element, XPathConstants.NODESET);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"ISBN"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='issn']//field[@name='value']", element, XPathConstants.NODESET);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"ISSN"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='identifier']/element[@name='none']//field[@name='value']", element, XPathConstants.NODESET);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"UNKNOWN"));
			nodes = (NodeList) xpath.evaluate(".//element[@name='identifier']//field[@name='value']", element, XPathConstants.NODESET);
			identifiers.addAll(nodeListToIdentifierSet(nodes,"UNKNOWN"));
			
			return identifiers;
		}	
		catch (Exception e)	{
			throw new MappingException("Could not parse identifiers");
		}
		
	}

	@Override
	public Set<ExportChunk> getExportChunks() {
		
		String path = ".//*[.='EXPORT']/..//element[@name='bitstream']/field[@name='url']";
		
		NodeList nodes = null;

		try {
			Set<ExportChunk> set = new HashSet<>();
			
			nodes = (NodeList) xpath.evaluate(path, element, XPathConstants.NODESET);

			for (int i=0; i < nodes.getLength(); i++) {
	        	
	        	Node node = nodes.item(i);
	        	String url = node.getTextContent();
	        	ExportChunk member = new ExportChunk(MapperUtils.exportChunkType(url), url);
	        	set.add(member);
	        }
			return set;
			
		}	
		catch (Exception e)	{
			throw new MappingException("Could not parse exportChunks " + MapperUtils.stringify(nodes));
		}
	}

	@Override
	public Set<String> getLanguages() {
		
		try {
			return getValueSet(".//element[@name='language']//field[@name='value']");
		} catch (Exception e) {
			throw new MappingException("Could not parse languages");
		}
	}

	@Override
	public Set<String> getSubjectsOther() {

		try {
			return getValueSet(".//element[@name='subject']//element[@name='other']//field[@name='value']");
		} catch (Exception e) {
			throw new MappingException("Could not parse other subjects");
		}
	}

	@Override
	public Set<LocalDate> getDatesAccessioned() {
		
		NodeList nodes = null;

		try {
			nodes = (NodeList) xpath.evaluate(".//element[@name='date']/element[@name='accessioned']//field[@name='value']", element, XPathConstants.NODESET);
			
			Set<LocalDate> set = new HashSet<>();
			
			for (int i=0; i < nodes.getLength(); i++) {
	        	
	        	Node node = nodes.item(i);
	        	String dateText = node.getTextContent();
	        	LocalDate date = MapperUtils.parseDate(dateText).orElseThrow();
	        	set.add(date);
	        }
			
			return set;
		} catch (Exception e) {
			throw new MappingException("Could not parse accessioned dates " + MapperUtils.stringify(nodes));
		}
	}

	@Override
	public Optional<Publisher> getPublisher() {
		
		try {
			NodeList nodes = (NodeList) xpath.evaluate(".//element[@name='oapen.relation.isPublishedBy']", element, XPathConstants.NODESET);
			JAXBContext jaxbContext = JAXBContext.newInstance(Publisher.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			if (nodes.getLength() > 0) {
	        	
	        	Node node = nodes.item(0);
	        	Publisher publisher = (Publisher) unmarshaller.unmarshal(node);
	        	return Optional.of(publisher);
	        }
			else return Optional.empty(); 
		}	
		catch (Exception e)	{
			throw new MappingException("Could not parse publisher");
		}
		
	}
	

	@Override
	public Optional<String> getHandle() {
		
		final String path = ".//element[@name='others']/field[@name='handle']";
		final String msg = "Could not parse handle";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getSysId() {
		
		final String path = ".//element[@name='others']/field[@name='uuid']";
		final String msg = "Could not parse sysId (uuid)";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getCollection() {

		final String path = ".//header/setSpec[starts-with(text(),'col')]";
		final String msg = "Could not parse collection";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getDownloadUrl() {
		
		final String path = ".//*[.='ORIGINAL']/..//element[@name='bitstream']/field[@name='url']";
		final String msg = "Could not parse downloadUrl";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getThumbnail() {
		
		final String path = ".//*[.='THUMBNAIL']/..//element[@name='bitstream']/field[@name='url']";
		final String msg = "Could not parse thumbnail";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getLicense() {
		
		final String path = ".//*[.='ORIGINAL']/..//element[@name='bitstream']/field[@name='rightsuri']";
		final String msg = "Could not parse license";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getWebshopUrl() {
		
		final String path = ".//*[.='ORIGINAL']/..//element[@name='bitstream']/field[@name='dcidentifierurlwebshop']";
		final String msg = "Could not parse webshopUrl";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getDateAvailable() {
		
		final String path = ".//element[@name='dc']/element[@name='date']/element[@name='available']//field[@name='value']";
		final String msg = "Could not parse dateAvailable";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getDateIssued() {
		
		final String path = ".//element[@name='dc']/element[@name='date']/element[@name='issued']//field[@name='value']";
		final String msg = "Could not parse dateIssued";
		return getValue(path, msg);
	}

	// TODO This field seems always empty
	@Override
	public Optional<String> getDescription() {
		
		final String path = ".//TODO";
		final String msg = "Could not parse description";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getDescriptionOtherLanguage() {
		
		final String path = ".//element[@name='oapen']/element[@name='description']/element[@name='otherlanguage']//field[@name='value']";
		final String msg = "Could not parse descriptionOtherLanguage";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getDescriptionAbstract() {
		
		final String path = ".//element[@name='dc']/element[@name='description']/element[@name='abstract']//field[@name='value']";
		final String msg = "Could not parse descriptionAbstract";
		return getValue(path, msg);
	}

	// TODO Not available in XOAI output
	@Override
	public Optional<String> getDescriptionProvenance() {
		
		final String path = ".//TODO";
		final String msg = "Could not parse descriptionProvenance";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getTermsAbstract() {
		
		final String path = ".//element[@name='dcterms']/element[@name='abstract']//field[@name='value']";
		final String msg = "Could not parse termsAbstract";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getAbstractOtherLanguage() {
		
		final String path = ".//element[@name='oapen']/element[@name='abstract']/element[@name='otherlanguage']//field[@name='value']";
		final String msg = "Could not parse abstractOtherLanguage";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getPartOfSeries() {
		
		final String path = ".//element[@name='ispartofseries']//field[@name='value']";
		final String msg = "Could not parse partOfSeries";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getTitle() {
		
		final String path = ".//element[@name='dc']/element[@name='title']//field[@name='value']";
		final String msg = "Could not parse title";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getTitleAlternative() {
		
		final String path = ".//element[@name='dc']/element[@name='title']/element[@name='alternative']//field[@name='value']";
		final String msg = "Could not parse titleAlternative";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getType() {
		
		final String path = ".//element[@name='dc']/element[@name='type']//field[@name='value']";
		final String msg = "Could not parse type";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getChapterNumber() {
		
		final String path = ".//element[@name='chapternumber']//field[@name='value']";
		final String msg = "Could not parse chapterNumber";
		return getValue(path, msg);
	}

	// TODO This field is always empty
	@Override
	public Optional<String> getEmbargo() {
		
		final String path = ".//TODO";
		final String msg = "Could not parse embargo";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getImprint() {
		
		final String path = ".//element[@name='imprint']//field[@name='value']";
		final String msg = "Could not parse imprint";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getPages() {
		
		final String path = ".//element[@name='pages']//field[@name='value']";
		final String msg = "Could not parse 'pages'";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getPlacePublication() {
		
		final String path = ".//element[@name='place']/element[@name='publication']//field[@name='value']";
		final String msg = "Could not parse placePublication";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getSeriesNumber() {
		
		final String path = ".//element[@name='series']/element[@name='number']//field[@name='value']";
		final String msg = "Could not parse seriesNumber";
		return getValue(path, msg);
	}

	@Override
	public Optional<String> getPartOfBook() {
		
		final String path = ".//element[@name='oapen.relation.isPartOfBook']/field[@name='handle']";
		final String msg = "Could not parse partOfBook";
		return getValue(path, msg);
	}

}
