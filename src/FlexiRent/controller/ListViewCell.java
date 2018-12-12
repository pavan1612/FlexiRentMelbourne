package FlexiRent.controller;

import org.controlsfx.control.GridCell;

import FlexiRent.Exception.AlertException;
import FlexiRent.model.PropertyBean;
import javafx.scene.control.ListCell;

public class ListViewCell extends GridCell<PropertyBean>{
/**

* The class helps update each Grid with their respective details in main display 

* @author Pavan Kumar Bore Gowda

*/

    @Override
    public void updateItem(PropertyBean pb, boolean empty)
    {
        super.updateItem(pb,empty);
        if(pb != null)
        {
            Data data = null;
			try {
				data = new Data();
			} catch (AlertException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            data.setInfo(pb);
            data.setPropertyBean(pb);
            setGraphic(data.getBox());
        }
    }
}
