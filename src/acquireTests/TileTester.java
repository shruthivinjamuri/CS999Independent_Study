package acquireTests;

import static org.junit.Assert.*;
import AcquireGame.Cell;
import AcquireGame.GameException;

import org.junit.Test;

public class TileTester {

	@Test
	public void testSetRowChar() {
		try {
			Cell testTile = new Cell('A',1);
			testTile.setRow('K');
			fail("invalid row value");
		} catch (GameException e){
			assertTrue(true);
		}
	}

	@Test
	public void testGetColumn() throws GameException {
		Cell testTile = new Cell('A',1);
		testTile.setCol(5);
		assertEquals("tile.getStatus() method fails",5,testTile.getCol());
	}

	@Test
	public void testSetColumn() {
		try {
			Cell testTile = new Cell('A', 1);
			testTile.setCol(14);
			fail("invalid column value");
		} catch (GameException e){
			assertTrue(true);
		}
	}
	
	@Test
	public void testTileColConstructor() {
		try {
			new Cell('A',14);
			fail("invalid values passed to constructor");
		} catch (GameException e){
			assertTrue(true);
		}
	}

	@Test
	public void testTileIntIntStringInt() {
		try {
			new Cell('Z',1);
			fail("invalid values passed to constructor");
		} catch (GameException e){
			assertTrue(true);
		}
	}

	@Test
	public void testEqualsTile() {
		try {
			Cell t1 = new Cell('A',1);
			Cell t2 = new Cell('A',1);
			assertTrue("Equals fails", t1.equals(t2));
		} catch (GameException e){
			assertTrue(false);
		}
	}

}
