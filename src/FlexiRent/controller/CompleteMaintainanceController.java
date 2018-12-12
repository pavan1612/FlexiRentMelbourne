package FlexiRent.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import FlexiRent.Exception.AlertException;
import FlexiRent.database.DBPath;
import FlexiRent.model.PropertyBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CompleteMaintainanceController implements DBPath {
	/**

	* The class helps to complete the maintainance of properties

	* @author Pavan Kumar Bore Gowda

	*/
	
	PropertyBean bean;
	Stage stagesend,stage ;

	public CompleteMaintainanceController(PropertyBean pb,Stage stagegot) throws AlertException {
		bean=pb;
		stagesend=stagegot;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/CompleteMaintainance.fxml"));

		fxmlLoader.setController(this);
		try {
			stage = new Stage();
			// Parent root=
			// FXMLLoader.load(getClass().getResource("/GUI/listOptions.fxml"));
			Parent root = fxmlLoader.load();
			stage.setTitle("Maintainance for " + pb.getProperty_ID());

			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {

			throw new AlertException(e.getLocalizedMessage(),new Stage());
		}
	}
	@FXML
    private DatePicker CompletionDate;

    @FXML
    void StartReturn(ActionEvent event) throws AlertException {
    	

    	String sql="UPDATE RentalProperties SET Maintainance ='"+CompletionDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"' WHERE Propert_id = '"+bean.getProperty_ID()+"';";
		System.out.println(sql);
    	try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql);){
			
			pstmt.execute();
		}
		catch (SQLException e) {
					
			throw new AlertException(e.getLocalizedMessage(),new Stage());
				}
		
		
    	String sql1="UPDATE RentalProperties SET Status = 0 WHERE Propert_id = '"+bean.getProperty_ID()+"';";
		try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql1);){
			
			pstmt.execute();
		}
		catch (SQLException e) {
					
			throw new AlertException(e.getLocalizedMessage(),new Stage());
				}
		stage.close();
		
		Alert alert = new Alert(AlertType.INFORMATION, "Property Completed Maintainance", ButtonType.OK);
    	alert.showAndWait();

    	if (alert.getResult() == ButtonType.OK) {
    		new UpdateController(bean, stagesend);
    	}
    }
	
	

}
