package FlexiRent.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import FlexiRent.Exception.AlertException;
import FlexiRent.database.DBPath;
import FlexiRent.model.Apartment;
import FlexiRent.model.DateTime;

import FlexiRent.model.PropertyBean;
import FlexiRent.model.RentalProperty;
import FlexiRent.model.RentalRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RentPropertyController implements DBPath{
	/**

	* The class helps to rent the selected property , updates database and updates view

	* @author Pavan Kumar Bore Gowda

	*/
	PropertyBean bean;
	Stage StageUpdate;

	public RentPropertyController(PropertyBean pb, Stage stagecaught) throws AlertException {
		StageUpdate = stagecaught;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/RentProperty.fxml"));

		fxmlLoader.setController(this);
		try {
			Stage stage = new Stage();
			// Parent root=
			// FXMLLoader.load(getClass().getResource("/GUI/listOptions.fxml"));
			Parent root = fxmlLoader.load();
			stage.setTitle("Rent " + pb.getProperty_ID());

			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			throw new AlertException(e.getLocalizedMessage(),new Stage());
		}
		bean = pb;
		rentdate.setEditable(false);
	}

	@FXML
	private TextField customer_id;

	@FXML
	private DatePicker rentdate;

	@FXML
	private TextField no_of_days;

	@FXML
	void StartRent(ActionEvent event) throws AlertException {

		int days = Integer.parseInt(no_of_days.getText());
		DateTime ERD = new DateTime(getDate(rentdate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
				Integer.parseInt(no_of_days.getText()));
		if (!canRent(getDate(rentdate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), days, ERD))
			return;

		// to add the Rental record
		new RentalRecord(bean.getProperty_ID(), customer_id.getText(),
				getDate(rentdate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), ERD, bean);
		
		String sql = "UPDATE RentalProperties SET Status = 1 WHERE Propert_id = '" + bean.getProperty_ID() + "';";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement pstmt = conn.prepareStatement(sql);) {

			pstmt.execute();
		} catch (SQLException e) {

			throw new AlertException(e.getLocalizedMessage(),new Stage());
		}
		Alert alert = new Alert(AlertType.INFORMATION, "Property Rented and Rental record Created", ButtonType.OK);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.OK) {
			new UpdateController(bean, StageUpdate);
		}

		Stage stage = (Stage) rentdate.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	// to convert string date into DateTime object.
	public static DateTime getDate(String dateIn) {

		int day = 0, month = 0, year = 0;

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

		DateTime date = new DateTime(day, month, year);
		return date;
	}

	// to check the day of the week for minimum renting
	public boolean canRent(DateTime date, int days, DateTime ERD) {

		if (bean.getType() == 0) {
			Date d = new Date();
			d.setDate(date.getdate());
			d.setMonth(date.getMonth());
			d.setYear(date.getYear());
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			System.out.println("Week is ");
			System.out.println(dayOfWeek);
			if ((dayOfWeek == 1 || dayOfWeek == 2 || dayOfWeek == 5 || dayOfWeek == 6 || dayOfWeek == 7) && days < 2) {
				alertMessage("You should stay a minimum of 2 days ");
				return false;
			} else if ((dayOfWeek == 3 || dayOfWeek == 4) && days < 3) {
				alertMessage("You should stay a minimum of 3 days ");
				return false;
			} else if (days > 28) {
				alertMessage("You cant stay longer than 28 days ");
				return false;
			} else
				return true;
		} else {

			DateTime LastMaintainance = getDate(bean.getLastMaintainance());

			DateTime NextMaintainance = new DateTime(LastMaintainance, 10);
			int diff = DateTime.diffDays(NextMaintainance, ERD);
			if (diff < 0) {
				alertMessage("Not maintained ,Should be gone for maintainace ");
				return false;
			} else if (days < 1) {
				alertMessage("Enter valid number of days");
				return false;
			} else
				return true;

		}

	}

	public void alertMessage(String msg) {
		Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.OK) {

		}

	}

}
