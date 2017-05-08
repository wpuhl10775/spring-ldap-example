package hello;

import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index(@CurrentUser User user) {
        return "Welcome " + user.getUsername() + " to the home page!";
    }
}
