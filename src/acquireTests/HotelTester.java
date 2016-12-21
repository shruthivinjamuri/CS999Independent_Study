package acquireTests;

import static org.junit.Assert.*;

import AcquireGame.Hotel;
import AcquireGame.GameException;
import AcquireGame.Cell;

import org.junit.Test;

public class HotelTester {

	@Test
	public void testEqualsHotel() {
		try {
			Cell testTile1 = new Cell('A', 1);
			Cell testTile2 = new Cell('A', 2);
			Hotel hotel1 = new Hotel("Westin");
			Hotel hotel2 = new Hotel("Westin");
			hotel1.expandHotel(testTile1);
			hotel1.expandHotel(testTile2);
			hotel2.expandHotel(testTile1);
			hotel2.expandHotel(testTile1);
			assertTrue("EqualsHotel Fails",hotel1.equals(hotel2));
		} catch (GameException e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
		try {
			Cell testTile1 = new Cell('A',1);
			Cell testTile2 = new Cell('A',2);
			Hotel hotel1 = new Hotel("InterContinental");
			Hotel hotel2 = new Hotel("InterContinental");
			hotel1.expandHotel(testTile1);
			hotel1.expandHotel(testTile2);
			hotel2.expandHotel(testTile1);
			assertFalse("EqualsHotel Fails",hotel1.equals(hotel2));
		} catch (GameException e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
		try {
			Cell testTile1 = new Cell('A',1);
			Cell testTile2 = new Cell('A',2);
			Hotel hotel1 = new Hotel("Westin");
			Hotel hotel2 = new Hotel("Westin");
			hotel1.expandHotel(testTile1);
			hotel2.expandHotel(testTile1);
			hotel2.expandHotel(testTile2);
			assertFalse("EqualsHotel Fails",hotel1.equals(hotel2));
		} catch (GameException e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
	}
	@Test
	public void testSetName() {
		try {
			Hotel testHotel= new Hotel("AMERICAN");
			testHotel.setName("");
			fail("invalid hotels name");
		} catch (GameException e){
			assertTrue(true);
		}
	}

	@Test
	public void testHasTile() {
		try {
			Cell testTile = new Cell('A', 1);
			Hotel hotel = new Hotel("Westin");
			hotel.expandHotel(testTile);
			assertTrue("HasTile Fails",hotel.isInHotel(testTile));
		} catch (GameException e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
		try {
			Cell testTile1 = new Cell('A',1);
			Cell testTile2 = new Cell('B',1);
			Hotel hotel = new Hotel("Westin");
			hotel.expandHotel(testTile1);
			assertFalse("HasTile Fails",hotel.isInHotel(testTile2));
		} catch (GameException e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
	}

}
