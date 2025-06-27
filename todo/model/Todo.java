package com.challeng.shopping_cart.todo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Todo {
    @Id
    private String id;
    private String name;
    private boolean completed;
}
