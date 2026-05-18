package com.projet.avance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;

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
    public String home(HttpSession session, Model model, HttpServletResponse response) {
        // Empêcher la mise en cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        // Vérifier si connecté
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        model.addAttribute("user", user);
        return "page/home";
    }

    @GetMapping("/page1")
    public String page1(HttpSession session, Model model, HttpServletResponse response){
        // Empêcher la mise en cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        // Vérifier si connecté
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        model.addAttribute("user", user);
        return "page/pageone";
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

        // Vérification mot de passe
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", "Les mots de passe ne correspondent pas");
            return "redirect:/signin";
        }

        // Vérification email déjà utilisé
        if (userRepository.existsByEmail(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email déjà utilisé");
            return "redirect:/signin";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDatenow(java.time.LocalDateTime.now().toString());

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Compte créé avec succès !");
        return "redirect:/signin";
    }


    @PostMapping("/login")
    public String doLogin(
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        // Vérifier si email existe
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute(
                    "emailError",
                    "Email introuvable");
            return "redirect:/";
        }

        // Vérifier mot de passe
        if (!passwordEncoder.matches(password, user.getPassword())) {
            redirectAttributes.addFlashAttribute(
                    "passwordError",
                    "Mot de passe incorrect");
            return "redirect:/";
        }

        // Sauvegarder utilisateur en session
        session.setAttribute("user", user);

        // Redirection vers home
        return "redirect:/home";
    }


}