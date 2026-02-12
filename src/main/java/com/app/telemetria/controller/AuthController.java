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

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        // Autenticação do usuário
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getSenha()
                )
        );

        Usuario usuario = usuarioRepository.findByLogin(request.getLogin())
                .orElseThrow();

        // Gera tokens
        String accessToken = jwtService.generateAccessToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    // ================= REFRESH TOKEN =================
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        // Valida refresh token
        if (!jwtService.isTokenValid(refreshToken)) {
            return ResponseEntity.status(403).build();
        }

        String login = jwtService.getLogin(refreshToken);
        Usuario usuario = usuarioRepository.findByLogin(login).orElseThrow();

        // Gera novos tokens
        String newAccessToken = jwtService.generateAccessToken(usuario);
        String newRefreshToken = jwtService.generateRefreshToken(usuario);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken));
    }
}
