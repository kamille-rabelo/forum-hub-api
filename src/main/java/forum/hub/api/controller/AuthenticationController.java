package forum.hub.api.controller;

import forum.hub.api.domain.user.UserLoginDTO;
import forum.hub.api.domain.user.UserRegisterDTO;
import forum.hub.api.infra.security.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @SecurityRequirement(name = "bearer-key")
    @PostMapping("/register")
    @Transactional
    public ResponseEntity register(@RequestBody @Valid UserRegisterDTO data) {
        return ResponseEntity.ok(service.register(data));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDTO data) {
        return ResponseEntity.ok(service.login(data));
    }
}
