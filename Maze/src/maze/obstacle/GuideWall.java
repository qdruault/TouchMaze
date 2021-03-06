package maze.obstacle;

public class GuideWall extends Wall {

	public static final int ID = 1;
	
	static{
		id2Class.put(ID, GuideWall.class);
	}

	@Override
	public boolean isVisibleByGuide() {
		return true;
	}

	@Override
	public boolean isTouchableByGuide() {
		return true;
	}

	@Override
	public boolean isTouchableByExplorer() {
		return false;
	}

	@Override
	public int getID() {
		return ID;
	}

}
