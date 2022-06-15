package poiupv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import DBAccess.NavegacionDAOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Session;
import poiupv.controller.MapController;
import static poiupv.controller.MapController.currentSessionFaults;
import static poiupv.controller.MapController.currentSessionHints;
import static poiupv.controller.MapController.currentUser;

/**
 *
 * @author jose
 */
public class PoiUPVApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/poiupv/view/Map.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("NavigApp");
        stage.setScene(scene);
        stage.setMinHeight(850);
        stage.setMinWidth(900);
        stage.getIcons().add(new Image("resources/helm.png"));
        stage.setOnCloseRequest(e -> {
            
            if(MapController.currentUser != null){
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("You haven't logged out");
                alert.setHeaderText("Unsaved session");
                alert.setContentText("There could be unrecorded scores.\nDo you want to exit without logging out?");
                alert.initModality(Modality.APPLICATION_MODAL);
               
                ButtonType acceptButton = new ButtonType("Confirm");
                ButtonType logoutButton = new ButtonType("Logout and exit");
                
                alert.getButtonTypes().setAll(acceptButton, logoutButton);
                
                Optional<ButtonType> result = alert.showAndWait();
                
                if(result.isPresent()){
                    ButtonType choosenButton = result.get();
                    
                    if(choosenButton == acceptButton){
                        System.exit(0);
                    }
                    else if(choosenButton == logoutButton){
                        Session currentSession = new Session(MapController.sessionInitialized, MapController.currentSessionHints ,MapController.currentSessionFaults);
                        try{
                        MapController.currentUser.addSession(currentSession);
                        System.exit(0);
                        }catch(NavegacionDAOException ex){
                            System.out.println(ex.getStackTrace());
                        }
                        
                    }
                }
                
            }else{
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit confirmation");
                alert.setHeaderText("All map changes will be lost");
                alert.setContentText("Do you want to exit?");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initOwner(stage.getScene().getWindow());
                
                ButtonType acceptButton = new ButtonType("Accept");
                ButtonType cancelButton = new ButtonType("Cancel");
                
                alert.getButtonTypes().setAll(acceptButton, cancelButton);
                
                Optional<ButtonType> result = alert.showAndWait();
                
                if(result.isPresent()){
                    ButtonType choosenButton = result.get();
                    
                    if(choosenButton == acceptButton){
                        System.exit(0);
                    }
                    else if(choosenButton == cancelButton){
                        e.consume();
                    }
                }

            }
                
            
        });
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
