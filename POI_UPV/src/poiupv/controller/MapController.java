/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv.controller;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Navegacion;
import model.Problem;
import model.Session;
import model.User;

/**
 *
 * @author jsoler
 */
public class MapController implements Initializable {
    
    public static User currentUser;
    public static int currentSessionHints;
    public static int currentSessionFaults;
    
    private Group zoomGroup;
    private Line linePainting;
    private Color currentColor = Color.BLACK;
    
    private LocalDateTime sessionInitialized;

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
    @FXML
    private MenuItem logoutButton;
    @FXML
    private MenuItem modifyButton;
    @FXML
    private Label welcomeLabel;
    @FXML
    private ToggleButton drawLine;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ToggleGroup drawingTools;

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
        
//        map_scrollpane.setOnDragDetected(event -> {
//            System.out.println("Dragging");
//            linePainting.setEndX(event.getX());
//            linePainting.setEndY(event.getY());
//            event.consume();
//        });
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
    
    
    private void loginSetup () {
        logoutButton.setVisible(true);
        modifyButton.setVisible(true);
        welcomeLabel.textProperty().setValue("You are logged as: " + currentUser.getNickName());
        welcomeLabel.setVisible(true);
    }
    
    private void logoutSetup(){
        currentSessionHints = 0;
        currentSessionFaults = 0;
        currentUser = null;
        sessionInitialized = null;
        logoutButton.setVisible(false);
	modifyButton.setVisible(false);
	welcomeLabel.textProperty().setValue(null);
	welcomeLabel.setVisible(false);
    }

    @FXML
    private void handleOnActionLoginButton(ActionEvent event) throws IOException {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/FXMLLogin.fxml"));
        BorderPane root = (BorderPane) myLoader.load();
        LoginController detailsController = myLoader.<LoginController>getController();
        Scene scene = new Scene (root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
        
        if(currentUser != null){
            sessionInitialized = LocalDateTime.now();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successfully logged in!");
            alert.setHeaderText(null);
            alert.setContentText(" Welcome back " + MapController.currentUser.getNickName() + "!");
            alert.showAndWait();
            loginSetup();
        }
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
    private void handleOnActionLogoutButton(ActionEvent event) throws NavegacionDAOException {
        
        Session currrentSession = new Session(sessionInitialized, currentSessionHints ,currentSessionFaults);
        currentUser.addSession(currrentSession);
        
        logoutSetup();
	
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
        private void checkStatisticsPressed(ActionEvent event) throws Exception {
            
             if(currentUser != null){
                FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/CheckStatistics.fxml"));
                BorderPane root = (BorderPane) myLoader.load();
                CheckStatisticsController statisticsController = myLoader.<CheckStatisticsController>getController();

                Scene scene = new Scene (root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Statistics");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setResizable(false);
                stage.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login required");
                alert.setHeaderText("LOG IN TO PERFORM THIS OPERATION");
                alert.setContentText("You must be logged in to check your statistics");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }
            
        }

	@FXML
	private void randomProblemPressed(ActionEvent event) throws Exception {
            
            if(currentUser != null){
                Random random = new Random();
                List<Problem> problems = Navegacion.getSingletonNavegacion().getProblems();
                int randomIndexProblem = random.nextInt(problems.size());
                Problem randomProblem = problems.get(randomIndexProblem);
        
                FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/Problem.fxml"));
                Parent root = (Parent) myLoader.load();
                ProblemController problemController = myLoader.<ProblemController>getController();
                problemController.setProblem(randomProblem);

                Scene scene = new Scene (root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Random problem");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setResizable(false);
                stage.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login required");
                alert.setHeaderText("LOG IN TO PERFORM THIS OPERATION");
                alert.setContentText("You must be logged in to make problems");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }
	}

	@FXML
	private void selectProblemPressed(ActionEvent event) throws Exception {
            
            if(currentUser != null){
                FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/ShowProblems.fxml"));
                BorderPane root = (BorderPane) myLoader.load();
                ShowProblemsController showProblemsController = myLoader.<ShowProblemsController>getController();

                Scene scene = new Scene (root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Problems");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setResizable(false);
                stage.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login required");
                alert.setHeaderText("LOG IN TO PERFORM THIS OPERATION");
                alert.setContentText("You must be logged in to make problems");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }
	}

   
    @FXML
    private void moveOrDrawPressed(MouseEvent event) {
        System.out.println("Hi mr. pressed!");
        System.out.println("Is draw line pressed? " + drawLine.isSelected());
        if(drawLine.isSelected())
        {
            linePainting = new Line(event.getX(), event.getY(), event.getX(), event.getY());
            zoomGroup.getChildren().add(linePainting);
            linePainting.setStroke(currentColor);
            linePainting.setOnContextMenuRequested(eventContext -> 
            {
                ContextMenu menuContext = new ContextMenu();
                MenuItem deleteItem = new MenuItem("Delete");
                menuContext.getItems().add(deleteItem);
                //If the user selects the option, we delete the line
                deleteItem.setOnAction( eventMenu ->
                {
                    zoomGroup.getChildren().remove((Node)eventContext.getSource());
                    eventMenu.consume();
                });
                menuContext.show(linePainting, event.getSceneX(), event.getSceneY());
                eventContext.consume();
            });
            System.out.println(linePainting);
        }
    }
    
    @FXML
    private void changeColor(ActionEvent event) {
        currentColor = colorPicker.getValue();
    }

    @FXML
    private void moveOrDrawReleased(MouseEvent event) 
    {
        System.out.println("Hi mr. released!");
        if(drawLine.isSelected())
        {
            linePainting.setEndX(event.getX());
            linePainting.setEndY(event.getY());
            event.consume();
            System.out.println(linePainting);
        }
         
    }

    @FXML
    private void handleDragOnMap(MouseEvent event) 
    {
        System.out.println("Dragging"); 
        if(drawLine.isSelected())
        {
            linePainting.setEndX(event.getX());
            linePainting.setEndY(event.getY());
            event.consume();
        }
    }

   

   

}
