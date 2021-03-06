package controller;

import java.util.Date;
import java.util.Optional;

import application.Controller;
import application.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import project.Chapter;
import project.PreviousChapter;

public class AnterieurController implements Controller {
	
	@FXML
	private TextArea zoneTextDisplay;
	
	private PreviousChapter chapAnt;
	
	private Chapter chap;

	public AnterieurController() {
	}

	@FXML
	public void initialize() {
		this.style();
		this.zoneTextDisplay.setText(chapAnt.getText());
	}

	/**
	 * Applique le CSS à la fenêtre
	 */
	private void style() {
		if(LayoutController.fontNBRegular != null) this.zoneTextDisplay.setFont(LayoutController.fontNBRegular);
	}

	public void setChapitreAnterieur(PreviousChapter previousChapter, Chapter chapter) {
		chapAnt = previousChapter;
		chap = chapter;
	}
	
	@FXML
	public void retablirChapitre(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Rétablissement de la version antérieure");
		alert.setContentText("Voulez vous rétablir cette version ?");
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(MainApp.getStyle());

		Optional<ButtonType> result = alert.showAndWait();
		if(result.isPresent()) {
			if (result.get() == ButtonType.OK) {
				String retab = chapAnt.getText();
				chapAnt.setText(chap.getText());
				chapAnt.setDate(new Date());
				chap.setText(retab);
				LayoutController.get().getEcritureController().getZoneTextEcriture().setText(chap.getText());
				Stage stage = (Stage) zoneTextDisplay.getScene().getWindow();
				stage.close();
			}
		}
	}
}
