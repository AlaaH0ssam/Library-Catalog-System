import java.io.IOException;
import java.util.*;
import java.time.LocalDate;
public class Library {
    private static ArrayList<Book> books = new ArrayList<>(0);
    private static ArrayList<Customer> customers = new ArrayList<>(0);
    private static ArrayList<Borrower> borrowers= new ArrayList<>(0);
    private static ArrayList<Transaction> transactions = new ArrayList<>(0);
    private static ArrayList<Book> reservated_books = new ArrayList<>(0);
    private static ArrayList<Borrower> borrowers_reserved = new ArrayList<>(0);
    Admin admin = new Admin("admin","admin@gmail.com","010000000","admin");

    public Library(ArrayList<Book> books, ArrayList<Customer> customers, ArrayList<Borrower> borrowers) {
        this.books = books;
        this.borrowers = borrowers;
        this.customers = customers;
    }

    public static ArrayList<Book> getBooks() {
        return books;
    }

    public static ArrayList<Customer> getCustomers() {
        return customers;
    }

    public static ArrayList<Borrower> getBorrowers() {
        return borrowers;
    }

    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public static void addBook(String book_title, String author, int publiction_date, boolean availablity, float price, String genre, double rating, int book_quantity) {
        Book newBook = new Book(price, book_title, author, publiction_date, availablity, genre, rating, book_quantity);
        books.add(newBook);
        System.out.println("Book added: " + book_title);

    }


    static public void addCustomer(String user_name, String password, String user_email, String user_phone) {
        customers.add(new Customer(user_name, user_email, user_phone, password));
        System.out.println("customer added: " + user_name);
    }

    public static void removeBook(int book_id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getbookId() == book_id) {
                books.remove(i);
                break;
            }
        }

    }

    public static void update_book(int book_id, int new_price, boolean new_availability, String new_title, int new_quantity) {
        for (Book book : books) {
            if (book.getbookId() == book_id) {
                book.set_price(new_price);
                book.setavailability(new_availability);
                book.setbookTitle(new_title);
                book.setBook_quantity(new_quantity);
            }
        }
    }

    public static void addBorrower(String user_name, String user_email, String user_phone, String password) {
        borrowers.add(new Borrower(user_name, user_email, user_phone, password));
        System.out.println("borrower added: " + user_name);
    }

    public static void removeBorrower(int borrower_id) {
        for (int i = 0; i < borrowers.size(); i++) {
            if (borrowers.get(i).getUser_id() == borrower_id) {
                borrowers.remove(i);
                break;
            }
        }
    }

    public static void removeCustomer(int custmer_id) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUser_id() == custmer_id) {
                customers.remove(i);
                break;
            }
        }
    }

    public static void updateUser(int user_id, String new_name, String new_email, String new_phone, String password) {
        for (int i=0;i<borrowers.size();i++) {
            if (borrowers.get(i).getUser_id() == user_id && customers.get(i).getUser_id() == user_id) {
                borrowers.get(i).setUser_name(new_name);
                borrowers.get(i).setUser_email(new_email);
                borrowers.get(i).setUser_phone(new_phone);
                borrowers.get(i).setPassword(password);

                customers.get(i).setUser_name(new_name);
                customers.get(i).setUser_email(new_email);
                customers.get(i).setUser_phone(new_phone);
                customers.get(i).setPassword(password);
                return;
            }
        }
        System.out.println("The user doesn't exist");
    }

    public static void transactions_history() {
        for (Borrower borrower : borrowers) {
            for (Transaction transaction : borrower.getTransactions()) {
                if (borrower.getTransactions() == null) {
                    System.out.println("no active transactions for this borrower");
                }
                System.out.println(transaction);
            }
        }
    }


    public static ArrayList<Book> show_available_books() {
        boolean availableBooks = false;
        ArrayList<Book> available_books = new ArrayList<>(0);
        System.out.println("\nAvailable Books:");
        for (Book book : books) {
            if (book.checkavailability()) {
                availableBooks = true;

                available_books.add(book);
            }
        }

        if (!availableBooks) {
            System.out.println("Sorry, no books are currently available.");
            return null;
        }
        return available_books;
    }
    public static ArrayList<Book> search_for_book(String title_or_author) {  //CHANGED!!!
        ArrayList<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getbookTitle().toLowerCase().contains(title_or_author.toLowerCase()) ||
                    book.getauthor().toLowerCase().contains(title_or_author.toLowerCase())) {
                results.add(book);
            }
        }
        if (results.isEmpty()) {
            System.out.println("No books found with the given title or author, please enter your preferred genre:");

            Scanner scanner = new Scanner(System.in);
            String preferred_genre = scanner.nextLine();
            System.out.println("Recommended books based on your preferred genre: " + preferred_genre);
            for (Book book : books) {
                if (book.getGenre().equalsIgnoreCase(preferred_genre)) {
                    results.add(book);
                }

            }

        }
        return results;
    }

    public static void show_search_result(String keyword){
        ArrayList<Book>result=Library.search_for_book(keyword);
        if(result == null){
            System.out.println("No books found for this genre.");
            return;
        }
        int i =0;
        for(Book book:result){
            i+=1;
            System.out.println(i+". "+book);
        }
    }

    public void notifyBorrowers() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        for (Borrower borrower : borrowers) {
            for (Transaction transaction : borrower.getTransactions()) {
                LocalDate dueDate = transaction.getReturn_date();

                if (dueDate.equals(tomorrow) && transaction.getActual_return_date() == null ) {
                    String notification = "Reminder: Borrower " + borrower.getUser_name() +
                            ", please return the book '" + transaction.getBook().getbookTitle() +
                            "' by tomorrow (" + dueDate + ").";

                    if (!borrower.getNotificationHistory().contains(notification)) {
                        borrower.addNotificationsHistory(notification);
                    }

                }
            }
        }
    }



    public static void add_transaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public boolean register(User user) {
        if (user instanceof Borrower) {
            for (User existingUser : Library.getBorrowers()) {
                if (existingUser.getUser_email().equals(user.getUser_email())) {
                    System.out.println("username already exist");
                    return false;
                }
            }
            //int user_id, String user_name, String user_email, String user_phone,String password
            Library.addBorrower( user.getUser_name(), user.getUser_email(), user.getUser_phone(), user.getPassword());
            return true;
        } else if (user instanceof Customer) {
            for (User existingUser : Library.getCustomers()) {
                if (existingUser.getUser_email().equals(user.getUser_email())) {
                    System.out.println("username already exist");
                    return false;
                }
            }
            //int user_id, String user_name, String user_email, String user_phone,String password
            Library.addCustomer(user.getUser_name(), user.getPassword(), user.getUser_email(), user.getUser_phone());
            return true;
        }
        return false;

    }

    public User login(int choice, String email, String password) {
        if (choice == 1) {
            for (User user : Library.getBorrowers()) {
                if (user.getUser_email().equals(email) && user.getPassword().equals(password)) {
                    // System.out.println("log in successful! welcome, " + user.getUser_name());
                    return user;
                }
            }
            //System.out.println("log in failed incorrect email or password");
            return null;
        } else if (choice == 2) {
            for (User user : Library.getCustomers()) {
                if (user.getUser_email().equals(email) && user.getPassword().equals(password)) {
                    //System.out.println("log in successful! welcome, " + user.getUser_name());
                    return user;
                }
            }

            //System.out.println("log in failed incorrect email or password");
            return null;
        }
        else if(choice == 3){
            if(email.equals("admin@gmail.com")&& password.equals("admin")){
                return admin;
            }
            // System.out.println("log in failed incorrect email or password");
            return null;
        }
        else return null;
    }
    public static void reserve_book(Borrower borrower1, Book book1){

        for(int i=0;i<reservated_books.size();i++){
            Borrower borrower = borrowers_reserved.get(i);
            Book book = reservated_books.get(i);
            if(borrower.getUser_id() == borrower1.getUser_id()&& book.getbookId() == book1.getbookId()){
                System.out.println("You've already reserved this book and we will notify ypu when it is available");
                return;
            }
        }
        reservated_books.add(book1);
        borrowers_reserved.add(borrower1);
        System.out.println("The book has been reserved successfully and we will notify you when it's available");

    }

    public static void manage_reservation() {
        for (int i = 0; i < reservated_books.size(); i++) {
            Borrower borrower = borrowers_reserved.get(i);
            Book book = reservated_books.get(i);

            if (book.checkavailability()) {
                String notificationMessage = "The book: " + book.getbookTitle() + " is now available";
                borrower.addNotificationsHistory(notificationMessage); // Add the single string instead of a list
                reservated_books.remove(i);
                borrowers_reserved.remove(i);
                i--; // Decrement index to avoid skipping the next entry after removal
            }
        }
    }


    public static boolean check_email(String email) {
        int atIndex = email.indexOf('@');
        int lastAtIndex = email.lastIndexOf('@');

        if (atIndex == -1 || atIndex != lastAtIndex) {
            return false;
        }

        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex + 1);

        if (localPart.isEmpty() || localPart.startsWith(".") || localPart.endsWith(".")) {
            return false;
        }

        if (domainPart.isEmpty() || domainPart.startsWith(".") || domainPart.endsWith(".")) {
            return false;
        }

        int dotIndex = domainPart.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == domainPart.length() - 1) {
            return false;
        }

        String topLevelDomain = domainPart.substring(dotIndex + 1);
        if (topLevelDomain.length() < 2) {
            return false;
        }

        return true;
    }

    public static ArrayList<Book> getReservated_books() {
        return reservated_books;
    }
    public static ArrayList<Borrower> getBorrowers_reserved() {
        return borrowers_reserved;
    }


}