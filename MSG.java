package sample;

import javafx.beans.property.SimpleStringProperty;

public class MSG {
    public SimpleStringProperty Date;
    public SimpleStringProperty Name;
    public SimpleStringProperty Message;
    public MSG(String date, String name, String Message) {
        this.Date=new SimpleStringProperty(date);
        this.Name=new SimpleStringProperty(name);
        this.Message=new SimpleStringProperty(Message);
    }

    public String getDate() {
        return Date.get();
    }

    public SimpleStringProperty dateProperty() {
        return Date;
    }

    public String getName() {
        return Name.get();
    }

    public SimpleStringProperty nameProperty() {
        return Name;
    }

    public String getMessage() {
        return Message.get();
    }

    public SimpleStringProperty messageProperty() {
        return Message;
    }
}
