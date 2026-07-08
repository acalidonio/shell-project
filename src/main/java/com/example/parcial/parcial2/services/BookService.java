package com.example.parcial.parcial2.services;

import com.example.parcial.parcial2.domain.dtos.BookRequestDto;
import com.example.parcial.parcial2.domain.dtos.GenreCountDto;
import com.example.parcial.parcial2.domain.entities.Book;
import com.example.parcial.parcial2.domain.entities.Genre;
import com.example.parcial.parcial2.exception.BusinessRuleException;
import com.example.parcial.parcial2.repositories.BookRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(BookRequestDto dto) {
        Book book = new Book();
        return setBookParameters(dto, book);
    }

    public Book getBookById(UUID id) {
        return bookRepository.findById(id).orElseThrow();
    }

    public List<Book> getAllBooks(String author, String genreStr) {
        if (author != null && genreStr != null) {
            Genre genre = Genre.valueOf(genreStr.toUpperCase());
            return bookRepository.findByAuthorAndGenre(author, genre).stream().filter(Book::isActive).toList();
        } else if (author != null) {
            return bookRepository.findByAuthor(author).stream().filter(Book::isActive).toList();
        } else if (genreStr != null) {
            Genre genre = Genre.valueOf(genreStr.toUpperCase());
            return bookRepository.findByGenre(genre).stream().filter(Book::isActive).toList();
        }
        return bookRepository.findAll().stream().filter(Book::isActive).toList();
    }

    public Book updateBook(UUID id, BookRequestDto dto) {
        Book book = bookRepository.findById(id).orElseThrow();
        return setBookParameters(dto, book);
    }

    @NonNull
    public Book setBookParameters(BookRequestDto dto, Book book) {
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setAvailable(dto.isAvailable());
        book.setAvailableCount(dto.getAvailableCount());
        if (dto.getGenre() != null) {
            book.setGenre(Genre.valueOf(dto.getGenre().toUpperCase()));
        }
        return bookRepository.save(book);
    }

    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }

    public void deleteBookSafe(UUID id) {
        Book book = bookRepository.findById(id)
                .filter(Book::isActive)
                .orElseThrow(() -> new BusinessRuleException("Producto no encontrado o ya es inactivo")); // Usar otra excepcion como resource not found

        book.setActive(false);
        bookRepository.save(book);
    }

    public void restoreBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Producto no encontrado"));

        if (book.isActive()) {
            throw new BusinessRuleException("El producto ya se encuentra activo");
        }

        book.setActive(true);
        bookRepository.save(book);
    }

    public List<GenreCountDto> getGenresAvailable() {
        return bookRepository.countBooksByGenre();
    }
}
