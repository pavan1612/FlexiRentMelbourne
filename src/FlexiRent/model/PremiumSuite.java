package FlexiRent.model;

import FlexiRent.Exception.AlertException;

public class PremiumSuite extends RentalProperty {
	/**

	* The class is the model for PremiumSuite 

	* @author Pavan Kumar Bore Gowda

	*/

	//constructor
	public PremiumSuite(String property_ID, String street_no, String street_name, String suburb,int nob,boolean type,int status,
			String lastMainatainace,String propimage) throws AlertException {
		super(property_ID, street_no, street_name, suburb, 3, true, 0, lastMainatainace, propimage);
		//LastMaintainance = lastMainatainace;

	}


}
