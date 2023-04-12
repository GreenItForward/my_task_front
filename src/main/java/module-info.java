module com.mytask.my_task_front {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires kernel;
    requires layout;

    opens com.mytask.front to javafx.fxml;
    exports com.mytask.front;
    exports com.mytask.front.controller;
    opens com.mytask.front.controller to javafx.fxml;
}