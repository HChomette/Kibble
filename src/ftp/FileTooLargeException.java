package ftp;

@SuppressWarnings("serial")
public class FileTooLargeException extends Exception {
	FileTooLargeException(){
		super("Le fichier est trop gros");
	}
}
