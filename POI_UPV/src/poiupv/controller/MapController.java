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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
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
import javafx.scene.robot.Robot;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
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
    private MouseEvent rulerPositionEvent;
    
    private Group zoomGroup;
    private Line linePainting = new Line();
    private Arc arcPainting;
    private Circle pointSelected;
    private double coordinateXCircle;
    private double coordinateYCircle;
    private Color currentColor = Color.BLACK;
    private double currentThickness = 5;
    
    public static LocalDateTime sessionInitialized;

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
    private ToggleButton removeButton;
    @FXML
    private MenuItem loginButton;
    @FXML
    private MenuItem signUpButton;
    @FXML
    private ToggleGroup drawingToolsMenu;
    @FXML
    private RadioMenuItem pointMenu;
    @FXML
    private RadioMenuItem arcMenu;
    @FXML
    private RadioMenuItem textMenu;
    @FXML
    private MenuItem colourMenu;
    @FXML
    private RadioMenuItem deleteMenu;
    @FXML
    private MenuItem clearMenu;
    @FXML
    private RadioMenuItem lineMenu;
    @FXML
    private Button clearButton;
    @FXML
    private Pane drawingPane;
    
    private List<Shape> list;
    private ObservableList<Shape> observableList; 
    @FXML
    private Menu problemsButton;
    @FXML
    private ToggleButton pannableButton;
    @FXML
    private ImageView rule;
    @FXML
    private ToggleButton ruleButton;
    @FXML
    private ToggleButton eraseButton;
	@FXML
	private RadioMenuItem dragMapMenu;
	@FXML
	private RadioMenuItem ruleMenu;

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
        colorPicker.setStyle("-fx-color-label-visible: false;");
        rule.setVisible(false);
        problemsButton.setDisable(true);
        problemsButton.setText("Problems (login to access)");
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        map_scrollpane.setHvalue(0.315);
        map_scrollpane.setVvalue(0.1);
        
        list = new ArrayList();
        observableList = FXCollections.observableArrayList(list);
        
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
        
        
        //zoomGroup.getChildren().forEach(this::makeDraggable);
        makeDraggable(rule);
        
        drawingTools.selectedToggleProperty().addListener(cl -> {
            if(pannableButton.isSelected())
                    map_scrollpane.setPannable(true);
            else
                    map_scrollpane.setPannable(false);
        });
        
        zoomGroup.setOnMousePressed(this:: moveOrDrawPressed);
        zoomGroup.setOnMouseDragged(this:: handleDragOnMap);
        
        ruleButton.setOnMouseClicked(mc -> {
            if(ruleButton.isSelected()){
                rule.setVisible(true);
                double x = rulerPositionEvent.getX();
                double y = rulerPositionEvent.getY();
                System.out.println(rulerPositionEvent.getX()+100);
                System.out.println(rulerPositionEvent.getY()-600);
                rule.setX(x+100);
                rule.setY(y-600);
        }else
            rule.setVisible(false);
            
        });
        
        pointMenu.selectedProperty().bindBidirectional(selectPoint.selectedProperty());
        lineMenu.selectedProperty().bindBidirectional(drawLine.selectedProperty());
        arcMenu.selectedProperty().bindBidirectional(drawCircle.selectedProperty());
        textMenu.selectedProperty().bindBidirectional(putText.selectedProperty());
        dragMapMenu.selectedProperty().bindBidirectional(pannableButton.selectedProperty());
        ruleMenu.selectedProperty().bindBidirectional(ruleButton.selectedProperty());
        deleteMenu.selectedProperty().bindBidirectional(eraseButton.selectedProperty());

        ruleMenu.setOnAction(this:: ruleButtonPressed);
    }

    
	
    @FXML
    private void muestraPosicion(MouseEvent event) {
        posicion.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
        rulerPositionEvent = event;
    }
    
    private void loginSetup () {
        loginButton.setVisible(false);
        signUpButton.setVisible(false);
        logoutButton.setVisible(true);
        modifyButton.setVisible(true);
        welcomeLabel.textProperty().setValue("You are logged as: " + currentUser.getNickName());
        welcomeLabel.setVisible(true);
        problemsButton.setText("Problems");
        problemsButton.setDisable(false);
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
        stage.getIcons().add(new Image("resources/helm.png"));
        stage.showAndWait();
        
        if(currentUser != null){
            sessionInitialized = LocalDateTime.now();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successfully logged in!");
            alert.setHeaderText(null);
            alert.setContentText(" Welcome back " + MapController.currentUser.getNickName() + "!");
            alert.initOwner(stage.getScene().getWindow());
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
        stage.setResizable(true);
        stage.setMinWidth(390);
        stage.setMinHeight(800);
        stage.getIcons().add(new Image("resources/helm.png"));
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
		alert.initOwner(map_scrollpane.getScene().getWindow());
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
        stage.getIcons().add(new Image("resources/helm.png"));
        stage.show();
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
            colorPicker.show();
            changeColor(event);
	}

	@FXML
	private void deleteMenuPressed(ActionEvent event) {
	}

	@FXML
	private void clearMenuPressed(ActionEvent event) {
		clearButtonPressed(event);
	}
        
	@FXML
	private void checkStatisticsPressed(ActionEvent event) throws Exception {


	   FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/CheckStatistics.fxml"));
	   BorderPane root = (BorderPane) myLoader.load();
	   CheckStatisticsController statisticsController = myLoader.<CheckStatisticsController>getController();

	   Scene scene = new Scene (root);
	   Stage stage = new Stage();
	   stage.setScene(scene);
	   stage.setTitle("Statistics");
	   stage.initModality(Modality.WINDOW_MODAL);
	   stage.setResizable(false);
           stage.getIcons().add(new Image("resources/helm.png"));
	   stage.show();


   }

	@FXML
	private void randomProblemPressed(ActionEvent event) throws Exception {
            
            
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
            stage.getIcons().add(new Image("resources/helm.png"));
            stage.show();
           
	}

	@FXML
	private void selectProblemPressed(ActionEvent event) throws Exception {
            
            
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/ShowProblems.fxml"));
            BorderPane root = (BorderPane) myLoader.load();
            ShowProblemsController showProblemsController = myLoader.<ShowProblemsController>getController();

            Scene scene = new Scene (root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Problems");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.getIcons().add(new Image("resources/helm.png"));
            stage.show();
           
	}

   
    @FXML
    private void moveOrDrawPressed(MouseEvent event) {
        
        if(drawLine.isSelected()){
            linePainting = new Line(event.getX(), event.getY(), event.getX(), event.getY());
            linePainting.setStroke(currentColor);
            linePainting.setStrokeWidth(currentThickness);
            
            linePainting.setOnMouseClicked(e -> {
                if(eraseButton.isSelected()){
                        Node src = (Node) e.getSource();
                        if(src != null)
                                src.setVisible(false);
                }
                else{
                    Node src = (Node) e.getSource();
                        if(src != null){
                            linePainting = (Line) src;
                            linePainting.setStroke(currentColor);
                            linePainting.setStrokeWidth(currentThickness);
                        }
                        
                }
            });
            
            zoomGroup.getChildren().add(linePainting);
            observableList.add(linePainting);
            event.consume();
        }
        else if(drawCircle.isSelected()){
            arcPainting = new Arc (0.0, 0.0, 0.0, 0.0, 0.0, 180.0);
			arcPainting.setStroke(currentColor);
			arcPainting.setFill(Color.TRANSPARENT);
			arcPainting.setStrokeWidth(currentThickness);
			
			arcPainting.setOnMouseClicked(e -> {
                if(eraseButton.isSelected()){
                        Node src = (Node) e.getSource();
                        if(src != null)
                                src.setVisible(false);
                }
                else{
                    Node src = (Node) e.getSource();
                        if(src != null){
                            arcPainting = (Arc) src;
                            arcPainting.setStroke(currentColor);
                            arcPainting.setStrokeWidth(currentThickness);
                        }
                        
                }
            });
			
			zoomGroup.getChildren().add(arcPainting);
            arcPainting.setCenterX(event.getX());
            arcPainting.setCenterY(event.getY());
            coordinateXCircle = event.getX();
            coordinateYCircle = event.getY();
            observableList.add(arcPainting);
            event.consume();
        }
        else if(putText.isSelected()){
            TextField text = new TextField();
            zoomGroup.getChildren().add(text);
            text.setLayoutX(event.getX());
            text.setLayoutY(event.getY());
            text.requestFocus();
            text.setOnAction(e -> {
                Text test = new Text(text.getText());
                test.setX(text.getLayoutX());
                test.setY(text.getLayoutY()+20);
                test.setStyle("-fx-font-size: 20;");
                test.setStroke(currentColor);
                
                test.setOnMouseClicked(mc -> {
                if(eraseButton.isSelected()){
                        Node src = (Node) mc.getSource();
                        if(src != null)
                                src.setVisible(false);
                }
                
            });
                
                zoomGroup.getChildren().add(test);
                zoomGroup.getChildren().remove(text);
                observableList.add(test);
            });
            event.consume();
            
        }else if(selectPoint.isSelected()){
            pointSelected = new Circle(6);
            pointSelected.setStroke(currentColor);
            pointSelected.setStrokeWidth(currentThickness);
            pointSelected.setFill(currentColor);
            
            pointSelected.setOnMouseClicked(e -> {
                if(eraseButton.isSelected()){
                        Node src = (Node) e.getSource();
                        if(src != null)
                                src.setVisible(false);
                }
                else{
                    Node src = (Node) e.getSource();
                        if(src != null){
                            pointSelected = (Circle) src;
                            pointSelected.setStroke(currentColor);
                            pointSelected.setStrokeWidth(currentThickness);
                        }
                        
                }
                
            });
            
            zoomGroup.getChildren().add(pointSelected);
            pointSelected.setCenterX(event.getX());
            pointSelected.setCenterY(event.getY());
            observableList.add(pointSelected);
            event.consume();
        } 
        
    }


    @FXML
    private void handleDragOnMap(MouseEvent event){
        if(drawLine.isSelected()){
            linePainting.setEndX(event.getX());
            linePainting.setEndY(event.getY());
            event.consume();
        }else if(drawCircle.isSelected()){
            double radiusX = Math.abs(event.getX() - coordinateXCircle);
            double radiusY = Math.abs(event.getY() - coordinateYCircle);
            arcPainting.setRadiusX(radiusX);
            arcPainting.setRadiusY(radiusY);
			arcPainting.setStartAngle(Math.atan2(event.getY(), event.getX())*100);
            event.consume();
        }
    }
    
    @FXML
    private void changeColor(ActionEvent event) {
        currentColor = colorPicker.getValue();
    }

    @FXML
    private void removePressed(ActionEvent event) {
        /*if(eraseButton.isPressed()){
            zoomGroup.getChildren().remove(event.getSource());
            observableList.remove(event.getSource());
        }*/
    }

    @FXML
    private void clearButtonPressed(ActionEvent event) {
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear confirmation");
        alert.setHeaderText("Are you sure you want to clear ALL the elements in the board?");
        alert.setContentText("If you press OK, all elements will be erased");
        alert.initOwner(drawLine.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
        {
        System.out.println(zoomGroup.getChildren());
        zoomGroup.getChildren().removeAll(observableList);
        observableList.clear();
        }
    }

    @FXML
    private void clickTest(MouseEvent event) {
    }

    @FXML
    private void ruleButtonPressed(ActionEvent event) {
       /* if(ruleButton.isSelected()){
            rule.setVisible(true);
            Robot robot = new Robot();
            Point2D point = robot.getMousePosition();
            

        double x = point.getX();
        double y = point.getX();
        rule.setX(map_scrollpane.getScene().getWindow().getWidth() + x);
        rule.setY(map_scrollpane.getScene().getWindow().getHeight() + y);

        }else
            rule.setVisible(false);*/
    }
    
    private double startX;
    private double startY;
    
    private void makeDraggable(ImageView node){
        node.setOnMousePressed(e ->{
            startX = e.getSceneX() - node.getTranslateX();
            startY = e.getSceneY() - node.getTranslateY();
        });
        
        node.setOnMouseDragged(e -> {
            node.setTranslateX(e.getSceneX() - startX);
            node.setTranslateY(e.getSceneY() - startY);
        });
    }
}
