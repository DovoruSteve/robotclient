package au.com.dovoru.robotclient;

/**
 * Holds the parameters to send to the robot REST API with the PLACE command
 * @author stephen
 *
 */
public record PlacementRequest(int x, int y, Direction facing) {
}
