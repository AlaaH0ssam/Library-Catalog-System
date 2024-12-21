import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Cart {
    private ArrayList<Book> items = new ArrayList<>(0);          // List of books in the cart
    private ArrayList<Integer> quantities = new ArrayList<>(0);
    Scanner scanner =new Scanner(System.in);
    public Cart() {
        items = new ArrayList<>();
        quantities = new ArrayList<>();

    }

    // Add a book to the cart or update its quantity
    public void addItem(Book book, int quantity) {              // CHANGED!!
        int index = items.indexOf(book); // Check if the book already exists in the cart
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        // Check if the requested quantity is greater than or equal to available stock
        Book libraryBook = null;
        for (Book b : Library.getBooks()) {
            if (b.getbookId() == book.getbookId()) {
                libraryBook = b;  // Find the specific book in the library
                break;
            }
        }

        if (libraryBook != null) {
            // Allow ordering if the quantity is less than or equal to available stock
            if (libraryBook.getBook_quantity() < quantity) {
                System.out.println("Out of stock");
                return; // Exit if not enough stock
            }
        } else {
            System.out.println("Error: Book not found in library.");
            return;
        }

        // If the book is already in the cart, update its quantity
        if (index != -1) {
            // If the book exists, update the quantity in the cart
            quantities.set(index, quantities.get(index) + quantity);
        } else {
            // Add the book and its quantity to the cart
            items.add(book);
            quantities.add(quantity);
            System.out.println("Added " + quantity + " of \"" + book.getbookTitle() + "\" to the cart.");
            System.out.println("Would you like to rate this book? (Y/N)");
            String rateChoice = scanner.nextLine();
            if (rateChoice.equalsIgnoreCase("Y")) {
                System.out.print("Enter your rating for \"" + book.getbookTitle() + "\" (0-5): ");
                double rating = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                book.addRating(rating);
                System.out.println("Thanks for rating \"" + book.getbookTitle() + "\" " + rating + "!");
            }
        }

        // Update the stock in the library for the specific book
        int updatedStock = libraryBook.getBook_quantity() - quantity;
        libraryBook.setBook_quantity(updatedStock); // Only update the stock for the specific book
    }


    public void deleteItem(String name, int quantityToDelete) {
        boolean found = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getbookTitle().equalsIgnoreCase(name)) {
                found = true;
                int currentQuantity = quantities.get(i);

                if (currentQuantity <= quantityToDelete) {
                    // If the quantity in the cart is less than or equal to the quantity to delete, remove the item completely
                    items.remove(i);
                    quantities.remove(i);
                    System.out.println("Removed " + currentQuantity + " of book " + name + " from the cart.");

                    // Update the stock quantity
                    for (Book book : Library.getBooks()) {
                        if (book.getbookTitle().equalsIgnoreCase(name)) {
                            book.setBook_quantity(book.getBook_quantity() + quantityToDelete);
                            break;
                        }
                    }
                } else {
                    // If the quantity to delete is less than the cart quantity, just decrease the quantity
                    quantities.set(i, currentQuantity - quantityToDelete);
                    System.out.println("Reduced quantity of book " + name + " by " + quantityToDelete);

                    // Update the stock for the book
                    for (Book book : Library.getBooks()) {
                        if (book.getbookTitle().equalsIgnoreCase(name)) {
                            book.setBook_quantity(book.getBook_quantity() + quantityToDelete);
                            break;
                        }
                    }
                }

                break;
            }
        }

        if (!found) {
            System.out.println("Book with name " + name + " not found in the cart.");
        }
    }

    public  double calculateTotalPrice() {      //CHANGED
        double totalPrice = 0.0;
        for (int i = 0; i < items.size(); i++) {
            totalPrice += items.get(i).getprice() * quantities.get(i);
        }
        // Apply discounts based on total price
        if(totalPrice<500){
            System.out.println("Total price :" + totalPrice);
        }
        else if (totalPrice >= 500 && totalPrice <= 1000) {
            System.out.println("Total price before discount:" + totalPrice);
            totalPrice -= totalPrice * (1.0 / 10);
            System.out.println("Total price after discount:" + totalPrice);// 10% discount
        } else if (totalPrice > 1000 && totalPrice <= 10000) {
            System.out.println("Total price before discount:" + totalPrice);
            totalPrice -= totalPrice * (2.0 / 10);  // 20% discount
            System.out.println("Total price after discount:" + totalPrice);
        }
        return totalPrice;
    }


    // View the cart details
    public boolean viewCartDetails() {
        if (items.isEmpty()) {
            System.out.println("The cart is empty.");
            return false;
        } else {
            System.out.println("Cart Details:");
            for (int i = 0; i < items.size(); i++) {
                Book book = items.get(i);
                int quantity = quantities.get(i);
                System.out.println(items.get(i).getbookTitle() +" by Author "+items.get(i).getauthor()+" ($"+items.get(i).getprice()+") with quantity "+ quantity);
                System.out.println("Total price"+ calculateTotalPrice());
            }
            return  true;
        }
    }

    // Calculate total price of items in the cart

    private void payment(double total) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a payment method: ");
        System.out.println("1. Credit Card");
        System.out.println("2. Cash on Delivery");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("\nProcessing payment via Credit Card...");
                System.out.println("\n");
                System.out.println("Enter your credit card details:");
                System.out.println("Card Number: ");
                String cardNumber = scanner.nextLine();

                System.out.println("Card Holder Name: ");
                String cardHolderName = scanner.nextLine();

                System.out.println("Expiry Date (MM/YY): ");
                String expiryDate = scanner.nextLine();

                System.out.println("CVV: ");
                String cvv = scanner.nextLine();

                // Simulate payment processing
                System.out.println("Processing payment...");
                System.out.println("Payment successful. Thank you!");

                clearCart();
                break;

            case 2:
                System.out.println("\nPayment will be made on delivery.");
                break;
            default:
                System.out.println("Invalid choice! Processing payment via default method (Credit Card).");
                break;
        }

        System.out.println("Total amount after discount : $" + total);
        System.out.println("Payment successful. Thank you!");
    }

    public void checkout() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty. Add items before checking out.");
            System.out.println("\n");
            return;
        }
        double total = calculateTotalPrice();
        payment(total);
        clearCart();
    }

    public void clearCart() {
        items.clear();
        quantities.clear();
        System.out.println("Cart has been cleared after checkout.");
    }

    public ArrayList<Book> getItems() {
        ArrayList<Book> items_to_send = new ArrayList<>(0);
        for(Book book: items){
            items_to_send.add(book);
        }
        return items_to_send;
    }

    public ArrayList<Integer> getQuantities() {
        return quantities;
    }

    public void loadItem(Book book, int quantity) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity for book: " + book.getbookTitle());
            return;
        }
        // Check if the book already exists in the cart
        int index = items.indexOf(book);
        if (index != -1) {
            quantities.set(index, quantities.get(index) + quantity);
        } else {
            items.add(book);
            quantities.add(quantity);
        }
    }

}