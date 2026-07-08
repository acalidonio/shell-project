package com.example.parcial.parcial2.services;

import com.example.parcial.parcial2.domain.dtos.BookRequestDto;
import com.example.parcial.parcial2.domain.dtos.GenreCountDto;
import com.example.parcial.parcial2.domain.entities.Book;
import com.example.parcial.parcial2.domain.entities.Genre;
import com.example.parcial.parcial2.repositories.BookRepository;
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
        return null;
    }

    public Book getBookById(UUID id) {
        return null;
    }

    public List<Book> getAllBooks(String author, String genreStr) {
        if (author != null && genreStr != null) {
            Genre genre = Genre.valueOf(genreStr.toUpperCase());
            return bookRepository.findByAuthorAndGenre(author, genre);
        } else if (author != null) {
            return bookRepository.findByAuthor(author);
        } else if (genreStr != null) {
            Genre genre = Genre.valueOf(genreStr.toUpperCase());
            return bookRepository.findByGenre(genre);
        }
        return bookRepository.findAll();
    }

    public Book updateBook(UUID id, BookRequestDto dto) {
        return null;
    }

    public void deleteBook(UUID id) {
    }

    public List<GenreCountDto> getGenresAvailable() {
        return null;
    }
}
