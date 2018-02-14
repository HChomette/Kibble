package projet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LigneFiche {
	@XmlAttribute
	private String nomChamp;
	private String valeurChamp;
	
	public LigneFiche(){
		
	}
	
	public LigneFiche(String nomChamp, String valeurChamp){
		this.nomChamp = nomChamp;
		this.valeurChamp = valeurChamp;
	}
	
	public String getNomChamp() {
		return nomChamp;
	}
	public void setNomChamp(String nomChamp) {
		this.nomChamp = nomChamp;
	}
	public String getValeurChamp() {
		return valeurChamp;
	}
	public void setValeurChamp(String valeurChamp) {
		this.valeurChamp = valeurChamp;
	}

}
