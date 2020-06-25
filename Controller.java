package sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ResourceBundle;
import java.util.Scanner;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import static sample.Chatcontroller.ServerPort;


public class Controller implements Runnable{
    String loginText="";
    String passwordText ="";
    static Boolean check = false;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginfield;

    @FXML
    private PasswordField passwordfield;

    @FXML
    private Button loginbutton;

    @FXML
    private Button registrbutton;

    @FXML
    private CheckBox checkbox1;

    @FXML
    private CheckBox checkbox2;

    @FXML
    void initialize() throws IOException {
        checkbox1.setOnAction(Event->{
            if(checkbox1.isSelected()){
                checkbox2.setSelected(false); //убирает галочку, если выбранно
            }
        });
        checkbox2.setOnAction(Event->{
            if(checkbox2.isSelected()){
                checkbox1.setSelected(false);
            }
        });
        registrbutton.setOnAction(actionEvent -> { //съебываемся
            registrbutton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();      //переход с одного окна на другое ... переход на регистрацию
            loader.setLocation(getClass().getResource("/sample/registr.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            stage.close();
        });
        loginbutton.setOnAction(actionEvent -> {  //логинимся
            new Thread(new Controller()).start(); //создаем уши
            loginText = loginfield.getText().trim();
            passwordText = passwordfield.getText().trim();
            if(!loginText.equals("")&& !passwordText.equals("")){
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress("localhost", Chatcontroller.ServerPort));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    writer.println("code::login");
                    writer.println(InetAddress.getLocalHost().getHostAddress());
                    writer.println(Chatcontroller.ClientPort);
                    writer.println(Chatcontroller.name);
                    if (checkbox1.isSelected() || checkbox2.isSelected()) {
                        writer.println(loginText);
                        writer.println(passwordText);
                        if(checkbox1.isSelected()) {
                            writer.println("user");
                            Chatcontroller.IsAdmin = false;
                        }
                        else{
                            writer.println("admin");
                            Chatcontroller.IsAdmin = true;
                        }
                        Thread.sleep(2000);
                        if (check) {
                            loginbutton.getScene().getWindow().hide();
                            FXMLLoader loader1 = new FXMLLoader();             //переход на чат
                            loader1.setLocation(getClass().getResource("/sample/chat.fxml"));
                            try {
                                loader1.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Parent root1 = loader1.getRoot();
                            Stage stage1 = new Stage();
                            stage1.setScene(new Scene(root1));
                            stage1.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error alert");
                            alert.setHeaderText("Cannot find user in DB");
                            alert.setContentText("Your login or password is wrong");
                            alert.showAndWait();
                            alert.close();
                        }
                        writer.close();
                        socket.close();
                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error alert");
                        alert.setHeaderText("Please, chose your type of user");
                        alert.setContentText("P.S. left downer corner");
                        alert.showAndWait();
                        alert.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("Login and password is empty");
            }
        });
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(Chatcontroller.ClientPort);
            Socket socket = serverSocket.accept();
            Scanner scanner = new Scanner(socket.getInputStream());
            ServerPort = Integer.parseInt(scanner.nextLine());
            String msg = scanner.nextLine();
            System.out.println(msg);
            if (msg.equals("code::login_accepted")) {
                check = true;
                Chatcontroller.ClientPort = Integer.parseInt(scanner.nextLine());
                Chatcontroller.name = scanner.nextLine();
            } else {
                System.out.println("Login is wrong go back piece of cake");
                check = false;
            }
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}