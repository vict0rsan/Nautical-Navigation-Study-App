/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Navegacion;
import model.User;

/**
 *
 * @author VicLo
 */
public class ModifyProfileController 
{
    
    private BooleanProperty validPassword;
    private BooleanProperty validEmail;
    private BooleanProperty equalPasswords; 

    private Navegacion nav;
    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    @FXML
    private TextField username;
    @FXML
    private Text usernameErrorText;
    @FXML
    private TextField email;
    @FXML
    private Label lIncorrectEmail;
    @FXML
    private PasswordField pass;
    @FXML
    private Button passHelp;
    @FXML
    private Label lIncorrectPass;
    @FXML
    private PasswordField rpass;
    @FXML
    private Label lPassdontmatch;
    @FXML
    private DatePicker birthdate;
    @FXML
    private Label lBirthdate;
    @FXML
    private ImageView avatar;
    @FXML
    private Button selectAvatarButton;

    
    public void initialize(URL url, ResourceBundle rb) 
    {
        try {nav = Navegacion.getSingletonNavegacion();} 
        catch(Exception e){System.out.println(e.getMessage());}
       
        validEmail = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();   
        equalPasswords = new SimpleBooleanProperty();
        
        validPassword.setValue(Boolean.FALSE);
        validEmail.setValue(Boolean.FALSE);
        equalPasswords.setValue(Boolean.FALSE);
        
       
        email.focusedProperty().addListener((observable, oldValue, newValue)->{
	    if(!newValue){ //focus lost.
		checkEditEmail(); }
            });
        
        pass.focusedProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue) 
            {
                checkEditPass();
            }
        });
        
        rpass.focusedProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue)
            {
                checkEqualPass();
            }
        });

        
        BooleanBinding validFields = Bindings.and(validEmail, validPassword)
                 .and(equalPasswords);
        
        bAccept.disableProperty().bind(Bindings.not(validFields));
    }
    
     private void manageError(Label errorLabel,TextField textField, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.FALSE);
        showErrorMessage(errorLabel,textField);
        textField.requestFocus();
    }
    
    private void manageError(Text errorText,TextField textField, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.FALSE);
        showErrorMessage(errorText,textField);
        textField.requestFocus();
    }
    /**
     * Updates the boolProp to true. Changes the background 
     * of the edit to the default value. Makes the error label invisible. 
     * @param errorLabel label added to alert the user
     * @param textField edit text added to allow user to introduce the value
     * @param boolProp property which stores if the value is correct or not
     */
    private void manageCorrect(Label errorLabel,TextField textField, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.TRUE);
        hideErrorMessage(errorLabel,textField); 
    }
    
    private void manageCorrect(Text errorText,TextField textField, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.TRUE);
        hideErrorMessage(errorText,textField);
    }
    
    private void manageCorrect(Label errorLabel,DatePicker datePicker, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.TRUE);
        hideErrorMessage(errorLabel,datePicker); 
    }
    /**
     * Changes to red the background of the edit and
     * makes the error label visible
     * @param errorLabel
     * @param textField 
     */
    private void showErrorMessage(Label errorLabel,TextField textField)
    {
        errorLabel.visibleProperty().set(true);
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");    
    }
    
    private void showErrorMessage(Text errorText,TextField textField)
    {
        errorText.visibleProperty().set(true);
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");    
    }
    
    private void showErrorMessage(Label errorLabel,DatePicker datepicker)
    {
        errorLabel.visibleProperty().set(true);
        datepicker.styleProperty().setValue("-fx-background-color: #FCE5E0");    
    }
    /**
     * Changes the background of the edit to the default value
     * and makes the error label invisible.
     * @param errorLabel
     * @param textField 
     */
    private void hideErrorMessage(Label errorLabel,TextField textField)
    {
        errorLabel.visibleProperty().set(false);
        textField.styleProperty().setValue("");
    }

    private void hideErrorMessage(Text errorText,TextField textField)
    {
        errorText.visibleProperty().set(false);
        textField.styleProperty().setValue("");
    }
    
    private void hideErrorMessage(Label errorLabel,DatePicker datePicker)
    {
        errorLabel.visibleProperty().set(false);
        datePicker.styleProperty().setValue("");
    }
    
     private void checkEditEmail()
    {
        if(!User.checkEmail(email.textProperty().getValueSafe())) 
            manageError(lIncorrectEmail, email,validEmail );
        else
            manageCorrect(lIncorrectEmail, email,validEmail );
    }
    
    private void checkEditPass()
    {
        if(!User.checkPassword(pass.textProperty().getValueSafe())) {
		lIncorrectPass.setText("Incorrect password.");
		manageError(lIncorrectPass, pass, validPassword);
	} else {
            manageCorrect(lIncorrectPass, pass, validPassword);
	}
    }
    
    private void checkEqualPass()
    {
        if(pass.textProperty().getValueSafe().compareTo(rpass.textProperty().getValueSafe()) != 0)
        {
            showErrorMessage(lPassdontmatch, rpass);
            equalPasswords.setValue(Boolean.FALSE);
            rpass.textProperty().setValue("");
            pass.requestFocus();
        }
        else manageCorrect(lPassdontmatch, rpass, equalPasswords);
    }

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) {
    }

    @FXML
    private void handleButtonCancelOnAction(ActionEvent event) {
    }

    @FXML
    private void handleBPassHelpPressed(ActionEvent event) {
    }

    @FXML
    private void handleSelectAvatarButton(ActionEvent event) {
    }
        
        
        
}
