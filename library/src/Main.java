import java.util.ArrayList;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.*;
import java.time.LocalDate;


public class Main {

    public static void main(String[] args) {
        ArrayList<Book> books = new ArrayList<Book>(2);
        ArrayList<Customer> c = new ArrayList<Customer>(2);
        ArrayList<Borrower> b = new ArrayList<Borrower>(2);
        Library library = new Library(books, c, b);
        Admin admin = null;
        FileHandler.readdata(b,c,admin,books);
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        while (true) {
            try{
                System.out.println("Welcome to the Book Management System!");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Please choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                Library.manage_reservation();
                library.notifyBorrowers();
                switch (choice) {
                    case 1:
                        System.out.println("Enter your name: ");
                        String user_name = scanner.nextLine();
                        String user_email;
                        while(true){
                            System.out.println("Enter your email: ");
                            user_email = scanner.nextLine();
                            if(Library.check_email(user_email)){
                                break;
                            }
                            System.out.println("This email is wrong please try again");
                        }

                        System.out.println("Enter your phone number: ");
                        String user_phone = scanner.nextLine();
                        System.out.println("Enter your password: ");
                        String user_password = scanner.nextLine();

                        Borrower new_borrower = new Borrower(user_name, user_email, user_phone, user_password);
                        library.register(new_borrower);

                        Customer new_customer = new Customer(user_name, user_email, user_phone, user_password);
                        library.register(new_customer);

                        break;
                    case 2:
                        System.out.print("Enter your email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter your password: ");
                        String password = scanner.nextLine();

                        if (library.login(3,email,password) != null) {
                            Admin current_admin = (Admin) library.login(3, email, password);

                            if (current_admin != null) {
                                boolean logged_in_admin = true;
                                while (logged_in_admin) {

                                    System.out.println("0.show available books");
                                    System.out.println("1.Update book");
                                    System.out.println("2.Add book");
                                    System.out.println("3.Remove book");
                                    System.out.println("4.Update User");
                                    System.out.println("5.Add User");
                                    System.out.println("6.Remove User");
                                    System.out.println("7.Show Borrowers");
                                    System.out.println("8.View the transactions");
                                    System.out.println("9.Log out");
                                    char menu_choice = scanner.next().charAt(0);
                                    scanner.nextLine();
                                    switch (menu_choice) {
                                        case '0':
                                            ArrayList<Book> result = Library.show_available_books();
                                            if(result == null){
                                                continue;
                                            }
                                            for (int i =0 ;i< result.size();i++){
                                                System.out.println((i + 1) + ". " + result.get(i));
                                            }
                                            break;
                                        case '1':
                                            //try {
                                            System.out.println("Enter book id: ");
                                            int id = scanner.nextInt();
                                            System.out.println("Enter the new price: ");
                                            int price = scanner.nextInt();
                                            System.out.println("Enter book availability: ");
                                            boolean availability = scanner.nextBoolean();
                                            scanner.nextLine();
                                            System.out.println("Enter book title: ");
                                            String title = scanner.nextLine();
                                            System.out.println("Enter book quantity: ");
                                            int quantity = scanner.nextInt();
                                            current_admin.updateBook(id, price, availability, title, quantity);
                                            break;
//                                            }catch (Exception ex){
//                                                System.out.println("You entered invalid input");
//                                            }finally {
//                                                break;
//                                            }
                                        case '2':
                                            System.out.println("Enter book title: ");
                                            String book_title = scanner.nextLine();
                                            System.out.println("Enter book author: ");
                                            String author = scanner.nextLine();
                                            System.out.println("Enter book genre: ");
                                            String book_genre = scanner.nextLine();
                                            System.out.println("Enter book rating: ");
                                            double rating = scanner.nextDouble();
                                            scanner.nextLine();
                                            System.out.println("Enter the new price: ");
                                            int book_price = scanner.nextInt();
                                            scanner.nextLine();
                                            System.out.println("Enter book availability: ");
                                            boolean book_availability = scanner.nextBoolean();
                                            scanner.nextLine();
                                            System.out.println("Enter book publication year: ");
                                            int publication_year = scanner.nextInt();
                                            scanner.nextLine();
                                            System.out.println("Enter book quantity: ");
                                            int book_quantity = scanner.nextInt();
                                            scanner.nextLine();
                                            current_admin.addBook(book_title, author, publication_year, book_availability, book_price, book_genre, rating, book_quantity);
                                            break;
                                        case '3':
                                            System.out.println("Enter the book id: ");
                                            int id_to_remove = scanner.nextInt();
                                            current_admin.removeBook(id_to_remove);
                                            break;

                                        case '4':
                                            System.out.println("Enter the user id");
                                            int borrower_id = scanner.nextInt();
                                            scanner.nextLine();
                                            System.out.println("Enter the user name");
                                            String new_name = scanner.nextLine();
                                            System.out.println("Enter the user email");
                                            String new_email = scanner.nextLine();
                                            System.out.println("Enter the user phone");
                                            String new_phone = scanner.nextLine();
                                            System.out.println("Enter the user password");
                                            String new_password = scanner.nextLine();
                                            current_admin.updateUser(borrower_id, new_name, new_email, new_phone, new_password);
                                            break;
                                        case '5':
                                            System.out.println("Enter user name: ");
                                            String userName = scanner.nextLine();
                                            String userEmail;
                                            while(true){
                                                System.out.println("Enter user email: ");
                                                userEmail = scanner.nextLine();
                                                if(Library.check_email(userEmail)){
                                                    break;
                                                }
                                                System.out.println("Wrong email please try again");
                                            }
                                            System.out.println("Enter user phone number: ");
                                            String userPhone = scanner.nextLine();
                                            System.out.println("Enter the user password: ");
                                            String userPassword = scanner.nextLine();
                                            current_admin.addUser(userName, userEmail, userPhone, userPassword);
                                            break;
                                        case '6':
                                            System.out.println("Enter user id: ");
                                            int user_id_to_remove = scanner.nextInt();
                                            scanner.nextLine();
                                            current_admin.removeUser(user_id_to_remove);
                                            break;
                                        case '7':
                                            for (Borrower borrower : Library.getBorrowers()) {
                                                System.out.println(borrower);
                                            }
                                            break;
                                        case '8':
                                            try {
                                                current_admin.view_history();
                                            } catch (NullPointerException e) {
                                                System.out.println("No Transactions");
                                            } finally {
                                                break;
                                            }
                                        case '9':
                                            logged_in_admin = false;
                                            break;
                                        default:
                                            System.out.println("invalid input");
                                    }


                                }
                                break;
                            }
                        } else if (library.login(1, email, password) != null && library.login(2, email, password) != null) {
                            Borrower current_borrower = (Borrower) library.login(1, email, password);
                            Customer current_customer = (Customer) library.login(2, email, password);
                            if (current_borrower != null && current_customer != null) {
                                System.out.println("Welcome back " + current_borrower.getUser_name());
                                ArrayList<Book> booksOrder = new ArrayList<>();
                                ArrayList<Integer> quantitiesOrder = new ArrayList<>();
                                boolean logged_in_user = true;
                                while (logged_in_user) {

                                    System.out.println("1.View transaction  History");
                                    System.out.println("2.Show Orders History");
                                    System.out.println("3.Show available Books");
                                    System.out.println("4.Search for book");
                                    System.out.println("5.Return Book");
                                    System.out.println("6.Show cart details");
                                    System.out.println("7.Check out");
                                    System.out.println("8.Show Notifications History");
                                    System.out.println("9.Log Out");


                                    char borrower_choice = scanner.next().charAt(0);
                                    scanner.nextLine();
                                    switch (borrower_choice) {
                                        case '1':
                                            try {
                                                current_borrower.view_history();
                                                System.out.println("************************************************************");
                                                // Library.view_transaction_history();
                                            } catch (NullPointerException e) {
                                                System.out.println("No Transactions");
                                            }
                                            break;

                                        case '2':
                                            try {
                                                current_customer.view_history();
                                                System.out.println("************************************************************");

                                            } catch (NullPointerException e) {
                                                System.out.println("No Order");
                                            }
                                            break;

                                        case '3':


                                            try {
                                                while (true){
                                                    ArrayList<Book> result = Library.show_available_books();
                                                    if(result == null){
                                                        continue;
                                                    }
                                                    for (int i =0 ;i< result.size();i++){
                                                        System.out.println((i + 1) + ". " + result.get(i));
                                                    }
                                                    System.out.println("\nWhat would you like to do?");
                                                    System.out.println("1. Borrow a book");
                                                    System.out.println("2. Add to cart");
                                                    System.out.println("3. Return to menu");
                                                    char action1 = scanner.next().charAt(0);
                                                    scanner.nextLine();
                                                    switch (action1) {
                                                        case '1':
                                                            System.out.println("Enter the number of the book you want to borrow:");
                                                            int borrowing_choice1 = scanner.nextInt();
                                                            if (borrowing_choice1 > 0 && borrowing_choice1 <= result.size()) {
                                                                current_borrower.borrowBook(result.get(borrowing_choice1 - 1), current_borrower);
                                                            }
                                                            break;

                                                        case '2':
                                                            System.out.println("Enter the number of the book you want to add to cart:");
                                                            int buying_choice1 = scanner.nextInt();
                                                            if (buying_choice1 > 0 && buying_choice1 <= result.size()) {
                                                                Book selectedBook1 = result.get(buying_choice1 - 1);
                                                                System.out.println("Enter the quantity: ");
                                                                int quantity = scanner.nextInt();
                                                                if (selectedBook1.getBook_quantity() >= quantity) {
                                                                    // current_customer.add_order(newOrder);
                                                                    booksOrder.add(selectedBook1);
                                                                    quantitiesOrder.add(quantity);
                                                                    current_customer.userCart.addItem(selectedBook1, quantity);

                                                                } else {
                                                                    System.out.println("Not enough stock");
                                                                }
                                                            } else {
                                                                System.out.println("Invalid number");
                                                            }
                                                            break;

                                                        case '3':
                                                            System.out.println("**********************************************");
                                                            break;
                                                        default:
                                                            System.out.println("invalide input");

                                                    }
                                                    break;
                                                }
                                            } catch (NullPointerException e) {
                                                System.out.println("No books are available");
                                            }
                                            break;
                                        case '4':
                                            System.out.println("Enter the name of the book or author: ");
                                            String searchQuery = scanner.nextLine();
                                            List<Book> results = Library.search_for_book(searchQuery);

                                            if (results.isEmpty()) {
                                                System.out.println("No books found.");
                                                break;
                                            } else {
                                                System.out.println("Books found:");
                                                for (int i = 0; i < results.size(); i++) {
                                                    System.out.println((i + 1) + ". " + results.get(i));
                                                }

                                                System.out.println("\nWhat would you like to do?");
                                                System.out.println("1. Borrow a book");
                                                System.out.println("2. Add to cart");
                                                System.out.println("3. Return to menu");

                                                char action = scanner.next().charAt(0);
                                                scanner.nextLine();
                                                switch (action) {
                                                    case '1':
                                                        System.out.println("Enter the number of the book you want to borrow:");
                                                        int borrowing_choice = scanner.nextInt();
                                                        if (borrowing_choice > 0 && borrowing_choice <= results.size()) {
                                                            boolean borrowed = current_borrower.borrowBook(results.get(borrowing_choice - 1), current_borrower);
                                                            if(!borrowed){
                                                                System.out.println("Do you want to reserve this book(Y/N)");
                                                                char reserve = scanner.next().charAt(0);
                                                                scanner.nextLine();
                                                                if(reserve == 'y' || reserve=='Y'){
                                                                    Library.reserve_book(current_borrower, results.get(borrowing_choice - 1));
                                                                }
                                                            }
                                                        }
                                                        break;

                                                    case '2':
                                                        System.out.println("Enter the number of the book you want to add to cart:");
                                                        int buying_choice = scanner.nextInt();
                                                        if (buying_choice > 0 && buying_choice <= results.size()) {
                                                            Book selectedBook = results.get(buying_choice - 1);
                                                            System.out.println("Enter the quantity: ");
                                                            int quantity = scanner.nextInt();
                                                            if (selectedBook.checkavailability() && selectedBook.getBook_quantity() >= quantity) {
                                                                // current_customer.add_order(newOrder);
                                                                booksOrder.add(selectedBook);
                                                                quantitiesOrder.add(quantity);
                                                                current_customer.userCart.addItem(selectedBook, quantity);

                                                            } else {
                                                                System.out.println("Not enough stock");
                                                            }
                                                        } else {
                                                            System.out.println("Invalid number");
                                                        }
                                                        break;

                                                    case '3':
                                                        System.out.println("**********************************************");
                                                        break;
                                                    default:
                                                        System.out.println("Invalid input");
                                                        break;

                                                }
                                                break;
                                            }

                                        case '5':
                                            System.out.println("Enter the name of book you will return: ");
                                            String book_Title = scanner.nextLine();
                                            current_borrower.returnBook(book_Title);
                                            break;


                                        case '6':
                                            boolean found_items = current_customer.userCart.viewCartDetails();
                                            if (found_items) {
                                                System.out.println("Do you want to delete items from cart? (Y/N)");
                                                char anss = scanner.next().charAt(0);
                                                scanner.nextLine();
                                                if (anss == 'Y' || anss == 'y') {
                                                    System.out.println("Enter book name:");
                                                    String name = scanner.nextLine();
                                                    System.out.println("Enter quantity :");
                                                    int quantityies = scanner.nextInt();
                                                    current_customer.userCart.deleteItem(name, quantityies);
                                                }
                                            }
                                            break;

                                        case '7':
                                            try {

                                                Order newOrder = new Order("ORD" + current_customer.user_id + "/" + (current_customer.getOrders().size() + 1), current_customer.userCart.getItems(), current_customer.userCart.getQuantities());
                                                current_customer.add_order(newOrder);
                                                newOrder.setTotalPrice_after_discount(current_customer.userCart.calculateTotalPrice());

                                                current_customer.userCart.checkout();
                                            }catch (Exception ex){
                                                System.out.println("You done your Check out Already");
                                            }finally {
                                                break;
                                            }
                                        case '8':

                                            List<String> notifications = current_borrower.getNotificationHistory();
                                            System.out.println("Notifications:");
                                            for (String notification : notifications) {
                                                System.out.println(notification);
                                            }
                                            break;

                                        case '9':
                                            logged_in_user = false;

                                            break;
                                        default:
                                            System.out.println("Invalid option!");
                                    }

                                }
                            }
                            break;
                        }
                        else{
                            System.out.println("Incorrect email or password");
                            break;
                        }

                    case 3:
                        // Exit program
                        System.out.println("Exiting program...");
                        FileHandler.writedata(b,c,admin,books );
                        return;
                    default:
                        System.out.println("Invalid input");

                }
            } catch (InputMismatchException e) {
                System.out.println("Please Enter a number");
                scanner.nextLine();
            }
        }

    }

}