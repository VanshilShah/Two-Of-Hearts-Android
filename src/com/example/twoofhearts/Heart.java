package com.example.twoofhearts;


import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("Heart")
public class Heart extends IBMDataObject {
    private final String  formAnswers = "FormAnswers", name = "Name", 
    		mateName = "MateName", location = "Location";
    
    public String getFormAnswers() {
        return (String) getObject(formAnswers);
    }
    public void setFormAnswers(String formAnswers) {
        setObject(this.formAnswers, (formAnswers != null) ? formAnswers : "");
    }
    
	public String getName() {
        return (String) getObject(name);
	}
	public void setName(String name) {
        setObject(this.name, (name != null) ? name : "");
	}
	
	public String getMateName() {
        return (String) getObject(mateName);
	}
	public void setMateName(String mateName) {
        setObject(this.mateName, (mateName != null) ? mateName : "");
	}
	
	public String getLocation() {
        return (String) getObject(location);
	}
	public void setLocation(String location) {
        setObject(this.location, (location != null) ? location : "");
	}
    
    
    
}