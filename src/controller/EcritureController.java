package controller;

import java.util.List;
import java.util.Optional;

import application.Controller;
import application.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import projet.Chapitre;
import projet.ChapitreAnterieur;
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

	private Chapitre chapitreEnCours = null;

	@FXML
	public void initialize() {
		this.style();
		this.zoneTextEcriture.setText("");
		this.chapitreEnCours = null;
	}

	public String getTextChapitre() {
		return this.zoneTextEcriture.getText();
	}

	/**
	 * Ouvre un chapitre dans le zone d'écriture
	 */
	public void setUpChapitre(Chapitre chapitre) {
		this.zoneTextEcriture.setEditable(true);
		this.chapitreEnCours = chapitre;
		this.zoneTextEcriture.setText(chapitreEnCours.getTexte());
		this.labelTitreChapitre.setText(chapitreEnCours.getNumero() + ".	" + chapitreEnCours.getTitre());
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
			List<ChapitreAnterieur> choix = chapitreEnCours.getChapitresAnterieurs();

			ChoiceDialog<ChapitreAnterieur> dialog = new ChoiceDialog<>(null, choix);
			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.getStylesheets().add(MainApp.getStyle());
			dialog.setTitle("Version antérieure");
			dialog.setHeaderText("Visualiser une version précédente");
			dialog.setContentText("Choisissez la version à consulter");

			Optional<ChapitreAnterieur> result = dialog.showAndWait();
			result.ifPresent(chapitreAnterieur -> showAnterieur(chapitreAnterieur, chapitreEnCours));
		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Aucun Chapitre");
			alert.setContentText("Vous devez d'abord ouvrir un chapitre");

			alert.showAndWait();
		}

	}

	/**
	 * Ouvre une fenêtre pour voir la version antérieure choisie
	 * @see openAnterieure()
	 */
	private void showAnterieur(ChapitreAnterieur chapitreAnterieur, Chapitre chap) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/AnterieurView.fxml"));
			AnterieurController a = new AnterieurController();
			a.setChapitreAnterieur(chapitreAnterieur, chap);
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
