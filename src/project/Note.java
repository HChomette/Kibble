package project;

public class Note {

	/*ATTRIBUTES*/

	private String name;
	private String text;

	/*CONSTRUCTORS*/

	public Note() {
		this("Project", "");
	}
	
	public Note(String name, String text) {
		this.name = name;
		this.text = text;
	}

	/*GETTERS & SETTERS*/

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	public String toString(){ return name; }

	

}
