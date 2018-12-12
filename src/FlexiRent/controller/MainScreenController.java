package FlexiRent.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import FlexiRent.Exception.AlertException;
import FlexiRent.database.DBPath;
import FlexiRent.model.Apartment;
import FlexiRent.model.DateTime;
import FlexiRent.model.PremiumSuite;
import FlexiRent.model.PropertyBean;
import FlexiRent.model.RentalRecord;
public class MainScreenController implements DBPath{
	/**

	* The class helps to interact with user by being the first screen it loads 

	* @author Pavan Kumar Bore Gowda

	*/

	@FXML
	    private MenuItem AddApartment;

	@FXML
    private TextField searchText;

    @FXML
    private MenuButton type;

    @FXML
    private MenuButton no_rooms;

    @FXML
    private MenuButton status;

    @FXML
    private GridView<PropertyBean> listView;
    
    @FXML
    private  void initialize()  throws SQLException {
    	refresh("SELECT * FROM RentalProperties");
    	listView.setCellWidth(350);
    	listView.setCellHeight(200);
    }
    // refresh used to filter the properties
    public void refresh(String query) throws SQLException{
    	
    	listView.setCellWidth(350);
    	listView.setCellHeight(200);
    	ArrayList <PropertyBean>RentalPropertyList =new ArrayList();
   	
   	 String sql = query;
       
       try (Connection conn = DriverManager.getConnection(url);
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)){
       	PropertyBean bean;
           // loop through the result set
           while (rs.next()) {
           	
           	if(rs.getInt("Type")==0) {
           		 bean = new PropertyBean(rs.getString("Propert_id") , rs.getString("Address"), rs.getInt("NOB"), rs.getInt("Type"), rs.getInt("Status"), rs.getString("Image"));
           		
           	}
           	else {
           		
           		bean = new PropertyBean(rs.getString("Propert_id") , rs.getString("Address"), rs.getInt("NOB"), rs.getInt("Type"), rs.getInt("Status"),rs.getString("Maintainance"), rs.getString("Image"));
           	}
           	
           	
               RentalPropertyList.add(bean);
           }
       }
   	 ObservableList<PropertyBean> List = FXCollections.observableArrayList(RentalPropertyList);
   	 listView.setItems(List);
   	 listView.setCellFactory(
                new Callback<GridView<PropertyBean>, GridCell<PropertyBean>>() {
                    @Override
                    public GridCell<PropertyBean> call(GridView<PropertyBean> listView) {
                        return new ListViewCell();
                    }
                });
    }

    @FXML
    void addProperty(ActionEvent event) throws AlertException {
    	
    	
		try {
		Parent root= FXMLLoader.load(getClass().getResource("/GUI/AddApartment.fxml"));
        Stage stage = (Stage) type.getScene().getWindow();
		stage.setTitle("Add Apartment");
        
        stage.setScene(new Scene(root));
        stage.show();
		} catch (IOException e) {
			throw new AlertException(e.getLocalizedMessage(),new Stage());
		}
    }
    
    @FXML
    void exportFile(ActionEvent event) throws AlertException {
    	Stage stage = new Stage();
    	final FileChooser fileChooser = new FileChooser();
    	String filepath;
    	Alert alert = new Alert(AlertType.INFORMATION, "Please select a .txt file that you want to export to.", ButtonType.OK);
      	alert.showAndWait();

      	if (alert.getResult() == ButtonType.OK) {
      		File file = fileChooser.showOpenDialog(stage);
        	filepath=file.getAbsolutePath();
      		new Exporthelper(filepath);
      	}
      	
    	
    	
    	
    }

    @FXML
    void importFile(ActionEvent event) throws AlertException {
    	Stage stage = new Stage();
    	final FileChooser fileChooser = new FileChooser();
    	File file = fileChooser.showOpenDialog(stage);
    	
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
          Alert alert = new Alert(AlertType.INFORMATION, "File imorted. Success", ButtonType.OK);
      	alert.showAndWait();

      	if (alert.getResult() == ButtonType.OK) {
      		try {
  				initialize();
  			} catch (SQLException e) {
  				
  				throw new AlertException(e.getLocalizedMessage(),new Stage());
  			}
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
    
    public int getStatusInt(String status) {
		
		if(status.equals("Available")) 
			return 0;
		else if(status.equals("Rented"))
			return 1;
		else
			return 2;
	}
    
    
    PropertyBean bean=null;
    private void importdb(String[] words, int length) throws NumberFormatException, AlertException {
		
    	
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

	@FXML
    void OnAvailable(ActionEvent event) throws SQLException {
    	refresh("SELECT * FROM RentalProperties where Status=0");
    }

    @FXML
    void OnMaintainance(ActionEvent event) throws SQLException{
    	refresh("SELECT * FROM RentalProperties where Status=2");
    }

    @FXML
    void OnRented(ActionEvent event) throws SQLException{
    	refresh("SELECT * FROM RentalProperties where Status=1");
    }

    @FXML
    void Search(ActionEvent event) throws SQLException{
    	refresh("SELECT * FROM RentalProperties");
    }

    @FXML
    void room1(ActionEvent event) throws SQLException{
    	refresh("SELECT * FROM RentalProperties where NOB=1");
    }

    @FXML
    void room2(ActionEvent event) throws SQLException{
    	refresh("SELECT * FROM RentalProperties where NOB=2");
    }

    @FXML
    void room3(ActionEvent event) throws SQLException{
    	refresh("SELECT * FROM RentalProperties where NOB=3");
    }

    @FXML
    void typeApartment(ActionEvent event) throws SQLException{
    	refresh("SELECT * FROM RentalProperties where Type=0");
    }

    @FXML
    void typePremiumSuite(ActionEvent event) throws SQLException{
    	refresh("SELECT * FROM RentalProperties where Type=1");
    }
    @FXML
    void exit(ActionEvent event) throws SQLException{
    	
    	Stage s=(Stage)status.getScene().getWindow();
    	s.close();
    }


}
