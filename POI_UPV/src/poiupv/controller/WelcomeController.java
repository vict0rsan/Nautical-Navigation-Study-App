/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author VicLo
 */
public class WelcomeController {

    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;

    @FXML
    private void handleLoginButton(ActionEvent event) throws Exception{
        
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/FXMLLogin.fxml"));

        Pane root = (Pane) myLoader.load();

            //Get the controller of the UI
        LoginController detailsController = myLoader.<LoginController>getController();
            //We pass the data to the cotroller. Passing the observableList we 
            //give controll to the modal for deleting/adding/modify the data 
            //we see in the listView

       Scene scene = new Scene (root);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
        
    }

    @FXML
    private void handleSignUpButton(ActionEvent event) throws Exception{
        
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/FXMLSignUp.fxml"));

        Pane root = myLoader.load();

            //Get the controller of the UI
        FXMLSignUpController detailsController = myLoader.<FXMLSignUpController>getController();
            //We pass the data to the cotroller. Passing the observableList we 
            //give controll to the modal for deleting/adding/modify the data 
            //we see in the listView

       Scene scene = new Scene (root);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }
    
}
