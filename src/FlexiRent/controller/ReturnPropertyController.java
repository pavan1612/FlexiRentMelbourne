package FlexiRent.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

import FlexiRent.Exception.AlertException;
import FlexiRent.database.DBPath;
import FlexiRent.model.DateTime;
import FlexiRent.model.PropertyBean;
import FlexiRent.model.RentalRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ReturnPropertyController implements DBPath{
	/**

	* The class helps to return the selected property , updates database,calculates rent and updates view

	* @author Pavan Kumar Bore Gowda

	*/


	 @FXML
	    private DatePicker ActualreturnDate;

    PropertyBean bean;
    Stage StageUpdate;
    public ReturnPropertyController(PropertyBean pb,Stage stagecaught) throws AlertException {
    	StageUpdate=stagecaught;
FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/ReturnProperty.fxml"));
        
        fxmlLoader.setController(this);
		try {
			Stage stage=new Stage();
    		//Parent root= FXMLLoader.load(getClass().getResource("/GUI/listOptions.fxml"));
    		Parent root= fxmlLoader.load();
    		stage.setTitle("Return  "+pb.getProperty_ID());
            
            stage.setScene(new Scene(root));
            stage.show();
    		} catch (IOException e) {
    			throw new AlertException(e.getLocalizedMessage(),new Stage());
    		}
		bean=pb;
		ActualreturnDate.setEditable(false);
	}

	@FXML
    void StartReturn(ActionEvent event) throws AlertException {
		String est_return=null,rentdate=null,recordid=null;
		int latedays=0;
		
		String erdQuery="Select * from "+bean.getProperty_ID()+";"; 
		
		
		try (Connection conn = DriverManager.getConnection(url);
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(erdQuery)){
			
			 while (rs.next()) {
				 
				 est_return=rs.getString("Est_date");
				 rentdate=rs.getString("Rent_date");
				 recordid=rs.getString("Record_id");
				 
			 }
			}
		catch (SQLException e) {
					
			throw new AlertException(e.getLocalizedMessage(),new Stage());
				}
		System.out.println(est_return+rentdate+recordid);
		
		double rent;
		int days=DateTime.diffDays((getDate(est_return)),getDate(rentdate));
		if(bean.getType()==0) {
		
		if (bean.getNOB()== 1)
			rent = 143 * days;
		else if (bean.getNOB() == 2)
			rent = 210 * days;
		else
			rent = 319 * days;
		}
		else
			rent= 554 * days;
		
		int diff = DateTime.diffDays(getDate(ActualreturnDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), getDate(est_return));

		if (diff > 0) {
			latedays = diff;
		}
		
		double latefee;
		if(bean.getType()==0) {
		if (bean.getNOB() == 1)
			latefee = 143 * latedays * 1.15;
		else if (bean.getNOB() == 2)
			latefee = 210 * latedays * 1.15;
		else
			latefee = 319 * latedays * 1.15;
		}
		else
			latefee = 662 * latedays;
		System.out.println(rent);
		System.out.println(latefee);
		
		String sql="UPDATE RentalProperties SET Status = 0 WHERE Propert_id = '"+bean.getProperty_ID()+"';";
		try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql);){
			
			pstmt.execute();
		}
		catch (SQLException e) {
					
			throw new AlertException(e.getLocalizedMessage(),new Stage());
				}
		String sqlupdaterecord="UPDATE "+bean.getProperty_ID()+" SET Act_date = '"+ActualreturnDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"' , Rentalfee ="+rent+" , Latefee ="+latefee+" WHERE Record_id = '"+recordid+"';";
		System.out.println(sqlupdaterecord);
		try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sqlupdaterecord);){
			
			pstmt.execute();
		}
		catch (SQLException e) {
					
			throw new AlertException(e.getLocalizedMessage(),new Stage());
				}
		
    	

    	Alert alert = new Alert(AlertType.INFORMATION, "Property Returned", ButtonType.OK);
    	alert.showAndWait();

    	if (alert.getResult() == ButtonType.OK) {
    		new UpdateController(bean, StageUpdate);
    	}
    	
    	
    	Stage stage = (Stage) ActualreturnDate.getScene().getWindow();
        // do what you have to do
        stage.close();

    }
	public static DateTime getDate(String dateIn) {
		boolean i = true;
		int day = 0, month = 0, year = 0;
		while (i) {

			int a = Integer.parseInt(String.valueOf(dateIn.charAt(0)));
			int b = Integer.parseInt(String.valueOf(dateIn.charAt(1)));
			day = a * 10 + b;
			a = Integer.parseInt(String.valueOf(dateIn.charAt(3)));
			b = Integer.parseInt(String.valueOf(dateIn.charAt(4)));
			month = a * 10 + b;
			a = Integer.parseInt(String.valueOf(dateIn.charAt(6)));
			b = Integer.parseInt(String.valueOf(dateIn.charAt(7)));
			int c = Integer.parseInt(String.valueOf(dateIn.charAt(8)));
			int d = Integer.parseInt(String.valueOf(dateIn.charAt(9)));
			year = a * 1000 + b * 100 + c * 10 + d;
			/*if (day > 31 || day < 0) {
				System.out.println("Enter Correct date");
				System.out.println("Enter Date Again");
				in.nextLine();
				dateIn = in.nextLine();
				continue;
			}
			if (month > 12 || month < 0) {
				System.out.println("Enter Correct month");
				System.out.println("Enter Date Again");
				in.nextLine();
				dateIn = in.nextLine();

				continue;
			}
			if (year > 2020 || year < 2017) {
				System.out.println("Enter Correct year");
				System.out.println("Enter Date Again");
				in.nextLine();
				dateIn = in.nextLine();

				continue;
			}*/
			i = false;

		}

		DateTime date = new DateTime(day, month, year);
		return date;
	}
}
