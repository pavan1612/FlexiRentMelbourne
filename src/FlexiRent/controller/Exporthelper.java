package FlexiRent.controller;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import FlexiRent.Exception.AlertException;
import FlexiRent.database.DBPath;
import FlexiRent.model.PropertyBean;
import javafx.stage.Stage;



public class Exporthelper implements DBPath {
	/**

	* The class helps exports the properties as a .txt file

	* @author Pavan Kumar Bore Gowda

	*/

	public Exporthelper(String filepath) throws AlertException {
		ArrayList <String> PropertyValues =new ArrayList();
	   	 String sql = "SELECT * FROM RentalProperties";
	       String Propdetails;
	       try (Connection conn = DriverManager.getConnection(url);
	            Statement stmt  = conn.createStatement();
	            ResultSet rs    = stmt.executeQuery(sql)){
	      
	           
	           while (rs.next()) {
	        	   if(rs.getInt("Type")==0) {
	        		   String status;
	        		   if( rs.getInt("Status")==0) 
	        			   status="Available";
	        		   else if( rs.getInt("Status")==1) 
	        			   status="Rented";
	        		   else status="Under maintainance";
	        		   String[] words=rs.getString("Address").split(" ");
	        		   if(words.length==4)
	        		   Propdetails  = (rs.getString("Propert_id")+":" + words[0]+":"+ words[1]+" "+words[2]+":"+ words[3]+":"+ "Apartment"+":"+ rs.getInt("NOB")+":"+ status+":"+ rs.getString("Image"));
	        		   else 
		        		   Propdetails  = (rs.getString("Propert_id")+":" + words[0]+":"+ words[1]+" "+words[2]+":"+ words[3]+" "+words[4]+":"+ "Apartment"+":"+ rs.getInt("NOB")+":"+ status+":"+ rs.getString("Image"));
	             		PropertyValues.add(Propdetails);
	             	}
	             	else {
	             		String status;
		        		   if( rs.getInt("Status")==0) 
		        			   status="Available";
		        		   else if( rs.getInt("Status")==1) 
		        			   status="Rented";
		        		   else status="Under maintainance";
		        		   String[] words=rs.getString("Address").split(" ");
		        		   if(words.length==4)
		        		   Propdetails  = (rs.getString("Propert_id")+":" + words[0]+":"+ words[1]+" "+words[2]+":"+ words[3]+":"+ "Premium Suite"+":"+ rs.getInt("NOB")+":"+ status+":"+rs.getString("Maintainance")+":"+ rs.getString("Image"));
		        		   else 
			        		   Propdetails  = (rs.getString("Propert_id")+":" + words[0]+":"+ words[1]+" "+words[2]+":"+ words[3]+" "+words[4]+":"+ "Premium Suite"+":"+ rs.getInt("NOB")+":"+ status+":"+rs.getString("Maintainance")+":"+ rs.getString("Image"));
		             		PropertyValues.add(Propdetails);
	             		
	             	}
	           }
	         
	           
				FileWriter fileWriter = new FileWriter(filepath);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				for (String list: PropertyValues) {
					printWriter.println(list);
					printrecord(list,printWriter);
		        	}
				
				fileWriter.flush();
				fileWriter.close();
	           } catch (SQLException e) {
	        	   throw new AlertException(e.getLocalizedMessage(),new Stage());
			} catch (FileNotFoundException e) {
				throw new AlertException(e.getLocalizedMessage(),new Stage());
			} catch (IOException e) {
				throw new AlertException(e.getLocalizedMessage(),new Stage());
			}


	       
	}

	private void printrecord(String list, PrintWriter printWriter) throws AlertException {
		String[] words = list.split(":");
		
	   	 String sql = "SELECT * FROM "+words[0];
	       String Propdetails;
	       try (Connection conn = DriverManager.getConnection(url);
	            Statement stmt  = conn.createStatement();
	            ResultSet rs    = stmt.executeQuery(sql)){
	      
	           
	           while (rs.next()) {
	        	   
	        	   String s1 =rs.getString("Record_id")+":" + rs.getString("Rent_date")+":"
   				+ rs.getString("Est_date")+":" 
   				+ rs.getString("Act_date")+":" + rs.getString("Rentalfee")+":"
   				+ rs.getString("Latefee");
	        	   printWriter.println(s1);
	           }
		
		
	} catch (SQLException e) {
		throw new AlertException(e.getLocalizedMessage(),new Stage());
	}
	}
	
}


