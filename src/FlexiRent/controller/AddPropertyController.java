package FlexiRent.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import FlexiRent.Application.MainApp;
import FlexiRent.Exception.AlertException;
import FlexiRent.model.Apartment;
import FlexiRent.model.PremiumSuite;
import FlexiRent.model.PropertyBean;
import FlexiRent.model.RentalProperty;
import FlexiRent.model.RentalRecord;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddPropertyController {
	/**

	* The class helps to add a new property 

	* @author Pavan Kumar Bore Gowda

	*/
	

   String imagechoosen="NoImage.png";
   
    ObservableList<String> ApartroomList = FXCollections.observableArrayList("1","2","3"); 
    ObservableList<String> ApartroomList2 = FXCollections.observableArrayList("3"); 
    ObservableList<String> ImageList = FXCollections.observableArrayList("Image1","Image2","Image3","Image4"); 
    ObservableList<String> PropertyList = FXCollections.observableArrayList("Apartment","Premium Suite");
    @FXML
    private ChoiceBox<String> noOfRooms;
    @FXML
    private TextField St_No;

    @FXML
    private TextField St_name;

    @FXML
    private TextField suburb;

    @FXML
    private ChoiceBox<String> ImageChoice;

    @FXML
    private ChoiceBox<String> TypeChoice;
    
    @FXML
    private ImageView DefaultImage;
   
    
    @FXML
    private Label lastLabel;

    @FXML
    private DatePicker LMDate;

    @FXML
    private void initialize() {
    	
    	noOfRooms.setValue("1");
    	noOfRooms.setItems(ApartroomList);
    	//ImageChoice.setValue("Image1");
    	//ImageChoice.setItems(ImageList);
    	TypeChoice.setValue("Apartment");
    	TypeChoice.setItems(PropertyList);
    	File file =new File("Images/NoImage.png");
       DefaultImage.setImage(new Image(file.toURI().toString()));
       LMDate.setVisible(false);
       LMDate.setEditable(false);
		 lastLabel.setVisible(false);
       
      /* ImageChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
    	      @Override
    	      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
    	    	  updateImage(ImageChoice.getItems().get((Integer) number2));
    	        
    	      }
    	    });*/
       TypeChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
 	      @Override
 	      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
 	    	  System.out.println();
 	    	 if(TypeChoice.getItems().get((Integer) number2).equals("Apartment")){
 	    		 
 	    		LMDate.setVisible(false);
 	    		 lastLabel.setVisible(false);
 	    		 
 	    		LMDate.setEditable(false);
 	    		noOfRooms.setValue("1");
 	       	noOfRooms.setItems(ApartroomList);
 	       noOfRooms.setValue("1");
 	    	 }
 	    	 else
 	    	 {
 	    		LMDate.setVisible(true);
	    		 lastLabel.setVisible(true);
	    		 noOfRooms.setItems(ApartroomList2);
	    		 noOfRooms.setValue("3");
	    	    	
 	    	 }
 	    	 
 	    	 
 	        
 	      }
 	    });
    }
    
    
    @FXML
    void ChooseImage(ActionEvent event) throws AlertException {

    	Stage stage = new Stage();
    	final FileChooser fileChooser = new FileChooser();
    	File file = fileChooser.showOpenDialog(stage);
    	System.out.println(file.getAbsolutePath());
    	Path src=file.getAbsoluteFile().toPath();
    	String[] arr=file.getName().split("/");
    	String image_loc=null;
    	for(int i=0;i<arr.length;i++) {
    		
    		image_loc=arr[i];
    	}
    	System.out.println(image_loc);
    	
    	Path dst=new File("Images/"+image_loc).getAbsoluteFile().toPath();
    	
    	
    	
    	
    	try {
			Files.copy(src,dst, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new AlertException(e.getLocalizedMessage(),new Stage());
		}
    	updateImage(image_loc);
    }
    private void updateImage(String Location) {
		
    	File file =new File("Images/"+Location);
    	DefaultImage.setImage(new Image(file.toURI().toString()));
    	imagechoosen=Location;
	}



   

    @FXML
    void goBack(ActionEvent event) throws AlertException {

    	try { 
    		new MainApp().start((Stage)St_No.getScene().getWindow()); } catch (Exception e) { throw new AlertException(e.getLocalizedMessage(),new Stage()); }
    	
    }

    @FXML
    void AddProperty(ActionEvent event) throws NumberFormatException, AlertException {
    	Random rand = new Random();
    	String id = String.format("%05d", rand.nextInt(100000));
    	
    	if(TypeChoice.getValue().equals("Apartment")) 		
    		new Apartment("A_"+id, St_No.getText(), St_name.getText(), suburb.getText(), Integer.parseInt(noOfRooms.getValue()), false, 0, imagechoosen);
    	
    	else
    	new PremiumSuite("S_"+id, St_No.getText(), St_name.getText(), suburb.getText(), Integer.parseInt(noOfRooms.getValue()), false, 0,LMDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), imagechoosen);
    	
    	Alert alert = new Alert(AlertType.INFORMATION, "Property Added", ButtonType.OK);
    	alert.showAndWait();

    	if (alert.getResult() == ButtonType.OK) {
    
			
    	}
		
    	System.out.println("Successfull");
    }

    @FXML
    void checkLastMaintainance(ActionEvent event) {

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
  			} catch (Exception e) {
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
    
}