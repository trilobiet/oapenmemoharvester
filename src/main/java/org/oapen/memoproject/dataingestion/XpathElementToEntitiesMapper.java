package org.oapen.memoproject.dataingestion;

import java.time.LocalDate;
import java.util.ArrayList;
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
	        	values.add(node.getTextContent());
	        }
		});
		
		return values;
	}
	
	
	private Optional<String> getTextValue(String xpathQuery) {

		Optional<Node> node = getNode(xpathQuery);
		
		if (node.isPresent())
			return Optional.of(node.get().getTextContent());
		else 
			return Optional.empty();
	}
	

	@Override
	public Set<Classification> getClassifications() {
		
		final String path = ".//element[@name='classification']//field/text()";
		Set<Classification> classifications = new HashSet<>();
		
		getNodeList(path).ifPresent(nodes -> {
			
			List<String> lines = new ArrayList<>(); 

			for (int i=0; i<nodes.getLength(); i++) 
				lines.add(nodes.item(i).getTextContent());
			
			classifications.addAll(MapperUtils.parseClassifications(lines));
		}); 
		
		return classifications;
	}
	
	
	@Override
	public Set<Contributor> getContributors() {
		
		final String path = ".//element[@name='contributor']//field[@name='value']";
		Set<Contributor> contributors = new HashSet<>();
		
		getNodeList(path).ifPresent(nodes -> {
			for (int i=0; i<nodes.getLength(); i++) 
				contributors.add( new Contributor(nodes.item(i).getTextContent()));
		});

		return contributors;
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
	

	@Override
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
		        	funders.add(funder);
		        }
			} catch (JAXBException e) {
				// TODO log
			}
				
		});
		
		return funders;
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
		
		final String grantNumberPath1 = ".//element[@name='oapen.grant.number']//field[@name='originalValue']";
		final String grantNumberPath2 = ".//element[@name='grant']/element[@name='number']//field[@name='value']";
		final String grantProgramPath1 = ".//element[@name='oapen.grant.program']//field[@name='originalValue']";
		final String grantProgramPath2 = ".//element[@name='grant']/element[@name='program']//field[@name='value']";
		final String grantProjectPath = ".//element[@name='grant']/element[@name='project']//field[@name='value']";
		final String grantAcronymPath = ".//element[@name='grant']/element[@name='acronym']//field[@name='value']";
		
		Set<GrantData> grantdata = new HashSet<>();
		
		getNodeList(grantNumberPath1).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "number")));
		getNodeList(grantNumberPath2).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "number")));
		getNodeList(grantProgramPath1).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "program")));
		getNodeList(grantProgramPath2).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "program")));
		getNodeList(grantProjectPath).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "project")));
		getNodeList(grantAcronymPath).ifPresent(nodes -> grantdata.addAll(nodeListToGrantDataSet(nodes, "acronym")));

		return grantdata;
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
	
	
	
	// TODO rewrite below here 
	
 	
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
			return getTextValueSet(".//element[@name='language']//field[@name='value']");
		} catch (Exception e) {
			throw new MappingException("Could not parse languages");
		}
	}

	@Override
	public Set<String> getSubjectsOther() {

		try {
			return getTextValueSet(".//element[@name='subject']//element[@name='other']//field[@name='value']");
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
	public Optional<Title> getItem() {
	
		Optional<Title> r = Optional.empty();
		
		Optional<String> sysId = getSysId();
		Optional<String> handle = getHandle();
		
		if (sysId.isPresent() && handle.isPresent()) {
		
			Title title = new Title(getSysId().get(),getHandle().get());
			
			getAbstractOtherLanguage().ifPresent(value -> title.setAbstractOtherLanguage(value));
			getChapterNumber().ifPresent(value -> title.setChapterNumber(value));
			getCollection().ifPresent(value -> title.setCollection(value));
			getDateAvailable().ifPresent(value -> title.setDateAvailable(value));
			getDateIssued().ifPresent(value -> title.setDateIssued(value));
			getDescription().ifPresent(value -> title.setDescription(value));
			getDescriptionAbstract().ifPresent(value -> title.setDescriptionAbstract(value));
			getDescriptionOtherLanguage().ifPresent(value -> title.setDescriptionOtherlanguage(value));
			getDescriptionProvenance().ifPresent(value -> title.setDescriptionProvenance(value));
			getDownloadUrl().ifPresent(value -> title.setDownloadUrl(value));
			getEmbargo().ifPresent(value -> title.setEmbargo(value));
			getImprint().ifPresent(value -> title.setImprint(value));
			getLicense().ifPresent(value -> title.setLicense(value));
			getPages().ifPresent(value -> title.setPages(value));
			getPartOfBook().ifPresent(value -> title.setPartOfBook(value));
			getPartOfSeries().ifPresent(value -> title.setPartOfSeries(value));
			getPlacePublication().ifPresent(value -> title.setPlacePublication(value));
			getSeriesNumber().ifPresent(value -> title.setSeriesNumber(value));
			getTermsAbstract().ifPresent(value -> title.setTermsAbstract(value));
			getThumbnail().ifPresent(value -> title.setThumbnail(value));
			getTitle().ifPresent(value -> title.setTitle(value));
			getTitleAlternative().ifPresent(value -> title.setTitleAlternative(value));
			getType().ifPresent(value -> title.setType(value));
			getWebshopUrl().ifPresent(value -> title.setWebshopUrl(value));
			
			getPublisher().ifPresent(title::setPublisher);
			
			title.setClassifications(getClassifications());
			title.setContributions(getContributions());
			title.setDatesAccessioned(getDatesAccessioned());
			title.setExportChunks(getExportChunks());
			title.setFunders(getFunders());
			title.setGrantData(getGrantData());
			title.setIdentifiers(getIdentifiers());
			title.setLanguages(getLanguages());
			title.setSubjectsOther(getSubjectsOther());
			
			r = Optional.of(title);
		}	
		
		return r;
		
	}
	

	@Override
	public Optional<String> getHandle() {
		
		final String path = ".//element[@name='others']/field[@name='handle']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getSysId() {
		
		final String path = ".//element[@name='others']/field[@name='uuid']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getCollection() {

		final String path = ".//header/setSpec[starts-with(text(),'col')]";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getDownloadUrl() {
		
		final String path = ".//*[.='ORIGINAL']/..//element[@name='bitstream']/field[@name='url']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getThumbnail() {
		
		final String path = ".//*[.='THUMBNAIL']/..//element[@name='bitstream']/field[@name='url']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getLicense() {
		
		final String path = ".//*[.='ORIGINAL']/..//element[@name='bitstream']/field[@name='rightsuri']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getWebshopUrl() {
		
		final String path = ".//*[.='ORIGINAL']/..//element[@name='bitstream']/field[@name='dcidentifierurlwebshop']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getDateAvailable() {
		
		final String path = ".//element[@name='dc']/element[@name='date']/element[@name='available']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getDateIssued() {
		
		final String path = ".//element[@name='dc']/element[@name='date']/element[@name='issued']//field[@name='value']";
		return getTextValue(path);
	}

	// TODO This field seems always empty
	@Override
	public Optional<String> getDescription() {
		
		final String path = ".//TODO";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getDescriptionOtherLanguage() {
		
		final String path = ".//element[@name='oapen']/element[@name='description']/element[@name='otherlanguage']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getDescriptionAbstract() {
		
		final String path = ".//element[@name='dc']/element[@name='description']/element[@name='abstract']//field[@name='value']";
		return getTextValue(path);
	}

	// TODO Not available in XOAI output
	@Override
	public Optional<String> getDescriptionProvenance() {
		
		final String path = ".//TODO";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getTermsAbstract() {
		
		final String path = ".//element[@name='dcterms']/element[@name='abstract']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getAbstractOtherLanguage() {
		
		final String path = ".//element[@name='oapen']/element[@name='abstract']/element[@name='otherlanguage']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getPartOfSeries() {
		
		final String path = ".//element[@name='ispartofseries']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getTitle() {
		
		final String path = ".//element[@name='dc']/element[@name='title']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getTitleAlternative() {
		
		final String path = ".//element[@name='dc']/element[@name='title']/element[@name='alternative']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getType() {
		
		final String path = ".//element[@name='dc']/element[@name='type']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getChapterNumber() {
		
		final String path = ".//element[@name='chapternumber']//field[@name='value']";
		return getTextValue(path);
	}

	// TODO This field is always empty
	@Override
	public Optional<String> getEmbargo() {
		
		final String path = ".//TODO";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getImprint() {
		
		final String path = ".//element[@name='imprint']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getPages() {
		
		final String path = ".//element[@name='pages']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getPlacePublication() {
		
		final String path = ".//element[@name='place']/element[@name='publication']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getSeriesNumber() {
		
		final String path = ".//element[@name='series']/element[@name='number']//field[@name='value']";
		return getTextValue(path);
	}

	@Override
	public Optional<String> getPartOfBook() {
		
		final String path = ".//element[@name='oapen.relation.isPartOfBook']/field[@name='handle']";
		return getTextValue(path);
	}

}
