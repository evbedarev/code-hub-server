package ru.madjo.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class CodeDto {

    @NonNull
    private String version;

    @NonNull
    private String projectName;

    @NonNull
    private String filePath;

    @NonNull
    private String codeText;
}
