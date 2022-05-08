/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML
    private Label welcomeMessage;
    
    private User currentUser;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
                if(currentUser != null)
                    welcomeMessage.setText("Welcome back\n" + currentUser.getNickName());
                
	}
        
        
        
        public void setUser(User user){
            this.currentUser = user;
        }
        
      
}
	

