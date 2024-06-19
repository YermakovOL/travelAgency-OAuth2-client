package yermakov.oleksii.touragencyoauth2client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/tourPage")
    public String showTourPage() {
        return "tourPage";
    }
}
