import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class FileHandler {

    private static final String USERS_FILE = "users.txt";
    private static final String BOOKS_FILE = "books.txt";
    private static final String TRANSACTIONS_FILE = "transaction.txt";
    private static final String ORDERS_FILE = "orders.txt";
    private static final String CART_FILE = "cart.txt";
    private static final String NOTIFICATIONS_FILE = "notifications.txt";
    private static final String RESERVATIONS_FILE = "reservation.txt";


    public static void readdata(List<Borrower> borrowers, List<Customer> customers, Admin admin, List<Book> books) {
        // Read users
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 6) {
                    if (userData[5].equals("admin")) {
                        admin = new Admin(userData[1], userData[2], userData[3], userData[4]);
                        admin.setUser_id(Integer.parseInt(userData[0]));
                    } else if (userData[5].equals("borrower")) {
                        Borrower borrower = new Borrower(userData[1], userData[2], userData[3], userData[4]);
                        borrower.setUser_id(Integer.parseInt(userData[0]));
                        borrowers.add(borrower);
                    } else if (userData[5].equals("customer")) {
                        Customer customer = new Customer(userData[1], userData[2], userData[3], userData[4]);
                        customer.setUser_id(Integer.parseInt(userData[0]));
                        customers.add(customer);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users from file: " + e.getMessage());
        }
        // Read books
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] bookData = line.split("\\|");
                if (bookData.length == 8) {
                    String title = bookData[0];
                    String author = bookData[1];
                    int publicationDate = Integer.parseInt(bookData[2]);
                    boolean availability = Boolean.parseBoolean(bookData[3]);
                    float price = Float.parseFloat(bookData[4]);
                    String genre = bookData[5];
                    double rating = Double.parseDouble(bookData[6]);
                    int quantity = Integer.parseInt(bookData[7]);

                    Book book = new Book(price, title, author, publicationDate, availability, genre, rating, quantity);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the books file: " + e.getMessage());
        }
        // Read orders
        try (BufferedReader br = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] orderData = line.split("\\|");

                if (orderData.length >= 5) {
                    String orderId = orderData[0];
                    int customerId = Integer.parseInt(orderData[1]);

                    ArrayList<Book> orderBooks = new ArrayList<>();
                    List<String> bookTitles = Arrays.asList(orderData[2].split(","));

                    for (String title : bookTitles) {
                        boolean bookFound = false;
                        for (Book book : books) {
                            if (book.getbookTitle().equalsIgnoreCase(title.trim())) {
                                orderBooks.add(book);
                                bookFound = true;
                                break;
                            }
                        }
                        if (!bookFound) {
                            System.err.println("Error: Book with title \"" + title.trim() + "\" not found.");
                        }
                    }
                    ArrayList<Integer> quantities = new ArrayList<>();
                    for (String quantity : orderData[3].split(",")) {
                        quantities.add(Integer.parseInt(quantity));
                    }

                    // check books and quantities
                    if (orderBooks.isEmpty() || quantities.isEmpty() || orderBooks.size() != quantities.size()) {
                        System.err.println("Error: Books and/or quantities are mismatched or empty for order ID " + orderId);
                        continue;
                    }
                    double totalPriceBeforeDiscount = Double.parseDouble(orderData[4]);
                    double totalPriceAfterDiscount = (orderData.length >= 6)
                            ? Double.parseDouble(orderData[5])
                            : totalPriceBeforeDiscount; // Fallback if no discount

                    Order order = new Order(orderId, new ArrayList<>(orderBooks), new ArrayList<>(quantities));
                    order.setTotalPrice(totalPriceBeforeDiscount);
                    order.setTotalPrice_after_discount(totalPriceAfterDiscount);

                    Customer customer = null;
                    for (Customer c : customers) {
                        if (c.getUser_id() == customerId) {
                            customer = c;
                            break;
                        }
                    }
                    if (customer != null) {
                        customer.getOrders().add(order);
                    } else {
                        System.err.println("Error: Customer with ID " + customerId + " not found for order ID " + orderId);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the orders file: " + e.getMessage());
        }

        // Read transactions
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //  "2024-12-12"
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] transactionData = line.split("\\|");
                if (transactionData.length == 6) {
                    int transactionId = Integer.parseInt(transactionData[0]);
                    int borrowerId = Integer.parseInt(transactionData[1]);
                    String bookTitle = transactionData[2].trim();
                    LocalDate borrowDate = LocalDate.parse(transactionData[3], formatter);
                    LocalDate returnDate = LocalDate.parse(transactionData[4], formatter);
                    LocalDate actualReturnDate = transactionData[5].equals("not returned yet")
                            ? null : LocalDate.parse(transactionData[5], formatter);

                    Book borrowedBook = null;
                    for (Book book : books) {
                        if (book.getbookTitle().equalsIgnoreCase(bookTitle)) {
                            borrowedBook = book;
                            break;
                        }
                    }
                    if (borrowedBook == null) {
                        System.err.println("Error: Book with title \"" + bookTitle + "\" not found.");
                        continue;
                    }

                    Borrower borrower = null;
                    for (Borrower b : borrowers) {
                        if (b.getUser_id() == borrowerId) {
                            borrower = b;
                            break;
                        }
                    }
                    if (borrower == null) {
                        System.err.println("Error: Borrower with ID \"" + borrowerId + "\" not found.");
                        continue;
                    }

                    Transaction transaction = new Transaction(borrowedBook, borrower);
                    transaction.setTransaction_id();
                    transaction.setBorrow_date(borrowDate);
                    transaction.setReturn_date(returnDate);
                    transaction.setActual_return_date(actualReturnDate);

                    Library.add_transaction(transaction);
                    borrower.addTransaction(transaction);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the transactions file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error while reading transactions: " + e.getMessage());
        }
        // Read cart
        try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    int userId = Integer.parseInt(parts[0]);
                    String bookTitle = parts[1].trim();
                    int quantity = Integer.parseInt(parts[2]);

                    Book book = null;
                    for (Book b : books) {
                        if (b.getbookTitle().equalsIgnoreCase(bookTitle)) {
                            book = b;
                            break;
                        }
                    }
                    if (book != null) {
                        for (Customer customer : customers) {
                            if (customer.getUser_id() == userId) {
                                customer.userCart.loadItem(book, quantity);
                                break;
                            }
                        }
                    } else {
                        System.out.println("Book with title \"" + bookTitle + "\" not found.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading cart file: " + e.getMessage());
        }
        // Read notifications
        try (BufferedReader reader = new BufferedReader(new FileReader(NOTIFICATIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove leading and trailing spaces

                // Skip blank lines
                if (line.isEmpty()) {
                    continue;
                }

                // Split the line, and check if it contains exactly 2 parts
                String[] parts = line.split("\\|", 2);
                if (parts.length < 2) {
                    continue;
                }

                try {
                    int borrowerId = Integer.parseInt(parts[0].trim()); // Parse the first part as an integer
                    String notification = parts[1].trim(); // Get the second part as the notification message

                    // Find the borrower with the corresponding ID and add the notification
                    boolean borrowerFound = false;
                    for (Borrower borrower : borrowers) {
                        if (borrower.getUser_id() == borrowerId) {
                            borrower.addNotificationsHistory(notification);
                            borrowerFound = true;
                            break;
                        }
                    }

                    if (!borrowerFound) {
                        System.out.println("Borrower with ID " + borrowerId + " not found.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid borrower ID in line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading notifications.txt: " + e.getMessage());
        }

        // Read reservations
        try (BufferedReader reader = new BufferedReader(new FileReader(RESERVATIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Validate input: Check if parts array has at least 2 elements
                if (parts.length < 2) {
                    System.out.println("Invalid line format: " + line);
                    continue; // Skip this line
                }

                // Validate that borrowerId and bookId are numeric
                if (!parts[0].matches("\\d+") || !parts[1].matches("\\d+")) {
                    System.out.println("Invalid line format (non-numeric ID): " + line);
                    continue; // Skip this line
                }

                // Parse borrower ID and book ID
                int borrowerId = Integer.parseInt(parts[0]);
                int bookId = Integer.parseInt(parts[1]);

                // Find the borrower
                Borrower borrower = null;
                for (Borrower b : borrowers) {
                    if (b.getUser_id() == borrowerId) {
                        borrower = b;
                        break;
                    }
                }

                // Find the book
                Book book = null;
                for (Book b : books) {
                    if (b.getbookId() == bookId) {
                        book = b;
                        break;
                    }
                }

                // Reserve the book if borrower and book are found
                if (borrower != null && book != null) {
                    Library.reserve_book(borrower, book);
                } else {
                    System.out.println("Could not find borrower or book for line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading reservations.txt: " + e.getMessage());
        }
    }
    public static void writedata(List<Borrower> borrowers, List<Customer> customers, Admin
            admin, List<Book> books) {
        // Write users
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            if (admin != null) {
                bw.write(admin.getUser_id() + "," + admin.getUser_name() + "," + admin.getUser_email() + "," +
                        admin.getUser_phone() + "," + admin.getPassword() + ",admin");
                bw.newLine();
            }

            for (Borrower borrower : borrowers) {
                bw.write(borrower.getUser_id() + "," + borrower.getUser_name() + "," + borrower.getUser_email() + "," +
                        borrower.getUser_phone() + "," + borrower.getPassword() + ",borrower");
                bw.newLine();
            }

            for (Customer customer : customers) {
                bw.write(customer.getUser_id() + "," + customer.getUser_name() + "," + customer.getUser_email() + "," +
                        customer.getUser_phone() + "," + customer.getPassword() + ",customer");
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing users to file: " + e.getMessage());
        }

        // Write books
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                bw.write(book.getBookDetails());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the books file: " + e.getMessage());
        }
        // write orders
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (Customer customer : customers) {
                for (Order order : customer.getOrders()) {
                    String bookTitles = String.join(",", order.get_books_names());

                    String quantities = order.getQuantities()
                            .stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","));

                    bw.write(order.getOrderId() + "|" +
                            customer.getUser_id() + "|" +
                            bookTitles + "|" +
                            quantities + "|" +
                            order.getTotalPrice() + "|" +
                            order.getTotalPrice_after_discount());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to the orders file: " + e.getMessage());
        }
        // Write transactions
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction transaction : Library.getTransactions()) {
                bw.write(transaction.getTransaction_id() + "|" +
                        transaction.getBorrower().getUser_id() + "|" +
                        transaction.getBook().getbookTitle() + "|" +
                        transaction.getBorrow_date() + "|" +
                        transaction.getReturn_date() + "|" +
                        (transaction.getActual_return_date() != null ? transaction.getActual_return_date() : "not returned yet"));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing transactions to file: " + e.getMessage());
        }

        // write cart
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CART_FILE))) {
            for (Customer customer : customers) {
                for (int i = 0; i < customer.userCart.getItems().size(); i++) {
                    String bookTitle = customer.userCart.getItems().get(i).getbookTitle();
                    int quantity = customer.userCart.getQuantities().get(i);
                    bw.write(customer.getUser_id() + "|" + bookTitle + "|" + quantity);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing the cart file: " + e.getMessage());
        }

        // Write notifications
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOTIFICATIONS_FILE))) {
            for (Borrower borrower : borrowers) {
                List<String> notifications = borrower.getNotificationHistory();
                for (String notification : notifications) {
                    writer.write(borrower.getUser_id() + "|" + notification);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing to notifications.txt: " + e.getMessage());
        }

        // Write reservations
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Borrower borrower : Library.getBorrowers_reserved()) {
                List<Book> reservedBooks = Library.getReservated_books();
                for (Book book : reservedBooks) {
                    writer.write(borrower.getUser_id() + "|" + book.getbookId());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing to reservations.txt: " + e.getMessage());
        }

    }
}
