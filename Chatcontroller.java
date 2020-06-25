package sample;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Chatcontroller {
    public static String name = "TEST-2";
    public static int ClientPort = 6777; //не статичен, меняется из бд
    public static int ServerPort = 7777; //не статичен, иногда меняется сервером
    public static boolean IsAdmin = false;
    @FXML
    private TextField messagefield;

    @FXML
    private Button sendbutton;

    @FXML
    private Button exitbutton;

    @FXML
    private Button configbutton;

    @FXML
    private Button onlyadminbutton;

    @FXML
        public TextArea textarea;

    @FXML
    void initialize() {
        new Thread(new ClientServer()).start();
        ClientServer.ccr = this;
        if(!IsAdmin){onlyadminbutton.setDisable(true);}
        configbutton.setOnAction(jojo ->{
            FXMLLoader loader2 = new FXMLLoader();             //выход из чата
            loader2.setLocation(getClass().getResource("Configuration.fxml"));
            try {
                loader2.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root1 = loader2.getRoot();
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(root1));
            stage1.showAndWait();
        });
        exitbutton.setOnAction(event -> {
            exitbutton.getScene().getWindow().hide();
            FXMLLoader loader1 = new FXMLLoader();             //выход из чата
            loader1.setLocation(getClass().getResource("sample.fxml"));
            try {
                loader1.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root1 = loader1.getRoot();
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(root1));
            stage1.showAndWait();
        });
        onlyadminbutton.setOnAction(jojo->{
            //onlyadminbutton.getScene().getWindow().hide();
            FXMLLoader loader3 = new FXMLLoader();             //выход из чата
            loader3.setLocation(getClass().getResource("adminwindow.fxml"));
            try {
                loader3.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root3 = loader3.getRoot();
            Stage stage3 = new Stage();
            stage3.setScene(new Scene(root3));
            stage3.showAndWait();
        });
        sendbutton.setOnAction(e -> {
            String input = messagefield.getText();
            if (!input.isEmpty()) {
                SendMSG(input);
                //Platform.runLater(()-> textarea.appendText("Пользователь " + name + " написал " + input + "\n"));
                messagefield.clear();
            }
        });
    }


    public static void SendMSG(String input) {
        try {
            Socket socket1 = new Socket();
            socket1.connect(new InetSocketAddress("localhost", ServerPort));
            PrintWriter writer = new PrintWriter(socket1.getOutputStream(), true);
            writer.println(input);
            writer.println(InetAddress.getLocalHost().getHostAddress());
            writer.println(ClientPort);
            writer.println(Chatcontroller.name);
            socket1.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}