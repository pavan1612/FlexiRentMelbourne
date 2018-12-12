package FlexiRent.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import FlexiRent.Exception.AlertException;
import FlexiRent.database.DBPath;
import FlexiRent.model.PropertyBean;
import javafx.stage.Stage;

public class UpdateController implements DBPath {
	/**

	* The class helps to update the view when property changes 

	* @author Pavan Kumar Bore Gowda

	*/
	PropertyBean bean;
	Stage StageUpdate;

	public UpdateController(PropertyBean pb,Stage stage) throws AlertException {
		
		bean=pb;
		StageUpdate=stage;
		try {
			update();
		} catch (SQLException e) {
			
			throw new AlertException(e.getLocalizedMessage(),new Stage());
		}
	}
	
	public void update() throws SQLException, AlertException {
		   
	   		 
	   	   	 String sql = "SELECT * FROM RentalProperties where Propert_id='"+bean.getProperty_ID()+"'";
	   	       
	   	       try (Connection conn = DriverManager.getConnection(url);
	   	            Statement stmt  = conn.createStatement();
	   	            ResultSet rs    = stmt.executeQuery(sql)){
	   	       	
	   	           // loop through the result set
	   	           while (rs.next()) {
	   	           	
	   	           	if(rs.getInt("Type")==0) {
	   	           		 bean = new PropertyBean(rs.getString("Propert_id") , rs.getString("Address"), rs.getInt("NOB"), rs.getInt("Type"), rs.getInt("Status"), rs.getString("Image"));
	   	           		
	   	           	}
	   	           	else {
	   	           		
	   	           		bean = new PropertyBean(rs.getString("Propert_id") , rs.getString("Address"), rs.getInt("NOB"), rs.getInt("Type"), rs.getInt("Status"),rs.getString("Maintainance"), rs.getString("Image"));
	   	           	}
	   	}
	   }
	   
	   	new ListOptionsController(bean, StageUpdate);
	   }

}
