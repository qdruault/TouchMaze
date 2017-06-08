package maze.obstacle;

public class TouchableWall extends Wall {

	public static final int ID = 3;
	
	static{
		id2Class.put(ID, TouchableWall.class);
	}

	@Override
	public boolean isVisibleByGuide() {
		return false;
	}

	@Override
	public boolean isTouchableByGuide() {
		return true;
	}

	@Override
	public boolean isTouchableByExplorer() {
		return true;
	}

	@Override
	public int getID() {
		return ID;
	}

}
