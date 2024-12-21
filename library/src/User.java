public abstract class User {
    protected int user_id;
    private String user_name;
    private String password;
    private String user_email;
    private String user_phone;
    public User(String user_name,String user_email,String user_phone,String password){

        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }
    public abstract void view_history();

    @Override
    public String toString() {
        return "***************************************************************************************************\n" +
                "user id: " + user_id + "\n" +
                "user name: " + user_name + '\n' +
                "password: " + password + '\n' +
                "user email: " + user_email + '\n' +
                "user phone: " + user_phone + '\n'

                ;
    }
}