package com.amelinroman.webfluxsecurity.security;

import com.amelinroman.webfluxsecurity.entity.UserEntity;
import com.amelinroman.webfluxsecurity.exception.UnauthorizedException;
import com.amelinroman.webfluxsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private UserRepository userRepository;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userRepository.findById(principal.getId())
                .filter(UserEntity::isEnabled)
                .switchIfEmpty(Mono.error(new UnauthorizedException("User diabled")))
                .map(user -> authentication);
    }
}
