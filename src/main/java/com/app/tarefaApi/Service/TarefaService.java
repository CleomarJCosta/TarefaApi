package com.app.tarefaApi.Service;

import com.app.tarefaApi.Domain.Entity.Tarefa;
import com.app.tarefaApi.Domain.Enums.Status;
import com.app.tarefaApi.Exception.TaskNotFoundException;
import com.app.tarefaApi.Exception.UserNotFoundException;
import com.app.tarefaApi.Rest.DTO.TarefaDTO;

import java.util.List;
import java.util.Optional;


public interface TarefaService {
    Tarefa criarTarefa(TarefaDTO tarefaDTO) throws UserNotFoundException;
    Optional<Tarefa> obterTarefaPorId(Long id) throws TaskNotFoundException;
    List<Tarefa> obterTodasTarefas();
    Tarefa atualizaTarefa(Long id, TarefaDTO tarefaDTO) throws UserNotFoundException, TaskNotFoundException;
    void excluiTarefa(Long id) throws TaskNotFoundException;
    List<Tarefa> listaTarefasPorStatus(Status status);
}
