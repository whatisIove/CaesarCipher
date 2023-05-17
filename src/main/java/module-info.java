module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;


    opens crypto.system to javafx.fxml;
    exports crypto.system;
}