package lab.zlren.client.controller;

import lab.zlren.client.service.IUserApi;
import lab.zlren.client.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author zlren
 * @date 2018-05-01
 */
@RestController
public class TestController {

    private final IUserApi userApi;

    @Autowired
    public TestController(IUserApi userApi) {
        this.userApi = userApi;
    }

    @GetMapping("/")
    public void test() {
        Flux<User> users = userApi.getAllUser();
        users.subscribe(System.out::print);
    }
}
