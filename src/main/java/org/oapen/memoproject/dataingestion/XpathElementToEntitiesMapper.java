package org.oapen.memoproject.dataingestion;

import java.util.HashSet;
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

	@Override
	public Set<Classification> getClassifications() {
		
		// TODO
		//NodeList nodes = (NodeList) xpath.evaluate(".//element[@name='classification']//field/text()", element, XPathConstants.NODESET);
		//return nodes;
		return null;
	}

	@Override
	public Set<Contributor> getContributors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Funder> getFunders() {

		try {
			NodeList fundernodes = (NodeList) xpath.evaluate(".//element[@name='oapen.relation.isFundedBy']", element, XPathConstants.NODESET);
			JAXBContext funderContext = JAXBContext.newInstance(Funder.class);
			Unmarshaller unmarshaller = funderContext.createUnmarshaller();
			
			Set<Funder> funders = new HashSet<>();
			
			for (int i=0; i < fundernodes.getLength(); i++) {
	        	
	        	Node fundernode = fundernodes.item(i);
	        	Funder funder = (Funder) unmarshaller.unmarshal(fundernode);
	        	funders.add(funder);
	        }
			
			return funders;
		}	
		catch (Exception e)	{
			throw new MappingException("Could not parse funders");
		}
		
	}

	@Override
	public Set<Funder> getFundings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Identifier> getIdentifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Contribution> getContributions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ExportChunk> getExportChunks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getLanguages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getSubjectsOther() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getDatesAccessioned() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Publisher getPublisher() {
		
		try {
			NodeList publishernodes = (NodeList) xpath.evaluate(".//element[@name='oapen.relation.isPublishedBy']", element, XPathConstants.NODESET);
			JAXBContext publisherContext = JAXBContext.newInstance(Publisher.class);
			Unmarshaller unmarshaller = publisherContext.createUnmarshaller();
			
			if (publishernodes.getLength() > 0) {
	        	
	        	Node publishernode = publishernodes.item(0);
	        	Publisher publisher = (Publisher) unmarshaller.unmarshal(publishernode);
	        	return publisher;
	        }
			else throw new MappingException("No publisher found");
		}	
		catch (Exception e)	{
			throw new MappingException("Could not parse publisher");
		}
		
	}

	@Override
	public String getHandle() {
		try {
			Node n = (Node) xpath.evaluate(".//element[@name='others']/field[@name='handle']", element, XPathConstants.NODE);
			return n.getTextContent();
		} catch (XPathExpressionException e) {
			throw new MappingException("Could not parse handle");
		}
	}

	@Override
	public String getSysId() {
		try {
			Node n = (Node) xpath.evaluate(".//element[@name='others']/field[@name='uuid']", element, XPathConstants.NODE);
			return n.getTextContent();
		} catch (XPathExpressionException e) {
			throw new MappingException("Could not parse handle");
		}
	}

	@Override
	public String getCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDownloadUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getThumbnail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLicense() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWebshopUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDateAvailable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDateIssued() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescriptionOtherLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescriptionAbstract() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescriptionProvenance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTermsAbstract() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAbstractOtherLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPartOfSeries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitleAlternative() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChapterNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEmbargo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImprint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlacePublication() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSeriesNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPartOfBook() {
		// TODO Auto-generated method stub
		return null;
	}

}
