package com.nikitin.roadmaps.dto.response.pageable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Pageable {

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("totalPages")
    private Integer totalPages;

    @JsonProperty("totalElement")
    private Integer totalElement;

    @JsonProperty("number")
    private Integer number;

    @JsonProperty("last")
    private Boolean last;

    @JsonProperty("first")
    private Boolean first;
}
