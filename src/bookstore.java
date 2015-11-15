
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
		 
		 showMenu();
		
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
	
	
	private void showMenu()
    {
	int choice;
	boolean quit;

	quit = false;
	
	try 
	{
	    // disable auto commit mode
	    con.setAutoCommit(false);

	    while (!quit)
	    {
		System.out.print("\n\nPlease choose one of the following: \n");
		System.out.print("1.  Insert branch\n");
		System.out.print("2.  Delete branch\n");
		System.out.print("3.  Update branch\n");
		System.out.print("4.  Show branch\n");
		System.out.print("5.  Quit\n>> ");

		choice = Integer.parseInt(in.readLine());
		
		System.out.println(" ");

		switch(choice)
		{
		   case 1:  insertBranch(); break;
		   case 2:  deleteBranch(); break;
		   case 3:  updateBranch(); break;
		   case 4:  showBranch(); break;
		   case 5:  quit = true;
		}
	    }

	    con.close();
            in.close();
	    System.out.println("\nGood Bye!\n\n");
	    System.exit(0);
	}
	catch (IOException e)
	{
	    System.out.println("IOException!");

	    try
	    {
		con.close();
		System.exit(-1);
	    }
	    catch (SQLException ex)
	    {
		 System.out.println("Message: " + ex.getMessage());
	    }
	}
	catch (SQLException ex)
	{
	    System.out.println("Message: " + ex.getMessage());
	}
    }
	
	 private void showBranch() {
		// TODO Auto-generated method stub
		 System.out.println("showing branch...");
		
	}

	private void updateBranch() {
		// TODO Auto-generated method stub
		System.out.println("updating branch...");
		
	}

	private void deleteBranch() {
		// TODO Auto-generated method stub
		System.out.println("deleting branch...");
	}

	private void insertBranch() {
		// TODO Auto-generated method stub
		System.out.println("inserting branch...");
		
	}

	public static void main(String args[])
	    {
	      bookstore b = new bookstore();
	      
	    }
	
}