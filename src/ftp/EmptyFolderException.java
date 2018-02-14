package ftp;

public class EmptyFolderException extends Exception {
	EmptyFolderException() {
		super("Il n'y a aucun fichier associé à votre compte sur le serveur.");
	}
}
