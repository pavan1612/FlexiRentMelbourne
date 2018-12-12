package FlexiRent.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import FlexiRent.Exception.AlertException;
import FlexiRent.database.DBPath;
import javafx.stage.Stage;

public abstract class RentalProperty  implements DBPath{
	/**

	* The class is the model for all properties

	* @author Pavan Kumar Bore Gowda

	*/

	private String Property_ID, Street_no, Street_name, Suburb,PropImage,LastMaintainance;

	public RentalProperty(String property_ID, String street_no, String street_name, String suburb, int nOB,
			boolean type, int status,String lastMaintainance,String propImage) throws AlertException {
		
		Property_ID = property_ID;
		Street_no = street_no;
		Street_name = street_name;
		Suburb = suburb;
		PropImage = propImage;
		LastMaintainance = lastMaintainance;
		NOB = nOB;
		Type = type;
		Status = status;
		
		
		
        
		String sql = "INSERT INTO RentalProperties(Propert_id,Address,Type,NOB,Status,Maintainance,Image) VALUES(?,?,?,?,?,?,?)";
		String createsql =  "CREATE TABLE IF NOT EXISTS "+Property_ID+"(\n"
	            + "	Record_id text PRIMARY KEY,\n"
	            + "	Rent_date text ,\n"
	            + "	Est_date text ,\n"
	            + "	Act_date text ,\n"
	            + "	Rentalfee real,\n"
	            + "	Latefee real\n"
	            + ");";
		 
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql);
        		PreparedStatement pstmt2 = conn.prepareStatement(createsql);) {
            pstmt.setString(1, Property_ID);
            pstmt.setString(2,Street_no+" "+Street_name+" "+Suburb);
            pstmt.setInt(3,Type? 1 : 0);
            pstmt.setInt(4, NOB);
            pstmt.setInt(5, Status);
            pstmt.setString(6, LastMaintainance);
            pstmt.setString(7, PropImage);
            pstmt.executeUpdate();
      
            pstmt2.execute();
        } catch (SQLException e) {
        	throw new AlertException(e.getLocalizedMessage(),new Stage());
        }
		
	}

	private int NOB;
	private boolean Type;
	private int Status;
	
	//constructor
	public RentalProperty(String property_ID, String street_no, String street_name, String suburb, int nOB,
			boolean type, int status,String propImage) throws AlertException {
		
		Property_ID = property_ID;
		Street_no = street_no;
		Street_name = street_name;
		Suburb = suburb;
		NOB = nOB;
		Type = type;
		Status = status;
		PropImage=propImage;
		
		
        
		String sql = "INSERT INTO RentalProperties(Propert_id,Address,Type,NOB,Status,Maintainance,Image) VALUES(?,?,?,?,?,?,?)";
		String createsql =  "CREATE TABLE IF NOT EXISTS "+Property_ID+"(\n"
	            + "	Record_id text PRIMARY KEY,\n"
	            + "	Rent_date text ,\n"
	            + "	Est_date text ,\n"
	            + "	Act_date text ,\n"
	            + "	Rentalfee real,\n"
	            + "	Latefee real\n"
	            + ");";
		
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                		PreparedStatement pstmt2 = conn.prepareStatement(createsql);) {
            pstmt.setString(1, Property_ID);
            pstmt.setString(2,Street_no+" "+Street_name+" "+Suburb);
            pstmt.setInt(3,Type? 1 : 0);
            pstmt.setInt(4, NOB);
            pstmt.setInt(5, Status);
            pstmt.setString(6, "NotValid ");
            pstmt.setString(7, PropImage);
            pstmt.executeUpdate();
            
            pstmt2.execute();
        } catch (SQLException e) {
        	throw new AlertException(e.getLocalizedMessage(),new Stage());
        }
    }
    
		
	



	//getters and setters
	public String getProperty_ID() {
		return Property_ID;
	}

	public String getStreet_no() {
		return Street_no;
	}

	public String getStreet_name() {
		return Street_name;
	}

	public String getSuburb() {
		return Suburb;
	}

	public int getNOB() {
		return NOB;
	}

	public boolean isType() {
		return Type;
	}

	public int getStatus() {
		return Status;
	}
	
	public void setStatus(int i) {
		Status=i;
	}
	


}
