package com.diti.examenapp.entity;

import javafx.beans.property.*;

import javax.persistence.*;

@Entity
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private double prix;

    @Column(nullable = false)
    private double quantite;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Propriétés JavaFX
    @Transient
    private final ObjectProperty<Category> categoryProperty = new SimpleObjectProperty<>();
    @Transient
    private final SimpleStringProperty codeProperty = new SimpleStringProperty();
    @Transient
    private final SimpleStringProperty nomProperty = new SimpleStringProperty();
    @Transient
    private final DoubleProperty prixProperty = new SimpleDoubleProperty();
    @Transient
    private final DoubleProperty quantiteProperty = new SimpleDoubleProperty();

    // Constructeur par défaut
    public Produit() {
        // Synchroniser les propriétés JavaFX avec les champs
        codeProperty.addListener((obs, old, newValue) -> code = newValue);
        nomProperty.addListener((obs, old, newValue) -> nom = newValue);
        prixProperty.addListener((obs, old, newValue) -> prix = newValue.doubleValue());
        quantiteProperty.addListener((obs, old, newValue) -> quantite = newValue.doubleValue());
        categoryProperty.addListener((obs, old, newValue) -> category = newValue);
    }

    // Getters et setters pour les champs
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) {
        this.code = code;
        this.codeProperty.set(code);
    }
    public String getNom() { return nom; }
    public void setNom(String nom) {
        this.nom = nom;
        this.nomProperty.set(nom);
    }
    public double getPrix() { return prix; }
    public void setPrix(double prix) {
        this.prix = prix;
        this.prixProperty.set(prix);
    }
    public double getQuantite() { return quantite; }
    public void setQuantite(double quantite) {
        this.quantite = quantite;
        this.quantiteProperty.set(quantite);
    }
    public Category getCategory() { return category; }
    public void setCategory(Category category) {
        this.category = category;
        this.categoryProperty.set(category);
    }

    // Getters pour les propriétés JavaFX
    public SimpleStringProperty codeProperty() { return codeProperty; }
    public SimpleStringProperty nomProperty() { return nomProperty; }
    public DoubleProperty prixProperty() { return prixProperty; }
    public DoubleProperty quantiteProperty() { return quantiteProperty; }
    public ObjectProperty<Category> categoryProperty() { return categoryProperty; }
}
