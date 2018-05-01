package lab.zlren.client.service;

import lab.zlren.ApiServer;
import lab.zlren.client.domain.User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ApiServer("http://localhost:8080/user")
public interface IUserApi {

    @GetMapping("/")
    Flux<User> getAllUser();

    @GetMapping("/{id}")
    Mono<User> getUserById(@PathVariable String id);

    @DeleteMapping("/{id}")
    Mono<Void> deleteUserById(@PathVariable String id);

    @PostMapping("/")
    Mono<User> createUser(@RequestBody Mono<User> user);
}
