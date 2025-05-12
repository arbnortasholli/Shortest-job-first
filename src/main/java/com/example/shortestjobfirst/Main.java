package com.example.shortestjobfirst;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SFJ.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 682, 485);
        stage.setTitle("Shortest-Job-First (Preemptive)");
        stage.setScene(scene);
        stage.show();
    }
}


