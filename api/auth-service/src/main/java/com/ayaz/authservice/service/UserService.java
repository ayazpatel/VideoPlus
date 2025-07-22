package com.ayaz.authservice.service;

import com.ayaz.authservice.model.User;
import com.ayaz.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with username: " + username)))
                .map(user -> user);
    }

    public Mono<UserDetails> loadUserById(String id) {
        return userRepository.findById(id)
                .map(user -> (UserDetails) user);
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }
}