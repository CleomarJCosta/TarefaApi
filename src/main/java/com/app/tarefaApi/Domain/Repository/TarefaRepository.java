package com.app.tarefaApi.Domain.Repository;

import com.app.tarefaApi.Domain.Entity.Tarefa;
import com.app.tarefaApi.Domain.Entity.Usuario;
import com.app.tarefaApi.Domain.Enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findTarefaByStatus(Status status);
}
