package main;

import java.io.IOException;
import java.util.logging.*;

import controllers.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Manages control flow for logins
 * @author Christian Dewey
 */
public class LoginManager {
    private Stage loginStage;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Callback method invoked when a user has been authenticated. Starts the main application view.
     * Passes the Username of the user to the main view for display.
     * @param sessionUsername the username of the User after successful login
     */
    public void authenticated(String sessionUsername) {
        showMainView(sessionUsername);
    }

    /**
     * Shows the Login Screen
     */
    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../views/login_view.fxml")
            );

            Stage primaryStage = new Stage();
            Scene scene = new Scene(new StackPane());
            scene.setRoot(loader.load());

            primaryStage.initStyle(StageStyle.UNDECORATED);

            primaryStage.setScene(scene);

            // allows the Login screen to be moved with a mouse drag.
            Parent root = primaryStage.getScene().getRoot();

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            });

            primaryStage.show();

            loginStage = primaryStage;

            LoginController controller =
                    loader.getController();
            controller.initManager(this);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Shows the Main Application Screen
     * @param sessionUsername the username of the User after successful login for display on the main screen
     */
    private void showMainView(String sessionUsername) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../views/main_view.fxml")
            );

            Stage primaryStage = new Stage();
            Scene scene = new Scene(new StackPane());
            scene.setRoot(loader.load());

            primaryStage.initStyle(StageStyle.DECORATED);

            primaryStage.setTitle("Application Scheduler");
            primaryStage.setScene(scene);
            primaryStage.show();

            controllers.MainController controller =
                    loader.getController();
            controller.initSession(sessionUsername);

            loginStage.hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}