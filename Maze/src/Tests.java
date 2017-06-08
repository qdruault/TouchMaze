import static org.junit.Assert.*;

import maze.Maze2D;
import maze.GuideCommunicator;
import maze.obstacle.*;
import static maze.Direction2D.*;
import org.junit.Test;

import position.Position1D;
import position.Position2D;
import static position.PositionBuilder.*;

public class Tests {

	@Test
	public void test1D() {
		Position1D p1 = p(0);
		assertTrue(p1.x == 0);
		p1.toRight();
		assertTrue(p1.x == 1);
		p1.toLeft();
		assertTrue(p1.x == 0);
		p1.toLeft();
		assertTrue(p1.x == -1);
		p1.toRear();
		assertTrue(p1.x == -1);
		assertTrue(p1.toRight().is(p1));
		assertTrue(p1.is(p(0)));
		p1.toRight().toRight().toRight().toRight();
		assertTrue(p1.x == 4);
		p1.toRight().toRight();
		assertTrue(p1.x == 6);
		System.out.println(p1);
	}
	
	@Test
	public void test2D(){
		Position2D p1 = new Position2D(0,0);
		assertTrue(p1.x == 0);
		assertTrue(p1.y == 0);
		p1.toFront();
		assertTrue(p1.y == 1);
		assertFalse(p1.toRight().is(p(1)));
		assertTrue(p1.toRight().is(p(2,1)));
		System.out.println(p1);
	}

	@Test
	public void testMaze() throws IllegalAccessException, InstantiationException {
		Maze2D m = new Maze2D();
		GuideCommunicator gc = new GuideCommunicator();
		m.init(gc);
		assertTrue(m.getExplorerPosition().is(p(19,0)));
		assertEquals(FRONT.obstacle.apply(m).getID(), ExplorerWall.ID);
		gc.receiveMessage("MOVE REAR");
		assertTrue(m.getExplorerPosition().is(p(19,0)));
		assertEquals(REAR.obstacle.apply(m).getID(), GuideWall.ID);
		gc.receiveMessage("MOVE LEFT");
		assertTrue(m.getExplorerPosition().is(p(18,0)));
		assertTrue(LEFT.obstacle.apply(m).getID() == Nothing.ID);
		gc.receiveMessage("MOVE LEFT");
		assertTrue(m.getExplorerPosition().is(p(17,0)));
		assertTrue(LEFT.obstacle.apply(m).getID() == Nothing.ID);
		gc.receiveMessage("MOVE LEFT");
		gc.receiveMessage("MOVE LEFT");
	}


	

}
