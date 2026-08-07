package com.library;

import java.util.ArrayList;
import java.util.List;

public class LibraryService implements Searchable {
    // 聚合关系 Aggregation: LibraryService 拥有多个 Book 对象
    private List<Book> bookList;

    public LibraryService() {
        bookList = new ArrayList<>();
        // 预载初始化图书数据 (测试用)
        bookList.add(new Book("B001", "Java Programming", "James Gosling"));
        bookList.add(new Book("B002", "Object-Oriented Design", "Robert Martin"));
        bookList.add(new Book("B003", "Data Structures & Algorithms", "Mark Allen"));
    }

    // 实现组员 A 定义的 Searchable 接口中的方法
    @Override
    public void search(String keyword) {
        System.out.println("正在搜索关键词: " + keyword);
    }

    // 搜寻并返回匹配的图书列表
    public List<Book> searchBooks(String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book book : bookList) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) || 
                book.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getBookId().equalsIgnoreCase(keyword)) {
                results.add(book);
            }
        }
        return results;
    }

    // 核心借书逻辑
    public String borrowBook(String bookId) {
        for (Book book : bookList) {
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                if (book.isBorrowed()) {
                    return "失败：图书《" + book.getTitle() + "》已经被借出了！";
                } else {
                    book.setBorrowed(true);
                    return "成功：你已成功借阅《" + book.getTitle() + "》！";
                }
            }
        }
        return "错误：未找到 ID 为 " + bookId + " 的图书！";
    }

    // 核心还书逻辑
    public String returnBook(String bookId) {
        for (Book book : bookList) {
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                if (!book.isBorrowed()) {
                    return "提示：该图书《" + book.getTitle() + "》本来就未被借出。";
                } else {
                    book.setBorrowed(false);
                    return "成功：图书《" + book.getTitle() + "》已成功归还！";
                }
            }
        }
        return "错误：未找到 ID 为 " + bookId + " 的图书！";
    }

    public List<Book> getAllBooks() {
        return bookList;
    }
}