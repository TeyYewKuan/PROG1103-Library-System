package com.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportingService {
    private List<Transaction> transactionHistory;

    public ReportingService() {
        transactionHistory = new ArrayList<>();
        // 预载初始化报表数据 (测试用)
        transactionHistory.add(new Transaction("T1001", "Tey Yew Kuan", "Java Programming", LocalDate.now().minusDays(10), 3));
        transactionHistory.add(new Transaction("T1002", "Ali Bin Ahmad", "Data Structures", LocalDate.now().minusDays(5), 0));
        transactionHistory.add(new Transaction("T1003", "Tan Ah Kow", "Object-Oriented Design", LocalDate.now().minusDays(15), 8));
    }

    public List<Transaction> getAllTransactions() {
        return transactionHistory;
    }

    // 统计系统总罚款金额
    public double getTotalFinesCollected() {
        double total = 0;
        for (Transaction t : transactionHistory) {
            total += t.getFineAmount();
        }
        return total;
    }

    // 添加新记录
    public void addTransaction(Transaction t) {
        transactionHistory.add(t);
    }
}