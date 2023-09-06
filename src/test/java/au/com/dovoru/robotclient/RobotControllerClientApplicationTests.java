package au.com.dovoru.robotclient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@SpringBootTest
class RobotControllerClientApplicationTests {
	@Autowired
	private GenericRestClient genericClient;
	
	private String restURI = "http://localhost:8080";
	private String username = "kizzie";    // TODO: Get these from the ConfigServer's robotserver.properties file in the protected directory
	private String password = "HappyHippo";
	/**
	 * Confirm placing the robot on the table works
	 */
	@Test
	void givenPlacement_whenPlacementOnTable_thenReturnRobot() {
		PlacementRequest placementRequest = new PlacementRequest(0,0,Direction.NORTH);
		RobotResponse robotResponse = genericClient.post(restURI+"/place", placementRequest, username, password, RobotResponse.class);
		Assertions.assertEquals(false,robotResponse.isLost());
		Assertions.assertEquals(0,robotResponse.getX());
		Assertions.assertEquals(0,robotResponse.getY());
		Assertions.assertEquals(Direction.NORTH,robotResponse.getFacing());
	}
	/**
	 * Confirm that an invalid placement (off the table) throws a 400 bad client request
	 */
	@Test
	void givenPlacement_whenPlacementOffTable_thenExpectException() {
		WebClientResponseException thrown = Assertions.assertThrows(WebClientResponseException.class, () -> {
			PlacementRequest placementRequest = new PlacementRequest(-1,0,Direction.NORTH);
			genericClient.post(restURI+"/place", placementRequest, username, password, RobotResponse.class);
		}, "WebClientResponseException was expected");
		
		Assertions.assertEquals("400 Bad Request from POST "+restURI+"/place", thrown.getMessage());
	}
	/**
	 * Place the robot on the table at 0,0 and move it one position north resulting in a valid robot at position 0,1
	 */
	@Test
	void givenPlacementAndMove_whenStillOnTable_thenReturnRobot() {
		PlacementRequest placementRequest = new PlacementRequest(0,0,Direction.NORTH);
		RobotResponse robotResponse = genericClient.post(restURI+"/place", placementRequest, username, password, RobotResponse.class);
		robotResponse = genericClient.put(restURI+"/move", null, username, password, RobotResponse.class);
		Assertions.assertEquals(false,robotResponse.isLost());
		Assertions.assertEquals(0,robotResponse.getX());
		Assertions.assertEquals(1,robotResponse.getY());
		Assertions.assertEquals(Direction.NORTH,robotResponse.getFacing());
	}
	/**
	 * Place the robot on the table at 0,0 and move it one position south resulting in the move being ignored and a valid robot at position 0,0
	 */
	@Test
	void givenPlacementAndMove_whenMovingOffTheTable_thenIgnoreMove() {
		PlacementRequest placementRequest = new PlacementRequest(0,0,Direction.SOUTH);
		RobotResponse robotResponse = genericClient.post(restURI+"/place", placementRequest, username, password, RobotResponse.class);
		robotResponse = genericClient.put(restURI+"/move", null, username, password, RobotResponse.class);
		Assertions.assertEquals(false,robotResponse.isLost());
		Assertions.assertEquals(0,robotResponse.getX());
		Assertions.assertEquals(0,robotResponse.getY());
		Assertions.assertEquals(Direction.SOUTH,robotResponse.getFacing());
	}
	/**
	 * Place the robot on the table at 0,0 facing north and turn left resulting in it facing west
	 */
	@Test
	void givenPlacementAndLeft_whenPlacementOnTable_thenRobotHasTurnedLeftOnePDirection() {
		PlacementRequest placementRequest = new PlacementRequest(0,0,Direction.NORTH);
		RobotResponse robotResponse = genericClient.post(restURI+"/place", placementRequest, username, password, RobotResponse.class);
		robotResponse = genericClient.put(restURI+"/left", null, username, password, RobotResponse.class);
		Assertions.assertEquals(false,robotResponse.isLost());
		Assertions.assertEquals(0,robotResponse.getX());
		Assertions.assertEquals(0,robotResponse.getY());
		Assertions.assertEquals(Direction.WEST,robotResponse.getFacing());
	}
	/**
	 * Place the robot on the table at 0,0 facing north and turn right resulting in it facing east
	 */
	@Test
	void givenPlacementAndRight_whenPlacementOnTable_thenRobotHasTurnedRightOnePDirection() {
		PlacementRequest placementRequest = new PlacementRequest(0,0,Direction.NORTH);
		RobotResponse robotResponse = genericClient.post(restURI+"/place", placementRequest, username, password, RobotResponse.class);
		robotResponse = genericClient.put(restURI+"/right", null, username, password, RobotResponse.class);
		Assertions.assertEquals(false,robotResponse.isLost());
		Assertions.assertEquals(0,robotResponse.getX());
		Assertions.assertEquals(0,robotResponse.getY());
		Assertions.assertEquals(Direction.EAST,robotResponse.getFacing());
	}
	/**
	 * Place the robot on the table and report
	 */
	@Test
	void givenPlacementAndReport_whenPlacementOnTable_thenRobotHasReportedCorrectly() {
		PlacementRequest placementRequest = new PlacementRequest(3,3,Direction.NORTH);
		RobotResponse robotResponse = genericClient.post(restURI+"/place", placementRequest, username, password, RobotResponse.class);
		robotResponse = genericClient.get(restURI+"/report", username, password, RobotResponse.class);
		Assertions.assertEquals(false,robotResponse.isLost());
		Assertions.assertEquals(3,robotResponse.getX());
		Assertions.assertEquals(3,robotResponse.getY());
		Assertions.assertEquals(Direction.NORTH,robotResponse.getFacing());
		Assertions.assertEquals("3,3,NORTH",robotResponse.report());
	}
}
