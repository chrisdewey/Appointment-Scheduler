package main;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 *
 * @author Christian Dewey
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginManager loginManager = new LoginManager();
        loginManager.showLoginScreen();
    }

    public static void main(String[] args) {
        DBConnection.startConnection();

        launch(args);

        DBConnection.closeConnection();
    }
}