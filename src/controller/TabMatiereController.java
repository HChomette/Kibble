package controller;

import java.awt.Desktop;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.controlsfx.control.textfield.TextFields;

import application.Controller;
import application.MainApp;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import project.Chapter;
import project.Note;

public class TabMatiereController implements Controller {
	// **********OBJETS FXML
	@FXML
	private Button buttonNouveau;
	@FXML
	private TextField zoneTextRecherche;
	@FXML
	private Button buttonDico;
	@FXML
	private Button buttonSynonyme;
	@FXML
	private Button buttonRime;
	@FXML
	private TableView<Chapter> chapitreTable;
	@FXML
	private TableColumn<Chapter, Integer> numChapitreColumn;
	@FXML
	private TableColumn<Chapter, String> titreChapitreColumn;
	@FXML
	private ComboBox<Note> listeNotes;
	@FXML
	private TextArea texteNotes;
	@FXML
	private Label labelTitreChapitre;
	@FXML
	private Label labelTitreNote;

	private Chapter chapterSelection = null;

	@FXML
	public void initialize() {

		this.style();

		chapterSelection = null;

		ArrayList dico = getDico("../bin/ressources/dicoFR.txt");
		if(dico != null) TextFields.bindAutoCompletion(this.zoneTextRecherche, dico);

		this.chapitreTable.getSelectionModel().selectAll();

		this.displayTableChapitres();

		this.chapitreTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> switchChapitre(newValue));

		this.displayListeNotes();

		texteNotes.textProperty().addListener((observableValue, oldValue, newValue) -> {
			try {
				listeNotes.getSelectionModel().getSelectedItem().setText(newValue);
			} catch (Exception e) {
			}
		});

	}

	private void displayListeNotes() {
		try {
			listeNotes.getItems().clear();
			ArrayList<Note> notes = new ArrayList<Note>();
			for (Chapter c : MainApp.getProject().getChapters())
				notes.add(c.getNotes());

			listeNotes.getItems().addAll(notes);
			listeNotes.getItems().add(MainApp.getProject().getNotes());
			listeNotes.getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> switchNote(newValue));
		} catch (Exception e) {
		}
	}

	private void switchNote(Note newValue) {
		try {
			texteNotes.setText(newValue.getText());
			texteNotes.setEditable(true);
		} catch (Exception e) {
			texteNotes.setText("");
			texteNotes.setEditable(false);
		}

	}

	private void displayTableChapitres() {
		this.titreChapitreColumn.setCellValueFactory(new PropertyValueFactory<Chapter, String>("titre"));
		this.numChapitreColumn.setCellValueFactory(new PropertyValueFactory<Chapter, Integer>("numero"));

		if (MainApp.getProject() != null)
			this.chapitreTable.setItems(FXCollections.observableList(MainApp.getProject().getChapters()));
	}

	@FXML
	public void handleNouveauChapitre() {
		if(MainApp.getProject() == null) return;
		TextInputDialog dialog = new TextInputDialog("Titre");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(MainApp.getStyle());
		dialog.setTitle("Nouveau chapitre");
		dialog.setHeaderText("Titre du nouveau chapitre");
		dialog.setContentText("Entrez le nom du chapitre n°" + (MainApp.getProject().getChapters().size() + 1) + " : ");
		Optional<String> nomChapitre = dialog.showAndWait();
		if (nomChapitre.isPresent()) {
			MainApp.getProject().getChapters()
					.add(new Chapter(nomChapitre.get(), MainApp.getProject().getChapters().size() + 1));
			this.displayTableChapitres();
		}
		this.displayListeNotes();
	}

	public TextField getZoneTextRecherche() {
		return zoneTextRecherche;
	}

	public void setZoneTextRecherche(TextField zoneTextRecherche) {
		this.zoneTextRecherche = zoneTextRecherche;
	}

	public Button getButtonDico() {
		return buttonDico;
	}

	public void setButtonDico(Button buttonDico) {
		this.buttonDico = buttonDico;
	}

	public Button getButtonSynonyme() {
		return buttonSynonyme;
	}

	public void setButtonSynonyme(Button buttonSynonyme) {
		this.buttonSynonyme = buttonSynonyme;
	}

	public Button getButtonRime() {
		return buttonRime;
	}

	public void setButtonRime(Button buttonRime) {
		this.buttonRime = buttonRime;
	}

	public TableView<Chapter> getChapitreTable() {
		return chapitreTable;
	}

	public void setChapitreTable(TableView<Chapter> chapitreTable) {
		this.chapitreTable = chapitreTable;
	}

	public TableColumn<Chapter, Integer> getNumChapitreColumn() {
		return numChapitreColumn;
	}

	public void setNumChapitreColumn(TableColumn<Chapter, Integer> numChapitreColumn) {
		this.numChapitreColumn = numChapitreColumn;
	}

	public TableColumn<Chapter, String> getTitreChapitreColumn() {
		return titreChapitreColumn;
	}

	public void setTitreChapitreColumn(TableColumn<Chapter, String> titreChapitreColumn) {
		this.titreChapitreColumn = titreChapitreColumn;
	}

	/**
	 * Remplace le chapitre actif
	 */
	private void switchChapitre(Chapter newvalue) {
		if (newvalue == null)
			return;
		// On save le texte du precedent
		if (chapterSelection != null)
			MainApp.getProject().getChapter(MainApp.getProject().getChapters().indexOf(chapterSelection))
					.setText(LayoutController.get().getEcritureController().getTextChapitre());

		// On affiche le texte du nouveau
		LayoutController.get().getEcritureController().setUpChapitre(newvalue);

		// On remplace le numero par le nouveau numero
		chapterSelection = chapitreTable.getSelectionModel().getSelectedItem();
	}

	private ArrayList<String> getDico(String chemin) {
		ArrayList<String> listDico = new ArrayList<String>();
		Scanner dico;
		//C'est dégueu de ouf
		//MAIS ça marche même dans le jar
		String text = null;
		try {
			text = new Scanner(MainApp.class.getResourceAsStream("/dicoFR.txt"), "UTF-8").useDelimiter("\\A").next();
		} catch (NullPointerException e) {
			return null;
		}
		dico = new Scanner(text);
		while (dico.hasNextLine())
			listDico.add(dico.nextLine());
		dico.close();
		return listDico;
	}

	public Chapter getChapitreActuel() {
		return chapitreTable.getSelectionModel().getSelectedItem();
	}

	@FXML
	private void renommerChapitre() {
		TextInputDialog dialog = new TextInputDialog("Nom");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(MainApp.getStyle());
		dialog.setTitle("Renommer");
		dialog.setHeaderText("Nouveau nom du chapitre");
		dialog.setContentText("Entrez le nom du chapitre n°"
				+ (chapitreTable.getSelectionModel().getSelectedItem().getNumber()) + " : ");

		Optional<String> nomChapitre = dialog.showAndWait();
		if (nomChapitre.isPresent()) {
			chapitreTable.getSelectionModel().getSelectedItem().setTitle(nomChapitre.get());
			initialize();

			// L'apparence de la table se refresh pas toute seule, aucune
			// méthode ne permet de le faire
			// On est donc forcés de trouver une technique pour la faire
			// actualiser visuellement
			chapitreTable.getColumns().get(1).setVisible(false);
			chapitreTable.getColumns().get(1).setVisible(true);

		}
	}

	@FXML
	private void deplacerChapitre() {
		TextInputDialog dialog = new TextInputDialog("Position");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(MainApp.getStyle());
		dialog.setTitle("Déplacer");
		dialog.setHeaderText("Nouvelle position du chapitre");
		dialog.setContentText("Entrez la nouvelle position du chapitre "
				+ (chapitreTable.getSelectionModel().getSelectedItem().getTitle()) + " : ");

		Optional<String> numChapitre = dialog.showAndWait();
		if (numChapitre.isPresent()) {
			int num;
			try {
				num = Integer.parseInt(numChapitre.get());
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Valeur invalide");
				alert.setContentText("La valeur entrée n'est pas un nombre entier");
				DialogPane alertPane = alert.getDialogPane();
				alertPane.getStylesheets().add(MainApp.getStyle());

				alert.showAndWait();
				return;
			}
			if (num < 1)
				num = 1;
			if (num > MainApp.getProject().getChapters().size())
				num = MainApp.getProject().getChapters().size();
			MainApp.getProject().moveChapter(chapitreTable.getSelectionModel().getSelectedItem().getNumber() - 1,
					num - 1);
			initialize();

			// L'apparence de la table se refresh pas toute seule, aucune
			// méthode ne permet de le faire
			// On est donc forcés de trouver une technique pour la faire
			// actualiser visuellement
			chapitreTable.getColumns().get(0).setVisible(false);
			chapitreTable.getColumns().get(0).setVisible(true);
			chapitreTable.getColumns().get(1).setVisible(false);
			chapitreTable.getColumns().get(1).setVisible(true);

		}
	}

	@FXML
	private void supprimerChapitre() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(MainApp.getStyle());
		alert.setTitle("Supprimer");
		alert.setHeaderText("Suppression de chapitre");
		alert.setContentText("Êtes vous sûr de vouloir supprimer le chapitre "
				+ chapitreTable.getSelectionModel().getSelectedItem().getTitle());

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			MainApp.getProject().deleteChapter(chapitreTable.getSelectionModel().getSelectedIndex());
			initialize();

			// Voir deplacerChapitre()
			chapitreTable.getColumns().get(0).setVisible(false);
			chapitreTable.getColumns().get(0).setVisible(true);
			chapitreTable.getColumns().get(1).setVisible(false);
			chapitreTable.getColumns().get(1).setVisible(true);
		}
	}

	private void openWebpage(String urlString) {
		try {
			Desktop.getDesktop().browse(new URL(urlString).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void rechercherDico() {
		String mot = zoneTextRecherche.getText().toLowerCase();
		String adresse = "https://fr.wiktionary.org/wiki/";
		String url = adresse + mot;
		openWebpage(url);
	}

	@FXML
	private void rechercherRime() {
		String mot = zoneTextRecherche.getText().toLowerCase();
		StringBuilder motBuild = new StringBuilder(mot);
		String hexValue;
		for (int i = 0; i < motBuild.length(); i++) {
			int c = (int) motBuild.charAt(i);
			if (c >= 128) {
				hexValue = Integer.toHexString(0x10000 | c).substring(1).toUpperCase();
				hexValue = hexValue.substring(2, 4);
				hexValue = "%" + hexValue;
				motBuild.deleteCharAt(i);
				motBuild.insert(i, hexValue);
			}
		}
		mot = motBuild.toString();
		String adresse = "http://www.dicodesrimes.com/rime/";
		String url = adresse + mot;
		openWebpage(url);
	}

	@FXML
	private void rechercherSynonyme() {
		String mot = zoneTextRecherche.getText().toLowerCase();
		String adresse = "http://www.crisco.unicaen.fr/des/synonymes/";
		String url = adresse + mot;
		openWebpage(url);
	}

	private void style() {
		if(LayoutController.fontMS != null) this.labelTitreChapitre.setFont(LayoutController.fontMS);
		if(LayoutController.fontMS != null) this.labelTitreNote.setFont(LayoutController.fontMS);
		this.numChapitreColumn.setStyle("-fx-alignment: CENTER;");
	}

}
