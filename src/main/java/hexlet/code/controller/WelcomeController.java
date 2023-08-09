package hexlet.code.controller;

import com.rollbar.notifier.Rollbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @Autowired
    private Rollbar rollbar;

    @GetMapping("/welcome")
    public String welcome() {
        rollbar.debug("Test message 5");
        return "welcome";
    }
}

