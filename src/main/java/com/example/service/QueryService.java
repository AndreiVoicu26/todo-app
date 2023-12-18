package com.example.service;

import com.example.entity.Todo;
import com.example.entity.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@Stateless
public class QueryService {
    @Inject
    EntityManager entityManager;

    public User findUserByEmail(String email) {
        List<User> userList = entityManager.createNamedQuery(User.FIND_USER_BY_EMAIL, User.class)
                .setParameter("email", email)
                .getResultList();
        if (!userList.isEmpty()) {
            return userList.get(0);
        }
        return null;
    }

    public List<?> countUserByEmail(String email) {
        return entityManager.createNativeQuery("select count (id) from TodoUser where exists(select id from TodoUser where email = ? )")
                .setParameter(1, email).getResultList();
    }

    public Todo findTodoById(long id, String email) {
        List<Todo> resultList = entityManager.createNamedQuery(Todo.FIND_TODO_BY_ID, Todo.class)
                .setParameter("id", id)
                .setParameter("email", email)
                .getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    public List<Todo> getAllTodos(String email) {
        return entityManager.createNamedQuery(Todo.FIND_ALL_TODOS_BY_USER, Todo.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<Todo> getAllTodosByTask(String taskText, String email) {
        return entityManager.createNamedQuery(Todo.FIND_TODO_BY_TASK, Todo.class)
                .setParameter("task", "%" + taskText + "%")
                .setParameter("email", email)
                .getResultList();
    }


}
