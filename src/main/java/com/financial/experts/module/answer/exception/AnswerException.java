package com.financial.experts.module.answer.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class AnswerException extends RuntimeException {
    private final HttpStatus status;

    public AnswerException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}