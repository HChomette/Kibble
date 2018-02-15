package project;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PreviousChapter {

	/*ATTRIBUTES*/

	private String text;
	private Date date;

	/*CONSTRUCTORS*/

	public PreviousChapter(String text, Date date) {
		this.text = text;
		this.date = date;
	}

	public PreviousChapter(){ }

	/*GETTERS & SETTERS*/
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }

	public Date getDate() { return date; }
	public void setDate(Date date) { this.date = date; }
	
	public String toString(){ return (new SimpleDateFormat("dd/MM/yyyy, H:m").format(date)); }

}
