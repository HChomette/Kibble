package project;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CardLine {

	/*ATTRIBUTES*/

	@XmlAttribute
	private String fieldName;
	private String fieldValue;

	/*CONSTRUCTORS*/

	public CardLine(){ }
	
	public CardLine(String fieldName, String fieldValue){
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	/*GETTERS & SETTERS*/

	public String getFieldName() { return fieldName; }
	public void setFieldName(String fieldName) { this.fieldName = fieldName; }

	public String getFieldValue() { return fieldValue; }
	public void setFieldValue(String fieldValue) { this.fieldValue = fieldValue; }

}
