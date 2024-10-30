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


    @Operation(summary = "Cria uma nova tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso", content = @Content(schema = @Schema(implementation = Tarefa.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping
    public ResponseEntity<?> criarTarefa(@RequestBody TarefaDTO tarefaDTO) {
        try {
            Tarefa tarefa = tarefaService.criarTarefa(tarefaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(tarefa);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar tarefa.");
        }
    }


    @Operation(summary = "Lista todas as tarefas")
    @ApiResponse(responseCode = "200", description = "Listagem bem-sucedida")
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodasTarefas(){
        List<Tarefa> tarefas =  tarefaService.obterTodasTarefas();
        return ResponseEntity.ok(tarefas);
    }


    @Operation(summary = "Busca tarefa pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Tarefa>> buscarTarefaPorId(@PathVariable Long id) throws TaskNotFoundException {
        Optional<Tarefa> tarefa = tarefaService.obterTarefaPorId(id);
        return ResponseEntity.ok(tarefa);
    }


    @Operation(summary = "Deleta tarefa pelo ID", description = "Deleta uma tarefa existente. Apenas o criador da tarefa pode atualizá-la.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) throws TaskNotFoundException {
        tarefaService.excluiTarefa(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Busca tarefas pelo status")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Tarefa>> buscaTarefaPorStatus(@PathVariable Status status){
        List<Tarefa> tarefas = tarefaService.listaTarefasPorStatus(status);
        return ResponseEntity.ok(tarefas);
    }

    @Operation(summary = "Atualizar tarefa", description = "Atualiza os dados de uma tarefa existente. Apenas o criador da tarefa pode atualizá-la.")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizaTarefa(
            @Parameter(description = "ID da tarefa a ser atualizada") @PathVariable Long id,
            @Valid @RequestBody TarefaDTO tarefaDTO) {
        try {
            Tarefa tarefaAtualizada = tarefaService.atualizaTarefa(id, tarefaDTO);
            return ResponseEntity.ok(tarefaAtualizada);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar tarefa.");
        }
    }
}

















