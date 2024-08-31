package com.example.rqchallenge.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DummyResponse<T> {
    private String status;
    private T data;
    private String message;
}
