package sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ConfigController {

    @FXML
    private TextField portfield;

    @FXML
    private Button changebutton;

    @FXML
    void initialize() {
        changebutton.setOnAction(jojo->{
            try {
                Socket socket1 = new Socket();
                socket1.connect(new InetSocketAddress("localhost", Chatcontroller.ServerPort));
                PrintWriter writer = new PrintWriter(socket1.getOutputStream(), true);
                writer.println("code::changeport");
                writer.println(InetAddress.getLocalHost().getHostAddress());
                writer.println(Chatcontroller.ClientPort);
                writer.println(Chatcontroller.name);
                Chatcontroller.ClientPort =Integer.parseInt(portfield.getText());
                writer.println(Chatcontroller.ClientPort);
                if (Chatcontroller.IsAdmin){ writer.println("admin");
                }else{writer.println("User");}
                socket1.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}