package projet;


import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Fiche {

	/*ATTRIBUTES*/

	@XmlAttribute
	private String nom;
	
	@XmlElementWrapper(name="Lignes")
    @XmlElement(name="Ligne")
	private ArrayList<LigneFiche> donnees;

	/*CONSTRUCTORS*/

	public Fiche(){
		donnees = new ArrayList<>();
	}
	
	public Fiche(String nom){
		this();
		this.nom = nom;
	}
	
	public Fiche(Fiche fiche){
		this.nom = fiche.getNom();
		this.donnees = (ArrayList<LigneFiche>) fiche.donnees.clone();
	}

	/*GETTERS & SETTERS*/

	public String getNom() { return nom; }
	public void setNom(String nom) { this.nom = nom; }

	public ArrayList<LigneFiche> getDonnees() { return donnees; }
	public void setDonnees(ArrayList<LigneFiche> donnees) { this.donnees = donnees; }

	/*METHODS*/

	public Fiche clone(){ return new Fiche(this); }
	
	/**
	 * Return a default character card
	 * TODO : Move default cards to a separate class
	 */
	public static Fiche getFichePersonnage(String nom){
		Fiche res = new Fiche(nom);
		
		res.donnees.add(new LigneFiche("Nom", ""));
		res.donnees.add(new LigneFiche("Prénom", ""));
		res.donnees.add(new LigneFiche("Surnom", ""));
		res.donnees.add(new LigneFiche("Sexe", ""));
		res.donnees.add(new LigneFiche("Âge/Année de naissance", ""));
		res.donnees.add(new LigneFiche("Importance dans le récit", ""));
		res.donnees.add(new LigneFiche("Situation initiale", ""));
		res.donnees.add(new LigneFiche("Evolution prévue", ""));
		res.donnees.add(new LigneFiche("Situation finale prévue", ""));
		res.donnees.add(new LigneFiche("Nationalité", ""));
		res.donnees.add(new LigneFiche("Lieu de vie", ""));
		res.donnees.add(new LigneFiche("Espèce/Race", ""));
		res.donnees.add(new LigneFiche("Rang Social", ""));
		res.donnees.add(new LigneFiche("Métier", ""));
		res.donnees.add(new LigneFiche("Conjoint", ""));
		res.donnees.add(new LigneFiche("Père", ""));
		res.donnees.add(new LigneFiche("Mère", ""));
		res.donnees.add(new LigneFiche("Frères/Soeurs", ""));
		res.donnees.add(new LigneFiche("Enfants", ""));
		res.donnees.add(new LigneFiche("Autres membres de famille", ""));
		res.donnees.add(new LigneFiche("Morphologie", ""));
		res.donnees.add(new LigneFiche("Peau", ""));
		res.donnees.add(new LigneFiche("Yeux", ""));
		res.donnees.add(new LigneFiche("Cheveux", ""));
		res.donnees.add(new LigneFiche("Elément visuel distinctif", ""));
		res.donnees.add(new LigneFiche("Personnalité", ""));
		res.donnees.add(new LigneFiche("Hobby", ""));
		res.donnees.add(new LigneFiche("Motivation", ""));
		res.donnees.add(new LigneFiche("Forces", ""));
		res.donnees.add(new LigneFiche("Faiblesses", ""));
		res.donnees.add(new LigneFiche("Philosophie", ""));
		res.donnees.add(new LigneFiche("Croyance", ""));
		res.donnees.add(new LigneFiche("Orientation politique", ""));
		res.donnees.add(new LigneFiche("Orientation sexuelle", ""));
		res.donnees.add(new LigneFiche("Style vestimentaire", ""));
		res.donnees.add(new LigneFiche("Objets particuliers", ""));
		
		return res;
	}

	/**
	 * Return a default place card
	 * @param nom card name
	 * @return
	 */
	public static Fiche getFicheLieu(String nom){
		Fiche res = new Fiche(nom);
		
		res.donnees.add(new LigneFiche("Nom", ""));
		res.donnees.add(new LigneFiche("Nature", ""));
		res.donnees.add(new LigneFiche("Âge/Date de Création", ""));
		res.donnees.add(new LigneFiche("Pays", ""));
		res.donnees.add(new LigneFiche("Région", ""));
		res.donnees.add(new LigneFiche("Ville", ""));
		res.donnees.add(new LigneFiche("Adresse", ""));
		res.donnees.add(new LigneFiche("Géographie du lieu", ""));
		res.donnees.add(new LigneFiche("Taille", ""));
		res.donnees.add(new LigneFiche("Architecture", ""));
		res.donnees.add(new LigneFiche("Habitants", ""));
		res.donnees.add(new LigneFiche("Contrôlé/Possédé par", ""));
		res.donnees.add(new LigneFiche("Importance dans l'histoire", ""));
		res.donnees.add(new LigneFiche("Situation initiale", ""));
		res.donnees.add(new LigneFiche("Evolution prévue", ""));
		res.donnees.add(new LigneFiche("Situation finale prévue", ""));
		
		return res;
	}

	/**
	 * Return a default event card
	 * @param nom
	 * @return
	 */
	public static Fiche getFicheEvnmt(String nom){
		Fiche res = new Fiche(nom);
		
		res.donnees.add(new LigneFiche("Nom", ""));
		res.donnees.add(new LigneFiche("Date", ""));
		res.donnees.add(new LigneFiche("Nature", ""));
		res.donnees.add(new LigneFiche("Acteurs", ""));
		res.donnees.add(new LigneFiche("Contexte général", ""));
		res.donnees.add(new LigneFiche("Cause", ""));
		res.donnees.add(new LigneFiche("Conséquences", ""));
		res.donnees.add(new LigneFiche("Caractéristiques notables", ""));
		
		return res;
	}

}
