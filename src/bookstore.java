
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
		System.out.print("1.  Insert Item\n");
		System.out.print("2.  Remove Item\n");
		System.out.print("3.  Update branch\n");
		System.out.print("4.  Show branch\n");
		System.out.print("5.  Quit\n>> ");

		choice = Integer.parseInt(in.readLine());
		
		System.out.println(" ");

		switch(choice)
		{
		   case 1:  insertItem(); break;
		   case 2:  removeItem(); break;
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
			String     bid;
			String     bname;
			String     baddr;
			String     bcity;
			String     bphone;
			Statement  stmt;
			ResultSet  rs;
			   
			try
			{
			  stmt = con.createStatement();

			  rs = stmt.executeQuery("SELECT * FROM branch");

			  // get info on ResultSet
			  ResultSetMetaData rsmd = rs.getMetaData();

			  // get number of columns
			  int numCols = rsmd.getColumnCount();

			  System.out.println(" ");
			  
			  // display column names;
			  for (int i = 0; i < numCols; i++)
			  {
			      // get column name and print it

			      System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			  }

			  System.out.println(" ");

			  while(rs.next())
			  {
			      // for display purposes get everything from Oracle 
			      // as a string

			      // simplified output formatting; truncation may occur

			      bid = rs.getString("branch_id");
			      System.out.printf("%-10.10s", bid);

			      bname = rs.getString("branch_name");
			      System.out.printf("%-20.20s", bname);

			      baddr = rs.getString("branch_addr");
			      if (rs.wasNull())
			      {
			    	  System.out.printf("%-20.20s", " ");
		              }
			      else
			      {
			    	  System.out.printf("%-20.20s", baddr);
			      }

			      bcity = rs.getString("branch_city");
			      System.out.printf("%-15.15s", bcity);

			      bphone = rs.getString("branch_phone");
			      if (rs.wasNull())
			      {
			    	  System.out.printf("%-15.15s\n", " ");
		              }
			      else
			      {
			    	  System.out.printf("%-15.15s\n", bphone);
			      }      
			  }
		 
			  // close the statement; 
			  // the ResultSet will also be closed
			  stmt.close();
			}
			catch (SQLException ex)
			{
			    System.out.println("Message: " + ex.getMessage());
			}	
		    }

	private void updateBranch() {
		// TODO Auto-generated method stub
		System.out.println("updating branch...");
		
	}

	private void removeItem() 
	{
		int                bid;
		PreparedStatement  ps;
		  
		try
		{
		  ps = con.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
		
		  System.out.print("\nBranch ID: ");
		  bid = Integer.parseInt(in.readLine());
		  ps.setInt(1, bid);

		  int rowCount = ps.executeUpdate();

		  if (rowCount == 0)
		  {
		      System.out.println("\nBranch " + bid + " does not exist!");
		  }

		  con.commit();

		  ps.close();
		}
		catch (IOException e)
		{
		    System.out.println("IOException!");
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());

	            try 
		    {
			con.rollback();	
		    }
		    catch (SQLException ex2)
		    {
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		    }
		}
	    }

	private void insertItem() 
	{
		String             upc;
		String             sellPrice;
		String             stock;
		String             taxable;
		PreparedStatement  ps;
		  
		try
		{
		  ps = con.prepareStatement("INSERT INTO item VALUES (?,?,?,?)");
		
		  System.out.print("\nUpc: ");
		  upc = in.readLine();
		  ps.setString(1, upc);

		  System.out.print("\nSelling Price: ");
		  sellPrice = in.readLine();
		  ps.setString(2, sellPrice);

		  System.out.print("\nStock: ");
		  stock = in.readLine();
		  ps.setString(3, stock);
		 
		  System.out.print("\nTaxable (y/n): ");
		  taxable = in.readLine();
		  ps.setString(4, taxable);

		  ps.executeUpdate();

		  // commit work 
		  con.commit();

		  ps.close();
		}
		catch (IOException e)
		{
		    System.out.println("IOException!");
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		    try 
		    {
			// undo the insert
			con.rollback();	
		    }
		    catch (SQLException ex2)
		    {
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		    }
		}
	    }
	
	

	public static void main(String args[])
	    {
	      bookstore b = new bookstore();
	      
	    }
	
}