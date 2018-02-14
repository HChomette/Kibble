package ftp;

@SuppressWarnings("serial")
public class FTPConnectException extends Exception {
	FTPConnectException(){
		super("Erreur lors de la connexion au serveur");
	}
}
