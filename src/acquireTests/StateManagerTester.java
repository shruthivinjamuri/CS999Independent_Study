package acquireTests;

import static org.junit.Assert.*;

import AcquireGame.Board;
import AcquireGame.Hotel;
import AcquireGame.Player;
import AcquireGame.Cell;
import AcquireGame.Game;
import AcquireGame.GameException;

import java.util.ArrayList;

import org.junit.Test;


public class StateManagerTester {

	@Test
	public void testBuyShare() {
		
		try {
			ArrayList<Player> players = new ArrayList<Player>();
			players.add(new Player("Shruthi"));
			players.add(new Player("Rakesh"));
			players.add(new Player("Renu"));
			Board board = new Board();
			Cell testTile1 = new Cell('A',1);
			Cell testTile2 = new Cell('B',2);
			Cell testTile3 = new Cell('B',3);
			Cell testTile5 = new Cell('C',2);
			Hotel hotel = new Hotel("Westin");
			hotel.expandHotel(testTile1);
			hotel.expandHotel(testTile2);
			hotel.expandHotel(testTile3);
			hotel.expandHotel(testTile2);
			hotel.expandHotel(testTile5);
			board.addHotel(hotel);
			hotel.expandHotel(testTile3);
			hotel.expandHotel(testTile2);
			hotel.expandHotel(testTile5);
			Game state = new Game(3, players);
			ArrayList<String> shares = new ArrayList<String>();
			shares.add("AA");
			assertNull("CheckState Failed", state.buyShares(players.get(0)));
		}catch (GameException e) {
			assertTrue(true);
		}
		try{
			ArrayList<Player> players = new ArrayList<Player>();
			players.add(new Player("Shruthi"));
			players.add(new Player("Santos"));
			players.add(new Player("Jose"));
			Board board = new Board();
			Cell testTile1 = new Cell('A',1);
			Cell testTile2 = new Cell('B',2);
			Cell testTile3 = new Cell('B',3);
			Cell testTile4 = new Cell('A',2);
			Cell testTile5 = new Cell('C',2);
			Hotel hotel = new Hotel("AMERICAN");
			//hotel.expandHotel(testTile1);
			//hotel.expandHotel(testTile2);
			hotel.expandHotel(testTile3);
			hotel.expandHotel(testTile2);
			hotel.expandHotel(testTile5);
			board.addHotel(hotel);
			Game state = new Game(3, players);
			ArrayList<String> shares = new ArrayList<String>();
			shares.add("AMERICAN");
			shares.add("AMERICAN");
			state.buyShares(players.get(0));
			assertEquals("CheckState Failed",players.get(0).getPlayerFund() == 5200);
			assertTrue("CheckState Failed",players.get(0).getShares().get("American") == 2);
		}catch (GameException e) {
			e.printStackTrace();
			fail("error occured");
		}
	}

}