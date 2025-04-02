package com.bruce.baguwen;

import com.bruce.dtos.Order;
import com.bruce.service.OrderoneService;
import com.bruce.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SessionController {
    @GetMapping("/")
    public String showForm(HttpSession session, Model model) {



        String name = (String) session.getAttribute("name");
        String email = (String) session.getAttribute("email");
        model.addAttribute("name", name);
        model.addAttribute("email", email);

        return "form";
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam String name, @RequestParam String email, HttpSession session) {
        session.setAttribute("name", name);
        session.setAttribute("email", email);

        return "redirect:/";
    }

}