package sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Regcontroller {
    @FXML
    private TextField loginfield;

    @FXML
    private PasswordField passwordfield;

    @FXML
    private Button registrbutton;

    @FXML
    private TextField namefield;

    @FXML
    private TextField serverportfield;

    @FXML
    private TextField clientportfield;

    @FXML
    private TextField ipfiled;

    @FXML
    private Button back_button;

    @FXML
    void initialize() {
        back_button.setOnAction(Event -> { //возвращаемся обратно
            registrbutton.getScene().getWindow().hide();
            FXMLLoader loader1 = new FXMLLoader();
            loader1.setLocation(getClass().getResource("/sample/sample.fxml"));
            try{
                loader1.load();
            }catch(IOException e){
                e.printStackTrace();
            }
            Parent root1 = loader1.getRoot();
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(root1));
            stage1.showAndWait();
        });
        registrbutton.setOnAction(actionEvent -> { //регаемся
            try {
                Chatcontroller.name = namefield.getText();//TODO Наверное, можно убрать
                singUpNewUser();
            } catch (IOException e) {
                e.printStackTrace();
            }
            registrbutton.getScene().getWindow().hide();
            FXMLLoader loader1 = new FXMLLoader();             //переход в меню логина
            loader1.setLocation(getClass().getResource("/sample/sample.fxml"));
            try{
                loader1.load();
            }catch(IOException e){
                e.printStackTrace();
            }
            Parent root1 = loader1.getRoot();
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(root1));
            stage1.showAndWait();
        });
    }

    private void singUpNewUser() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost",7777));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("code::registration");
        writer.println(InetAddress.getLocalHost().getHostAddress());
        writer.println(socket.getPort());
        writer.println(namefield.getText());
        writer.println(loginfield.getText());
        writer.println(passwordfield.getText());
        writer.println(clientportfield.getText());
        writer.println(ipfiled.getText());
        socket.close();
    }
}

