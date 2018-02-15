package controller;

import java.util.List;
import java.util.Optional;

import application.Controller;
import application.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import project.Chapter;
import project.PreviousChapter;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

public class EcritureController implements Controller {
	// **********OBJETS FXML
	@FXML
	private TextArea zoneTextEcriture;
	@FXML
	private Label labelTitreChapitre;
	@FXML
	private Button buttonVersion;

	private Chapter chapterEnCours = null;

	@FXML
	public void initialize() {
		this.style();
		this.zoneTextEcriture.setText("");
		this.chapterEnCours = null;
	}

	public String getTextChapitre() {
		return this.zoneTextEcriture.getText();
	}

	/**
	 * Ouvre un chapter dans le zone d'écriture
	 */
	public void setUpChapitre(Chapter chapter) {
		this.zoneTextEcriture.setEditable(true);
		this.chapterEnCours = chapter;
		this.zoneTextEcriture.setText(chapterEnCours.getText());
		this.labelTitreChapitre.setText(chapterEnCours.getNumber() + ".	" + chapterEnCours.getTitle());
	}

	public TextArea getZoneTextEcriture() {
		return zoneTextEcriture;
	}

	public void setZoneTextEcriture(TextArea zoneTextEcriture) {
		this.zoneTextEcriture = zoneTextEcriture;
	}

	public Label getLabelTitreChapitre() {
		return labelTitreChapitre;
	}

	public void setLabelTitreChapitre(Label labelTitreChapitre) {
		this.labelTitreChapitre = labelTitreChapitre;
	}

	/**
	 * Propose à l'utilisateur d'ouvrir une version antérieure
	 */
	public void openAnterieure() {
		try {
			List<PreviousChapter> choix = chapterEnCours.getPreviousChapters();

			ChoiceDialog<PreviousChapter> dialog = new ChoiceDialog<>(null, choix);
			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.getStylesheets().add(MainApp.getStyle());
			dialog.setTitle("Version antérieure");
			dialog.setHeaderText("Visualiser une version précédente");
			dialog.setContentText("Choisissez la version à consulter");

			Optional<PreviousChapter> result = dialog.showAndWait();
			result.ifPresent(previousChapter -> showAnterieur(previousChapter, chapterEnCours));
		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Aucun Chapter");
			alert.setContentText("Vous devez d'abord ouvrir un chapitre");

			alert.showAndWait();
		}

	}

	/**
	 * Ouvre une fenêtre pour voir la version antérieure choisie
	 * @see openAnterieure()
	 */
	private void showAnterieur(PreviousChapter previousChapter, Chapter chap) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/AnterieurView.fxml"));
			AnterieurController a = new AnterieurController();
			a.setChapitreAnterieur(previousChapter, chap);
			loader.setController(a);
			AnchorPane layout = loader.load();

			Scene scene = new Scene(layout);
			stage.setScene(scene);
			stage.setResizable(false);

			scene.getStylesheets().add(MainApp.getStyle());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Applique le CSS à la fenêtre
	 */
	private void style() {
		if(LayoutController.fontNBRegular != null) this.zoneTextEcriture.setFont(LayoutController.fontNBRegular);
		if(LayoutController.fontNBBold != null) this.labelTitreChapitre.setFont(LayoutController.fontNBBold);
	}

}
