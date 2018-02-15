package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

import com.itextpdf.text.DocumentException;

import application.Controller;
import application.MainApp;
import ftp.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import outils.ExportToPDF;
import outils.SauvegardeXML;
import project.Chapter;

public class LayoutController implements Controller {

	private static LayoutController lc = null;

	private FicheController ficheController;
	private TabMatiereController tabMatiereController;
	private EcritureController ecritureController;

	public static Font fontMS;
	public static Font fontNBRegular;
	public static Font fontNBRegularSmall;
	public static Font fontNBBold;
	public static Font fontNBBoldSmall;
	public static Font fontNBBoldItalic;

	@FXML
	public void initialize() {
		fontMS = getFont("/ressources/fonts/MarckScript.ttf", 26);
		fontNBRegular = getFont("/ressources/fonts/NewBaskerville.otf", 17);
		fontNBRegularSmall = getFont("/ressources/fonts/NewBaskerville.otf", 12);
		fontNBBold = getFont("/ressources/fonts/NewBaskerville_Bold.otf", 22);
		fontNBBoldSmall = getFont("/ressources/fonts/NewBaskerville_Bold.otf", 14);
		fontNBBoldItalic = getFont("/ressources/fonts/NewBaskerville_BoldItalic.otf", 24);
	}

	/**
	 * Get a font from its path
	 * @param path
	 * @param size
	 * @return
	 */
	private Font getFont(String path, double size){
		URL resource = MainApp.class.getResource(path);
		if(resource != null) return Font.loadFont(resource.toExternalForm(), size);
		else return null;
	}

	// **********EVENEMENTS
	@FXML
	private void handleOuvrirProjet() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(MainApp.getStage());
		if (file != null)
			try {
				// nettoyer();
				MainApp.setProject(SauvegardeXML.loadFromXML(file));
				tabMatiereController.initialize();
				ecritureController.initialize();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@FXML
	private void handleRenommerProjet() {
		if (MainApp.getProject() == null)
			return;
		TextInputDialog dialog = new TextInputDialog("Nom");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(MainApp.getStyle());
		dialog.setTitle("Renommer le project");
		dialog.setHeaderText("Nouveau nom du project");
		dialog.setContentText("Entrez le nouveau nom de votre project : ");
		Optional<String> nomProjet = dialog.showAndWait();
		if (nomProjet.isPresent()) {
			MainApp.getProject().setName(nomProjet.get());
			MainApp.setStageTitreProjet(nomProjet.get());
		}

	}

	private void enregistrer(File file) {
		// On enregistre le contenu du chapitre en cours d'édition dans son
		// objet
		Chapter c = tabMatiereController.getChapitreActuel();
		if (c != null) {
			c.setText(ecritureController.getTextChapitre());
		}
		MainApp.getProject().enregistrerAnterieurs();
		SauvegardeXML.saveToXML(MainApp.getProject(), file);
		MainApp.getProject().backupChapters();
	}

	@FXML
	private void handleEnregistrerProjet() {
		if (MainApp.getProject() == null)
			return;
		File file = SauvegardeXML.getFilePath();
		if (file != null)
			enregistrer(file);
		else
			handleEnregistrerSousProjet();
	}

	@FXML
	private void handleEnregistrerSousProjet() {
		if (MainApp.getProject() == null)
			return;
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(MainApp.getStage());

		if (file != null) {
			/*
			 * if (!file.getPath().endsWith(".xml")) file = new
			 * File(file.getPath() + ".xml");
			 */
			enregistrer(file);
		}
	}

	@FXML
	private void handleNouveauProjet() {
		TextInputDialog dialog = new TextInputDialog("Nom");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(MainApp.getStyle());
		dialog.setTitle("Nouveau project");
		dialog.setHeaderText("Nom du project");
		dialog.setContentText("Entrez le nom de votre project : ");
		Optional<String> nomProjet = dialog.showAndWait();
		if (nomProjet.isPresent()) {
			// this.nettoyer();
			MainApp.creationProjet(nomProjet.get());
			tabMatiereController.initialize();
			ecritureController.initialize();
		}
	}

	@FXML
	private void handleExportToPDFProjet() {
		if (MainApp.getProject() == null)
			return;
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(MainApp.getStage());
		if (file != null) {
			try {
				ExportToPDF.createPdf(file.getPath());
			} catch (IOException | DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void handleNouveauChapitre() {
		tabMatiereController.handleNouveauChapitre();
	}

	@FXML
	public void handleUploadPDF() {
		if (MainApp.getProject() == null)
			return;
		String type = "uploadé"; // Pour le message d'erreur / succès
		try {
			FtpClient.uploadDialog();
			showSuccess(type);
		} catch (FTPConnectException | FileTooLargeException | KibbleConnectException e) {
			showErr(type, e);
		}
	}

	@FXML
	public void handleUploadSave() {
		if (MainApp.getProject() == null)
			return;
		String type = "uploadé";
		try {
			FtpClient.upload(SauvegardeXML.getFilePath());
			showSuccess(type);
		} catch (Exception e) {
			showErr(type, e);
		}
	}

	@FXML
	public void handleDownloadPdf() {
		String type = "téléchargé", ext = ".pdf";
		try {
			ArrayList<String> noms = FtpClient.listFiles(ext);
			ChoiceDialog<String> dialog = new ChoiceDialog<>(noms.get(0), noms);
			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.getStylesheets().add(MainApp.getStyle());
			dialog.setTitle("Fichier à télécharger");
			dialog.setHeaderText("Choisissez le fichier .pdf à télécharger");
			dialog.setContentText("Nom du fichier :");
			Optional<String> res = dialog.showAndWait();
			if (res.isPresent()) {
				if (FtpClient.downloadDialog(res.get() + ext, "*" + ext))
					showSuccess(type);
			}
		} catch (FTPConnectException | KibbleConnectException | EmptyFolderException e) {
			showErr(type, e);
		}
	}

	@FXML
	public void handleDownloadXml() {
		String type = "téléchargé", ext = ".xml";
		try {
			ArrayList<String> noms = FtpClient.listFiles(ext);
			ChoiceDialog<String> dialog = new ChoiceDialog<>(noms.get(0), noms);
			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.getStylesheets().add(MainApp.getStyle());
			dialog.setTitle("Projets");
			dialog.setHeaderText("Choisissez le projet à récupérer");
			dialog.setContentText("Nom du projet :");
			Optional<String> res = dialog.showAndWait();
			if (res.isPresent()) {
				if (FtpClient.downloadDialog(res.get() + ext, "*" + ext))
					showSuccess(type);
			}
		} catch (FTPConnectException | KibbleConnectException | EmptyFolderException e) {
			showErr(type, e);
		}
	}

	@FXML
	private void aboutKibble() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setResizable(true);
		alert.getDialogPane().setPrefSize(480, 250);
		alert.setTitle("Informations");
		alert.setHeaderText("A propos de Kibble");
		alert.setContentText("Kibble est un projet réalisé par Xavier, Nolwenn, Aline, Lucas et Hector. \n"
				+ "Nous sommes 5 élèves de l'IUT Paris Descartes, et ce programme a été réalisé dans le cadre d'un projet de fin d'année. \n\n"
				+ "Le logiciel est programmé en Java et utilise JavaFX pour ses interfaces. "
				+ "Nous avons également utilisé la bibliothèque iTextPdf(Génération de pdf)");

		alert.showAndWait();
	}

	private void showErr(String type, Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Erreur");
		alert.setHeaderText("Le fichier n'a pas été " + type);
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}

	private void showSuccess(String type) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText("Le fichier a été " + type + " avec succès");
		alert.show();
	}

	FicheController getFicheController() {
		return ficheController;
	}

	public void setFicheController(FicheController ficheController) {
		this.ficheController = ficheController;
	}

	public static void setLayoutController(LayoutController layoutController) {
		lc = layoutController;
	}

	public TabMatiereController getTabMatiereController() {
		return tabMatiereController;
	}

	public void setTabMatiereController(TabMatiereController tabMatiereController) {
		this.tabMatiereController = tabMatiereController;
	}

	EcritureController getEcritureController() {
		return ecritureController;
	}

	public void setEcritureController(EcritureController ecritureController) {
		this.ecritureController = ecritureController;
	}

	public static LayoutController get() {
		if (lc == null)
			lc = new LayoutController();
		return lc;
	}

	@FXML
	public void handleFichePersonnage() {
		ficheController.handleNouvelleFichePersonnage();
	}

	@FXML
	public void handleFicheLieu() {
		ficheController.handleNouvelleFicheLieu();
	}

	@FXML
	public void handleFicheEvnmt() {
		ficheController.handleNouvelleFicheEvenement();
	}

	@FXML
	public void handleFichePersonnalise() {
		ficheController.handleNouvelleFichePersonnalise();
	}

}
