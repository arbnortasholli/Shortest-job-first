module com.example.shortestjobfirst {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.shortestjobfirst to javafx.fxml;
    exports com.example.shortestjobfirst;
}