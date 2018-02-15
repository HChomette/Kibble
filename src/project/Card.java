package project;


import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Card {

	/*ATTRIBUTES*/

	@XmlAttribute
	private String name;
	
	@XmlElementWrapper(name="Lines")
    @XmlElement(name="Line")
	private ArrayList<CardLine> datas;

	/*CONSTRUCTORS*/

	public Card(){
		datas = new ArrayList<>();
	}
	
	public Card(String name){
		this();
		this.name = name;
	}
	
	public Card(Card card){
		this.name = card.getName();
		this.datas = (ArrayList<CardLine>) card.datas.clone();
	}

	/*METHODS*/

	public Card clone(){ return new Card(this); }
	
	/**
	 * Return a default character card
	 * TODO : Move default cards to a separate class
	 */
	public static Card getCharacterCard(String name){
		Card res = new Card(name);
		
		res.datas.add(new CardLine("Nom", ""));
		res.datas.add(new CardLine("Prénom", ""));
		res.datas.add(new CardLine("Surnom", ""));
		res.datas.add(new CardLine("Sexe", ""));
		res.datas.add(new CardLine("Âge/Année de naissance", ""));
		res.datas.add(new CardLine("Importance dans le récit", ""));
		res.datas.add(new CardLine("Situation initiale", ""));
		res.datas.add(new CardLine("Evolution prévue", ""));
		res.datas.add(new CardLine("Situation finale prévue", ""));
		res.datas.add(new CardLine("Nationalité", ""));
		res.datas.add(new CardLine("Lieu de vie", ""));
		res.datas.add(new CardLine("Espèce/Race", ""));
		res.datas.add(new CardLine("Rang Social", ""));
		res.datas.add(new CardLine("Métier", ""));
		res.datas.add(new CardLine("Conjoint", ""));
		res.datas.add(new CardLine("Père", ""));
		res.datas.add(new CardLine("Mère", ""));
		res.datas.add(new CardLine("Frères/Soeurs", ""));
		res.datas.add(new CardLine("Enfants", ""));
		res.datas.add(new CardLine("Autres membres de famille", ""));
		res.datas.add(new CardLine("Morphologie", ""));
		res.datas.add(new CardLine("Peau", ""));
		res.datas.add(new CardLine("Yeux", ""));
		res.datas.add(new CardLine("Cheveux", ""));
		res.datas.add(new CardLine("Elément visuel distinctif", ""));
		res.datas.add(new CardLine("Personnalité", ""));
		res.datas.add(new CardLine("Hobby", ""));
		res.datas.add(new CardLine("Motivation", ""));
		res.datas.add(new CardLine("Forces", ""));
		res.datas.add(new CardLine("Faiblesses", ""));
		res.datas.add(new CardLine("Philosophie", ""));
		res.datas.add(new CardLine("Croyance", ""));
		res.datas.add(new CardLine("Orientation politique", ""));
		res.datas.add(new CardLine("Orientation sexuelle", ""));
		res.datas.add(new CardLine("Style vestimentaire", ""));
		res.datas.add(new CardLine("Objets particuliers", ""));
		
		return res;
	}

	/**
	 * Return a default place card
	 * @param name card name
	 * @return
	 */
	public static Card getPlaceCard(String name){
		Card res = new Card(name);
		
		res.datas.add(new CardLine("Nom", ""));
		res.datas.add(new CardLine("Nature", ""));
		res.datas.add(new CardLine("Âge/Date de Création", ""));
		res.datas.add(new CardLine("Pays", ""));
		res.datas.add(new CardLine("Région", ""));
		res.datas.add(new CardLine("Ville", ""));
		res.datas.add(new CardLine("Adresse", ""));
		res.datas.add(new CardLine("Géographie du lieu", ""));
		res.datas.add(new CardLine("Taille", ""));
		res.datas.add(new CardLine("Architecture", ""));
		res.datas.add(new CardLine("Habitants", ""));
		res.datas.add(new CardLine("Contrôlé/Possédé par", ""));
		res.datas.add(new CardLine("Importance dans l'histoire", ""));
		res.datas.add(new CardLine("Situation initiale", ""));
		res.datas.add(new CardLine("Evolution prévue", ""));
		res.datas.add(new CardLine("Situation finale prévue", ""));
		
		return res;
	}

	/**
	 * Return a default event card
	 * @param name
	 * @return
	 */
	public static Card getEventCard(String name){
		Card res = new Card(name);
		
		res.datas.add(new CardLine("Nom", ""));
		res.datas.add(new CardLine("Date", ""));
		res.datas.add(new CardLine("Nature", ""));
		res.datas.add(new CardLine("Acteurs", ""));
		res.datas.add(new CardLine("Contexte général", ""));
		res.datas.add(new CardLine("Cause", ""));
		res.datas.add(new CardLine("Conséquences", ""));
		res.datas.add(new CardLine("Caractéristiques notables", ""));
		
		return res;
	}

	/*GETTERS & SETTERS*/

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public ArrayList<CardLine> getDatas() { return datas; }
	public void setDatas(ArrayList<CardLine> datas) { this.datas = datas; }
}
