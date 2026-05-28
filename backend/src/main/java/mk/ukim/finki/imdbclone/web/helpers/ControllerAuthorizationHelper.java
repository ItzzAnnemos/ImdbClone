package mk.ukim.finki.imdbclone.web.helpers;

import mk.ukim.finki.imdbclone.model.dto.DisplayUserDto;
import mk.ukim.finki.imdbclone.service.application.UserApplicationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ControllerAuthorizationHelper {

    private final UserApplicationService userApplicationService;

    public ControllerAuthorizationHelper(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    public boolean isAuthenticatedUserId(Long userId, Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        return userApplicationService.findByUsername(authentication.getName())
                .map(DisplayUserDto::id)
                .map(userId::equals)
                .orElse(false);
    }

    public boolean isAuthenticatedUsername(String username, Authentication authentication) {
        return authentication != null && username.equals(authentication.getName());
    }
}
