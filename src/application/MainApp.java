package application;

import java.io.File;
import java.io.IOException;

import controller.EcritureController;
import controller.FicheController;
import controller.LayoutController;
import controller.TabMatiereController;
import ftp.FtpClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.Window;
import outils.SauvegardeXML;
import projet.Projet;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainApp extends Application {
	//**********ATTRIBUTS
	private static Projet projet;
	private FicheController ficheController;
	private TabMatiereController tabMatiereController;
	private EcritureController ecritureController;
	
	//**********FENETRE FXML LAYOUT
	private static Stage stage;
	private BorderPane layout;


	@Override
	public void start(Stage s) {
		stage = s;
		stage.setTitle("KIBBLE");
		LayoutController controller = LayoutController.get();
		controller = initRootLayout();
		tabMatiereController = new TabMatiereController();
		ecritureController = new EcritureController();
		ficheController = new FicheController();
		
		this.layout.setLeft(this.getWindow(tabMatiereController, "TabMatiereView"));
		this.layout.setCenter(this.getWindow(ecritureController, "EcritureView"));
		this.layout.setRight(this.getWindow(ficheController, "FicheView"));
		
		controller.setTabMatiereController(tabMatiereController);
		controller.setEcritureController(ecritureController);
		controller.setFicheController(ficheController);
		
		LayoutController.setLayoutController(controller);
		style();
		if(projet == null){
			Alert alert = new Alert(AlertType.INFORMATION);
			Stage stageA = (Stage) alert.getDialogPane().getScene().getWindow();
			try {
				stageA.getIcons().add(new Image("ressources/images/Logo.png"));
			} catch (Exception e) {
				System.out.println("Logo not found!");
			}
			DialogPane dialogPane = alert.getDialogPane();
			String style = MainApp.getStyle();
			if(style != null) dialogPane.getStylesheets().add(style);
			alert.setTitle("Kibble");
			alert.setHeaderText("Bienvenue sur Kibble");
			alert.setContentText("Pour commencer, créez un nouveau projet ou ouvrez un projet existant dans le menu \"Projet\".");

			alert.showAndWait();
		}
		
		try {
			FtpClient.load();
		} catch (Exception e) {
			showFtpWarning(e);
		}
	}

	//**********CHARGEMENT DES FENETRES
	private LayoutController initRootLayout() {
		LayoutController controller = null;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/LayoutView.fxml"));
			this.layout = loader.load();

			Scene scene = new Scene(this.layout);
			stage.setScene(scene);

			controller = loader.getController();
			
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		//Chargement du PATH
		File file = SauvegardeXML.getFilePath();
	    if (file != null)
			try {
				projet = SauvegardeXML.loadFromXML(file);
				projet.backupChapitres();
				MainApp.setStageTitreProjet(projet.getNomProjet());
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Erreur");
		        alert.setHeaderText("Impossible de charger l'ancien fichier");
		        alert.setContentText("Nous n'avons pas pu charger le fichier\n" + file.getPath());

		        alert.showAndWait();
		        e.printStackTrace();
			}
	    
	    return controller;

	}
	
	//Methode qui retourne une fenetre chargée à partir d'un XML
	private AnchorPane getWindow(Controller controller, String view){
		try {
			//Charger la page 
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/"+ view +".fxml"));
			loader.setController(controller);
			return loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void creationProjet(String nomProjet){
		projet = new Projet(nomProjet);
		SauvegardeXML.setFilePath(null);
		MainApp.setStageTitreProjet(nomProjet);
	}
	
	public static void setStageTitreProjet(String titreProjet){
		stage.setTitle("KIBBLE - " +titreProjet);
	}
	
	/**
	 * Applique le CSS à la fenêtre
	 */
	private void style(){
		try {
			stage.getIcons().add(new Image("ressources/images/Logo.png"));
			layout.getStylesheets().add(getStyle());
		} catch (Exception e) {
			System.out.println("Logo not found!");
		}
	}
	
	/**
	 * Retourne le fichier CSS de l'application
	 */
	public static String getStyle(){
		String style;
		try {
			style = MainApp.class.getResource("/ressources/application.css").toExternalForm();
		} catch (Exception e) {
			System.out.println("CSS not found!");
			return null;
		}
		return style;
	}
	
	/**
	 * Erreur en cas de problème de connexion au ftp
	 */
	private void showFtpWarning(Exception e){	
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Information");
		alert.setHeaderText("Attention : stockage en ligne non disponible");
		alert.setContentText("Message d'erreur :\n" + e.getClass().getSimpleName() + " : " + e.getMessage());
		alert.show();
		FtpClient.disable();
	}
	
	//**********GETTERs & SETTERs
	public static Window getStage() {
		return stage;
	}
	
	public static Projet getProjet(){
		return projet;
	}
	
	public static void setProjet(Projet p){
		projet = p;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
