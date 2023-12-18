package com.example.rest;

import com.example.entity.Todo;
import com.example.service.QueryService;
import com.example.service.TodoService;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.util.List;

@Path("todo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authz
public class TodoRest {
    @EJB
    TodoService todoService;
    @Inject
    QueryService queryService;
    @Context
    SecurityContext securityContext;


    @Path("/new")
    @POST
    public Response createTodo(Todo todo) {
        todoService.createTodo(todo, securityContext.getUserPrincipal().getName());
        return Response.ok(todo).build();
    }

    @Path("/update/{id}")
    @PUT
    public Response updateTodo(@PathParam("id") Long id, Todo todo) {
        Todo updatedTodo = todoService.updateTodo(id, todo, securityContext.getUserPrincipal().getName());
        if (updatedTodo == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedTodo).build();
    }

    @Path("{id}")
    @GET
    public Todo getTodo(@PathParam("id") Long id) {
        return queryService.findTodoById(id, securityContext.getUserPrincipal().getName());
    }

    @Path("list")
    @GET
    public List<Todo> getTodos() {
        return queryService.getAllTodos(securityContext.getUserPrincipal().getName());
    }

    @Path("status")
    @POST
    public Response markAsComplete(@QueryParam("id") Long id){
        Todo todo = queryService.findTodoById(id, securityContext.getUserPrincipal().getName());
        todo.setIsCompleted(true);
        todo.setDateCompleted(LocalDate.now());
        todoService.updateTodo(id, todo, securityContext.getUserPrincipal().getName());
        return Response.ok(todo).build();
    }
}
