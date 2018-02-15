package project;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {

	/*ATTRIBUTES*/

	@XmlAttribute
	private String name;

	private Note notes;
	
	@XmlElementWrapper(name="chapters")
    @XmlElement(name="chapter")
	private List<Chapter> chapters;

	@XmlElementWrapper(name="fiches")
    @XmlElement(name="fiche")
	private List<Card> fiches;

	/*CONSTRUCTORS*/
	public Project(){
		this(null);
	}
	
	public Project(String nom){
		chapters = new ArrayList<>();
		fiches = new ArrayList<>();
		notes = new Note();
		this.name = nom;
		
	}

	/*METHODS*/

	/**
	 * Return a new project
	 * @param name project name
	 */
	public static Project creer(String name){
		return new Project(name);
	}
	
	/**
	 * Rename the project
	 * @param name new name
	 */
	public void rename(String name){
		this.name = name;
	}

	/**
	 * Create a new chapter in this project
	 * @param name chapter's name
	 * @param number chapter's number
	 */
	public void createChapter(String name, int number){
		Chapter c = new Chapter();
		chapters.add(number, c);
	}
	
	/**
	 * Change chapters numbers according to project list
	 */
	public void numberChapters(){
		for(int i = 0; i< chapters.size(); i++)
			chapters.get(i).setNumber(i+1);
	}
	
	/**
	 * Change chapter place
	 * @param originalNumber chapter current number
	 * @param targetNumber chapter new place
	 */
	public void moveChapter(int originalNumber, int targetNumber){
		Chapter c = chapters.get(originalNumber);
		chapters.remove(originalNumber);
		chapters.add(targetNumber, c);
		for(int i = 0; i< chapters.size(); i++)
			chapters.get(i).setNumber(i+1);
	}

	/**
	 * Delete a chapter
	 * @param selectedIndex chapter index
	 */
	public void deleteChapter(int selectedIndex) {
		chapters.remove(selectedIndex);
		numberChapters();
	}

	/**
	 * Save each chapter text in its backup field
	 * {@link Chapter#backupText() backupText}
	 */
	public void backupChapters() {
		for(Chapter c : chapters)
			c.backupText();
	}
	
	/**
	 * Register each chapter text in its previous field
	 * {@link Chapter#savePrevious()}
	 */
	public void enregistrerAnterieurs(){
		for(Chapter c : chapters)
			c.savePrevious();
	}

	/* GETTERS & SETTERS */

	public String getName() { return name; }
	public void setName(String name) { this.name = name;
	}
	public Note getNotes() { return notes; }
	public void setNotes(Note notes) { this.notes = notes; }

	public List<Chapter> getChapters() { return chapters; }
	public void setChapters(List<Chapter> chapters) { this.chapters = chapters; }
	public Chapter getChapter(int index) { return chapters.get(index); }

	public List<Card> getFiches() { return fiches; }
	public void setFiches(List<Card> fiches) { this.fiches = fiches; }

}
