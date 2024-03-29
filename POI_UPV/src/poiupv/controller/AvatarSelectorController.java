/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class AvatarSelectorController implements Initializable {
    private ImageView avatar;
    
    @FXML
    private Button avatar1Button;
    @FXML
    private ImageView avatar1;
    @FXML
    private Button avatar2Button;
    @FXML
    private ImageView avatar2;
    @FXML
    private Button avatar3Button;
    @FXML
    private ImageView avatar3;
    @FXML
    private Button avatar4Button;
    @FXML
    private ImageView avatar4;
    @FXML
    private Button avatarDefaultButton;
    @FXML
    private ImageView avatarDefault;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void initData(ImageView avatar)
    {
        this.avatar = avatar;
    }

    @FXML
    private void handleAvatar1(ActionEvent event) 
    {
        setAndDisplayImage("1");
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    private void handleAvatar2(ActionEvent event) 
    {
        setAndDisplayImage("2");
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    private void handleAvatar3(ActionEvent event) 
    {
        setAndDisplayImage("3");
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    private void handleAvatar4(ActionEvent event) 
    {
        setAndDisplayImage("4");
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    private void handleAvatarDefault(ActionEvent event) 
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Imagen");

        // Agregar filtros para facilitar la busqueda
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")
        );

        // Obtener la imagen seleccionada
        File imgFile = fileChooser.showOpenDialog(null);

        // Mostar la imagen
        if (imgFile != null) {
            Image image = new Image("file:" + imgFile.getAbsolutePath());
            avatar.setImage(image);
        }
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
    
    private void setAndDisplayImage(String n)
    {
        String name = String.format("resources/avatars/avatar%s.png", n);
        Image myImage = new Image(name);
        avatar.setImage(myImage);
    }
    
}
