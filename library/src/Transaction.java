import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private Book book;
    private Borrower borrower;
    private LocalDate borrow_date;
    private LocalDate return_date;
    private LocalDate actual_return_date;
    private int transaction_id;



    public Transaction(Book book, Borrower borrower) {
        this.book = book;
        this.borrower = borrower;
        borrow_date = LocalDate.now();
        return_date = set_dueDate();
        transaction_id = setTransaction_id();

    }
    public void setBorrow_date(LocalDate borrow_date) {
        this.borrow_date = borrow_date;
    }

    public int setTransaction_id() {
        return Library.getTransactions().size() + 1;
    }

    public void setReturn_date(LocalDate return_date) {
        this.return_date = return_date;
    }


    public Borrower getBorrower() {
        return borrower;
    }

    public void setActual_return_date(LocalDate actual_return_date) {
        this.actual_return_date = actual_return_date;
    }

    public LocalDate getActual_return_date() {
        return actual_return_date;
    }

    public Book getBook() {
        return book;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public LocalDate getReturn_date() {
        return return_date;
    }

    public LocalDate getBorrow_date() {
        return borrow_date;
    }

    public LocalDate set_dueDate() {
        return borrow_date.plusDays(7);
    }

    public double calc_fees() {
        if (return_date.isAfter(actual_return_date) || return_date.isEqual(actual_return_date)) {
            return 0;
        } else {
            return (ChronoUnit.DAYS.between(return_date, actual_return_date) * 10);
        }
    }

    @Override
    public String toString() {
        return "Transaction [" + transaction_id + "]: \n" +
                "Book: " + (book != null ? book.getbookTitle() : "null") + "\n"+
                "Borrower Id: " + (borrower != null ? borrower.getUser_id() : "null") +"\n"+
                "Borrow date: " + borrow_date +"\n"+
                "Return date: " + return_date +"\n"+
                "Actual return date: " + (actual_return_date != null ? actual_return_date : "not returned yet") +
                "\n*******************************************************************************************************************************************************\n";
    }

}