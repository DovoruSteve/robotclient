package au.com.dovoru.robotclient;

import java.io.Console;
import java.util.StringTokenizer;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Entry point for the Robot client interactive console application
 *  {@inheritDoc}
 * @author stephen
 *
 */
@SpringBootApplication
public class RobotClientApplication implements ApplicationRunner {
	/**
	 * @param args No args are expected
	 */
	public static void main(String[] args) {
		SpringApplication.run(RobotClientApplication.class, args);
	}

	@Autowired
	private GenericRestClient genericClient;

	private String restURI = "http://localhost:8080";
	
	enum Command {
		PLACE,
		MOVE,
		LEFT,
		RIGHT,
		REPORT
	}

	/**
	 * Validates the user input parameters to the PLACE command and returns a PlacementRequest object if valid or null if invalid
	 * 
	 * NOTE this does not check for the coordinates being off the table because I wanted to show that incorrect data causes a server-side
	 * exception that is passed to the UI and the reason is displayed. The client should probably fetch the table size and use it in this validate 
	 * method to block the call to the API.
	 *  
	 * @param tokenizer The input tokenizer
	 * @return The placementRequest object if valid or null if invalid
	 */
	private PlacementRequest validatePlaceCommand(StringTokenizer tokenizer) {
		if (!tokenizer.hasMoreTokens()) {
			System.out.println(Command.PLACE+" requires 3 comma separated parameters, an x coordinate, a y coordinate, and a direction, but none were given -- ignoring command and attempting to continue");
			return null;
		}
		String placeParams = tokenizer.nextToken();
		String[] placeParamsArray = placeParams.split(",");
		if (placeParamsArray.length != 3) {
			System.out.println(Command.PLACE+" requires 3 comma separated parameters, an x coordinate, a y coordinate, and a direction, but I was given '"+placeParams+"'  -- ignoring command and attempting to continue");
			return null;
		}
		int x, y;
		Direction facing;
		try {
			x = Integer.parseInt(placeParamsArray[0]);
		} catch(Throwable th) {
			System.out.println(Command.PLACE+" requires an integer x coordinate as the first parameter, but I was given '"+placeParamsArray[0]+"'  -- ignoring command and attempting to continue");
			return null;
		}
		try {
			y = Integer.parseInt(placeParamsArray[1]);
		} catch(Throwable th) {
			System.out.println(Command.PLACE+" requires an integer y coordinate as the second parameter, but I was given '"+placeParamsArray[1]+"'  -- ignoring command and attempting to continue");
			return null;
		}
		try {
			facing = Direction.valueOf(placeParamsArray[2]);
		} catch(Throwable th) {
			System.out.println(Command.PLACE+" requires a direction as the third parameter, but I was given '"+placeParamsArray[2]+"'  -- ignoring command and attempting to continue");
			return null;
		}
		return new PlacementRequest(x,y,facing);
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
	   Console console = System.console();
	   if (console == null) { // e.g. no console will be available during unit tests
		   return;
	   }
		while (true) {
			String commands = console.readLine("Enter commands: ");
			StringTokenizer tokenizer = new StringTokenizer(commands);
		    while (tokenizer.hasMoreTokens()) {
		    	String token = tokenizer.nextToken();
		    	Command command = null;
		    	try {
			    	command = Command.valueOf(token);
		    	} catch(IllegalArgumentException ex) {
		    		System.out.println("Illegal command '"+token+"' -- ignoring");
		    		continue;
		    	}
		    	try {
		    		RobotResponse robotResponse;
			    	switch (command) {
			    	case PLACE:
			    		PlacementRequest placementRequest = validatePlaceCommand(tokenizer);
			    		if (placementRequest != null) {
		    				robotResponse = genericClient.post(restURI+"/place", placementRequest, RobotResponse.class);
		    			}
			    		break;
			    	case MOVE:
			    		robotResponse = genericClient.put(restURI+"/move", null, RobotResponse.class);
			    		break;
					case LEFT:
						robotResponse = genericClient.put(restURI+"/left", null, RobotResponse.class);
						break;
					case RIGHT:
						robotResponse = genericClient.put(restURI+"/right", null, RobotResponse.class);
						break;
					case REPORT:
						robotResponse = genericClient.get(restURI+"/report", RobotResponse.class);
						System.out.println("Output: "+robotResponse.report());
						break;
			    	}
    			} catch (WebClientResponseException ex) {
    				try {
    					String message = new JSONObject(ex.getResponseBodyAsString()).getString("message");
    					System.out.println("Output: Server Response: "+message);	
    				} catch (Exception ex1) {	// If we're unable to convert the response body to a valid error json object containing a message ...
    					System.out.println("Output: Server Error: "+ex+" response body "+ex.getResponseBodyAsString());
    				}
    			}
		    }
		}


	}

}
