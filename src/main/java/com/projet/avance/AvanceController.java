package com.projet.avance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;

import com.projet.avance.model.User;
import com.projet.avance.model.Visiteur;
import com.projet.avance.model.Site;
import com.projet.avance.model.Visiter;
import com.projet.avance.repository.UserRepository;
import com.projet.avance.repository.VisiteurRepository;
import com.projet.avance.repository.SiteRepository;
import com.projet.avance.repository.VisiterRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Controller
public class AvanceController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VisiteurRepository visiteurRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private VisiterRepository visiterRepository;

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

    @GetMapping("/visiteur")
    public String visiteur(HttpSession session, Model model, HttpServletResponse response){
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
        return "page/visiteur";
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

    // ==================== API ENDPOINTS POUR LES VISITEURS ====================

    // Récupérer tous les visiteurs
    @GetMapping("/api/visiteurs")
    @ResponseBody
    public ResponseEntity<List<Visiteur>> getAllVisiteurs() {
        List<Visiteur> visiteurs = visiteurRepository.findAll();
        return ResponseEntity.ok(visiteurs);
    }

    // Ajouter un nouveau visiteur
    @PostMapping("/api/visiteurs")
    @ResponseBody
    public ResponseEntity<Visiteur> addVisiteur(@RequestBody Visiteur visiteur) {
        Visiteur savedVisiteur = visiteurRepository.save(visiteur);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVisiteur);
    }

    // Récupérer un visiteur par ID
    @GetMapping("/api/visiteurs/{id}")
    @ResponseBody
    public ResponseEntity<Visiteur> getVisiteur(@PathVariable Long id) {
        Optional<Visiteur> visiteur = visiteurRepository.findById(id);
        return visiteur.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Modifier un visiteur
    @PutMapping("/api/visiteurs/{id}")
    @ResponseBody
    public ResponseEntity<Visiteur> updateVisiteur(@PathVariable Long id, @RequestBody Visiteur visiteurDetails) {
        Optional<Visiteur> visiteur = visiteurRepository.findById(id);
        
        if (visiteur.isPresent()) {
            Visiteur existingVisiteur = visiteur.get();
            existingVisiteur.setNameVisiteur(visiteurDetails.getNameVisiteur());
            existingVisiteur.setAdresseVisiteur(visiteurDetails.getAdresseVisiteur());
            Visiteur updatedVisiteur = visiteurRepository.save(existingVisiteur);
            return ResponseEntity.ok(updatedVisiteur);
        }
        
        return ResponseEntity.notFound().build();
    }

    // Supprimer un visiteur
    @DeleteMapping("/api/visiteurs/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteVisiteur(@PathVariable Long id) {
        if (visiteurRepository.existsById(id)) {
            visiteurRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



    @GetMapping("/site")
    public String site(HttpSession session, Model model, HttpServletResponse response){
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
        return "page/site";
    }

    @GetMapping("/visiter")
    public String visiter(HttpSession session, Model model, HttpServletResponse response){
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
        return "page/visiter";
    }

    // ==================== API ENDPOINTS POUR LES SITES ====================

    // Récupérer tous les sites
    @GetMapping("/api/sites")
    @ResponseBody
    public ResponseEntity<List<Site>> getAllSites() {
        List<Site> sites = siteRepository.findAll();
        return ResponseEntity.ok(sites);
    }

    // Ajouter un nouveau site
    @PostMapping("/api/sites")
    @ResponseBody
    public ResponseEntity<Site> addSite(@RequestBody Site site) {
        Site savedSite = siteRepository.save(site);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSite);
    }

    // Récupérer un site par ID
    @GetMapping("/api/sites/{id}")
    @ResponseBody
    public ResponseEntity<Site> getSite(@PathVariable Long id) {
        Optional<Site> site = siteRepository.findById(id);
        return site.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Modifier un site
    @PutMapping("/api/sites/{id}")
    @ResponseBody
    public ResponseEntity<Site> updateSite(@PathVariable Long id, @RequestBody Site siteDetails) {
        Optional<Site> site = siteRepository.findById(id);
        
        if (site.isPresent()) {
            Site existingSite = site.get();
            existingSite.setNameSite(siteDetails.getNameSite());
            existingSite.setLieuSite(siteDetails.getLieuSite());
            existingSite.setTarifJournalier(siteDetails.getTarifJournalier());
            Site updatedSite = siteRepository.save(existingSite);
            return ResponseEntity.ok(updatedSite);
        }
        
        return ResponseEntity.notFound().build();
    }

    // Supprimer un site
    @DeleteMapping("/api/sites/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        if (siteRepository.existsById(id)) {
            siteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==================== API ENDPOINTS POUR LES VISITES ====================

    // Récupérer toutes les visites
    @GetMapping("/api/visites")
    @ResponseBody
    public ResponseEntity<List<Visiter>> getAllVisites() {
        List<Visiter> visites = visiterRepository.findAll();
        return ResponseEntity.ok(visites);
    }

    // Ajouter une nouvelle visite
    @PostMapping("/api/visites")
    @ResponseBody
    public ResponseEntity<Visiter> addVisite(@RequestBody Visiter visite) {
        // Auto-remplir la date de visite si elle est null
        if (visite.getDateVisite() == null) {
            visite.setDateVisite(LocalDate.now());
        }
        Visiter savedVisite = visiterRepository.save(visite);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVisite);
    }

    // Récupérer une visite par ID
    @GetMapping("/api/visites/{id}")
    @ResponseBody
    public ResponseEntity<Visiter> getVisite(@PathVariable Long id) {
        Optional<Visiter> visite = visiterRepository.findById(id);
        return visite.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Modifier une visite
    @PutMapping("/api/visites/{id}")
    @ResponseBody
    public ResponseEntity<Visiter> updateVisite(@PathVariable Long id, @RequestBody Visiter visiteDetails) {
        Optional<Visiter> visite = visiterRepository.findById(id);
        
        if (visite.isPresent()) {
            Visiter existingVisite = visite.get();
            existingVisite.setVisiteur(visiteDetails.getVisiteur());
            existingVisite.setSite(visiteDetails.getSite());
            existingVisite.setNbrJours(visiteDetails.getNbrJours());
            existingVisite.setDateVisite(visiteDetails.getDateVisite());
            Visiter updatedVisite = visiterRepository.save(existingVisite);
            return ResponseEntity.ok(updatedVisite);
        }
        
        return ResponseEntity.notFound().build();
    }

    // Supprimer une visite
    @DeleteMapping("/api/visites/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteVisite(@PathVariable Long id) {
        if (visiterRepository.existsById(id)) {
            visiterRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/rapport-visiteurs")
    public String rapportVisiteurs(HttpSession session, Model model, HttpServletResponse response){
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";
        model.addAttribute("user", user);
        return "page/rapport-visiteurs";
    }

    @GetMapping("/statistiques")
    public String statistiques(HttpSession session, Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";
        model.addAttribute("user", user);
        return "page/statistiques";
    }

}