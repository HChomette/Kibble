package projet;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Chapitre {

	/*ATTRIBUTES*/

	private static final int MAX_VERSIONS = 5;
	@XmlAttribute
	private int numero;
	@XmlAttribute
	private String titre;

	private String texte;

	@XmlTransient
	private String backupTexte;// Sert pour enregistrer la version dans les
								// chapitres antérieurs
	
	private Note notes;


	@XmlElementWrapper(name = "Chapitres_Anterieurs")
	@XmlElement(name = "Chapitre_Anterieur")
	private List<ChapitreAnterieur> chapitresAnterieurs;

	/*CONSTRUCTORS*/
	
	public Chapitre() {
		this.texte="";
		this.chapitresAnterieurs = new ArrayList<>();
	}

	public Chapitre(String titre, int num) {
		this.titre = titre;
		this.numero = num;
		this.texte="";
		this.notes = new Note(this.titre, "");
		this.chapitresAnterieurs = new ArrayList<>();
	}

	public Chapitre(int num) {
		this.numero = num;
		this.chapitresAnterieurs = new ArrayList<>();
	}

	/*METHODS*/

	/**
	 * Enregistre la version précédente dans les chapitres antérieurs 
	 * Limite de version réglée sur 5
	 */
	public void enregistrerAnterieur() {
		if (backupTexte != null && !backupTexte.equals("")&& !backupTexte.equals(texte)) {
			//On enregistre uniquement un texte non vide qui a été modifié
			
				chapitresAnterieurs.add(new ChapitreAnterieur(backupTexte, new Date()));
				if (chapitresAnterieurs.size() > MAX_VERSIONS)
					chapitresAnterieurs.remove(0);
			
		}
	}

	/**
	 * Enregistre le texte dans le String backup 
	 * Est utilisé pour enregistrer les versione anterieures 
	 * {@link #enregistrerAnterieur() enregistrerAnterieur}
	 */
	public void backupTexte() {
		this.backupTexte = texte;
	}

	/*GETTERS & SETTERS*/

	public int getNumero() { return numero; }
	public void setNumero(int numero) { this.numero = numero; }

	public String getTitre() { return titre; }
	public void setTitre(String titre) {
		this.titre = titre;
		this.notes.setNom(titre);
	}

	public String getTexte() { return texte; }
	public void setTexte(String texte) { this.texte = texte; }

	public String getBackupTexte() { return backupTexte; }
	
	public void saveNote(String note){ this.notes.setTexte(note); }

	public Note getNotes() { return notes; }
	public void setNotes(Note notes) { this.notes = notes; }

	public List<ChapitreAnterieur> getChapitresAnterieurs() { return chapitresAnterieurs; }
	public void setChapitresAnterieurs(List<ChapitreAnterieur> chapitresAnterieurs) { this.chapitresAnterieurs = chapitresAnterieurs; }


	public String toString() { return titre; }
}
