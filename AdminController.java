package sample;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import javax.swing.*;

import static sample.Chatcontroller.ClientPort;
import static sample.Chatcontroller.ServerPort;


public class AdminController {

    static String date = null;
    static String name = null;
    static String message = null;
    static ObservableList<MSG> list;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<MSG> TableView;

    @FXML
    private TableColumn<MSG,String> dateintablefield;

    @FXML
    private TableColumn<MSG,String> nameintablefield;

    @FXML
    private TableColumn<MSG,String> messageintablefield;

    @FXML
    private TextField datefield;

    @FXML
    private TextField namefield;

    @FXML
    private TextField messagefield;

    @FXML
    private Button recievebutton;

    @FXML
    private Button backtochatbutton;

    @FXML
    private Button backtologinbutton;

    @FXML
    private TextField datefield1;

    @FXML
    void initialize() {
        backtochatbutton.setOnAction(jojo ->{
            backtochatbutton.getScene().getWindow().hide();
        });
        recievebutton.setOnAction(jojo->{
            String datainput ="";
            String select ="SELECT * FROM messages where ";
            String name ="Name ='" + namefield.getCharacters()+"'";
            String msg = "Message LIKE'%" + messagefield.getCharacters()+"%'";//TODO проверка 75 минуты
            System.out.println(datefield.getText().equals(""));
            System.out.println(!datefield1.getText().equals(""));
            System.out.println(datefield.getText());
            System.out.println(datefield1.getText());
            if(datefield.getText().equals("") && !datefield1.getText().equals("")){//если админ смотрит до какой-то даты
                datainput = "Date between '20070101000000' and '"+datefield1.getCharacters()+"'";
            }else{
                datainput = "Date between '" + datefield.getCharacters() +"' and '"+datefield1.getCharacters()+"'";
            }
            if(!name.equals("Name =''")){
                select +=name;
                if(!msg.equals("Message LIKE'%%'") || !datainput.equals("Date between '' and ''")){
                    select +=" and ";
                }
            }
            if(!msg.equals("Message LIKE'%%'")){
                select+=msg;
                if(!datainput.equals("Date between '' and ''")) {
                    select += " and ";
                }
            }
            if(!datainput.equals("Date between '' and ''")){select +=datainput;}
            select+=";";
            if(select.equals("SELECT * FROM messages where ;")){select="SELECT * FROM messages;";}
            try {
                System.out.println(select);
                SendSELECT(select);
            } catch (IOException e) {
                e.printStackTrace();
            }
            list = TableView.getItems();
            new Thread(new TempServer()).start();
            for (MSG m: TempServer.msgs) {
                list.add(m);
            }
            nameintablefield.setCellValueFactory(new PropertyValueFactory<MSG,String>("Name"));
            dateintablefield.setCellValueFactory(new PropertyValueFactory<MSG,String>("Date"));
            messageintablefield.setCellValueFactory(new PropertyValueFactory<MSG,String>("Message"));
            list.clear();
        });
         /*date = datefield.getText();
        name = namefield.getText();
        message = messagefield.getText();
            ArrayList<String> namelist = new ArrayList();
            ArrayList<Date> datelist = new ArrayList();
            ArrayList<String> messagelist = new ArrayList();
            ResultSet result = null;
            String select = "SELECT * FROM message WHERE date ='"+ datefield +"' AND name ='"+ namefield +"' AND message ='"+ messagefield +"';";
            try {
                PreparedStatement pr1 = DatabaseHandler.getDbConnection().prepareStatement(select);
                result = pr1.executeQuery();
                while(result.next()) {
                    datelist.add(result.getDate("date"));
                    namelist.add(result.getString("name"));
                    messagelist.add(result.getString("message"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            String name = namelist.get(0);
            Date date = datelist.get(0);
            String message = messagelist.get(0);
            System.out.println(name+"  \n");
            System.out.println(date+"  \n");
            System.out.println(message+"  \n");

            //TableView.getColumns().addAll();
        });*/
    }
    public void SendSELECT(String select) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", Chatcontroller.ServerPort));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("code::select");
        writer.println(InetAddress.getLocalHost().getHostAddress());
        writer.println(Chatcontroller.ClientPort);
        writer.println(Chatcontroller.name);
        writer.println(select);
    }
}
class TempServer implements Runnable {
    static List<MSG> msgs = new ArrayList<>();
    @Override
    public void run() {
        System.out.println("я на сервере");
        try (ServerSocket serverSocket = new ServerSocket(ClientPort+1)) {
            Socket socket = serverSocket.accept();
            Scanner scanner = new Scanner(socket.getInputStream());
            while (scanner.hasNext()) {
                Chatcontroller.ServerPort = Integer.parseInt(scanner.nextLine());
                String date = scanner.nextLine();
                String name = scanner.nextLine();
                String msg = scanner.nextLine();
                String endDate = scanner.nextLine();
                AdminController.list.add(new MSG(date,name,msg));
                System.out.println(endDate);
                if(endDate.equals("end")){break;}
            }
            System.out.println("Я ливаю");
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

