package com.anma.rest.dto;

public record SpaceDTO(
        long id,
        String title,
        String category,
        String key,
        long authorId
) {
}
