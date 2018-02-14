package ftp;

import application.MainApp;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class FtpClient {
	//TODO Récuperer ça dans un fichier de config ??
	private static final String USER_FTP = "u88728077";
	private static final String PASSWORD_FTP = "kibblepjs4";
	private static final String IP_FTP = "home674713130.1and1-data.host";
	private static final int PORT_FTP = 21;
	private static final FTPClient ftp = new FTPClient();
	private static final int MAX_FILE_SIZE = 30000000;
	private static Connection connexion;
	private static final String IP_BD = "jdbc:mysql://162.253.126.108:3306/kibblefr_kibble";
	private static final String USER_BD = "kibblefr_user";
	private static final String PASSWORD_BD = "azerty123456789";
	private static boolean isConnected;
	private static boolean isDisabled;

	/**
	 * Etablit ou réinitialise la connexion avec le Ftp (un seul appel est nécessaire).<br/>
	 * Elle est appelée dès le chargement du programme.<br/>
	 * A ne pas confondre avec la méthode connexionFtp qui est appelée à la demande
	 * de l'utilisateur et se charge de l'authentifier.
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void load() throws SQLException, IOException, ClassNotFoundException{
		isConnected = false;
		isDisabled = false;
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out))); //Affiche les logs
		ftp.connect(IP_FTP, PORT_FTP);
		if (!FTPReply.isPositiveCompletion(ftp.getReplyCode()))
			ftp.disconnect();
		ftp.enterLocalPassiveMode();
		ftp.login(USER_FTP, PASSWORD_FTP);
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
		Class.forName("com.mysql.cj.jdbc.Driver");
		connexion = DriverManager.getConnection(IP_BD, USER_BD, PASSWORD_BD);
}

	/**
	 * Authentifie l'utilisateur auprès de Kibble, et modifie le répertoire courant du Ftp si il a pu se connecter.
	 * Cette méthode est est appelée automatiquement lorsque l'utilisateur
	 * tente d'effectuer une action qui requiert sa connexion.
	 * @throws KibbleConnectException
	 * @throws FTPConnectException
	 */
	public static void connexionFtp() throws KibbleConnectException, FTPConnectException {
		if(isDisabled)
			throw new FTPConnectException();
		Optional<Pair<String, String>> res = showDialog();
		if(res.isPresent()) {
			Pair<String, String> result = res.get();
			try {
				authKibble(result);
				String userDirectory = "/" + result.getKey();
				ftp.changeWorkingDirectory(userDirectory); //On se place dans son répertoire de travail
				if (ftp.getReplyCode() == 550) {
					ftp.makeDirectory(userDirectory); //Si il n'existe pas, on le crée
					ftp.changeWorkingDirectory(userDirectory);
				}
				isConnected = true;
			} catch (SQLException e) {
				throw new KibbleConnectException();
			} catch (IOException e) {
				throw new FTPConnectException();
			}
		}
		else throw new KibbleConnectException();

	}

	/**
	 * Affiche une boite de dialogue demandant le login et mdp de l'utilisateur, attend sa réponse puis la renvoie.
	 * @return - le login et mdp de l'utilisateur si il a cliqué sur login<br/>
	 * 		   - null si il a cliqué sur annuler
	 */
	private static Optional<Pair<String, String>> showDialog(){
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Connexion");
		dialog.setHeaderText("Connectez vous à votre compte");

		ButtonType loginButtonType = new ButtonType("Connexion", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		username.textProperty().addListener((observable, oldValue, newValue) ->
				loginButton.setDisable(newValue.trim().isEmpty()));

		dialog.getDialogPane().setContent(grid);
		Platform.runLater(username::requestFocus);

		dialog.setResultConverter(dialogButton -> (dialogButton == loginButtonType) ?
				new Pair<>(username.getText(), password.getText()) : null);
		return dialog.showAndWait();
	}

	/**
	 * Méthode privée permettant l'authentification auprès de Kibble.<br/>
	 * Elle demande l'authentification en boucle jusqu'à ce que le login et mdp
	 * soient bons ou que l'utilisateur clique sur annuler.<br/>
	 * Un message d'erreur s'affiche si le login ou mdp est incorrect.
	 * @param result les login et mdp de l'utilisateur
	 * @throws SQLException
	 * @throws FTPConnectException
	 * @throws KibbleConnectException
	 */
	private static void authKibble(Pair<String, String> result)
			throws SQLException, FTPConnectException, KibbleConnectException {
		Statement st = connexion.createStatement();
		ResultSet rs = st.executeQuery("select * from AUTEURS where Mdp = '" + result.getValue() +
				"' and Id_A = '" + result.getKey() + "'");
		if (!rs.next()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Erreur de connexionFtp");
			alert.setHeaderText("Erreur de connexionFtp");
			alert.setContentText("Login ou mot de passe invalide");
			alert.showAndWait();
			connexionFtp();
		}
	}

	/**
	 * Ouvre une fenêtre de choix du fichier à uploader, puis envoie ce dernier au Ftp.<br/>
	 * Authentifie l'utilisateur si ce n'est pas déjà fait.
	 * @throws FileTooLargeException
	 * @throws FTPConnectException
	 * @throws KibbleConnectException
	 */
	public static void uploadDialog()
			throws FileTooLargeException, FTPConnectException, KibbleConnectException {
		if(!isConnected || isDisabled)
			connexionFtp();
		FileChooser fc = new FileChooser();
		File file = fc.showOpenDialog(MainApp.getStage());
		if(file != null)
			upload(file);
	}

	/**
	 * Upload un fichier sur le Ftp.<br/>
	 * Authentifie l'utilisateur si ce n'est pas déjà fait
	 * @param file le fichier à transmettre, attention sa taille est limitée.
	 * @throws FileTooLargeException
	 * @throws FTPConnectException
	 * @throws KibbleConnectException
	 */
	public static void upload(File file)
			throws FileTooLargeException, FTPConnectException, KibbleConnectException {
		if(!isConnected || isDisabled)
			connexionFtp();
		if (file.length() > MAX_FILE_SIZE)
			throw new FileTooLargeException();
		try {
			InputStream inputStream = new FileInputStream(file);
			ftp.storeFile(file.getName(), inputStream);
			inputStream.close();
		} catch (IOException e){
			throw new FTPConnectException();
		}
	}

	/**
	 * Ouvre une fenêtre de choix de l'emplacement du fichier à télécharger, et l'enregistre.
	 * @param source l'emplacement du fichier sur le serveur
	 * @param ext l'extension du fichier
	 * @return true si un fichier a été téléchargé, false sinon
	 * @throws FTPConnectException
	 * @throws KibbleConnectException
	 */
	public static boolean downloadDialog(String source, String ext)
			throws FTPConnectException, KibbleConnectException {
		if (!isConnected || isDisabled)
			connexionFtp();
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(ext, ext));
		File file = fc.showSaveDialog(MainApp.getStage());
		return file != null && download(source, file.getPath());
	}

	/**
	 * Télécharge le fichier à l'emplacement donné.
	 * @param source la source du fichier sur le serveur
	 * @param destLocal L'emplacement de destination
	 * @return true si un fichier a été téléchargé
	 * @throws FTPConnectException
	 * @throws KibbleConnectException
	 */
	public static boolean download(String source, String destLocal)
			throws FTPConnectException, KibbleConnectException {
		if(!isConnected || isDisabled)
			connexionFtp();
		try {
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destLocal));
			ftp.retrieveFile(source, outputStream);
			outputStream.close();
			return true;
		} catch (IOException e){
			throw new FTPConnectException();
		}
	}
/*
	public void delete(String fileName){
		if(isConnected)
			ftp.deleteFile(fileName);
		else {
			if(connexionFtp())
				delete(fileName);
		}
	}
*/

	/**
	 * Recherche la liste des fichiers présents dans le répertoire courant du serveur
	 * @param extension le type de fichier à rechercher
	 * @return la liste des fichiers qui ont l'extension indiquée
	 * @throws FTPConnectException
	 * @throws KibbleConnectException
	 * @throws EmptyFolderException
	 */
	public static ArrayList<String> listFiles(String extension) throws FTPConnectException, KibbleConnectException, EmptyFolderException {
		if(!isConnected || isDisabled)
			connexionFtp();
		try {
			FTPFile[] files = ftp.listFiles();
			if(files.length == 2)
				throw new EmptyFolderException();
			ArrayList<String> noms = new ArrayList<>();
			for (FTPFile file : files) {
				String nom = file.getName();
				if (nom.endsWith(extension))
					noms.add(nom.substring(0, nom.length() - extension.length()));
			}
			return noms;
		} catch (IOException e){
			throw new FTPConnectException();
		}
	}

	/**
	 * Désactive le Ftp dans le cas où la connexion est impossible,
	 * lors d'une erreur d'accès aux serveurs 1&1 par exemple.
	 */
	public static void disable(){
		isDisabled = true;
	}

	public static void disconnect() throws IOException {
		isConnected = false;
		if (ftp.isConnected()) {
			ftp.logout();
			ftp.disconnect();
		}
	}

}