package FlexiRent.model;

import FlexiRent.Exception.AlertException;

public class Apartment extends RentalProperty {
	/**

	* The class is the model for Apartments 

	* @author Pavan Kumar Bore Gowda

	*/
	

	//constructor
	public Apartment(String id, String sno, String sname, String sub, int nob,boolean type,int status,String image) throws AlertException {
		super(id, sno, sname, sub, nob, type, status,image);

	}



}
