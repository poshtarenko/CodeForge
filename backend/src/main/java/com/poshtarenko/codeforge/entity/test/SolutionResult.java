package com.poshtarenko.codeforge.entity.test;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class SolutionResult {

    Boolean isCompleted;

    String error;

}