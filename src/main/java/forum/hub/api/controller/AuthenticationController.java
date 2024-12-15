package forum.hub.api.controller;

import forum.hub.api.domain.exception.ValidationException;
import forum.hub.api.domain.user.User;
import forum.hub.api.domain.user.UserRegisterDTO;
import forum.hub.api.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity register(@RequestBody @Valid UserRegisterDTO data) {
        var userEmail = repository.findByEmail(data.email());
        if (userEmail.isPresent()) {
            throw new ValidationException("User already exists");
        }

        var encryptedPassword = passwordEncoder.encode(data.password());
        var newUser = new User(null, data.name(), data.email(), encryptedPassword, data.role());

        repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
