package forum.hub.api.infra.security;

import forum.hub.api.domain.exception.ValidationException;
import forum.hub.api.domain.user.User;
import forum.hub.api.domain.user.UserLoginDTO;
import forum.hub.api.domain.user.UserRegisterDTO;
import forum.hub.api.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    public JwtTokenDTO register(UserRegisterDTO data) {
        var userEmail = repository.findByEmail(data.email());
        if (userEmail != null) {
            throw new ValidationException("User already exists");
        }

        var encryptedPassword = passwordEncoder.encode(data.password());
        var newUser = new User(null, data.name(), data.email(), encryptedPassword, data.role());

        repository.save(newUser);

        var token = tokenService.generateToken(newUser);
        return new JwtTokenDTO(token);
    }

    public JwtTokenDTO login(UserLoginDTO data) {
        var authToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = manager.authenticate(authToken);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return new JwtTokenDTO(token);
    }
}
