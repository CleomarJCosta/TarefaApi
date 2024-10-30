package com.app.tarefaApi.Service;

import com.app.tarefaApi.Domain.Entity.Usuario;
import com.app.tarefaApi.Exception.UserAlreadyExistsException;
import com.app.tarefaApi.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface UsuarioService {
    Usuario registrar(Usuario usuario) throws UserAlreadyExistsException;
    List<Usuario> listarUsuarios();
    Optional<Usuario> obterUsuarioPorId(Long id) throws UserNotFoundException;
    void excluirUsuario(Long id) throws UserNotFoundException;

}
