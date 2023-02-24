package org.oapen.memoproject.dataingestion;

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
	
	
	private Set<String> getValueSet(String xpathQuery, Element element) throws XPathExpressionException {

		NodeList nodes = (NodeList) xpath.evaluate(xpathQuery, element, XPathConstants.NODESET);
		
		Set<String> values = new HashSet<>();
		
		for (int i=0; i < nodes.getLength(); i++) {
        	
        	Node node = nodes.item(i);
        	values.add(node.getTextContent());
        }
		
		return values;
	}
	
	private Optional<String> getValue(String xpathQuery, Element element) throws XPathExpressionException {

		Node node = (Node) xpath.evaluate(xpathQuery, element, XPathConstants.NODE);
		
		if (node != null) 
			return Optional.of(node.getTextContent());
		else 
			return Optional.empty();
	}
	

	@Override
	public Set<Classification> getClassifications() {
		
		try {
			NodeList nodes = (NodeList) xpath.evaluate(".//element[@name='classification']//field/text()", element, XPathConstants.NODESET);
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

		try {
			Set<Contributor> contributors = new HashSet<>();
			NodeList nodes = (NodeList) xpath.evaluate(".//element[@name='contributor']//field[@name='value']", element, XPathConstants.NODESET);
			
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

		try {
			NodeList nodes = (NodeList) xpath.evaluate(".//element[@name='oapen.relation.isFundedBy']", element, XPathConstants.NODESET);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getLanguages() {
		
		try {
			return getValueSet(".//element[@name='language']//field[@name='value']", element);
		} catch (Exception e) {
			throw new MappingException("Could not parse languages");
		}
	}

	@Override
	public Set<String> getSubjectsOther() {

		try {
			return getValueSet(".//element[@name='subject']//element[@name='other']//field[@name='value']", element);
		} catch (Exception e) {
			throw new MappingException("Could not parse other subjects");
		}
	}

	@Override
	public Set<String> getDatesAccessioned() {
		// TODO Auto-generated method stub
		return null;
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
		try {
			return getValue(".//element[@name='others']/field[@name='handle']", element);
		} catch (XPathExpressionException e) {
			throw new MappingException("Could not parse handle");
		}
	}

	@Override
	public Optional<String> getSysId() {
		try {
			return getValue(".//element[@name='others']/field[@name='uuid']", element);
		} catch (Exception e) {
			throw new MappingException("Could not parse sysId (uuid)");
		}
	}

	@Override
	public Optional<String> getCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getDownloadUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getThumbnail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getLicense() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getWebshopUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getDateAvailable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getDateIssued() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getDescriptionOtherLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getDescriptionAbstract() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getDescriptionProvenance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getTermsAbstract() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getAbstractOtherLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getPartOfSeries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getTitleAlternative() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getChapterNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getEmbargo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getImprint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getPages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getPlacePublication() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getSeriesNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getPartOfBook() {
		// TODO Auto-generated method stub
		return null;
	}

}
