/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.User;

/**
 * FXML Controller class
 *
 * @author nash1
 */
public class FunctionSelectorController implements Initializable {

    @FXML
    private Button logoutButton;
    
    private User currentUser;
    @FXML
    private Text welcomeText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }



    public void setUser(User user){
        currentUser = user;
        if(currentUser != null)
            welcomeText.textProperty().setValue("Welcome back\n" + currentUser.getNickName());            
    }   

    @FXML
    private void handleOnActionLogoutButton(ActionEvent event) throws IOException {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/FMXLLogin.fxml"));
	BorderPane root = (BorderPane) myLoader.load();
	//LoginController loginController = myLoader.<LoginController>getController();
        
	Scene scene = new Scene (root);
	Stage stage = new Stage();
	stage.setScene(scene);
	stage.setTitle("Login");
	stage.initModality(Modality.APPLICATION_MODAL);
	stage.setResizable(false);
	stage.show();
        Node source = (Node) event.getSource();
        Stage oldStage = (Stage) source.getScene().getWindow();
	oldStage.close();
    }
        
      
}
	

