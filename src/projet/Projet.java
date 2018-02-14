package projet;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Projet {
	
	private Note notes;
	
	@XmlAttribute
	private String nomProjet;
	
	@XmlElementWrapper(name="chapitres")
    @XmlElement(name="chapitre")
	private List<Chapitre> chapitres; 
	
	public Note getNotes() {
		return notes;
	}

	public void setNotes(Note notes) {
		this.notes = notes;
	}

	@XmlElementWrapper(name="fiches")
    @XmlElement(name="fiche")
	private List<Fiche> fiches;
	
	public List<Chapitre> getChapitres() {
		return chapitres;
	}
	
	public void setChapitres(List<Chapitre> chapitres) {
		this.chapitres = chapitres;
	}
	public List<Fiche> getFiches() {
		return fiches;
	}
	public void setFiches(List<Fiche> fiches) {
		this.fiches = fiches;
	}
	public String getNomProjet() {
		return nomProjet;
	}
	public void setNomProjet(String nomProjet) {
		this.nomProjet = nomProjet;
	}
	
	public Projet(){
		this(null);
	}
	
	public Projet(String nom){
		chapitres = new ArrayList<>();
		fiches = new ArrayList<>();
		notes = new Note();
		this.nomProjet = nom;
		
	}
	
	/**
	 * Cree un nouveau projet et le retourne
	 * @param emplacement
	 */
	public static Projet creer(String nom){
		return new Projet(nom);
	}
	
	/**
	 * Renomme le projet dans l'appli java
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
	
	public Chapitre getChapitre(int numeroSelection) {
		return chapitres.get(numeroSelection);
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
	 * {@link #enregistrerAnterieur() enregistrerAnterieur}
	 */
	public void enregistrerAnterieurs(){
		for(Chapitre c : chapitres)
			c.enregistrerAnterieur();
	}
	
	

}
