/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    // private final HashMap<String, Poi> hm = new HashMap<>();
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    private MenuButton map_pin;
    private MenuItem pin_info;
    @FXML
    private Label posicion;
	@FXML
	private Pane buttonPane;
	
	private User currentUser;
	@FXML
	private MenuItem logoutButton;
	@FXML
	private MenuItem modifyButton;
	@FXML
	private Label welcomeLabel;

    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    void listClicked(MouseEvent event) {
        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();

        // Animación del scroll hasta la posicion del item seleccionado
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = itemSelected.getPosition().getX() / mapWidth;
        double scrollV = itemSelected.getPosition().getY() / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // movemos el objto map_pin hasta la posicion del POI
        double pinW = map_pin.getBoundsInLocal().getWidth();
        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX(itemSelected.getPosition().getX());
        map_pin.setLayoutY(itemSelected.getPosition().getY());
        pin_info.setText(itemSelected.getDescription());
        map_pin.setVisible(true);
    }

    /*private void initData() {
        hm.put("2F", new Poi("2F", "Edificion del DSIC", 325, 225));
        hm.put("Agora", new Poi("Agora", "Agora", 600, 360));
        map_listview.getItems().add(hm.get("2F"));
        map_listview.getItems().add(hm.get("Agora"));
    }*/

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //initData();
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.9);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
    }

	public void setUser (User user) {
		currentUser = user;
		logoutButton.setVisible(true);
		modifyButton.setVisible(true);
		welcomeLabel.textProperty().setValue("Welcome back, " + user.getNickName());
		welcomeLabel.setVisible(true);
	}
	
    @FXML
    private void muestraPosicion(MouseEvent event) {
        posicion.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }

    private void cerrarAplicacion(ActionEvent event) {
        ((Stage)zoom_slider.getScene().getWindow()).close();
    }

    private void acercaDe(ActionEvent event) {
        Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2022");
        mensaje.showAndWait();
    }

	@FXML
	private void handleOnActionLoginButton(ActionEvent event) throws IOException {
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
            stage.setTitle("Login");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
			
			setUser(detailsController.getUser());
	}

	@FXML
	private void handleOnActionSignUpButton(ActionEvent event) throws IOException {
		FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/FXMLSignUp.fxml"));

        Pane root = myLoader.load();

        FXMLSignUpController detailsController = myLoader.<FXMLSignUpController>getController();

        Scene scene = new Scene (root);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
	}

	@FXML
	private void handleOnActionLogoutButton(ActionEvent event) {
		currentUser = null;
		logoutButton.setVisible(false);
		modifyButton.setVisible(false);
		welcomeLabel.textProperty().setValue(null);
		welcomeLabel.setVisible(false);
		
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Logout");
		alert.setHeaderText(null);
		alert.setContentText("You have succesfully logged out.");
		alert.initModality(Modality.WINDOW_MODAL);
		alert.showAndWait();
	}

	@FXML
	private void handleOnActionModifyButton(ActionEvent event) throws IOException {
		FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/ModifyProfile.fxml"));
		BorderPane root = (BorderPane) myLoader.load();
		ModifyProfileController modifyProfileController = myLoader.<ModifyProfileController>getController();
        modifyProfileController.setUser(currentUser);
        
		Scene scene = new Scene (root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Modify profile");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		stage.show();
	}

	@FXML
	private void pointMenuPressed(ActionEvent event) {
	}

	@FXML
	private void lineMenuPressed(ActionEvent event) {
	}

	@FXML
	private void arcMenuPressed(ActionEvent event) {
	}

	@FXML
	private void textMenuPressed(ActionEvent event) {
	}

	@FXML
	private void colourMenuPressed(ActionEvent event) {
	}

	@FXML
	private void deleteMenuPressed(ActionEvent event) {
	}

	@FXML
	private void clearMenuPressed(ActionEvent event) {
	}

	@FXML
	private void randomProblemPressed(ActionEvent event) {
	}

	@FXML
	private void selectProblemPressed(ActionEvent event) {
	}


}
