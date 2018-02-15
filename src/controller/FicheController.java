package controller;

import java.util.ArrayList;
import java.util.Optional;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import application.Controller;
import application.MainApp;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import project.Card;
import project.CardLine;

public class FicheController implements Controller {

	// **********OBJETS FXML
	@FXML
	private TabPane tabFiches;
	@FXML
	private Button buttonOuvrir;
	@FXML
	private TextField zoneTextRechercheFiche;
	@FXML
	private Label labelTitre;

	private Card sauvCard;
	
	private AutoCompletionBinding test;

	@FXML
	public void initialize() {
		this.style();
		sauvCard = new Card();
		try {
			test = TextFields.bindAutoCompletion(this.zoneTextRechercheFiche, getNomsFiches());
		} catch (NullPointerException e) {
		}
	}

	private ArrayList<String> getNomsFiches() {
		ArrayList<String> tmp = new ArrayList<>();
		try {
			for (Card card : MainApp.getProject().getFiches()) {
				tmp.add(card.getName());
			}
		} catch (NullPointerException e) {
		}
		return tmp;
	}

	/**
	 * Affiche la card passée en paramètre dans un nouvel onglet
	 */
	public void displayFiche(Card card) {
		Tab tab = new Tab(card.getName());
		tab.setContent(getVBoxFiche(card, false));
		tabFiches.getTabs().add(tab);
		tabFiches.getSelectionModel().select(tab);
		
		test.dispose();
		test = TextFields.bindAutoCompletion(this.zoneTextRechercheFiche, getNomsFiches());
		
	}

	private GridPane getGridFiche(Card card, boolean isEditable) {
		// Création de la grille
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20, 5, 20, 10));
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(99);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(1);
		grid.getColumnConstraints().addAll(column1, column2);
		grid.setVgap(2);
		// grid.setGridLinesVisible(true);

		// Le compteur de ligne
		int gridLigne = 2;

		Label nomFiche = new Label(card.getName());
		if(LayoutController.fontNBBoldItalic != null) nomFiche.setFont(LayoutController.fontNBBoldItalic);
		nomFiche.setContextMenu(getContextMenuFiche(card));
		GridPane.setHalignment(nomFiche, HPos.CENTER);
		grid.add(nomFiche, 0, 0, 2, gridLigne);

		++gridLigne;
		++gridLigne;

		for (CardLine ligne : card.getDatas()) {
			Label nomChamp = new Label(ligne.getFieldName());
			nomChamp.setWrapText(true);
			// nomChamp.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
			if(LayoutController.fontNBBoldSmall != null) nomChamp.setFont(LayoutController.fontNBBoldSmall);
			GridPane.setHalignment(nomChamp, HPos.LEFT);
			grid.add(nomChamp, 0, gridLigne);

			Label champ = new Label(ligne.getFieldValue());
			champ.setWrapText(true);
			// champ.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
			if(LayoutController.fontNBRegularSmall != null) champ.setFont(LayoutController.fontNBRegularSmall);
			GridPane.setHalignment(champ, HPos.LEFT);
			grid.add(champ, 0, ++gridLigne);

			if (isEditable) {
				grid.getColumnConstraints().get(0).setPercentWidth(90);
				grid.getColumnConstraints().get(1).setPercentWidth(10);
				Button editLigne = new Button("/");
				GridPane.setHalignment(editLigne, HPos.CENTER);
				grid.add(editLigne, 1, gridLigne);
				editLigne.setOnAction(event -> {
					Button appuyed = (Button) event.getSource();
					int indexLigne = GridPane.getRowIndex(appuyed);
					if (appuyed.getText().equals("/")) {
						Label editLabel = (Label) LayoutController.get().getFicheController()
								.getNodeByRowColumnIndex(indexLigne, 0, grid);
						grid.getChildren().remove(editLabel);
						TextField newText = new TextField(editLabel.getText());
						if(LayoutController.fontNBRegularSmall != null) newText.setFont(LayoutController.fontNBRegularSmall);
						grid.add(newText, 0, indexLigne);
						appuyed.setText("V");
					} else {
						TextField editTextField = (TextField) LayoutController.get().getFicheController()
								.getNodeByRowColumnIndex(indexLigne, 0, grid);
						grid.getChildren().remove(editTextField);
						Label newLabel = new Label(editTextField.getText());
						if(LayoutController.fontNBRegularSmall != null) newLabel.setFont(LayoutController.fontNBRegularSmall);
						newLabel.setWrapText(true);
						grid.add(newLabel, 0, indexLigne);
						card.getDatas().get(indexLigne / 4 - 1).setFieldValue(editTextField.getText());
						appuyed.setText("/");
					}
				});
				// Image imageOk = new
				// Image(getClass().getResourceAsStream("../ressources/images/edit.png"));
				// Button supprLigne = new Button(null, new ImageView(imageOk));
				// supprLigne.setMaxHeight(10);
				Button supprLigne = new Button("X");
				GridPane.setHalignment(supprLigne, HPos.CENTER);
				grid.add(supprLigne, 1, gridLigne - 1);
				supprLigne.setOnAction(event -> {
					Button appuyed = (Button) event.getSource();
					int indexLigne = GridPane.getRowIndex(appuyed);
					card.getDatas().remove(indexLigne / 4 - 1);
					LayoutController.get().getFicheController().majTab(card, true);
				});
			}

			++gridLigne;
			++gridLigne;
			++gridLigne;

		}

		if (isEditable) {
			Button enreg = new Button("Enregistrer");
			GridPane.setHalignment(enreg, HPos.CENTER);
			grid.add(enreg, 0, gridLigne);
			enreg.setOnAction(event -> {
				for (Node node : grid.getChildren()) {
					if (node.getClass().equals(TextField.class)) {
						return;
					}
				}
				card.setDatas(card.getDatas());
				LayoutController.get().getFicheController().majTab(card, false);
			});
			Button annul = new Button("Annuler");
			GridPane.setHalignment(annul, HPos.RIGHT);
			grid.add(annul, 0, gridLigne);
			annul.setOnAction(event -> LayoutController.get().getFicheController().majTab(sauvCard, false));
		} else {
			Button modif = new Button("Modifier");
			GridPane.setHalignment(modif, HPos.RIGHT);
			modif.setOnAction(event -> {
				sauvCard = card.clone();
				LayoutController.get().getFicheController().majTab(card, true);
			});
			grid.add(modif, 0, ++gridLigne);

			Button ajou = new Button("Ajouter ligne");
			// Les nouveaux champs !
			TextField nomChamp = new TextField();
			TextField valeurChamp = new TextField();
			GridPane.setHalignment(ajou, HPos.CENTER);
			grid.add(ajou, 0, gridLigne);
			ajou.setOnAction(event -> {

				Button button = (Button) event.getSource();

				Button annuler = new Button("X");
				if (button.getText().equals("Ajouter ligne")) {
					button.setText("Enregistrer");
					grid.getColumnConstraints().get(0).setPercentWidth(90);
					grid.getColumnConstraints().get(1).setPercentWidth(10);
					int gridLigne1 = card.getDatas().size() * 4 + 4;
					modif.setVisible(false);

					nomChamp.setPromptText("Nom du champ");
					valeurChamp.setPromptText("Valeur du champ");

					grid.add(nomChamp, 0, gridLigne1 - 2);
					grid.add(valeurChamp, 0, gridLigne1 - 1);

					GridPane.setHalignment(annuler, HPos.CENTER);
					grid.add(annuler, 1, gridLigne1 - 2);
					annuler.setOnAction(event1 -> LayoutController.get().getFicheController().majTab(card, false));
				} else if (button.getText().equals("Enregistrer") && !nomChamp.getText().equals("")) {
					CardLine newLigne = new CardLine(nomChamp.getText(), valeurChamp.getText());
					card.getDatas().add(newLigne);
					card.setDatas(card.getDatas());
					LayoutController.get().getFicheController().majTab(card, false);
				}
			});
		}

		return grid;
	}

	private ContextMenu getContextMenuFiche(Card card) {
		ContextMenu cm = new ContextMenu();
		MenuItem renommer = new MenuItem("Renommer");
		renommer.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				TextInputDialog dialog = new TextInputDialog(card.getName());
				DialogPane dialogPane = dialog.getDialogPane();
				dialogPane.getStylesheets().add(MainApp.getStyle());
				dialog.setTitle("Card");
				dialog.setHeaderText("Renommer la card " + card.getName());
				dialog.setContentText("Entrez le nouveau nom de votre card : ");

				// Traditional way to get the response value.
				Optional<String> nomFiche = dialog.showAndWait();
				if (nomFiche.isPresent()) {
					if (!nomFicheUnique(nomFiche.get())) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Erreur");
						alert.setHeaderText("Erreur sur le nom de la card");
						alert.setContentText("Le nom de card : " + nomFiche.get()
								+ " est déjà utilisé, vous ne pouvez pas avoir deux fiches avec le même nom");

						alert.showAndWait();
						this.handle(e);
					} else {
						fermerTabFiche(card);
						card.setName(nomFiche.get());
						displayFiche(card);
					}

				}

			}
		});
		MenuItem supprimer = new MenuItem("Supprimer");
		supprimer.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation suppression");
			alert.setHeaderText("Suppression de la card " + card.getName());
			alert.setContentText("Êtes-vous sûr de supprimer cette card ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				fermerTabFiche(card);
				MainApp.getProject().getFiches().remove(card);
			}
		});
		cm.getItems().addAll(renommer, supprimer);
		return cm;
	}

	public void majTab(Card card, boolean isModifiable) {
		this.tabFiches.getTabs().get(this.tabFiches.getSelectionModel().getSelectedIndex())
				.setContent(getVBoxFiche(card, isModifiable));
	}

	private VBox getVBoxFiche(Card card, boolean isModifiable) {
		ScrollPane sp = new ScrollPane(getGridFiche(card, isModifiable));
		sp.setFitToWidth(true);
		sp.setStyle("-fx-background-color:transparent;");
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		VBox box = new VBox();
		box.setStyle("-fx-background-color:transparent;");
		box.getChildren().addAll(sp);
		return box;
	}

	private boolean isOuverte(String titreFiche) {
		for (Tab tab : tabFiches.getTabs()) {
			if (tab.getText().equals(titreFiche))
				return true;
		}
		return false;
	}

	@FXML
	private void handleAjouterFiche() {
		if (getNomsFiches().contains(zoneTextRechercheFiche.getText())
				&& !isOuverte(zoneTextRechercheFiche.getText())) {
			displayFiche(
					MainApp.getProject().getFiches().get(getNomsFiches().indexOf(zoneTextRechercheFiche.getText())));
			zoneTextRechercheFiche.setText("");
		}
	}

	private String handleNouvelleFiche(String type) {
		if (MainApp.getProject() == null)
			return null;
		TextInputDialog dialog = new TextInputDialog("Nom");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(MainApp.getStyle());
		dialog.setTitle("Card");
		dialog.setHeaderText("Nouvelle fiche " + type);
		dialog.setContentText("Entrez le nom de votre nouvelle fiche " + type + " :");

		// Traditional way to get the response value.
		Optional<String> nomFiche = dialog.showAndWait();
		if (nomFiche.isPresent()) {
			if (!nomFicheUnique(nomFiche.get())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Erreur sur le nom de la fiche");
				alert.setContentText(
						nomFiche.get() + " est déjà utilisé, vous ne pouvez pas avoir deux fiches avec le même nom");

				alert.showAndWait();
				handleNouvelleFiche(type);
			} else
				return nomFiche.get();
		}

		return null;
	}

	@FXML
	protected void handleNouvelleFicheLieu() {
		String nomFiche = handleNouvelleFiche("lieu");
		if (nomFiche != null) {
			Card newCard = Card.getPlaceCard(nomFiche);
			MainApp.getProject().getFiches().add(newCard);
			displayFiche(newCard);
		}
	}

	@FXML
	protected void handleNouvelleFichePersonnage() {
		String nomFiche = handleNouvelleFiche("personnage");
		if (nomFiche != null) {
			Card newCard = Card.getCharacterCard(nomFiche);
			MainApp.getProject().getFiches().add(newCard);
			displayFiche(newCard);
		}
	}

	@FXML
	protected void handleNouvelleFicheEvenement() {
		String nomFiche = handleNouvelleFiche("évènement");
		if (nomFiche != null) {
			Card newCard = Card.getEventCard(nomFiche);
			MainApp.getProject().getFiches().add(newCard);
			displayFiche(newCard);
		}
	}

	@FXML
	protected void handleNouvelleFichePersonnalise() {
		String nomFiche = handleNouvelleFiche("personnalisée");
		if (nomFiche != null) {
			Card newCard = new Card(nomFiche);
			MainApp.getProject().getFiches().add(newCard);
			displayFiche(newCard);
		}
	}

	public Node getNodeByRowColumnIndex(int row, int column, GridPane gridPane) {
		Node result = null;
		ObservableList<Node> childrens = gridPane.getChildren();

		for (Node node : childrens) {
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
				result = node;
				break;
			}
		}

		return result;
	}

	private void fermerTabFiche(Card card) {
		for (Tab tab : tabFiches.getTabs()) {
			if (tab.getText().equals(card.getName())) {
				tabFiches.getTabs().remove(tab);
				break;
			}
		}
	}

	public boolean nomFicheUnique(String nom) {
		for (Card card : MainApp.getProject().getFiches()) {
			if (card.getName().equals(nom))
				return false;
		}
		return true;
	}

	private void style() {
		if(LayoutController.fontMS != null) this.labelTitre.setFont(LayoutController.fontMS);
	}

}
