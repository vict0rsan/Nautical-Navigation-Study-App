/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Answer;
import model.Problem;

/**
 *
 * @author VicLo
 */
public class ProblemController implements Initializable{
    
    private Problem currentProblem;
    @FXML
    private Text problemText;
    @FXML
    private RadioButton answer1;
    @FXML
    private ToggleGroup answer;
    @FXML
    private RadioButton answer2;
    @FXML
    private RadioButton answer3;
    @FXML
    private RadioButton answer4;
    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void onActionSubmitButton(ActionEvent event) {
        List<Answer> answers = currentProblem.getAnswers();
        
        if(answer1.isSelected() && answers.get(0).getValidity()){
            markAsCorrect(answer1);
        }else if(answer2.isSelected() && answers.get(1).getValidity()){
            markAsCorrect( answer2);
        }else if(answer3.isSelected() && answers.get(2).getValidity()){
            markAsCorrect( answer3);
        }else if(answer4.isSelected() && answers.get(3).getValidity()){
            markAsCorrect( answer4);
        }else{
            markAsIncorrectAndFindCorrect(answers);
        }
        
        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);
        answer4.setDisable(true);
        submitButton.setDisable(true);
        cancelButton.requestFocus();
        
    }

    @FXML
    private void onActionCancelButton(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage oldStage = (Stage) source.getScene().getWindow();
        oldStage.close();
    }
    
    public void setProblem(Problem problem){
        this.currentProblem = problem;
        problemText.setText(problem.getText());
        
        List<Answer> answers = problem.getAnswers();
        List<Answer> mixedAnswers = new ArrayList(answers);
        Collections.shuffle(mixedAnswers);

        answer1.setText(mixedAnswers.get(0).getText());
        answer2.setText(mixedAnswers.get(1).getText());
        answer3.setText(mixedAnswers.get(2).getText());
        answer4.setText(mixedAnswers.get(3).getText());
    }
    
    private void markAsCorrect(RadioButton answer){
        MapController.currentSessionHints += 1;
        answer.setTextFill(Color.GREEN);
    }
    
    private void markAsIncorrectAndFindCorrect(List<Answer> answers){
        MapController.currentSessionFaults += 1;
        RadioButton selectedButton = (RadioButton) answer.getSelectedToggle();
        selectedButton.setTextFill(Color.RED);
        
         if(answers.get(0).getValidity()){
            answer1.setTextFill(Color.GREEN);
        }else if(answers.get(1).getValidity()){
            answer2.setTextFill(Color.GREEN);
        }else if(answers.get(2).getValidity()){
            answer3.setTextFill(Color.GREEN);
        }else if(answers.get(3).getValidity()){
            answer4.setTextFill(Color.GREEN);
        }
    }
    
}
