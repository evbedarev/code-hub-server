package ru.madjo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectCodeDto {

    private long id;

    private String projectName;
}
