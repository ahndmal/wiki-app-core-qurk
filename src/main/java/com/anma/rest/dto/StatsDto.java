package com.anma.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StatsDto(
        long pages,
        long spaces,
        long blogs,
        long comments
) {}
