package com.projet.avance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import com.projet.avance.model.User;
import com.projet.avance.repository.UserRepository;

@Controller
public class AvanceController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/signin")
    public String inscrire() {
        return "inscription/signin";
    }

    @GetMapping("/home")
    public String home() {
        return "page/home";
    }

    @GetMapping("/page1")
    public String page1(){
        return "page/pageone";
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", "Les mots de passe ne correspondent pas");
            return "redirect:/signin";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDatenow(java.time.LocalDateTime.now().toString());

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Compte créé avec succès !");
        return "redirect:/signin";
    }


}