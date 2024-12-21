import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Order {
    private String orderId;
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Integer> quantities = new ArrayList<>();
    private double totalPrice;
    private double totalPrice_after_discount;

    public Order(String orderId, ArrayList<Book> books, ArrayList<Integer> quantities) {
        this.orderId = orderId;
        this.books = new ArrayList<>(books);  // Create a copy of the books list
        this.quantities = new ArrayList<>(quantities);  // Create a copy of the quantities list
        this.totalPrice = getTotalPrice();  // Calculate the total price upon creation
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public ArrayList<Integer> getQuantities() {
        return quantities;
    }

    public double getTotalPrice() {
        if (books.isEmpty() || quantities.isEmpty() || books.size() != quantities.size()) {
            throw new IllegalStateException("Books and quantities are not properly initialized.");
        }
        double totalPrice = 0.0;
        for (int i = 0; i < books.size(); i++) {
            totalPrice += books.get(i).getprice() * quantities.get(i);
        }
        return totalPrice;
    }

    public double getTotalPrice_after_discount() {
        return totalPrice_after_discount;
    }

    public void setTotalPrice_after_discount(double totalPrice_after_discount) {
        this.totalPrice_after_discount = totalPrice_after_discount;
    }

    public String get_books_names() {
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < books.size(); i++) {
            names.append(books.get(i).getbookTitle());
            if (i < books.size() - 1) {
                names.append(","); // Add a comma between titles except after the last one
            }
        }
        return names.toString();
    }

    public String get_books_quantities() {
        StringBuilder quantities_to_return = new StringBuilder();
        for (int quantity : quantities) {
            quantities_to_return.append(" ").append(quantity);
        }
        return quantities_to_return.toString();
    }


    @Override
    public String toString() {
        return "**********************************************************************\n" +
                "Order Id: [" + orderId + "]\n" +
                "Books: [" + get_books_names()+"]\n" +
                "Quantities: [" + get_books_quantities()+"]\n" +
                "Total Price Before Discount: " + getTotalPrice()+"\n" +
                "Total Price After Discount: " + totalPrice_after_discount +
                "\n************************************************************************\n";
    }

}