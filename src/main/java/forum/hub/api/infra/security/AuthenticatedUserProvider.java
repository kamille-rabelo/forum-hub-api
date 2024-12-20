package forum.hub.api.infra.security;

import forum.hub.api.domain.user.User;

public interface AuthenticatedUserProvider {
    User getUser();
    boolean isAdmin();
}
