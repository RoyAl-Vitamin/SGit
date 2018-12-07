package vi.al.ro.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@org.springframework.stereotype.Controller
public class Controller {

    @Value("${welcome.message:test}")
    private String message = "Hello World";

    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        model.put("message", this.message);
        return "index";
    }
}
