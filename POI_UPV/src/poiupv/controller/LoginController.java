/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv.controller;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Navegacion;
import model.User;

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
    private TextField username;
    @FXML
    private Label incorrectEmail;
    @FXML
    private PasswordField password;
    @FXML
    private Label incorrectPassword;
    
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
        
        username.focusedProperty().addListener((observable, oldValue, newValue)->{
            if (username.textProperty().getValue().isBlank()) {
                validEmail.setValue(Boolean.TRUE);
            }
        });
        
        password.focusedProperty().addListener((observable, oldValue, newValue)->{
            if (!password.textProperty().getValue().isBlank()) {
                validpass.setValue(Boolean.TRUE);
            }
        });
        
        BooleanBinding validFields = Bindings.and(validEmail, validpass);
        bAccept.disableProperty().bind(Bindings.not(validFields));
    }    

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) throws NavegacionDAOException, IOException {
        Navegacion database = Navegacion.getSingletonNavegacion();
        String currentNickname = username.getText();
        Boolean existsNickName = database.exitsNickName(currentNickname);
        
        if(!existsNickName){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("LOGIN ERROR: ");
            alert.setHeaderText("LOGIN FAILED BECAUSE: ");
            alert.setContentText("User " + currentNickname + " does not exist");
            alert.initModality(Modality.WINDOW_MODAL);
            alert.showAndWait();
            clearFields();
            return;
        }
        
        User user = database.loginUser(currentNickname, password.getText());
        
        if(user == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("LOGIN ERROR: ");
            alert.setHeaderText("LOGIN FAILED BECAUSE: ");
            alert.setContentText("User and password do not match");
            alert.initModality(Modality.WINDOW_MODAL);
            alert.showAndWait();
            clearFields();
            return;
        }
        
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/FunctionSelector.fxml"));
	SplitPane root = (SplitPane) myLoader.load();
	FunctionSelectorController detailsController = myLoader.<FunctionSelectorController>getController();
        
	Scene scene = new Scene (root);
	Stage stage = new Stage();
	stage.setScene(scene);
	stage.setTitle("MAIN MENU");
	stage.initModality(Modality.APPLICATION_MODAL);
	stage.setResizable(false);
	stage.showAndWait();
    }
    
    private void clearFields(){
        username.setText("");
        password.setText("");
        validEmail.setValue(Boolean.FALSE);
        validpass.setValue(Boolean.FALSE);
        username.requestFocus();
    }
    
}
