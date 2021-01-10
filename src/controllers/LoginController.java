package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.LoginManager;
import utils.DBConnection;
import utils.DBQuery;
import utils.Language;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller class for the Login Screen login_view.fxml.
 * @author Christian Dewey
 */
public class LoginController implements Initializable {
    @FXML TextArea loginConsoleHandler;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginBtn;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;

    /**
     * Displays location and system information in the login TextArea console based on system language.
     */
    @FXML void loginConsoleText() {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime myZDT = ZonedDateTime.of(LocalDateTime.now(), zone);

        loginConsoleHandler.setText(Language.consoleLanguage("appLanguage"));
        loginConsoleHandler.setText(loginConsoleHandler.getText() + Language.consoleLanguage("appLocation"));
        loginConsoleHandler.setText(loginConsoleHandler.getText() + myZDT.format(DateTimeFormatter.RFC_1123_DATE_TIME) + ".\n");
    }

    /**
     * Exits the application.
     */
    @FXML
    public void XButtonHandler(ActionEvent actionEvent) {
         Platform.exit();
    }

    /**
     * The instance of the LoginManager is passed in. When the 'Login' button is pressed (or when the ENTER key is
     * pressed while focus is on the 'password' field), it first calls the authorize() method to check username and
     * password credentials against the database. If it is successful, then the username is used as the 'sessionID'
     * which is passed back to the LoginManager via the 'loginManager.authenticated()' method, which opens the
     * main application screen 'main_view.fxml'.
     * @param loginManager the instance of the loginManager
     */
    public void initManager(final LoginManager loginManager) {
        loginBtn.setOnAction(event -> {
            String sessionID = null;
            try {
                sessionID = authorize();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (sessionID != null) {
                loginManager.authenticated(sessionID);
                loginActivityLog(true);
                loginConsoleHandler.setText(Language.languageCheck("successful") + "\n" + loginConsoleHandler.getText());
            } else {
                loginActivityLog(false);
                loginConsoleHandler.setText(Language.languageCheck("failed") + "\n" + loginConsoleHandler.getText());
            }
        });

        passwordField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                loginBtn.fire();
            }
        });
    }

    /**
     * Writes a log to login_activity.txt with time of login attempt and whether it was successful or not.
     * @param bool whether the login was successful or not
     */
    private void loginActivityLog(Boolean bool) {

        try {
            FileWriter writer = new FileWriter ("login_activity.txt", true);
            if (bool) {
                writer.append("Login Successful at ").append(String.valueOf(LocalDateTime.now())).append(".\n");
            } else {
                writer.append("Login Failed at ").append(String.valueOf(LocalDateTime.now())).append(".\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check login credentials from the 'usernameField' and 'passwordField'.
     * If accepted, return the username as the 'sessionID'. Otherwise, return null.
     * @return the Username as the 'sessionID' if the login attempt was successful. Else return null.
     */
    private String authorize() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String selectStatement = "SELECT User_Name, Password FROM users";

        DBQuery.setPreparedStatement(connection, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.execute();

        ResultSet rs = ps.getResultSet();

        boolean matchFound = false;

        while(rs.next()) {
            String username = rs.getString("User_Name");
            String password = rs.getString("Password");

            if(username.equals(usernameField.getText()) && password.equals(passwordField.getText())) {
                matchFound = true;
                break;
            }
        }

        return
                matchFound ? usernameField.getText() : null;
    }

    /**
     * Sets language of Labels and Buttons based on system language.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        usernameLabel.setText(Language.languageCheck("username"));
        passwordLabel.setText(Language.languageCheck("password"));
        loginBtn.setText(Language.languageCheck("login"));

        loginConsoleText();
    }
}