package FlexiRent.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import FlexiRent.Application.MainApp;
import FlexiRent.Exception.AlertException;
import FlexiRent.database.DBPath;
import FlexiRent.model.Apartment;
import FlexiRent.model.DateTime;
import FlexiRent.model.PremiumSuite;
import FlexiRent.model.PropertyBean;
import FlexiRent.model.RentalRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ListOptionsController implements DBPath{
	PropertyBean pb;
	

    @FXML
    private Label AddLabel;

    @FXML
    private Label TypeLabel;

    @FXML
    private Label RoomLabel;

    @FXML
    private Label StatusLabel;

    @FXML
    private Label LMLabel;

    @FXML
    private Label LM;
    @FXML
    private Button rent;

    @FXML
    private Button returnProp;

    @FXML
    private Button prepare;

    @FXML
    private Button complete;
    
    public Stage stagesend;

   
	public ListOptionsController(PropertyBean pB2 ,Stage stage) throws AlertException  {
		/**

		* The class helps interact with listOption view which display the selected property details 

		* @author Pavan Kumar Bore Gowda

		*/
		pb=pB2;
		stagesend=stage;

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/listOptions.fxml"));
        
        fxmlLoader.setController(this);
		try {
    		//Parent root= FXMLLoader.load(getClass().getResource("/GUI/listOptions.fxml"));
    		Parent root= fxmlLoader.load();
    		stage.setTitle("Property Details ");
            
            stage.setScene(new Scene(root));
            stage.show();
    		} catch (IOException e) {
    			throw new AlertException(e.getLocalizedMessage(),new Stage());
    		}
		setInfo(pb);
		if(pb.getStatus()==1) {
			
			rent.setDisable(true);
			complete.setDisable(true);
			prepare.setDisable(true);
		}
		else if(pb.getStatus()==2) {
			
			rent.setDisable(true);
			returnProp.setDisable(true);
			prepare.setDisable(true);
		}
		else {
			
			returnProp.setDisable(true);
			complete.setDisable(true);
		}
		
		ArrayList <String>RecordList =new ArrayList();
	   	
	   	 String sql = "Select * from "+pb.getProperty_ID();
	       
	       try (Connection conn = DriverManager.getConnection(url);
	            Statement stmt  = conn.createStatement();
	            ResultSet rs    = stmt.executeQuery(sql)){
	       	
	           while (rs.next()) {
	           	
	           	
	           	System.out.println(rs.getString("Record_id"));
	           	String s = "\nRecord ID:                " + rs.getString("Record_id") + "\nRental Date:                " + rs.getString("Rent_date")
	    				+ "\nEstimated Return Date:      " + rs.getString("Est_date");
	    		String s1 = "\nRecord ID:                  " + rs.getString("Record_id") + "\nRental Date:                " + rs.getString("Rent_date")
	    				+ "\nEstimated Return Date:      " + rs.getString("Est_date") + "\nActual Return Date          "
	    				+ rs.getString("Act_date") + "\nRental Fee                  " + rs.getString("Rentalfee")
	    				+ "\nLate Fee                    " + rs.getString("Latefee");
	    		if (Float.parseFloat(rs.getString("Rentalfee")) == 0) {
	    			RecordList.add(s);
	    		} else
	    			RecordList.add(s1);
	           	
	               
	           }
	       } catch (SQLException e) {
			
	    	   throw new AlertException(e.getLocalizedMessage(),new Stage());
		}
	   	 ObservableList<String> List = FXCollections.observableArrayList(RecordList);
	   	 RentalRecordList.setItems(List);
		
	}
	
	public String getTypeString(PropertyBean bean) {
		if(bean.getType()==0) 
			return "Apartment";
		else
			return "Premium Suite";
	}
	public String getStatusString(PropertyBean bean) {
		System.out.println(bean.getStatus());
		if(bean.getStatus()==0) 
			return "Available";
		else if(bean.getStatus()==1)
			return "Rented";
		else
			return " Under Maintainance";
	}
	
	
	private void setInfo(PropertyBean pb) {
		Info.setText(pb.getProperty_ID());
		AddLabel.setText(pb.getAddress());
		RoomLabel.setText(Integer.toString(pb.getNOB()));
		TypeLabel.setText(getTypeString(pb));
		StatusLabel.setText(getStatusString(pb));
		File file =new File("Images/"+pb.getPropImage());
    	PropImage.setImage(new Image(file.toURI().toString()));
    	if(pb.getType()==0) {
    		
    		LMLabel.setVisible(false);
    		LM.setVisible(false);
    	}
    	else
    	{
    		LMLabel.setText(pb.getLastMaintainance());
    		
    	}
	}


    @FXML
    private ImageView PropImage;
	@FXML
    private Label Info;

    @FXML
    private ListView<String> RentalRecordList;

    @FXML
    void goBack(ActionEvent event) throws AlertException {

    	try { 
    		new MainApp().start((Stage)Info.getScene().getWindow()); } catch (Exception e) { throw new AlertException(e.getLocalizedMessage(),new Stage()); }
    	
    }
    @FXML
    void CompleteMaintainance(ActionEvent event) throws AlertException {
    	if(pb.getType()==1) {
    		
    		new CompleteMaintainanceController(pb,stagesend);
    	}
    	else {
    	
    	String sql="UPDATE RentalProperties SET Status = 0 WHERE Propert_id = '"+pb.getProperty_ID()+"';";
		try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql);){
			
			pstmt.execute();
		}
		catch (SQLException e) {
					
			throw new AlertException(e.getLocalizedMessage(),new Stage());
				}
		
		Alert alert = new Alert(AlertType.INFORMATION, "Property Completed Maintainance", ButtonType.OK);
    	alert.showAndWait();

    	if (alert.getResult() == ButtonType.OK) {
    		new UpdateController(pb, stagesend);
    	}
    	}

    }

    @FXML
    void prepareMaintainance(ActionEvent event) throws AlertException {
    
    	
    	String sql="UPDATE RentalProperties SET Status = 2 WHERE Propert_id = '"+pb.getProperty_ID()+"';";
		try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql);){
			
			pstmt.execute();
		}
		catch (SQLException e) {
					
			throw new AlertException(e.getLocalizedMessage(),new Stage());
				}
		
		Alert alert = new Alert(AlertType.INFORMATION, "Property put under Maintainance", ButtonType.OK);
    	alert.showAndWait();

    	if (alert.getResult() == ButtonType.OK) {
    		new UpdateController(pb, stagesend);
    	}
    }
    

    @FXML
    void rentProperty(ActionEvent event) throws AlertException {

    	new RentPropertyController(pb,stagesend);
    }

    @FXML
    void returnProperty(ActionEvent event) throws AlertException {

    	new ReturnPropertyController(pb,stagesend);
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
      			new MainApp().start((Stage)Info.getScene().getWindow()); } catch (Exception e) { throw new AlertException(e.getLocalizedMessage(),new Stage()); }
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

	


}
