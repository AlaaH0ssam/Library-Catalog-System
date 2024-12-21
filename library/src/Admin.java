class Admin extends User{

    public Admin(String user_name, String user_email, String user_phone,String password) {
        super(user_name, user_email, user_phone,password);
    }

    public void addBook( String book_title, String author, int publiction_date, boolean availablity, float price, String genre, double rating, int book_quantity) {

        Library.addBook(book_title,author,publiction_date,availablity,price,genre,rating,book_quantity);
        System.out.println("Admin added book: "+book_title);

    }

    public void removeBook( int book_id) {
        Library.removeBook(book_id);
    }

    public void updateBook( int book_id, int new_price, boolean new_availability, String new_title, int new_quantitye) {
        Library.update_book(book_id,new_price,new_availability,new_title,new_quantitye);
        System.out.println("Admin updated book details");
    }

    public void addUser(String user_name, String user_email, String user_phone,String password) {
        Library.addBorrower(user_name,user_email,user_phone,password);
        Library.addCustomer(user_name, password, user_email, user_phone);
    }

    public void updateUser(int borrower_id, String new_name, String new_email, String new_phone,String password) {
        Library.updateUser(borrower_id,new_name,new_email,new_phone,password);
    }

    public void removeUser(int user_id) {
        Library.removeBorrower(user_id);
        Library.removeCustomer(user_id);
    }

    public void show_available_books(){
        Library.show_available_books();
    }

    public void view_history(){
        Library.transactions_history();
    }

}