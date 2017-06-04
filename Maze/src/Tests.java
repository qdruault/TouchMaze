import static org.junit.Assert.*;

import maze.Direction2D;
import maze.Maze2D;
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
		assertTrue(p1.x == 0);
		p1.toRear();
		assertTrue(p1.x == 0);
		assertTrue(p1.toRight().is(p1));
		assertTrue(p1.is(p(1)));
		p1.toRight().toRight().toRight().toRight();
		assertTrue(p1.x == 5);
		p1.toRight().toRight();
		assertTrue(p1.x == 5);
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
		assertTrue(m.getExplorerPosition().is(p(19,0)));
		assertTrue(REAR.obstacle.apply(m).getID() == ExplorerWall.ID);
		m.moveTo(REAR);
		assertTrue(m.getExplorerPosition().is(p(19,0)));
		assertTrue(REAR.obstacle.apply(m).getID() == ExplorerWall.ID);
		m.moveTo(LEFT);
		assertTrue(m.getExplorerPosition().is(p(18,0)));
		assertTrue(RIGHT.obstacle.apply(m).getID() == Nothing.ID);

	}


	

}
