package com.example.wnabudgetbackend.dto.patch;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryGroupPatch {

    private String name;
    private UUID   user_id;
}