/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv.controller;

import DBAccess.NavegacionDAOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Navegacion;
import model.Problem;

/**
 *
 * @author VicLo
 */
public class ShowProblemsController implements Initializable {

    @FXML
    private ListView<Problem> shownProblems;
    
    private ObservableList<Problem> problemsToShow;
    
    private List<Problem> problems;
    
    @FXML
    private Label problemDescription;
    @FXML
    private ImageView avatar;
    @FXML
    private Text user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            Navegacion database = Navegacion.getSingletonNavegacion();
            problems = database.getProblems();
            problemsToShow = FXCollections.observableArrayList(problems);
            shownProblems.setItems(problemsToShow);
            shownProblems.setCellFactory(c -> new ProblemListCell());
        } catch (NavegacionDAOException ex) {
        }
        
        avatar.setImage(MapController.currentUser.getAvatar());
        user.setText(MapController.currentUser.getNickName());
        
        
        shownProblems.getSelectionModel().selectedIndexProperty().
                addListener((o, oldVal, newVal) -> {
                    if(newVal.intValue() == -1){
                        problemDescription.setText("");
                    }else{
                        Problem selectedProblem = shownProblems.getSelectionModel().getSelectedItem();
                        problemDescription.setText(selectedProblem.getText());
                    }
                });
    }

    @FXML
    private void handleSelectProblemButton(ActionEvent event) throws Exception {
        
        if(shownProblems.getSelectionModel().getSelectedIndex() != -1){
            Problem selectedProblem = shownProblems.getSelectionModel().getSelectedItem();
            
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/Problem.fxml"));
            Parent root = (Parent) myLoader.load();
            ProblemController problemController = myLoader.<ProblemController>getController();
            problemController.setProblem(selectedProblem);
            
            Scene scene = new Scene (root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Problem nº " + shownProblems.getSelectionModel().getSelectedIndex());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    private void handleSelectRandomProblemButton(ActionEvent event) throws Exception {
        
        Random random = new Random();
        int randomIndexProblem = random.nextInt(problems.size());
        Problem randomProblem = problemsToShow.get(randomIndexProblem);
        
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
        
    }
    
    
    
    class ProblemListCell extends ListCell<Problem> {
    
    @Override
    protected void updateItem(Problem problem, boolean empty){
        super.updateItem(problem, empty);
        if(problem == null || empty){
            
        }else{
            setText("Problem  -  " + problem.getText().substring(0, 35) + "...");
        }
            
        
    }
    
    }
    
}
