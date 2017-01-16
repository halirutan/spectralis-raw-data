package de.halirutan.spectralis.gui.test;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Controller {
    public VBox root;
    public Button button;
    public Label label;

    public void doSomething() {
        Task task = getWorker();
        Service service = new Service() {
            @Override
            protected Task createTask() {
                return task;
            }
        };
        service.start();

    }

    private Task getWorker() {
        Task task =  new Task() {
            @Override
            protected Object call() throws Exception {
                for(int i = 5; i > 0; i--) {
                    updateMessage(i + " seconds..");
                    Thread.sleep(1000);
                }
                failed();
                return null;
            }
        };

        label.textProperty().unbind();
        label.textProperty().bind(task.messageProperty());

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                alertDialog("Error happened ");
                System.out.println(event.getSource().getException().getMessage());
                button.setDisable(false);
            }
        });

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Controller.this.alertDialog(event.getSource().getState().toString());
                button.setDisable(false);
            }

        });

        task.setOnRunning(event -> {
            button.setDisable(true);
        });

        return task;

    }

    private void alertDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
