module com.example.graph {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.graph to javafx.fxml;
    exports com.example.graph;
}