package com.example.Bai2.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    private List<Book> books = new java.util.ArrayList<>();

    public BookService() {
        this.books = new java.util.ArrayList<>();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void addBook(Book book) {
        // Luôn set id = 0 để Hibernate tự sinh id mới
        book.setId(0);
        bookRepository.save(book);
        books.add(book);
    }

    public void updateBook(int id, Book updatedBook) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            bookRepository.save(book);
        }
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
        books.removeIf(book -> book.getId() == id);
    }
}
