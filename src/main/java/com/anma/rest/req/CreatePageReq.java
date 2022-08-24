package com.anma.rest.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreatePageReq(
        @JsonProperty(value = "title")
        String title,
        String body,
        @JsonProperty(value = "space_key")
        String spaceKey
) {
}
