package FlexiRent.Exception;

import FlexiRent.Application.MainApp;
import FlexiRent.controller.UpdateController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AlertException extends Throwable{
	/**

	* The exception class displays exceptions for all classes with an alert

	* @author Pavan Kumar Bore Gowda

	*/

	public AlertException(String msg,Stage stage) {
		Alert alert = new Alert(AlertType.INFORMATION, msg+" /n/n/n Go back to main screen.", ButtonType.OK);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.OK) {
			try { 
	    		new MainApp().start(stage); } catch (Exception e) { e.printStackTrace(); }
		}
		
	}
	
	

}
