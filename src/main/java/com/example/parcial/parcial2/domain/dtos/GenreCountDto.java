package com.example.parcial.parcial2.domain.dtos;

import com.example.parcial.parcial2.domain.entities.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreCountDto {
    private String genre;
    private long count;

    public GenreCountDto(Genre genreEnum, Long count) {
        this.genre = genreEnum != null ? genreEnum.name() : null;
        this.count = count != null ? count : 0L;
    }
}
