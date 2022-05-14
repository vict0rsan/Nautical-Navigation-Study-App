package poiupv.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Navegacion;
import model.User;

/**
 *
 * @author VicLo
 */
public class ModifyProfileController implements Initializable
{
    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    @FXML
    private TextField email;
    @FXML
    private Label incorrectEmail;
    @FXML
    private PasswordField password;
    @FXML
    private Button passwordHelpButton;
    @FXML
    private Label incorrectPassword;
    @FXML
    private PasswordField passwordConfirmation;
    @FXML
    private Label incorrectPasswordConfirmation;
    @FXML
    private DatePicker birthdate;
    @FXML
    private Label incorrectBirthday;
    @FXML
    private ImageView avatar;
    @FXML
    private Button selectAvatarButton;
    @FXML
    private TextField username;
    
    //properties to control valid fieds values. 
    private BooleanProperty validPassword;
    private BooleanProperty validEmail;
    private BooleanProperty equalPasswords;  
    
    //When to strings are equal, compareTo returns zero
    private final int EQUALS = 0; 
    @FXML
    private Button selectAvatarFromDeviceButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validEmail = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();   
        equalPasswords = new SimpleBooleanProperty();
        
        validEmail.setValue(Boolean.TRUE);
        validPassword.setValue(Boolean.FALSE);
        equalPasswords.setValue(Boolean.FALSE);
       
        email.focusedProperty().addListener((observable, oldValue, newValue)->{
	    if(!newValue) //focus lost.
		checkEditEmail(); 
            });
        
        password.focusedProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue) 
                checkEditPass();
        });
      
        passwordConfirmation.focusedProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue)
                checkEqualPass();
        });
 
        BooleanBinding validFields = Bindings.and(validEmail, validPassword)
                 .and(equalPasswords);
        
        bAccept.disableProperty().bind(Bindings.not(validFields));
        
        username.setDisable(true);
        birthdate.setDisable(true);
        username.textProperty().setValue(MapController.currentUser.getNickName());
        birthdate.setValue(MapController.currentUser.getBirthdate());
        avatar.imageProperty().setValue(MapController.currentUser.getAvatar());
        email.textProperty().setValue(MapController.currentUser.getEmail());
    }
    
     private void checkEditEmail(){
        if(!User.checkEmail(email.textProperty().getValueSafe())) 
            manageError(incorrectEmail, email,validEmail );
        else
            manageCorrect(incorrectEmail, email,validEmail );
    }
    
    private void checkEditPass(){
        if(!User.checkPassword(password.textProperty().getValueSafe())) {
		incorrectPassword.setText("Not a valid password");
		manageError(incorrectPassword, password, validPassword);
	} else {
            manageCorrect(incorrectPassword, password, validPassword);
	}
    }
    
    private void checkEqualPass(){
        if(password.textProperty().getValueSafe().compareTo(passwordConfirmation.textProperty().getValueSafe()) != EQUALS)
        {
            showErrorMessage(incorrectPasswordConfirmation, passwordConfirmation);
            equalPasswords.setValue(Boolean.FALSE);
            passwordConfirmation.textProperty().setValue("");
            password.requestFocus();
        }
        else manageCorrect(incorrectPasswordConfirmation, passwordConfirmation, equalPasswords);
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
   
    private void showErrorMessage(Label errorLabel,TextField textField){
        errorLabel.visibleProperty().set(true);
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");    
    }
    
    private void showErrorMessage(Text errorText,TextField textField){
        errorText.visibleProperty().set(true);
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");    
    }
    
    private void showErrorMessage(Label errorLabel,DatePicker datepicker){
        errorLabel.visibleProperty().set(true);
        datepicker.styleProperty().setValue("-fx-background-color: #FCE5E0");    
    }
  
    private void hideErrorMessage(Label errorLabel,TextField textField){
        errorLabel.visibleProperty().set(false);
        textField.styleProperty().setValue("");
    }

    private void hideErrorMessage(Text errorText,TextField textField){
        errorText.visibleProperty().set(false);
        textField.styleProperty().setValue("");
    }
    
    private void hideErrorMessage(Label errorLabel,DatePicker datePicker){
        errorLabel.visibleProperty().set(false);
        datePicker.styleProperty().setValue("");
    }

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) throws Exception {
        
        MapController.currentUser.setEmail(email.textProperty().getValue());
        MapController.currentUser.setPassword(password.textProperty().getValue());
        MapController.currentUser.setAvatar(avatar.getImage());
        	
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CHANGES DONE");
        alert.setHeaderText(null);
        alert.setContentText("YouR data has been succesfully updated!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Node source = (Node) event.getSource();
            Stage oldStage = (Stage) source.getScene().getWindow();
            oldStage.close();
        }		
    }

    @FXML
    private void handleButtonCancelOnAction(ActionEvent event) throws IOException{
        Node source = (Node) event.getSource();
        Stage oldStage = (Stage) source.getScene().getWindow();
	oldStage.close();
    }

    @FXML
    private void handleBPassHelpPressed(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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

    @FXML
    private void handleSelectAvatarButton(ActionEvent event) throws IOException {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/AvatarSelector.fxml"));
        Pane root = (Pane) myLoader.load();
        AvatarSelectorController avatarController = myLoader.<AvatarSelectorController>getController();

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
    private void handleSelectAvatarFromDeviceButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Imagen");

        // Agregar filtros para facilitar la busqueda
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        // Obtener la imagen seleccionada
        File imgFile = fileChooser.showOpenDialog(null);

        // Mostar la imagen
        if (imgFile != null) {
            Image image = new Image("file:" + imgFile.getAbsolutePath());
            avatar.setImage(image);
        }
    }
}
