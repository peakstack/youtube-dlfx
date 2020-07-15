module youtube.dlfx {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires log4j;
    requires java.youtube.downloader;
    requires jlayer;
    requires com.jfoenix;
    requires fastjson;
    requires java.sql;
    opens app to javafx.graphics;
    opens controller to javafx.fxml;
}