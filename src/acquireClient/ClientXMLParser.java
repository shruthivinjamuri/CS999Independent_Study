package acquireClient;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class ClientXMLParser {

	private PlayerAdmin playerAdmin;
	Board clientState;
	private boolean isBoardReceived;
	private boolean isHotelReceived;
	private boolean isPlayerReceived;

	public ClientXMLParser(int noOfPlayers) {
		clientState = null;
		playerAdmin = new PlayerAdmin(noOfPlayers);
		isBoardReceived = false;
		isHotelReceived = false;
		isPlayerReceived = false;
	}

	public String processInput(String input) throws XMLStreamException {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		List<Tile> tilesOnBoard = new ArrayList<Tile>();
		Set<Hotel> activeHotels = new HashSet<Hotel>();
		Map<String, Integer> shares = new HashMap<String, Integer>();
		String returnString = "";

		// go through the input
		try {
			ValidateXML(xmlInputFactory, input);
			XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new StringReader(input));

			while (xmlEventReader.hasNext()) {
				Set<Tile> tilesInHotel = new HashSet<Tile>();
				StrategyPlayer currentPlayer = null;
				Hotel currentHotel = null;
				List<Tile> tilesOfPlayer = new ArrayList<Tile>();
				Map<String, Integer> playerShares = new HashMap<>();

				XMLEvent xmlEvent = xmlEventReader.nextEvent();

				if (xmlEvent.isStartElement()) {
					StartElement startElement = xmlEvent.asStartElement();
					
					if(startElement.getName().getLocalPart().equals("start")) {
						returnString = playerAdmin.setup();
					}

					if (startElement.getName().getLocalPart().equals("setup")) {

						int i = 1;

						for (StrategyPlayer player : playerAdmin.getPlayers()) {
							List<Tile> initialTiles = new ArrayList<>();
							String tileString = "";
							Attribute tileStringAttr = startElement.getAttributeByName(new QName("player" + i));
							i++;
							if (tileStringAttr != null) {
								tileString = tileStringAttr.getValue();
							}
							String[] tiles = tileString.split("|");
							for (String tile : tiles) {
								initialTiles.add(new Tile((tile.charAt(0)), Integer.parseInt(tile.substring(1))));
							}
							player.setTiles(initialTiles);
						}
					}
					// board tag
					else if (startElement.getName().getLocalPart().equals("board")) {
						isBoardReceived = true;
					}
					
					else if (startElement.getName().getLocalPart().equals("error")) {
						return "</game>";
					}

					else if (startElement.getName().getLocalPart().equals("tile")) {
						Tile tile = null;
						Attribute colAttr = startElement.getAttributeByName(new QName("column"));
						Attribute rowAttr = startElement.getAttributeByName(new QName("row"));
						if (colAttr != null && rowAttr != null) {
							tile = new Tile((rowAttr.getValue().charAt(0)), Integer.parseInt(colAttr.getValue()));
						}

						if (isPlayerReceived) {
							tilesOfPlayer.add(tile);
						}

						if (isBoardReceived && isHotelReceived) {
							tilesInHotel.add(tile);
						}

						if (isBoardReceived) {
							tilesOnBoard.add(tile);
						}
					}

					// hotel tag
					else if (startElement.getName().getLocalPart().equals("hotel")) {
						isHotelReceived = true;
						Attribute hotelName = startElement.getAttributeByName(new QName("name"));
						currentHotel = new Hotel(hotelName.getValue());
					}

					// share tag
					else if (startElement.getName().getLocalPart().equals("share")) {
						Attribute hotelNameAttr = startElement.getAttributeByName(new QName("count"));
						Attribute shareCountAttr = startElement.getAttributeByName(new QName("from"));
						if (hotelNameAttr != null && shareCountAttr != null) {
							if (isPlayerReceived) {
								playerShares.put(hotelNameAttr.getValue(), Integer.parseInt(shareCountAttr.getValue()));
							}
						}
						if (hotelNameAttr != null && shareCountAttr != null) {
							shares.put(hotelNameAttr.getValue(), Integer.parseInt(shareCountAttr.getValue()));
						}
					}

					// player tag
					else if (startElement.getName().getLocalPart().equals("player")) {
						isPlayerReceived = true;
						Attribute playerNameAttr = startElement.getAttributeByName(new QName("count"));
						Attribute cashAttr = startElement.getAttributeByName(new QName("from"));

						if (playerNameAttr != null && cashAttr != null) {
							for (StrategyPlayer player : playerAdmin.getPlayers()) {
								if (player.getPlayerName().equals(playerNameAttr.getValue())) {
									currentPlayer = player;
									currentPlayer.updatePlayerFund(Double.parseDouble(cashAttr.getValue()));
								}
							}
						}
					}
				}

				// check for complete end of the xml and print errors

				if (xmlEvent.isEndElement()) {
					EndElement endElement = xmlEvent.asEndElement();

					if (endElement.getName().getLocalPart().equals("board")) {
						isBoardReceived = false;
					}

					else if (endElement.getName().getLocalPart().equals("hotel")) {
						currentHotel.addTiles(tilesInHotel);
						activeHotels.add(currentHotel);
						isHotelReceived = false;
					} else if (endElement.getName().getLocalPart().equals("player")) {
						currentPlayer.setTiles(tilesOfPlayer);
						currentPlayer.setShares(playerShares);
						isPlayerReceived = false;
					} else if (endElement.getName().getLocalPart().equals("clientState")) {
						clientState = new Board(tilesOnBoard, activeHotels, shares);
						playerAdmin.setCurrentClientState(clientState);
						returnString = playerAdmin.playTurn();
					} else if (endElement.getName().getLocalPart().equals("setup")) {
						returnString = "<game>";
					}
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
