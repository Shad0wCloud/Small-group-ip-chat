package sample;

import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static sample.Chatcontroller.ClientPort;
import static sample.Chatcontroller.ServerPort;

class ClientServer implements Runnable {
    static Chatcontroller ccr;

    @Override
    public void run() {
        /*FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("chat.fxml"));
        try {
            Pane root = loader.load();
            Scene scene = new Scene(root, 800, 550);
            Stage stage = new Stage();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //Chatcontroller ctrl = loader.getController();
        try (ServerSocket serverSocket = new ServerSocket(ClientPort)) {
            System.out.println("Я родился");
            File sound = new File("msg.wav");
            while (true) {
                Socket socket = serverSocket.accept();
                Scanner scanner = new Scanner(socket.getInputStream());
                ServerPort = Integer.parseInt(scanner.nextLine());
                String msg = scanner.nextLine();
                System.out.println(msg);
                if (msg.equals("code::exit")) {
                    serverSocket.close();
                    System.out.println("Выключаю сервер");
                    break;
                } else if (msg.equals("server: code::serverportchange")) {
                    System.out.println("Смена порта сервера");
                } else {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(sound);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                    ccr.textarea.appendText(msg + "\n");
                    System.out.println(serverSocket.getLocalPort());
                    //Platform.runLater(()->ctrl.textarea.appendText(msg));
                }
            }
            System.out.println("я вышел из цикла");
            new Thread(new ClientServer()).start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}