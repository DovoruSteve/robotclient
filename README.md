# robotclient

Robot Client for the Coding Challenge 1


This provides a command line interface to place and move the robot.

Installation

	The config project and robotserver project are required to be installed and running first.
	They are at the following URL's
	    https://github.com/DovoruSteve/config.git
	    https://github.com/DovoruSteve/robotserver.git
	    
	Java 17 or newer is required to compile and / or run the robot client.

	Maven 3 or newer is required only if you want to recompile and run the unit tests.

	Download / clone the repo. I've included the jar so you can run it without rebuilding it.

	Once downloaded (and extracted, if you downloaded the zip), make sure the config server
	and the robot server are running, and then in a shell (or command window on Windows), 
	run it by changing into the robotclient directory and running 

	     java -jar target/robotclient-0.0.2.jar


Instructions

	This is a console application. The following commands are acceptable.

	    PLACE x,y,direction
	    	  This allows you to place the robot onto the table, which defaults to 5x5.
		  x and y can each take a value between 0 and 4 inclusive.
		  The direction can only be NORTH, EAST, SOUTH, or WEST

	    MOVE
		This moves the robot one position in the direction it is currently facing.
		If such a move would cause the robot to fall off the table, the move is ignored.

	    LEFT
		This turns the robot 90 degrees to the left

	    RIGHT
		This turns the robot 90 degrees to the right

	    REPORT
	        This displays the current position of the robot or "MISSING" if it isn't on the table.
		

Assumptions

	The only time the Robot is “MISSING” is from when the REST server is started up until a valid PLACE
	command is entered. Once it is on the table, it is not possible for it to leave the table until the
	server is restarted.


Included

	Unit Tests for the client 


Not included / Out of Scope

	The robot is not persisted across server restarts. 

	Change History

		v0.0.2 Gets username and password from config rather than hard coded 
				Exclude useless json library when running tests (just to hide the warning) 
		v0.0.1 Initial version 
	