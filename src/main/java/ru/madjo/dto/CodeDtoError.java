package ru.madjo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CodeDtoError {

    private boolean hasError;

    private String errorMessage;

}
