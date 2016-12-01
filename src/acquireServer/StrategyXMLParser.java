package acquireServer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import AcquireGame.Cell;

public class StrategyXMLParser {

	private boolean isSetupReceived = false;
	private boolean isPlayerReceived = false;
	private boolean isTurnReceived = false;
	private StrategyGame game = null;
	private boolean isGame = false;

	public String processInput(String input) throws XMLStreamException {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

		String returnString = "";
		List<ServerPlayer> players = new ArrayList<>();
		Map<String, Integer> playerShares = new HashMap<String, Integer>();
		Map<String, Integer> currentShares = new HashMap<String, Integer>();

		// go through the input
		try {
			ValidateXML(xmlInputFactory, input);
			XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new StringReader(input));

			while (xmlEventReader.hasNext()) {

				XMLEvent xmlEvent = xmlEventReader.nextEvent();
				String playerName = "";
				double playerCash = -1.0;

				if (xmlEvent.isStartElement()) {
					StartElement startElement = xmlEvent.asStartElement();

					if (startElement.getName().getLocalPart().equals("setup")) {
						isSetupReceived = true;
					}

					else if (startElement.getName().getLocalPart().equals("game")) {
						isGame = true;
					}

					else if (startElement.getName().getLocalPart().equals("player")) {
						if (isSetupReceived) {
							isPlayerReceived = true;
							Attribute playerNameAttr = startElement.getAttributeByName(new QName("name"));
							Attribute cashAttr = startElement.getAttributeByName(new QName("cash"));
							if (playerNameAttr != null && cashAttr != null) {
								playerName = playerNameAttr.getValue();
								playerCash = Double.parseDouble(cashAttr.getValue());
							}
						}

					}

					// share tag
					else if (startElement.getName().getLocalPart().equals("share")) {
						Attribute hotelNameAttr = startElement.getAttributeByName(new QName("count"));
						Attribute shareCountAttr = startElement.getAttributeByName(new QName("from"));
						if (hotelNameAttr != null && shareCountAttr != null) {
							if (isPlayerReceived) {
								playerShares.put(hotelNameAttr.getValue(), Integer.parseInt(shareCountAttr.getValue()));
							} else if (isTurnReceived) {
								currentShares.put(hotelNameAttr.getValue(),
										Integer.parseInt(shareCountAttr.getValue()));
							}
						}

						else if (startElement.getName().getLocalPart().equals("place")) {
							Cell tile = null;
							Attribute rowAttr = startElement.getAttributeByName(new QName("row"));
							Attribute colAttr = startElement.getAttributeByName(new QName("column"));
							Attribute hotelAttr = startElement.getAttributeByName(new QName("hotel"));
							if (colAttr != null && rowAttr != null) {
								tile = new Cell((rowAttr.getValue().charAt(0)), Integer.parseInt(colAttr.getValue()));
							}

							if (hotelAttr != null) {
								// call founding
								game.findingHotel(tile, hotelAttr.getValue());
							} else {
								game.playTile(tile);
							}

						}

						else if (startElement.getName().getLocalPart().equals("turn")) {
							isTurnReceived = true;
						}
					}
				}

				// check for complete end of the xml and print errors

				if (xmlEvent.isEndElement()) {
					EndElement endElement = xmlEvent.asEndElement();

					if (endElement.getName().getLocalPart().equals("player")) {
						ServerPlayer player = new ServerPlayer(playerName, playerCash, playerShares);
						players.add(player);
						playerShares = new HashMap<>();
						isPlayerReceived = false;
					} else if (endElement.getName().getLocalPart().equals("setup")) {
						game = new StrategyGame(players);
						returnString = game.clientPlayerSetup();
					} else if (endElement.getName().getLocalPart().equals("turn")) {
						game.buyShares(currentShares);
						isGame = game.afterEachTurn();
					} else if (endElement.getName().getLocalPart().equals("game")) {
						game.announceWinner();
						isGame = false;
					}
				}
				
				if(isGame && !game.endOfGame()) {
					returnString = game.currentGameState();
					game.setPlayerTurn();
					return returnString;
				}
			}

		} catch (Exception e) {
			return e.getMessage();
		}

		return returnString;
	}

	private static void ValidateXML(XMLInputFactory xmlInputFactory, String xml)
			throws XMLStreamException, SAXException, IOException {
		XMLStreamReader reader;
		try {
			reader = xmlInputFactory.createXMLStreamReader(new StringReader(xml));
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory
					.newSchema(new File(System.getProperty("user.dir") + "\\src\\Implementations\\schema.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new StAXSource(reader));
		} catch (XMLStreamException | SAXException | IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
