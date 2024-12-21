
import java.util.ArrayList;
import java.io.*;

public class Customer extends User {
    private ArrayList<Order> orders = new ArrayList<>(0);
    Cart userCart = new Cart();

    public Customer(String user_name,String user_email, String user_phone,String password){
        super(user_name, user_email, user_phone,password);
        user_id = set_id();
        this.orders = new ArrayList<>();
    }

    public int set_id(){
        return Library.getCustomers().size()+1;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }


    public void add_order(Order order) {
        if (order != null) {
            orders.add(order);
            System.out.println("Order with ID: " + order.getOrderId() + " has been added successfully.");

        } else {
            System.out.println("Invalid order. Cannot add a null order.");
        }

    }

    @Override
    public void view_history(){
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        // Display orders
        for (Order order : orders) {
            System.out.println(order);
        }
    }
}