package co.kh.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("anonymous")
public class UnSecureController {
    
    @GetMapping("/test")
    public String test(){
        return "For anonymous";
    }
    
}
