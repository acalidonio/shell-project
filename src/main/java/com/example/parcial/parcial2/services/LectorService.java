package com.example.parcial.parcial2.services;

import com.example.parcial.parcial2.domain.dtos.LectorRequestDto;
import com.example.parcial.parcial2.domain.entities.Lector;
import com.example.parcial.parcial2.repositories.LectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LectorService {

    private final LectorRepository lectorRepository;

    public LectorService(LectorRepository lectorRepository) {
        this.lectorRepository = lectorRepository;
    }

    public Lector registerLector(LectorRequestDto dto) {
        Lector lector = new Lector();
        lector.setName(dto.getName());
        lector.setDui(dto.getDui());
        lector.setLastname(dto.getLastname());
        lector.setEmail(dto.getEmail());

        return lectorRepository.save(lector);
    }

    public Lector getLectorById(UUID id) {
        return lectorRepository.findById(id).orElseThrow();
    }

    public List<Lector> getAllLectors() {
        return lectorRepository.findAll();
    }

    public Lector updateLector(UUID id, LectorRequestDto dto) {
        Lector lector = lectorRepository.findById(id).orElseThrow();
        lector.setName(dto.getName());
        lector.setDui(dto.getDui());
        lector.setLastname(dto.getLastname());
        lector.setEmail(dto.getEmail());

        return lectorRepository.save(lector);
    }

    public void deleteLector(UUID id) {
        lectorRepository.deleteById(id);
    }

    public Lector deleteLectorSafe(UUID id) {
        Lector lector = lectorRepository.findById(id).orElseThrow();
        lector.setActive(false);
        return lectorRepository.save(lector);
    }
}
