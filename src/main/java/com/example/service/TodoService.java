package com.example.service;

import com.example.entity.Todo;
import com.example.entity.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.Map;

@Stateless
public class TodoService {
    @Inject
    EntityManager entityManager;
    @Inject
    private QueryService queryService;
    @Inject
    private SecurityUtil securityUtil;

    public User saveUser(User user) {
        Long count = (Long) queryService.countUserByEmail(user.getEmail()).get(0);

        if (user.getId() == null && count == 0) {
            Map<String, String> credMap = securityUtil.hashPassword(user.getPassword());

            user.setPassword(credMap.get(SecurityUtil.HASHED_PASSWORD_KEY));
            user.setSalt(credMap.get(SecurityUtil.SALT_KEY));

            entityManager.persist(user);
            credMap.clear();
        }
        return user;
    }

    public Todo createTodo(Todo todo, String email) {
        User userByEmail = queryService.findUserByEmail(email);
        if (userByEmail != null) {
            todo.setTodoOwner(userByEmail);
            entityManager.persist(todo);
        }
        return todo;
    }

    public Todo updateTodo(Long id, Todo todo, String email) {
        User userByEmail = queryService.findUserByEmail(email);
        Todo currentTodo = queryService.findTodoById(id, email);
        if (userByEmail != null && currentTodo != null) {
            currentTodo.setTodoOwner(userByEmail);
            currentTodo.setTask(todo.getTask());
            currentTodo.setDueDate(todo.getDueDate());
            currentTodo.setIsCompleted(todo.getIsCompleted());
            currentTodo.setDateCompleted(todo.getDateCompleted());
            return entityManager.merge(currentTodo);
        }
        return null;
    }
}
