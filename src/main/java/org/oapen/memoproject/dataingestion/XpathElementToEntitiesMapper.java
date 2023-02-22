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
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
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
		// TODO Auto-generated method stub
		return null;
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
	
	
	@Override
	public Set<Identifier> getIdentifiers() {
		
		try {
			NodeList nodesOnix = (NodeList) xpath.evaluate(".//element[@name='identifier']//field[starts-with(text(),'ONIX')]", element, XPathConstants.NODESET);
			NodeList nodesDoi = (NodeList) xpath.evaluate(".//element[@name='doi']//field[@name='value']", element, XPathConstants.NODESET);
			NodeList nodesIsbn = (NodeList) xpath.evaluate(".//element[@name='isbn']//field[@name='value']", element, XPathConstants.NODESET);
			//NodeList nodes = (NodeList) xpath.evaluate(".//element[@name='identifier' or @name='isbn']/element[not(@name='uri')]//field[@name='value']", element, XPathConstants.NODESET);
			
			Set<Identifier> identifiers = new HashSet<>();
			
			for (int i=0; i < nodesOnix.getLength(); i++) {
	        	
	        	Node node = nodesOnix.item(i);
	        	Identifier identifier = new Identifier(node.getTextContent(),"LALALA");
	        	identifiers.add(identifier);
	        }
			
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
