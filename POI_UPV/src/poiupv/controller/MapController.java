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
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
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
    private Circle circlePainting;
    private Circle pointSelected;
    private double coordinateXCircle;
    private Color currentColor = Color.BLACK;
    private double currentThickness = 5;
    
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
    private ToggleButton drawCircle;
    @FXML
    private ToggleButton putText;
    @FXML
    private Slider thicknessSlider;
    @FXML
    private ToggleButton selectPoint;
    @FXML
    private ToggleButton removeButton;
    @FXML
    private MenuItem loginButton;
    @FXML
    private MenuItem signUpButton;
    @FXML
    private ToggleGroup drawingToolsMenu;

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
        map_scrollpane.setHvalue(0.315);
        map_scrollpane.setVvalue(0.1);
        
        
        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        thicknessSlider.setMin(1);
        thicknessSlider.setMax(10);
        thicknessSlider.setValue(5);
        thicknessSlider.valueProperty().addListener(l -> {
            currentThickness = thicknessSlider.getValue();
        });
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
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
        loginButton.setVisible(false);
        signUpButton.setVisible(false);
        logoutButton.setVisible(true);
        modifyButton.setVisible(true);
        welcomeLabel.textProperty().setValue("You are logged as: " + currentUser.getNickName());
        welcomeLabel.setVisible(true);
    }
    
    private void logoutSetup(){
        loginButton.setVisible(true);
        signUpButton.setVisible(true);
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
        Pane root = (Pane) myLoader.load();
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
        
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Security check");
        dialog.setHeaderText("For security reasons, confirm your actual password");
        //dialog.setGraphic(new Circle(15, Color.RED)); 
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField pwd = new PasswordField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter your actual password:"), pwd);
        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return pwd.getText();
            }
            return null;
        });
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            if(currentUser.checkPassword(result.get()))
            {
                Scene scene = new Scene (root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Modify profile");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setResizable(false);
                stage.show();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Security error");
                alert.setHeaderText("Password is not correct");
                alert.setContentText("Enter your actual password in order to modify your profile!");
                alert.initModality(Modality.WINDOW_MODAL);
                alert.showAndWait();
            }
        }
	
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
        System.out.println("Pressing...");
        System.out.println("Is drawLine pressed? " + drawLine.isSelected());
        System.out.println("Is drawCricle pressed? " + drawCircle.isSelected());
        System.out.println("Is putText pressed? " + putText.isSelected());
        if(drawLine.isSelected()){
            linePainting = new Line(event.getX(), event.getY(), event.getX(), event.getY());
            linePainting.setStroke(currentColor);
            linePainting.setStrokeWidth(currentThickness);
            zoomGroup.getChildren().add(linePainting);
            
        }else if(drawCircle.isSelected()){
            circlePainting = new Circle(1);
            circlePainting.setStroke(currentColor);
            circlePainting.setFill(Color.TRANSPARENT);
            circlePainting.setStrokeWidth(currentThickness);
            zoomGroup.getChildren().add(circlePainting);
            circlePainting.setCenterX(event.getX());
            circlePainting.setCenterY(event.getY());
            coordinateXCircle = event.getX();
        }else if(putText.isSelected()){
            TextField text = new TextField();
            zoomGroup.getChildren().add(text);
            text.setLayoutX(event.getX());
            text.setLayoutY(event.getY());
            text.requestFocus();
            System.out.println("Puting text?");
            text.setOnAction(e -> {
                Text test = new Text(text.getText());
                test.setX(text.getLayoutX());
                test.setY(text.getLayoutY());
                test.setStyle("-fx-font-family: Gafata; -fx-font-size: 40;");
                zoomGroup.getChildren().add(test);
                zoomGroup.getChildren().remove(text);
                e.consume();
            });
            
        }else if(selectPoint.isSelected()){
            pointSelected = new Circle(5);
            pointSelected.setStroke(currentColor);
            pointSelected.setStrokeWidth(currentThickness);
            zoomGroup.getChildren().add(pointSelected);
            pointSelected.setCenterX(event.getX());
            pointSelected.setCenterY(event.getY());
        } 
    }
    
    

    @FXML
    private void moveOrDrawReleased(MouseEvent event){
        System.out.println("Releasing...");
        if(drawLine.isSelected()){
            linePainting.setEndX(event.getX());
            linePainting.setEndY(event.getY());
            event.consume();
        }else if(drawCircle.isSelected()){
            double radius = Math.abs(event.getX() - coordinateXCircle);
            circlePainting.setRadius(radius);
            event.consume();
        }
    }

    @FXML
    private void handleDragOnMap(MouseEvent event){
        System.out.println("Dragging..."); 
        if(drawLine.isSelected()){
            linePainting.setEndX(event.getX());
            linePainting.setEndY(event.getY());
            event.consume();
        }else if(drawCircle.isSelected()){
            double radius = Math.abs(event.getX() - coordinateXCircle);
            circlePainting.setRadius(radius);
            event.consume();
        }
    }
    
    @FXML
    private void changeColor(ActionEvent event) {
        currentColor = colorPicker.getValue();
    }

    @FXML
    private void removePressed(ActionEvent event) {
        
    }

    @FXML
    private void clickTest(MouseEvent event) {
        if (removeButton.isSelected()) {
            zoomGroup.getChildren().remove(event.getSource());
        }
    }

}
