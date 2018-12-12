package FlexiRent.model;

public class PropertyBean {
	/**

	* The class is used as a bean class while doing Database operations 

	* @author Pavan Kumar Bore Gowda

	*/
	private String Property_ID, Address,PropImage;

	private int NOB;
	private int Type;
	private int Status;
	private String LastMaintainance;
	
	
	public PropertyBean(String property_ID, String address, int nOB, int type,
			int status, String lastMaintainance,String propImage) {
		super();
		Property_ID = property_ID;
		Address=address;
		NOB = nOB;
		Type = type;
		Status = status;
		LastMaintainance = lastMaintainance;
		PropImage=propImage;
	}
	
	
	public PropertyBean(String property_ID, String address, int nOB, int type,
			int status,String propImage) {
		super();
		Property_ID = property_ID;
		Address=address;
		NOB = nOB;
		Type = type;
		Status = status;
		PropImage=propImage;
	}


	public String getPropImage() {
		return PropImage;
	}


	public void setPropImage(String propImage) {
		PropImage = propImage;
	}


	public String getProperty_ID() {
		return Property_ID;
	}
	public void setProperty_ID(String property_ID) {
		Property_ID = property_ID;
	}

	public int getNOB() {
		return NOB;
	}
	public void setNOB(int nOB) {
		NOB = nOB;
	}

	public String getAddress() {
		return Address;
	}


	public void setAddress(String address) {
		Address = address;
	}


	public int getType() {
		return Type;
	}


	public void setType(int type) {
		Type = type;
	}


	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public String getLastMaintainance() {
		return LastMaintainance;
	}
	public void setLastMaintainance(String lastMaintainance) {
		LastMaintainance = lastMaintainance;
	}
	
	
}
