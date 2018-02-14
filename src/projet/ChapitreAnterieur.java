package projet;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChapitreAnterieur {
	
	private String texte;
	private Date date;

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ChapitreAnterieur(String texte, Date date) {
		this.texte = texte;
		this.date = date;
	}
	
	public ChapitreAnterieur(){
		
	}
	
	public String toString(){
		return (new SimpleDateFormat("dd/MM/yyyy, H:m").format(date));
	}

}
