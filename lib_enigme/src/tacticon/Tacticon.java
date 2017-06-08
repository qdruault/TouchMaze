package tacticon;

public class Tacticon {
	
	private boolean isOn;
	private boolean isReplaceable;
	
	public Tacticon(){
		isOn=true;
		isReplaceable=false;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}

	public boolean isReplaceable() {
		return isReplaceable;
	}

	public void setReplaceable(boolean isReplaceable) {
		this.isReplaceable = isReplaceable;
	}
	
	
	
}
