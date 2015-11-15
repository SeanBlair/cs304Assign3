
// We need to import the java.sql package to use JDBC
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//for reading from the command line
import java.io.*;



public class bookstore implements ActionListener{
	
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private Connection con;
	
	
	
	public bookstore(){
		System.out.println("yey, new bookstore!!");	
		
		 try 
	      {
		// Load the Oracle JDBC driver
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		
		System.out.println("oracle ldbc driver loaded..");
	      }
	      catch (SQLException ex)
	      {
		System.out.println("Message: " + ex.getMessage());
		System.exit(-1);
	      }
		 
		 connect();
		 System.out.println("connected!!???");
		
	}
	
	private boolean connect()
    {
      String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug"; 
    	
      try 
      {
	con = DriverManager.getConnection(connectURL,"ora_o5v9a","a55877147");

	System.out.println("\nConnected to Oracle!");
	return true;
      }
      catch (SQLException ex)
      {
	System.out.println("Message: " + ex.getMessage());
	return false;
      }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	 public static void main(String args[])
	    {
	      bookstore b = new bookstore();
	      
	    }
	
}