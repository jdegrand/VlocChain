package Server;
/**
 * @author Owen Sullivan @multiojuice
 * @file Server.Controller
 * This is going to be ran in parralel with our flask
 * app. This is going to take the uploaded videos and
 * distribute them across a network of video holders.
 * Then alert the holders on when to send up a connection
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

/**
 * Where the server socket is ran
 * creates a new client handler for all client sockets encountered
 * then it is dealt with there.
 */
public class Controller {

    // Standardized messages to comunicate between controller and holder
    public static final String NEW_VIDEO = "new_video";
    public static final String RECALL = "recall";
    
    // And where our videos are
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver"; 
    static final String DB_URL = "jdbc:mariadb://localhost/users";
    static final String USER = "root";
    static final String PASS = "brickhack4";
    public static Connection conn;

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException{

        // Create our server socket to get connections
        ServerSocket serverSocket = new ServerSocket(10000);
        // Alert the console
        System.out.println("Server Running");

	// Connect To Database
	Class.forName(JDBC_DRIVER);
	conn = DriverManager.getConnection(DB_URL, USER, PASS);
	System.out.println("Connected to DB");	

        // Wait for at least one connectin before we start sending files
        Socket clientSocket1 = serverSocket.accept();
        // Add the IP object to our data structures
	Statement stmt = conn.createStatement();

	String sqlStatement = "INSERT INTO location_storage (IP_location, storage_amount) "
		+ "VALUES ( '" 
		+ clientSocket1.getInetAddress()
		+ "' , 0)";

	try {
	    stmt.executeUpdate(sqlStatement);	
	    System.out.println(sqlStatement);
            System.out.println("Connected to the first holder");
	    
	} catch (SQLIntegrityConstraintViolationException dup){
		
	    System.out.println("Pre-exisisting Ip has Returned!");
	    
	}

        // Create the FileMonitor
        FileMonitor fileMonitor = new FileMonitor();
        Thread monitorThread = new Thread(fileMonitor);
        monitorThread.start();

	RecallMonitor recallMonitor = new RecallMonitor();
	Thread recallThread = new Thread(recallMonitor);
	recallThread.start();

        // Keep connecting to new holders
        while(true) { // Loop and look for new connections
            // Accept a new connection
            Socket clientSocket = serverSocket.accept();
            stmt = conn.createStatement();

	    sqlStatement = "INSERT INTO location_storage (IP_location, storage_amount) "
		+ "VALUES ( '" 
		+ clientSocket.getInetAddress()
		+ "' , 0)";
	    try {
	    stmt.executeUpdate(sqlStatement);	
	    System.out.println("New Connection stored in DB");

	    } catch (SQLIntegrityConstraintViolationException dup){
   	        System.out.println("Pre-exisisting Ip has Returned!");
	    }

            // ADD MONITORING FOR RECALLS
            // TODO add recalls
        }
    }
}
