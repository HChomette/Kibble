package project;

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
public class Chapter {

	/*ATTRIBUTES*/

	private static final int MAX_VERSIONS = 5;
	@XmlAttribute
	private int number;
	@XmlAttribute
	private String title;

	private String text;

	@XmlTransient
	private String backupText;// Is used to save in previous chapters
	
	private Note notes;


	@XmlElementWrapper(name = "Previous_Chapters")
	@XmlElement(name = "Previous_Chapter")
	private List<PreviousChapter> previousChapters;

	/*CONSTRUCTORS*/
	
	public Chapter() {
		this.text ="";
		this.previousChapters = new ArrayList<>();
	}

	public Chapter(String title, int num) {
		this.title = title;
		this.number = num;
		this.text ="";
		this.notes = new Note(this.title, "");
		this.previousChapters = new ArrayList<>();
	}

	public Chapter(int num) {
		this.number = num;
		this.previousChapters = new ArrayList<>();
	}

	/*METHODS*/

	/**
	 * Save the previous version in previousChapters
	 * Default version limit set to 5
	 * @see Chapter#MAX_VERSIONS
	 */
	public void savePrevious() {
		if (backupText != null && !backupText.equals("")&& !backupText.equals(text)) {
			//Only modified and non empty texts are saved
			
				previousChapters.add(new PreviousChapter(backupText, new Date()));
				if (previousChapters.size() > MAX_VERSIONS)
					previousChapters.remove(0);
			
		}
	}

	/**
	 * Save text in backupText
	 * {@link #savePrevious() savePrevious}
	 */
	public void backupText() {
		this.backupText = text;
	}

	/*GETTERS & SETTERS*/

	public int getNumber() { return number; }
	public void setNumber(int number) { this.number = number; }

	public String getTitle() { return title; }
	public void setTitle(String title) {
		this.title = title;
		this.notes.setName(title);
	}

	public String getText() { return text; }
	public void setText(String text) { this.text = text; }

	public String getBackupText() { return backupText; }
	
	public void saveNote(String note){ this.notes.setText(note); }

	public Note getNotes() { return notes; }
	public void setNotes(Note notes) { this.notes = notes; }

	public List<PreviousChapter> getPreviousChapters() { return previousChapters; }
	public void setPreviousChapters(List<PreviousChapter> previousChapters) { this.previousChapters = previousChapters; }


	public String toString() { return title; }
}
