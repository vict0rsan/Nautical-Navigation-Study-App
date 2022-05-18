/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import model.Session;

/**
 * FXML Controller class
 *
 * @author nash1
 */
public class GeneralStatisticsController implements Initializable {

    @FXML
    private Label sessionLabel;
    @FXML
    private Label problemLabel;
    @FXML
    private Label correctLabel;
    @FXML
    private Label incorrectLabel;
    
    private int problems = 0, hits = 0, misses = 0;
    @FXML
    private Label user;
    @FXML
    private PieChart pieChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Session> sessions = MapController.currentUser.getSessions();
        sessionLabel.setText("" + sessions.size());
        
        for (int i = 0; i < sessions.size(); i++) {
            hits += sessions.get(i).getHits();
            misses += sessions.get(i).getFaults();
        }
        problems = hits + misses;
        problemLabel.setText("" + problems);
        correctLabel.setText("" + hits);
        incorrectLabel.setText("" + misses);
        user.setText("- " + MapController.currentUser.getNickName() + " -");
        
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.add(new PieChart.Data("Hits", hits));
        data.add(new PieChart.Data("Faults", misses));
        pieChart.setData(data);
    }    
}
