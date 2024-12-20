package forum.hub.api.infra.security;

import forum.hub.api.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUserProvider implements AuthenticatedUserProvider {

    @Override
    public User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public boolean isAdmin() {
        return getUser().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
