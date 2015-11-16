
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
		
		 try 
	      {
		// Load the Oracle JDBC driver
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		
	      }
	      catch (SQLException ex)
	      {
		System.out.println("Message: " + ex.getMessage());
		System.exit(-1);
	      }
		 
		 connect();
		 
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
		System.out.print("3.  List popular textbooks running low\n");
		System.out.print("4.  List top 3 selling items last week\n");
		System.out.print("5.  Show item relation\n");
		System.out.print("6.  Quit\n>> ");

		choice = Integer.parseInt(in.readLine());
		
		System.out.println(" ");

		switch(choice)
		{
		   case 1:  insertItem(); break;
		   case 2:  removeItem(); break;
		   case 3:  listLowPopularTextbooks(); break;
		   case 4:  list3TopSellingItems(); break;
		   case 5:  showItem(); break;
		   case 6:  quit = true;
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
	
	 private void list3TopSellingItems() {
			String     upc;
			String     totalSales;
			Statement  stmt;
			ResultSet  rs;
			   
			try
			{
			  stmt = con.createStatement();
			  
			  String query = "with temp(upc, total) as (select IP.upc, sum(IP.quantity) as total "
			  		+ "from purchase P, itemPurchase IP where P.purchaseDate >= '15-10-25' "
			  		+ "and P.purchaseDate <= '15-10-31' group by IP.upc) "
			  		+ "select temp.upc, temp.total * I.sellingPrice as totalSales "
			  		+ "from temp, item I where I.upc = temp.upc order by totalSales desc";

			  rs = stmt.executeQuery(query);

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
			  
			  int count = 0;
			  
			  while(rs.next()&& count < 3)
			  {
			      // for display purposes get everything from Oracle 
			      // as a string

			      // simplified output formatting; truncation may occur

			      upc = rs.getString("upc");
			      System.out.printf("%-10.10s", upc);

			      totalSales = rs.getString("totalSales");
			      System.out.printf("%-20.20s\n", totalSales);

			      count++;
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

	private void listLowPopularTextbooks()
	{
	String     upc;
	String     bname;
	//String     baddr;
	//String     bcity;
	//String     bphone;
	Statement  stmt;
	ResultSet  rs;
	   
	try
	{
	  stmt = con.createStatement();
	  
	  String query = "select B.upc, B.title from book B, item I where B.upc = I.upc "
	  		+ "and I.stock < 10 and B.flag_text = 'y' and B.upc in "
	  		+ "(select IP.upc from purchase P, itemPurchase IP "
	  		+ "where P.purchaseDate >= '15-10-25' and P.purchaseDate <= '15-10-31' "
	  		+ "group by IP.upc having sum(IP.quantity) > 50)";

	  rs = stmt.executeQuery(query);

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

	      upc = rs.getString("upc");
	      System.out.printf("%-10.10s", upc);

	      bname = rs.getString("title");
	      System.out.printf("%-60.60s\n", bname);

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
	

	private void removeItem() 
	{
		String                upc;
		PreparedStatement  ps;
		  
		try
		{
		  ps = con.prepareStatement("DELETE FROM item WHERE upc = ?");
		
		  System.out.print("\nUpc: ");
		  upc = in.readLine();
		  ps.setString(1, upc);

		  int rowCount = ps.executeUpdate();

		  if (rowCount == 0)
		  {
		      System.out.println("\nItem " + upc + " does not exist!");
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
		showItem();
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
			//if(ex.getMessage().contains("ORA-00001")){
			if(ex.getErrorCode() == 1){
				System.out.println("ERROR: The UPC already exists in the database!!");
			}else{
		    System.out.println("Message: " + ex.getMessage());
			}
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
		showItem();
	    }
	
	
	
	 private void showItem()
	    {
		String     upc;
		String     sellingPrice;
		String     stock;
		String     taxable;
		//String     bphone;
		Statement  stmt;
		ResultSet  rs;
		   
		try
		{
		  stmt = con.createStatement();

		  rs = stmt.executeQuery("SELECT * FROM item");

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

		      upc = rs.getString("upc");
		      System.out.printf("%-10.10s", upc);

		      sellingPrice = rs.getString("sellingPrice");
		      System.out.printf("%-20.20s", sellingPrice);

		      stock = rs.getString("stock");
		      System.out.printf("%-20.20s", stock);
		      

		      taxable = rs.getString("taxable");
		      System.out.printf("%-15.15s\n", taxable);
		      
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
	
	

	public static void main(String args[])
	    {
	      bookstore b = new bookstore();
	      
	    }
	
}