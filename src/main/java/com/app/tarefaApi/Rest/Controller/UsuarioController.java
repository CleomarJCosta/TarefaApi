package com.app.tarefaApi.Rest.Controller;

import com.app.tarefaApi.Domain.Entity.Usuario;
import com.app.tarefaApi.Exception.SenhaInvalidaException;
import com.app.tarefaApi.Exception.UserAlreadyExistsException;
import com.app.tarefaApi.Exception.UserNotFoundException;
import com.app.tarefaApi.Rest.DTO.LoginDTO;
import com.app.tarefaApi.Rest.DTO.TokenDTO;
import com.app.tarefaApi.Security.Jwt.JwtService;
import com.app.tarefaApi.Service.Impl.UsuarioServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Usuarios", description = "Endpoints de usuários para gestão de tarefas")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    private final JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);


    @Operation(summary = "Registrar um novo usuário", description = "Cria um novo usuário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Usuário já existe")
    })
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) throws UserAlreadyExistsException {
        logger.info("Iniciando o registro do usuário com email: {}", usuario.getEmail());
        try {
            logger.info("Usuário registrado com sucesso: {}", usuario.getEmail());
            Usuario novoUsuario = usuarioService.registrar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (UserAlreadyExistsException e) {
            logger.error("Erro ao registrar usuário: {}", e.getMessage());
            throw new UserAlreadyExistsException(e.getMessage());
        }
    }



    @Operation(summary = "Autenticar usuário", description = "Realiza a autenticação do usuário e gera um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> autenticar(@RequestBody LoginDTO loginDTO) throws SenhaInvalidaException {
        logger.info("Iniciando autenticação para o email: {}", loginDTO.getEmail());
        try {
            Usuario usuario = Usuario.builder()
                    .email(loginDTO.getEmail())
                    .senha(loginDTO.getSenha())
                    .build();

            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            logger.info("Autenticação bem-sucedida para o email: {}", loginDTO.getEmail());
            return ResponseEntity.ok(new TokenDTO(usuario.getEmail(), token));
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            logger.error("Erro de autenticação para o email {}: {}", loginDTO.getEmail(), e.getMessage());
            throw new SenhaInvalidaException("Credenciais inválidas.");
        }
    }


    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes de um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Optional<Usuario>> getUsuarioById(@PathVariable Long id) throws UserNotFoundException {
        logger.info("Buscando usuário pelo ID: {}", id);
        Optional<Usuario> usuario = usuarioService.obterUsuarioPorId(id);
        logger.info("Usuário encontrado: {}", usuario);
        return ResponseEntity.ok(usuario);
    }



    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        logger.info("Listando todos os usuários registrados");
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        logger.info("Número de usuários encontrados: {}", usuarios.size());
        return ResponseEntity.ok(usuarios);
    }



    @Operation(summary = "Excluir um usuário por ID", description = "Remove um usuário do sistema com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) throws UserNotFoundException {
        logger.info("Excluindo usuário com ID: {}", id);
        usuarioService.excluirUsuario(id);
        logger.info("Usuário com ID {} excluído com sucesso", id);
        return ResponseEntity.noContent().build();
    }

}
