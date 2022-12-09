package com.anma.rest.dto;

public record BlogDto(
        long id,
        String title,
        String body,
        String spaceKey
) {
}
