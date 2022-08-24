package com.anma.rest.dto;

public record PageDTO(
        long id,
        String title,
        String body,
        String spaceKey
) {
}
