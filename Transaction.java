public class Transaction {
    private final String transactionId;
    private String userName;
    private String bookTitle;
    private int overdueDays;
    private double fineAmount;

    public Transaction(String transactionId, String userName, String bookTitle, int overdueDays) {
        this.transactionId = transactionId;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.overdueDays = overdueDays;
        this.fineAmount = calculateFine(overdueDays);
    }

    private double calculateFine(int days) {
        if (days <= 0) return 0.0;
        return days * 0.50;
    }

    public String getTransactionId() { return transactionId; }
    public String getUserName() { return userName; }
    public String getBookTitle() { return bookTitle; }
    public int getOverdueDays() { return overdueDays; }
    public double getFineAmount() { return fineAmount; }

    @Override
    public String toString() {
        return "[" + transactionId + "] " + userName + " - " + bookTitle
                + " | Overdue: " + overdueDays + " day(s) | Fine: RM " + String.format("%.2f", fineAmount);
    }
}