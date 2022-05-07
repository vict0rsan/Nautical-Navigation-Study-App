/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv.controller;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.binding.*;
import javafx.beans.property.*;
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
public class LoginController implements Initializable {

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

    private BooleanProperty validEmail;
    private BooleanProperty validpass;
            
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        bCancel.setOnAction( (event) -> {
            bCancel.getScene().getWindow().hide();
        });
        
        validEmail = new SimpleBooleanProperty();
        validpass = new SimpleBooleanProperty();
        
        validEmail.setValue(Boolean.FALSE);
        validpass.setValue(Boolean.FALSE);
        
        email.focusedProperty().addListener((observable, oldValue, newValue)->{
            if (email.textProperty().getValue() != "") {
                validEmail.setValue(Boolean.TRUE);
            }
        });
        
        pass.focusedProperty().addListener((observable, oldValue, newValue)->{
            if (pass.textProperty().getValue() != "") {
                validpass.setValue(Boolean.TRUE);
            }
        });
        
        BooleanBinding validFields = Bindings.and(validEmail, validpass);
        bAccept.disableProperty().bind(Bindings.not(validFields));
    }    

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) {
    }
    
}
