package FlexiRent.Application;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import FlexiRent.Exception.AlertException;
import FlexiRent.database.CreateRentalPropertyTable;
import FlexiRent.database.DBConnection;
import FlexiRent.database.DBPath;
import FlexiRent.model.Apartment;
import FlexiRent.model.PremiumSuite;
import FlexiRent.model.PropertyBean;
import FlexiRent.model.RentalRecord;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainApp extends Application implements DBPath {
	/**

	* The class helps listens to the start the program 

	* @author Pavan Kumar Bore Gowda

	*/
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/GUI/MainScreen.fxml"));

        Scene scene = new Scene(root);
        //scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("FlexiRentSystem");
        stage.setScene(scene);
        stage.show();
    }

    // application begins
    public static void main(String[] args) throws AlertException, SQLException {
    	
    	DBConnection.connect();
    	CreateRentalPropertyTable.createNewTable();
    	//if(checkDB()) {
    	//	importDB();
    	//}
    	
    		launch(args);
        
    }

	private static void importDB() throws SQLException, AlertException {
	
    	File file = new File("src/importdata.txt");
    	
    	FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
     
        try {
          fis = new FileInputStream(file);
     
          // Here BufferedInputStream is added for fast reading.
          bis = new BufferedInputStream(fis);
          dis = new DataInputStream(bis);
     
          // dis.available() returns 0 if the file does not have more lines.
          while (dis.available() != 0) {
     
        	  String x =dis.readLine();
        	  String[] words = x.split(":");
        	  importdb(words,words.length);
            System.out.println(words.length);
            
          }
      
  		
     
          // dispose all the resources after using them.
          fis.close();
          bis.close();
          dis.close();
     
        } catch (FileNotFoundException e) {
        	throw new AlertException(e.getLocalizedMessage(),new Stage());
        } catch (IOException e) {
        	throw new AlertException(e.getLocalizedMessage(),new Stage());
        }
    }
    
	static PropertyBean bean=null;
    private static void importdb(String[] words, int length) throws NumberFormatException, AlertException {
		
    	
    	if(length==9) {
    		
    		new PremiumSuite(words[0], words[1], words[2], words[3], Integer.parseInt(words[5]),true, getStatusInt(words[6]) , words[7],words[8]);
    	 bean= new PropertyBean(words[0], words[1]+words[2]+words[3],  Integer.parseInt(words[5]), 1, getStatusInt(words[6]), words[7], words[8]);
    	}
    	else if(length==8) {
    		
    		
    		new Apartment(words[0], words[1], words[2], words[3], Integer.parseInt(words[5]),false, getStatusInt(words[6]) , words[7]);
    		bean= new PropertyBean(words[0], words[1]+words[2]+words[3],  Integer.parseInt(words[5]), 0, getStatusInt(words[6]), words[7]);
    	}
    	else if(length==6) {
    		
    		new RentalRecord(words[0], words[1], words[2],words[3],words[4],words[5],bean);
    		
    	}
    	
	}
    
    

		
	
public static int getStatusInt(String status) {
	
		if(status.equals("Available")) 
			return 0;
		else if(status.equals("Rented"))
			return 1;
		else
			return 2;
	}

	private static boolean checkDB() {
		try (Connection conn = DriverManager.getConnection(url);
	            Statement stmt  = conn.createStatement();
	            ResultSet rs    = stmt.executeQuery("SELECT * FROM RentalProperties")){
			if(rs.next()) {
				return false;
			}
			
		} catch (SQLException e) {
			
		}
		return true;
	}
}