package com.challeng.shopping_cart.todo.repository;

import com.challeng.shopping_cart.todo.model.Todo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TodoRepository extends ReactiveMongoRepository<Todo, String> {
}
