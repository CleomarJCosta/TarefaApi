package com.app.tarefaApi.Rest.Controller;

import com.app.tarefaApi.Domain.Entity.Tarefa;
import com.app.tarefaApi.Domain.Enums.Status;
import com.app.tarefaApi.Exception.TaskNotFoundException;
import com.app.tarefaApi.Exception.UserNotFoundException;
import com.app.tarefaApi.Rest.DTO.TarefaDTO;
import com.app.tarefaApi.Service.Impl.TarefaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Builder
@RestController
@RequestMapping("api/tarefas")
@Tag(name = "Tarefas", description = "Endpoints para gestão de tarefas")
public class TarefaController {
    @Autowired
    private TarefaServiceImpl tarefaService;

    private static final Logger logger = LoggerFactory.getLogger(TarefaController.class);


    @Operation(summary = "Cria uma nova tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso", content = @Content(schema = @Schema(implementation = Tarefa.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping
    public ResponseEntity<?> criarTarefa(@RequestBody TarefaDTO tarefaDTO) {
        logger.info("Recebendo solicitação para criar tarefa.");
        try {
            Tarefa tarefa = tarefaService.criarTarefa(tarefaDTO);
            logger.info("Tarefa criada com sucesso: {}", tarefa);
            return ResponseEntity.status(HttpStatus.CREATED).body(tarefa);
        } catch (UserNotFoundException e) {
            logger.warn("Usuário não encontrado ao tentar criar tarefa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao criar tarefa", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar tarefa.");
        }
    }


    @Operation(summary = "Lista todas as tarefas")
    @ApiResponse(responseCode = "200", description = "Listagem bem-sucedida")
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodasTarefas(){
        logger.info("Listando todas as tarefas.");
        List<Tarefa> tarefas =  tarefaService.obterTodasTarefas();
        logger.info("Total de tarefas encontradas: {}", tarefas.size());
        return ResponseEntity.ok(tarefas);
    }


    @Operation(summary = "Busca tarefa pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Tarefa>> buscarTarefaPorId(@PathVariable Long id) throws TaskNotFoundException {
        logger.info("Buscando tarefa com ID: {}", id);
        Optional<Tarefa> tarefa = tarefaService.obterTarefaPorId(id);
        if (tarefa.isPresent()) {
            logger.info("Tarefa encontrada: {}", tarefa);
        } else {
            logger.warn("Tarefa com ID {} não encontrada.", id);
        }
        return ResponseEntity.ok(tarefa);
    }


    @Operation(summary = "Deleta tarefa pelo ID", description = "Deleta uma tarefa existente. Apenas o criador da tarefa pode atualizá-la.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) throws TaskNotFoundException {
        logger.info("Tentativa de exclusão da tarefa com ID: {}", id);
        tarefaService.excluiTarefa(id);
        logger.info("Tarefa com ID {} excluída com sucesso.", id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Busca tarefas pelo status")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Tarefa>> buscaTarefaPorStatus(@PathVariable Status status){
        logger.info("Buscando tarefas com status: {}", status);
        List<Tarefa> tarefas = tarefaService.listaTarefasPorStatus(status);
        logger.info("Total de tarefas com status {} encontradas: {}", status, tarefas.size());
        return ResponseEntity.ok(tarefas);
    }


    @Operation(summary = "Atualizar tarefa", description = "Atualiza os dados de uma tarefa existente. Apenas o criador da tarefa pode atualizá-la.")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizaTarefa(
            @Parameter(description = "ID da tarefa a ser atualizada") @PathVariable Long id,
            @Valid @RequestBody TarefaDTO tarefaDTO) {
        logger.info("Recebendo solicitação para atualizar tarefa com ID: {}", id);
        try {
            Tarefa tarefaAtualizada = tarefaService.atualizaTarefa(id, tarefaDTO);
            logger.info("Tarefa com ID {} atualizada com sucesso.", id);
            return ResponseEntity.ok(tarefaAtualizada);
        } catch (TaskNotFoundException e) {
            logger.warn("Tarefa com ID {} não encontrada: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            logger.warn("Acesso negado ao tentar atualizar a tarefa com ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar a tarefa com ID {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar tarefa.");
        }
    }
}

















