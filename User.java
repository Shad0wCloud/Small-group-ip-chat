package sample;

public class User {
    private String login;
    private String password;
    private String name;
    private String serverport;
    private String clientport;
    private String ip;

    public User(String login, String password, String name, String serverport, String clientport, String ip) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.serverport = serverport;
        this.clientport = clientport;
        this.ip = ip;
    }

    public User() { }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerport() {
        return serverport;
    }

    public void setServerport(String serverport) {
        this.serverport = serverport;
    }

    public String getClientport() {
        return clientport;
    }

    public void setClientport(String clientport) {
        this.clientport = clientport;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
