module com.diti.examenapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires static lombok;
    requires java.sql;

    requires org.hibernate.orm.core; // si tu l'as dans un module




    opens com.diti.examenapp.entity to org.hibernate.orm.core;

    opens com.diti.examenapp to javafx.fxml;
    exports com.diti.examenapp;
    exports com.diti.examenapp.entity;
    exports com.diti.examenapp.controllers;
    opens com.diti.examenapp.controllers to javafx.fxml;
    // opens com.diti.examenapp.entity to javafx.fxml;
}