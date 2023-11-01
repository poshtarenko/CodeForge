package com.poshtarenko.codeforge.entity.code;


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
public class Code {

    Boolean isCompleted;

    String error;

}