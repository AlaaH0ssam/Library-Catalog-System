
public class Book {
    private int book_id;
    private String book_title;
    private String author;
    private int publication_year;
    private boolean availability;
    private float price;
    private String genre;
    private double rating;
    private int ratingCount;
    private int book_quantity;

    public Book(float price, String book_title, String author, int publication_date, boolean availability, String genre, double rating, int book_quantity) {
        this.price = price;
        this.book_id = setbookId();
        this.book_title = book_title;
        this.author = author;
        this.publication_year = publication_date;
        this.availability = availability;
        this.genre = genre;
        this.rating = rating;
        this.ratingCount = 1;
        this.book_quantity = book_quantity;
    }


    public int getBook_quantity() {
        return book_quantity;
    }

    public void setBook_quantity(int book_quantity) {
        this.book_quantity = book_quantity;
        if (book_quantity <= 0) {
            availability = false;
        } else {
            availability = true;
        }
    }

    public int setbookId() {
        return Library.getBooks().size() + 1;
    }

    public void setbookTitle(String book_title) {
        this.book_title = book_title;
    }

    public void setavailability(boolean availability) {
        this.availability = availability;
    }

    public void set_price(float price) {
        this.price = price;
    }

    public int getbookId() {
        return book_id;
    }

    public String getbookTitle() {
        return book_title;
    }

    public String getauthor() {
        return author;
    }

    public boolean checkavailability() {
        return availability;
    }

    public float getprice() {
        return price;
    }


    public void addRating(double newRating) {
        rating = ((rating * ratingCount) + newRating) / (++ratingCount);
    }



    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {   //CHANGED!!!!!
        return "\n*****************************************************************************\n" +
                "book_id=" + book_id + "\n" +
                ", book_title='" + book_title + "\n" +
                ", author='" + author + "\n" +
                ", publication_date=" + publication_year + "\n" +
                ", availability=" + availability + "\n" +
                ", price=" + price + "\n" +
                ", genre='" + genre + "\n" +
                ", rating=" + rating + "\n" +
                ", quantity=" + book_quantity +
                "\n*****************************************************************************\n"
                ;
    }

    public String getBookDetails() {
        return book_title + "|" + author + "|" + publication_year + "|" + availability + "|" + price + "|" + genre + "|" + rating + "|" + book_quantity;
    }
}