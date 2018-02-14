package ftp;

@SuppressWarnings("serial")
public class KibbleConnectException extends Exception {
	KibbleConnectException(){
		super("Vous n'avez pas été authentifié");
	}
}
