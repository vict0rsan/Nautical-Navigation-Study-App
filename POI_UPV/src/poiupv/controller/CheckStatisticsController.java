/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv.controller;

import DBAccess.NavegacionDAOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Problem;
import model.Session;

/**
 * FXML Controller class
 *
 * @author guill
 */
public class CheckStatisticsController implements Initializable {

    @FXML
    private ImageView avatar;
    @FXML
    private Text user;
    @FXML
    private ListView<Session> shownSessions;
    @FXML
    private PieChart pieChart;
    @FXML
    private Text problemCount;
    @FXML
    private Text correctAnswers;
    @FXML
    private Text incorrectAnswers;
    
    private ObservableList<Session> sessionsToShow;
    @FXML
    private DatePicker dateFilter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        List<Session> sessions = MapController.currentUser.getSessions();
        sessionsToShow = FXCollections.observableArrayList(sessions);
        shownSessions.setItems(sessionsToShow);
        shownSessions.setCellFactory(c -> new SessionListCell());
        
        avatar.setImage(MapController.currentUser.getAvatar());
        user.setText(MapController.currentUser.getNickName());
        
        shownSessions.getSelectionModel().selectedIndexProperty().
                addListener((o, oldVal, newVal) -> {
                    if(newVal.intValue() == -1){
                        problemCount.setText("-");
                        correctAnswers.setText("-");
                        incorrectAnswers.setText("-");
                    }else{
                        Session selectedSession = shownSessions.getSelectionModel().getSelectedItem();
                        int hits = selectedSession.getHits();
                        int faults = selectedSession.getFaults();
                        problemCount.setText(Integer.toString(hits+faults));
                        correctAnswers.setText(Integer.toString(hits));
                        incorrectAnswers.setText(Integer.toString(faults));
                        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
                        data.add(new PieChart.Data("Hits", hits));
                        data.add(new PieChart.Data("Faults", faults));
                        pieChart.setData(data);
                    }
                });
    }    

    @FXML
    private void handleGeneralStatisticsOnAction(ActionEvent event) throws Exception {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/view/GeneralStatistics.fxml"));

        Pane root = (Pane) myLoader.load();

        Scene scene = new Scene (root);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("General Statistics");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.show();

        Node source = (Node) event.getSource();
        Stage oldStage = (Stage) source.getScene().getWindow();
        oldStage.close();
    }

    @FXML
    private void handleFilterSessionsOnAction(ActionEvent event) {
        List<Session> sessions = MapController.currentUser.getSessions();
        LocalDate dateFrom = dateFilter.getValue();
        sessions = sessions.stream().filter(s -> s.getLocalDate().compareTo(dateFrom) >= 0).collect(Collectors.toList());
        sessionsToShow = FXCollections.observableArrayList(sessions);
        shownSessions.setItems(sessionsToShow);
    }
    
    
    class SessionListCell extends ListCell<Session> {
    
        @Override
        protected void updateItem(Session session, boolean empty){
            super.updateItem(session, empty);
            if(session == null || empty){

            }else{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
                LocalDateTime currentTime = session.getTimeStamp();
                setText("Session - " +  currentTime.format(formatter));
            }
        }
    }
}
