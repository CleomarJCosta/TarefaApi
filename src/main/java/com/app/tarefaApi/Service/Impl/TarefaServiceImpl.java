package com.app.tarefaApi.Service.Impl;

import com.app.tarefaApi.Domain.Entity.Tarefa;
import com.app.tarefaApi.Domain.Entity.Usuario;
import com.app.tarefaApi.Domain.Enums.Status;
import com.app.tarefaApi.Domain.Repository.TarefaRepository;
import com.app.tarefaApi.Domain.Repository.UsuarioRepository;
import com.app.tarefaApi.Exception.TaskNotFoundException;
import com.app.tarefaApi.Exception.UserNotFoundException;
import com.app.tarefaApi.Rest.DTO.TarefaDTO;
import com.app.tarefaApi.Service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaServiceImpl implements TarefaService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Override
    public Tarefa criarTarefa(TarefaDTO tarefaDTO) throws UserNotFoundException {
        // Obtem o email do usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = authentication.getName(); //será o email do criador da tarefa

        // Obtem o nome do usuário autenticado através do email de login.
        Usuario usuarioCriador = usuarioRepository.findUserByEmail(emailAutenticado);

        //Usuario que receberá a tarefa.
        Long idUsuario = tarefaDTO.getIdUsuario();
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if(usuario.isPresent()){
            Tarefa tarefa = new Tarefa();
            tarefa.setTitulo(tarefaDTO.getTitulo());
            tarefa.setDescricao(tarefaDTO.getDescricao());
            tarefa.setStatus(tarefaDTO.getStatus());
            tarefa.setUsuario(usuario.get());
            tarefa.setDataCriacao(LocalDateTime.now());
            tarefa.setCriador(usuarioCriador.getNomeUsuario());//adiciona o nome do criador para verificarmos quem é o criador da tarefa
            tarefa.setEmailCriador(emailAutenticado); //adiciona o email do criador para verificarmos quem é o criador da tarefa

            return tarefaRepository.save(tarefa);

        }else{
            throw new UserNotFoundException("Usuario Inexistente.");
        }
    }

    @Override
    public Optional<Tarefa> obterTarefaPorId(Long id) throws TaskNotFoundException {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        if(tarefa.isPresent()){
            return tarefa;
        }
        throw new TaskNotFoundException("Tarefa Inexiste");
    }

    @Override
    public List<Tarefa> obterTodasTarefas() {
        return tarefaRepository.findAll();
    }

    @Override
    public Tarefa atualizaTarefa(Long id, TarefaDTO tarefaDTO) throws TaskNotFoundException, AccessDeniedException {
        // Obtem o email do usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = authentication.getName();



        // Obtem a tarefa pelo ID
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Tarefa Inexistente."));

        // Verifica se o usuário autenticado é o criador da tarefa
        if (!tarefaExistente.getEmailCriador().equals(emailAutenticado)) {
            throw new AccessDeniedException("Só pode atualizar tarefas criadas por você.");
        }

        // Atualiza os campos da tarefa com os dados do DTO
        tarefaExistente.setTitulo(tarefaDTO.getTitulo());
        tarefaExistente.setDescricao(tarefaDTO.getDescricao());
        tarefaExistente.setStatus(tarefaDTO.getStatus());

        return tarefaRepository.save(tarefaExistente);
    }

    @Override
    public void excluiTarefa(Long id) throws TaskNotFoundException {
        // Obtem o email do usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = authentication.getName();

        // Obtem a tarefa pelo ID
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Tarefa Inexistente."));

        // Verifica se o usuário autenticado é o criador da tarefa
        if (!tarefaExistente.getEmailCriador().equals(emailAutenticado)) {
            throw new AccessDeniedException("Só pode excluir tarefas criadas por você.");
        }

         tarefaRepository.deleteById(id);




    }

    public List<Tarefa> listaTarefasPorStatus(Status status){
        return (List<Tarefa>) tarefaRepository.findTarefaByStatus(status);
    }
}
