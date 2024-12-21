import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;


public class Borrower extends User{

    private ArrayList<Transaction> transactions = new ArrayList<>(0);
    private ArrayList<String>notificationHistory = new ArrayList<>(0);;
    public Borrower( String user_name, String user_email, String user_phone,String password) {
        super( user_name, user_email, user_phone,password);
        user_id = set_id();
    }

    public int set_id (){
        return Library.getBorrowers().size()+1;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction t){
        transactions.add(t);
    }

    public List<String> getNotificationHistory() {
        return notificationHistory;
    }

    public void addNotificationsHistory(String message) {
        notificationHistory.add(message);
    }

    public boolean borrowBook(Book book,Borrower borrower){

        if (book.getBook_quantity() <= 0) {
            System.out.println("The book is out of stock.");
            return false;
        }

        for (Transaction t : transactions) {
            if (t.getBook().equals(book)&& t.getActual_return_date() == null) {
                System.out.println("You have already borrowed this book.");
                return true;
            }
        }
        book.setBook_quantity(book.getBook_quantity() - 1);
        Transaction transaction = new Transaction(book, borrower);
        Library.add_transaction(transaction);
        transactions.add(transaction);
        System.out.println("Book borrowed successfully");
        System.out.println("You should return this book any time till "+ transaction.getReturn_date());
        return true;
    }
    public boolean returnBook(String bookTitle) {

        for (Transaction transaction : transactions) {
            if (transaction.getBook().getbookTitle().equalsIgnoreCase(bookTitle)) {
                if (transaction.getActual_return_date() == null) {
                    transaction.setActual_return_date(LocalDate.now());
                    if(transaction.calc_fees() != 0){
                        System.out.println("You have been late for returning this book since the return date was "+ transaction.getReturn_date());
                        System.out.println("You should pay "+ transaction.calc_fees()+"$ as fees");

                    }
                    transaction.getBook().setBook_quantity(transaction.getBook().getBook_quantity()+1);

                    System.out.println("Book '" + bookTitle + "' has been successfully returned.");
                    return true;
                }
            }
        }

        System.out.println("Book '" + bookTitle + "' not found or the user has returned this book.");
        return false;
    }

    @Override
    public void view_history() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        }
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    @Override
    public String toString() {
        return super.toString() +

                "transactions count: " + (!transactions.isEmpty() ? transactions.size() : 0) +"\n"+
                "*******************************************************************************************************\n";
    }
}