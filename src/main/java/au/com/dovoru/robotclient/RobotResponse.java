package au.com.dovoru.robotclient;

/** 
 * Create the singleton Robot object as a pseudo data store
 * @author stephen
 *
 */
public class RobotResponse {
	private int x, y;			// Coords of the current position on the table
	private Direction facing; 	// The direction the robot is facing. If this is null, the robot is lost!
	private boolean lost;

	public String report() {
		if (lost) {
			return "ROBOT MISSING";
		} else {
			return x+","+y+","+facing;
		}
	}

	@Override
	public String toString() {
		return "Robot [x=" + x + ", y=" + y + ", facing=" + facing + ", lost=" + lost + "]";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Direction getFacing() {
		return facing;
	}

	public void setFacing(Direction facing) {
		this.facing = facing;
	}

	public boolean isLost() {
		return lost;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
	}
	
}
