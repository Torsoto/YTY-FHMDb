module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires ormlite.jdbc;

    requires com.jfoenix;
    requires okhttp3;
    requires com.google.gson;
    requires java.sql;

    opens at.ac.fhcampuswien.fhmdb.DataLayer to ormlite.jdbc;
    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb;
}