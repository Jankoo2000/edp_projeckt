module com.example.edp_projeckt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    // dodawac wszsytkie package
    opens com.example.edp_projeckt to javafx.fxml;
    opens com.example.edp_projeckt.controllers to javafx.fxml;
    opens com.example.edp_projeckt.api to javafx.fxml, com.fasterxml.jackson.databind;

    exports com.example.edp_projeckt;
    exports com.example.edp_projeckt.controllers;
    exports com.example.edp_projeckt.api;
}