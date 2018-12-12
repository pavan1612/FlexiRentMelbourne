package FlexiRent.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import FlexiRent.Exception.AlertException;
import FlexiRent.model.PropertyBean;

public class Data
/**

* The class helps to generate data for GridView

* @author Pavan Kumar Bore Gowda

*/
{
    @FXML
    private HBox hBox;
    @FXML
    private Label label1;

    @FXML
    private ImageView PropertyImage;

   PropertyBean PB;
    public Data() throws AlertException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/listCellItems.fxml"));
        
        fxmlLoader.setController(this);
        try
        {
        	
            fxmlLoader.load();
        }
        catch (IOException e)
        {
        	throw new AlertException(e.getLocalizedMessage(),new Stage());
        }
    }
    
    @FXML
    void moreDetails(ActionEvent event) throws AlertException {
    	
    	Stage stage = (Stage) label1.getScene().getWindow();
    	new ListOptionsController(PB,stage);
    	 
         System.out.println("Working");
    }

    public void setInfo(PropertyBean pb)
    {
    	
    	File file =new File("Images/"+pb.getPropImage());
    	label1.setText(pb.getAddress());
        //label2.setText(string);
       PropertyImage.setImage(new Image(file.toURI().toString()));
    }
    public void setPropertyBean(PropertyBean pb) {
    	
    	PB=pb;
    }

    public HBox getBox()
    {
        return hBox;
    }
}