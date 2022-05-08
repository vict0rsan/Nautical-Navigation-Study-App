/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv.controller;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import model.Navegacion;
import model.User;


/**
 *
 * @author svalero
 */
public class FXMLSignUpController implements Initializable {


 
    //properties to control valid fieds values. 
    private BooleanProperty validPassword;
    private BooleanProperty validEmail;
    private BooleanProperty equalPasswords;  
    private BooleanProperty validUsername;
    private BooleanProperty validBirthdate;
    
    //When to strings are equal, compareTo returns zero
    private final int EQUALS = 0;  
    @FXML
    private TextField email;
    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField rpass;
    @FXML
    private Label lIncorrectEmail;
    @FXML
    private Label lIncorrectPass;
    @FXML
    private Label lPassdontmatch;
    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    @FXML
    private TextField username;
    @FXML
    private Text usernameErrorText;
    private Navegacion nav;
    @FXML
    private DatePicker birthdate;
    @FXML
    private ImageView avatar;
    @FXML
    private Button selectAvatarButton;
    @FXML
    private Label lBirthdate;
	@FXML
	private Button passHelp;
    
   
    

    /**
     * Updates the boolProp to false.Changes to red the background of the edit. 
     * Makes the error label visible and sends the focus to the edit. 
     * @param errorLabel label added to alert the user
     * @param textField edit text added to allow user to introduce the value
     * @param boolProp property which stores if the value is correct or not
     */
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
    
    //=========================================================
    // you must initialize here all related with the object 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {nav = Navegacion.getSingletonNavegacion();} 
        catch(Exception e){System.out.println(e.getMessage());}
       
        validEmail = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();   
        equalPasswords = new SimpleBooleanProperty();
        validUsername = new SimpleBooleanProperty();
		validBirthdate = new SimpleBooleanProperty();
        
        validPassword.setValue(Boolean.FALSE);
        validEmail.setValue(Boolean.FALSE);
        equalPasswords.setValue(Boolean.FALSE);
        validUsername.setValue(Boolean.FALSE);
		validBirthdate.setValue(Boolean.FALSE);
        
       
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
        
        username.focusedProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue) 
            {
                checkUsername();
            }
        });
        
        rpass.focusedProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue)
            {
                checkEqualPass();
            }
        });
	
		birthdate.focusedProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue)
            {
                checkBirthdate();
            }
        });
        
        BooleanBinding validFields = Bindings.and(validEmail, validPassword)
                 .and(equalPasswords).and(validUsername);
        
        bAccept.disableProperty().bind(Bindings.not(validFields));
        bCancel.setOnAction( (event) -> {
            bCancel.getScene().getWindow().hide();
        });
    } ;
    
    private void checkUsername()
    {
        if(!User.checkNickName(username.textProperty().getValueSafe()))
        {
            usernameErrorText.textProperty().setValue("Incorrect username. A nickname is " +
                    "valid if it is between 6 and 15 characters long and " +
                    "contains uppercase or lowercase letters or the " +
                    "hyphens '-' and '_' .");
            manageError(usernameErrorText, username, validUsername);
        }
        else if(nav.exitsNickName(username.textProperty().getValueSafe()))
        {
            usernameErrorText.textProperty().setValue("Username already exists.");
            manageError(usernameErrorText, username, validUsername);        }
        else
        {
            usernameErrorText.textProperty().setValue("");
            manageCorrect(usernameErrorText, username, validUsername);
        }
            
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
        if(pass.textProperty().getValueSafe().compareTo(rpass.textProperty().getValueSafe()) != EQUALS)
        {
            showErrorMessage(lPassdontmatch, rpass);
            equalPasswords.setValue(Boolean.FALSE);
            rpass.textProperty().setValue("");
            pass.requestFocus();
        }
        else manageCorrect(lPassdontmatch, rpass, equalPasswords);
    }
    
    private void checkBirthdate() {
	if (Period.between(birthdate.getValue(), LocalDate.now()).getYears() < 12 || 
	    Period.between(birthdate.getValue(), LocalDate.now()).getYears() > 100) {
	    showErrorMessage(lBirthdate, birthdate);
	    validBirthdate.setValue(Boolean.FALSE);
            birthdate.setValue(null);
            birthdate.requestFocus();
	}
	else manageCorrect(lBirthdate, birthdate, validBirthdate);
    }

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) throws Exception
    {
        try{nav.registerUser(username.textProperty().getValue(), email.textProperty().getValue(), pass.textProperty().getValue(), birthdate.getValue());}
        catch(Exception e){ System.out.println(e.getMessage());}
        email.textProperty().setValue("");
        pass.textProperty().setValue("");
        rpass.textProperty().setValue("");
        username.textProperty().setValue("");
		birthdate.setValue(null);
        
        
        validEmail.setValue(Boolean.FALSE);
        validPassword.setValue(Boolean.FALSE);
        equalPasswords.setValue(Boolean.FALSE);
        validUsername.setValue(Boolean.FALSE);
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Registration confirmed");
		alert.setHeaderText(null);
		alert.setContentText("You have been succesfully registered!");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/FXMLLogin.fxml"));

			BorderPane root = (BorderPane) myLoader.load();

				//Get the controller of the UI
			LoginController detailsController = myLoader.<LoginController>getController();
				//We pass the data to the cotroller. Passing the observableList we 
				//give controll to the modal for deleting/adding/modify the data 
				//we see in the listView

			Scene scene = new Scene (root);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Function selector");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.showAndWait();
			
			Node source = (Node) event.getSource();
			Stage oldStage = (Stage) source.getScene().getWindow();
			oldStage.close();
		}		
    }

    @FXML
    private void handleSelectAvatarButton(ActionEvent event) throws IOException
    {
        
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/AvatarSelector.fxml"));

        Pane root = (Pane) myLoader.load();

            //Get the controller of the UI
        AvatarSelectorController avatarController = myLoader.<AvatarSelectorController>getController();
            //We pass the data to the cotroller. Passing the observableList we 
            //give controll to the modal for deleting/adding/modify the data 
            //we see in the listView

        Scene scene = new Scene (root);
        avatarController.initData(avatar);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Avatar Selector");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    private void handleBPassHelpPressed(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Password help");
		alert.setHeaderText("A password is correct if:");
		alert.setContentText(
			"- contains between 8 and 20 characters\n" + 
			"- contains at least one upper case letter\n" +
			"- contains at least one lower case letter\n" + 
			"- contains at least one digit" +
			"- contains a special character from the set: !@#$%&*&*()-+=\n" +
			"- does not contain any blank spaces"
		);
		alert.initModality(Modality.WINDOW_MODAL);
		alert.showAndWait();
	}
}