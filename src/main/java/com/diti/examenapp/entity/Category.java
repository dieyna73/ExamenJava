
        package com.diti.examenapp.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCat;

    @Column(unique = true, nullable = false, length = 50, name = "code")
    private String code;

    @Column(nullable = false, length = 50, name = "nom")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Produit> produits = new ArrayList<>();

    // Propriété JavaFX pour name, ignorée par Hibernate
    @Transient
    private final StringProperty nameProperty = new SimpleStringProperty();

    // Getter et setter pour name afin de synchroniser avec nameProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameProperty.set(name);
    }

    // Getter pour la propriété JavaFX
    public StringProperty nameProperty() {
        return nameProperty;
    }
}
