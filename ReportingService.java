public class ReportingService {
    private Transaction[] transactionHistory;
    private int transactionCount;
    private int nextTransactionNumber;

    public ReportingService() {
        transactionHistory = new Transaction[20];
        transactionCount = 0;
        nextTransactionNumber = 1001;

        addTransaction(new Transaction(generateTransactionId(), "Tey Yew Kuan", "Java Programming", 3));
        addTransaction(new Transaction(generateTransactionId(), "Ali Bin Ahmad", "Data Structures", 0));
        addTransaction(new Transaction(generateTransactionId(), "Tan Ah Kow", "Object-Oriented Design", 8));
    }

    public String generateTransactionId() {
        String id = "T" + nextTransactionNumber;
        nextTransactionNumber++;
        return id;
    }

    public void addTransaction(Transaction t) {
        if (transactionCount < transactionHistory.length) {
            transactionHistory[transactionCount] = t;
            transactionCount++;
        }
    }

    public Transaction[] getAllTransactions() {
        Transaction[] result = new Transaction[transactionCount];
        for (int i = 0; i < transactionCount; i++) {
            result[i] = transactionHistory[i];
        }
        return result;
    }

    public double getTotalFinesCollected() {
        double total = 0;
        for (int i = 0; i < transactionCount; i++) {
            total += transactionHistory[i].getFineAmount();
        }
        return total;
    }
}