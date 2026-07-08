package com.example.parcial.parcial2.services;

import com.example.parcial.parcial2.domain.dtos.MovementRequestDto;
import com.example.parcial.parcial2.domain.entities.Movement;
import com.example.parcial.parcial2.domain.entities.MovementType;
import com.example.parcial.parcial2.domain.entities.Lector;
import com.example.parcial.parcial2.domain.entities.Book;
import com.example.parcial.parcial2.repositories.BookRepository;
import com.example.parcial.parcial2.repositories.LectorRepository;
import com.example.parcial.parcial2.repositories.MovementRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final LectorRepository lectorRepository;
    private final BookRepository bookRepository;

    public MovementService(MovementRepository movementRepository,
                           LectorRepository lectorRepository,
                           BookRepository bookRepository) {
        this.movementRepository = movementRepository;
        this.lectorRepository = lectorRepository;
        this.bookRepository = bookRepository;
    }

    public Movement borrowBook(MovementRequestDto dto) {
        Book book = bookRepository.findByIsbn(dto.getIsbn()).orElseThrow();
        Lector lector = lectorRepository.findByEmail(dto.getEmail()).orElseThrow();

        if (book.getAvailableCount() <= 0) {
            throw new IllegalArgumentException("No hay libros disponibles");
        }

        Optional<Movement> lastMovement = movementRepository.findTopByLectorAndBookOrderByTimestampDesc(lector, book);
        if (lastMovement.isPresent() && lastMovement.get().getType() == MovementType.BORROWING) {
            throw new IllegalArgumentException("Ya tienes una copia de este libro");
        }

        Movement movement = new Movement();
        movement.setBook(book);
        movement.setLector(lector);
        movement.setType(MovementType.BORROWING);
        movement.setTimestamp(Instant.now());

        book.setAvailableCount(book.getAvailableCount() - 1);
        bookRepository.save(book);

        return movementRepository.save(movement);
    }

    public Movement returnBook(MovementRequestDto dto) {
        Book book = bookRepository.findByIsbn(dto.getIsbn()).orElseThrow();
        Lector lector = lectorRepository.findByEmail(dto.getEmail()).orElseThrow();

        Optional<Movement> lastMovement = movementRepository.findTopByLectorAndBookOrderByTimestampDesc(lector, book);
        if (lastMovement.isEmpty() || lastMovement.get().getType() == MovementType.RETURN) {
            throw new IllegalArgumentException("No puedes devolver un libro que no has prestado");
        }

        Movement movement = new Movement();
        movement.setBook(book);
        movement.setLector(lector);
        movement.setType(MovementType.RETURN);
        movement.setTimestamp(Instant.now());

        book.setAvailableCount(book.getAvailableCount() + 1);
        bookRepository.save(book);

        return movementRepository.save(movement);
    }
}
