package FlexiRent.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import FlexiRent.Exception.AlertException;
import FlexiRent.database.DBPath;
import javafx.stage.Stage;

public class RentalRecord  implements DBPath {
	/**

	* The class creates Rental Record for the properties

	* @author Pavan Kumar Bore Gowda

	*/

	private String RecordID, PID;
	private DateTime RentDate, Est_return_date, Act_return_date;
	private float RentalFee = 0, LateFee = 0;

	//constructor 
	public RentalRecord(String pID, String CID, DateTime rentDate, DateTime est_return_date,PropertyBean pb) throws AlertException {
		super();
		PID = pID;
		RecordID = PID + "_" + CID + "_" + rentDate;
		RentDate = rentDate;
		Est_return_date = est_return_date;
		
        
		String sql = "INSERT INTO "+pb.getProperty_ID()+"(Record_id,Rent_date,Est_date,Act_date,Rentalfee,Latefee) VALUES(?,?,?,?,?,?)";
		System.out.println("Rentalrecord created ");
		try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, RecordID);
            pstmt.setString(2,RentDate.toString());
            pstmt.setString(3,Est_return_date.toString());
            pstmt.setString(4, "Not returned");
            pstmt.setFloat(5, 0);
            pstmt.setFloat(6, 0);
            pstmt.executeUpdate();
      
            
        } catch (SQLException e) {
        	throw new AlertException(e.getLocalizedMessage(),new Stage());
        }
	}

	public RentalRecord(String recordID, String rentDate, String est_return_date, String actual_return_date, String rentalfee, String latefee, PropertyBean bean) throws AlertException {

        
		String sql = "INSERT INTO "+bean.getProperty_ID()+"(Record_id,Rent_date,Est_date,Act_date,Rentalfee,Latefee) VALUES(?,?,?,?,?,?)";
		System.out.println("Rentalrecord created ");
		try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, recordID);
            pstmt.setString(2,rentDate);
            pstmt.setString(3,est_return_date);
            pstmt.setString(4, actual_return_date);
            pstmt.setFloat(5, Float.parseFloat(rentalfee));
            pstmt.setFloat(6, Float.parseFloat(latefee));
            pstmt.executeUpdate();
      
            
        } catch (SQLException e) {
        	throw new AlertException(e.getLocalizedMessage(),new Stage());
        }
	}


	public String toString() {
		String s = RecordID + ":" + RentDate.toString() + ":" + Est_return_date.toString() + ":"
				+ Act_return_date.toString() + ":" + RentalFee + ":" + LateFee;
		return s;
	}

	public String getPID() {

		return PID;
	}

	public DateTime getAct_return_date() {
		return Act_return_date;
	}

	public void setAct_return_date(DateTime act_return_date) {
		Act_return_date = act_return_date;
	}

	public double getRentalFee() {
		return RentalFee;
	}

	

	public double getLateFee() {
		return LateFee;
	}



	public String getRecordID() {
		return RecordID;
	}

	public DateTime getRentDate() {
		return RentDate;
	}

	public DateTime getEst_return_date() {
		return Est_return_date;
	}

}
