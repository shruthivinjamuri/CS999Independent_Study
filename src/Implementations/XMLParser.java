package Implementations;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XMLParser {
	
	private GraphLibrary library = new GraphLibrary();
	
	public static void main(String[] args) throws XMLStreamException {
		XMLParser parser = new XMLParser();
		String ans=parser.processInput("<graphProject><graph name=\"graph1\" costInterval=\"45\">"
				+ "<edge from=\"A\" to=\"B\" cost=\"6\"></edge><edge from=\"A\" to=\"D\" cost=\"3\"/>"
				+ "<edge from=\"D\" to=\"A\" cost=\"2\"/><edge from=\"D\" to=\"B\" cost=\"1\"/>"
				+ "<edge from=\"D\" to=\"C\" cost=\"2\"/><edge from=\"A\" to=\"C\" cost=\"9\"/>"
				+ "<edge from=\"C\" to=\"E\" cost=\"8\"/><edge from=\"E\" to=\"A\" cost=\"7\"/>"
				+ "</graph><path graph=\"graph1\" from=\"C\" to=\"C\" /></graphProject>");
		System.out.println(ans);
	}
	
		
	public String processInput(String input) throws XMLStreamException{
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		String graphName = "";
		String returnString = "";
		Double costInterval = 0.0;
		
		// go through the input
		try{
		ValidateXML(xmlInputFactory, input);
		XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new StringReader(input));
		while(xmlEventReader.hasNext()){
			XMLEvent xmlEvent = xmlEventReader.nextEvent();
			if(xmlEvent.isStartElement()){
				StartElement startElement = xmlEvent.asStartElement();
				// graphTag
				if(startElement.getName().getLocalPart().equals("graph")){
					Attribute nameAttr = startElement.getAttributeByName(new QName("name"));
					Attribute costAttr = startElement.getAttributeByName(new QName("costInterval"));
					
					if(nameAttr != null && costAttr != null){
						graphName = nameAttr.getValue();
						costInterval = Double.parseDouble(costAttr.getValue());
						library.createGraph(graphName, costInterval);
					}
					
				}
				
				// edge tag
				else if(startElement.getName().getLocalPart().equals("edge")){
					Attribute fromAttr = startElement.getAttributeByName(new QName("from"));
					Attribute toAttr = startElement.getAttributeByName(new QName("to"));
					Attribute cost = startElement.getAttributeByName(new QName("cost"));
					
					if(fromAttr != null && toAttr != null && cost != null){
						library.addEdge(graphName, fromAttr.getValue(), toAttr.getValue(), 
								Double.parseDouble(cost.getValue()));
					}
				}
				
				// path tag
				else if(startElement.getName().getLocalPart().equals("path")){
					Attribute nameAttr = startElement.getAttributeByName(new QName("graph"));
					Attribute fromAttr = startElement.getAttributeByName(new QName("from"));
					Attribute toAttr = startElement.getAttributeByName(new QName("to"));
					Attribute pathAlgoAttr = startElement.getAttributeByName(new QName("pathAlgo"));
					String pathAlgo = pathAlgoAttr == null ? "" : pathAlgoAttr.getValue();
					
					if(nameAttr != null && fromAttr != null && toAttr != null){
						returnString = library.computePath(
								nameAttr.getValue(), fromAttr.getValue(), 
								toAttr.getValue(), pathAlgo);
					}
				}
			}
			
			// check for complete end of the xml and print errors
			if(xmlEvent.isEndElement()){
				EndElement endElement = xmlEvent.asEndElement();
				if(endElement.getName().getLocalPart().equals("graphProject")){
					// print any errors
				}
			}
		}
		
		} catch (Exception e){
			return e.getMessage();
		}
		
		return returnString;
	}
	
	private static void ValidateXML(XMLInputFactory xmlInputFactory, String xml) throws XMLStreamException, SAXException, IOException{
		XMLStreamReader reader;
		try{
			reader = xmlInputFactory.createXMLStreamReader(new StringReader(xml));
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(System.getProperty("user.dir")+"\\src\\Implementations\\schema.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new StAXSource(reader));		
	} catch (XMLStreamException | SAXException | IOException e){
		System.out.println(e.getMessage());
	}
 }
}