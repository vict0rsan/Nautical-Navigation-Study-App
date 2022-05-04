/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author VicLo
 */
public class FXMLSignUpController implements Initializable {

    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    @FXML
    private TextField email;
    @FXML
    private Label lIncorrectEmail;
    @FXML
    private PasswordField pass;
    @FXML
    private Label lIncorrectPass;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) {
    }

    @FXML
    private void handleBCancelOnAction(ActionEvent event) {
    }
    
}
