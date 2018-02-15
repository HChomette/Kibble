package projet;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.*;

/**
 * Classe gérant le
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Projet {

	/*ATTRIBUTES*/

	@XmlAttribute
	private String nomProjet;

	private Note notes;
	
	@XmlElementWrapper(name="chapitres")
    @XmlElement(name="chapitre")
	private List<Chapitre> chapitres;

	@XmlElementWrapper(name="fiches")
    @XmlElement(name="fiche")
	private List<Fiche> fiches;

	/*CONSTRUCTORS*/
	public Projet(){
		this(null);
	}
	
	public Projet(String nom){
		chapitres = new ArrayList<>();
		fiches = new ArrayList<>();
		notes = new Note();
		this.nomProjet = nom;
		
	}

	/*METHODS*/

	/**
	 * Cree un nouveau projet et le retourne
	 * @param nom
	 */
	public static Projet creer(String nom){
		return new Projet(nom);
	}
	
	/**
	 * Renomme le projet dans l'appli
	 * @param nom le nouveau nom
	 */
	public void renommer(String nom){
		this.nomProjet = nom;
	}

	/**
	 * Cree un nouveau chapitre
	 * @param nom le nom du chapitre
	 * @param numero le numéro du chapitre
	 */
	public void creerChapitre(String nom, int numero){
		//Initialise un chapitre
		Chapitre c = new Chapitre();
		//Inserer c dans ArrayList au bon emplacement
		chapitres.add(numero, c);
	}
	
	/**
	 * Refait la numerotation des chapitres par ordre de la liste
	 */
	public void reordonnerChapitres(){
		for(int i = 0; i<chapitres.size(); i++)
			chapitres.get(i).setNumero(i+1);
	}
	
	/**
	 * Change l'emplacement d'un chapitre
	 * @param numeroOrigine le chapitre designe
	 * @param numeroArrivee l'endroit ou il arrive
	 */
	public void bougerChapitre(int numeroOrigine,int numeroArrivee){
		Chapitre c = chapitres.get(numeroOrigine);
		chapitres.remove(numeroOrigine);
		chapitres.add(numeroArrivee, c);
		for(int i = 0; i<chapitres.size(); i++)
			chapitres.get(i).setNumero(i+1);
	}

	/**
	 * Supprime le chapitre à l'index donne, et reordonne le reste en consequence
	 */
	public void deleteChapitre(int selectedIndex) {
		chapitres.remove(selectedIndex);
		reordonnerChapitres();
	}

	/**
	 * Enregistre le texte de chaque chapitre dans sa version de backup
	 * {@link Chapitre#backupTexte() backupTexte}
	 */
	public void backupChapitres() {
		for(Chapitre c : chapitres)
			c.backupTexte();
	}
	
	/**
	 * Enregistre la version précédente de chaque chapitre dans ses chapitres antérieurs
	 * {@link Chapitre#enregistrerAnterieur()}
	 */
	public void enregistrerAnterieurs(){
		for(Chapitre c : chapitres)
			c.enregistrerAnterieur();
	}

	/* GETTERS & SETTERS */

	public String getNomProjet() { return nomProjet; }
	public void setNomProjet(String nomProjet) { this.nomProjet = nomProjet;
	}
	public Note getNotes() { return notes; }
	public void setNotes(Note notes) { this.notes = notes; }

	public List<Chapitre> getChapitres() { return chapitres; }
	public void setChapitres(List<Chapitre> chapitres) { this.chapitres = chapitres; }
	public Chapitre getChapitre(int numeroSelection) { return chapitres.get(numeroSelection); }

	public List<Fiche> getFiches() { return fiches; }
	public void setFiches(List<Fiche> fiches) { this.fiches = fiches; }

}
