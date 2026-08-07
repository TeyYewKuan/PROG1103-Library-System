package com.library;

import java.time.LocalDate;

public class Transaction {
    private String transactionId;
    private String userName;
    private String bookTitle;
    private LocalDate borrowDate;
    private int overdueDays;
    private double fineAmount;

    public Transaction(String transactionId, String userName, String bookTitle, LocalDate borrowDate, int overdueDays) {
        this.transactionId = transactionId;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.overdueDays = overdueDays;
        this.fineAmount = calculateFine(overdueDays); // 自动计算罚款
    }

    // 逾期罚款计算逻辑 (每天 RM 0.50)
    private double calculateFine(int days) {
        if (days <= 0) return 0.0;
        return days * 0.50; 
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getUserName() { return userName; }
    public String getBookTitle() { return bookTitle; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public int getOverdueDays() { return overdueDays; }
    public double getFineAmount() { return fineAmount; }
}