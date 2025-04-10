package com.example.reactor_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;
import org.springframework.ui.Model;

@Controller
public class MainController {

        @GetMapping("/")
        public Mono<String> home(Model model) {
            User user = new User("Alice Smith", "alice@example.com");
            model.addAttribute("user", user);
            return Mono.just("index");
        }

        static class User {
            private String name;
            private String email;

            // Constructor, getters
            public User(String name, String email) {
                this.name = name;
                this.email = email;
            }
            public String getName() { return name; }
            public String getEmail() { return email; }
        }

}
