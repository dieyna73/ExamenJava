package com.diti.examenapp.controllers;

import com.diti.examenapp.entity.Category;
import com.diti.examenapp.entity.Produit;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProduitController implements Initializable {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("examen");

    @FXML
    private Button ajouterbtn;

    @FXML
    private TableColumn<Produit, String> codeCol;

    @FXML
    private TableColumn<Produit, String> nameCol;

    @FXML
    private TableColumn<Produit, Double> priceCol;

    @FXML
    private TableColumn<Produit, Double> quantityCol;

    @FXML
    private TableColumn<Produit, String> categoryCol;

    @FXML
    private TableColumn<Produit, Double> amontCol;

    @FXML
    private ComboBox<Category> categoryCom;

    @FXML
    private TextField codeTf;

    @FXML
    private TextField nameTf;

    @FXML
    private TextField priceTf;

    @FXML
    private TextField quantityTf;

    @FXML
    private TableView<Produit> produitTb;

    @FXML
    private Label totalLabel;

    private ObservableList<Produit> produitList = FXCollections.observableArrayList();
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser les colonnes du TableView
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        categoryCol.setCellValueFactory(cellData -> {
            Produit produit = cellData.getValue();
            return produit.getCategory() != null ? produit.getCategory().nameProperty() : null;
        });
        amontCol.setCellValueFactory(cellData -> {
            Produit produit = cellData.getValue();
            return Bindings.createDoubleBinding(
                    () -> produit.getPrix() * produit.getQuantite(),
                    produit.prixProperty(), produit.quantiteProperty()
            ).asObject();
        });

        // Lier le TableView à la liste observable
        produitTb.setItems(produitList);

        // Configurer le ComboBox pour afficher le nom de la catégorie
        categoryCom.setCellFactory(lv -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        categoryCom.setButtonCell(new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        categoryCom.setItems(categoryList);

        // Charger les catégories et produits depuis la base de données
        loadCategories();
        loadProduits();

        // Afficher le prochain code généré
        updateCodeField();

        // Mettre à jour le total initial
        updateTotal();
    }

    @FXML
    private void AjouterProduit(ActionEvent event) {
        try {
            // Valider les entrées
            String nom = nameTf.getText().trim();
            String prixText = priceTf.getText().trim();
            String quantiteText = quantityTf.getText().trim();
            Category category = categoryCom.getValue();

            // Vérifier que tous les champs sont remplis
            if (nom.isEmpty() || prixText.isEmpty() || quantiteText.isEmpty() || category == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs sauf le code.");
                return;
            }

            double prix = Double.parseDouble(prixText);
            double quantite = Double.parseDouble(quantiteText);

            // Valider que le prix et la quantité sont positifs
            if (prix < 0 || quantite < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix et la quantité doivent être positifs.");
                return;
            }

            // Créer un nouveau produit
            Produit produit = new Produit();
            produit.setCode(generateProductCode());
            produit.setNom(nom);
            produit.setPrix(prix);
            produit.setQuantite(quantite);
            produit.setCategory(category);

            // Sauvegarder dans la base de données
            saveProduit(produit);

            // Ajouter à la liste observable
            produitList.add(produit);

            // Réinitialiser les champs
            nameTf.clear();
            priceTf.clear();
            quantityTf.clear();
            categoryCom.getSelectionModel().clearSelection();

            // Mettre à jour le champ code
            updateCodeField();

            // Mettre à jour le total
            updateTotal();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Produit ajouté avec succès !");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix et la quantité doivent être des nombres valides.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveProduit(Produit produit) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Assurer que la catégorie est gérée par l'EntityManager
            produit.setCategory(em.merge(produit.getCategory()));
            em.persist(produit);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private void loadProduits() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Produit> produits = em.createQuery("SELECT p FROM Produit p", Produit.class).getResultList();
            produitList.addAll(produits);
        } finally {
            em.close();
        }
    }

    private void loadCategories() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Category> categories = em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
            categoryList.addAll(categories);
        } finally {
            em.close();
        }
    }

    private String generateProductCode() {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(p) FROM Produit p", Long.class).getSingleResult();
            String code;
            do {
                count++;
                code = String.format("Pr-%03d", count);
            } while (em.createQuery("SELECT COUNT(p) FROM Produit p WHERE p.code = :code", Long.class)
                    .setParameter("code", code)
                    .getSingleResult() > 0);
            return code;
        } finally {
            em.close();
        }
    }

    private void updateCodeField() {
        codeTf.setText(generateProductCode());
    }

    private void updateTotal() {
        double total = produitList.stream()
                .mapToDouble(p -> p.getPrix() * p.getQuantite())
                .sum();
        totalLabel.setText(String.format("TOTAL : %.2f", total));
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
