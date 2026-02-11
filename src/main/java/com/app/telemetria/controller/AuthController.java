package com.app.telemetria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import com.app.telemetria.dto.*;
import com.app.telemetria.entity.Usuario;
import com.app.telemetria.repository.UsuarioRepository;
import com.app.telemetria.security.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getSenha()
                )
        );

        Usuario usuario = usuarioRepository
                .findByLogin(request.getLogin())
                .orElseThrow();

        String token = jwtService.generateToken(usuario);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
