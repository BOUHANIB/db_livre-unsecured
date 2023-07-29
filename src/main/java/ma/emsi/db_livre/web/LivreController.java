package ma.emsi.db_livre.web;

import jakarta.validation.Valid;
import ma.emsi.db_livre.entities.Livre;
import ma.emsi.db_livre.repositories.ExposantRepository;
import ma.emsi.db_livre.repositories.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LivreController {

    @Autowired
    LivreRepository livreRepository;

    @Autowired
    ExposantRepository exposantRepository;

    @GetMapping(path = "/listLivres")
    public String livres(Model model,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "size", defaultValue = "5") int size,
                         @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Livre> pageLivres = livreRepository.findByTitreContains(keyword, PageRequest.of(page, size));
        List<Livre> livres = pageLivres.getContent();

        // Pour chaque livre, vérifier les champs et attribuer "Indisponible" si nécessaire
        for (Livre livre : livres) {
            if (livre.getTitre() == null || livre.getTitre().isEmpty()) {
                livre.setTitre("Indisponible");
            }
            if (livre.getAuteur() == null || livre.getAuteur().isEmpty()) {
                livre.setAuteur("Indisponible");
            }
            if (livre.getEditeur() == null || livre.getEditeur().isEmpty()) {
                livre.setEditeur("Indisponible");
            }
            // Vous pouvez faire de même pour les autres champs si nécessaire
        }

        model.addAttribute("listLivres", livres);
        model.addAttribute("pages", new int[pageLivres.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "livres";
    }

    @GetMapping("/deleteLivre")
    public String deleteLivre(@RequestParam(name = "id") Long id, String keyword, int page){
        livreRepository.deleteById(id);
        return "redirect:/listLivres?page="+page+"&keyword="+keyword;
    }


    @GetMapping("/livredetails")
    public String livreDetails(@RequestParam(name = "id") Long id, Model model) {
        Livre livre = livreRepository.findById(id).orElse(null);
        if (livre == null) {
            return "redirect:/listLivres";
        }
        model.addAttribute("livre", livre);
        return "livredetails";
    }

    @GetMapping(path = "/formLivres")
    public String formLivre(Model model) {
        model.addAttribute("livre", new Livre());
        model.addAttribute("listExposants", exposantRepository.findAll()); // Récupérer tous les exposants
        return "formLivres";
    }

    @PostMapping("/saveLivre")
    public String saveLivre(Model model, @Valid Livre livre, BindingResult bindingResult,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) {
            model.addAttribute("listExposants", exposantRepository.findAll()); // Récupérer tous les exposants en cas d'erreur
            return "formLivres";
        }
        livre.setExposant(exposantRepository.findById(livre.getExposant().getExposantId()).orElse(null)); // Récupérer l'exposant à partir de l'ID sélectionné
        livreRepository.save(livre);
        return "redirect:/listLivres?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/editLivre")
    public String editLivre(Long id, Model model, String keyword, int page){
        Livre livre = livreRepository.findById(id).orElse(null);
        if (livre == null) throw new RuntimeException("Livre introuvable");
        model.addAttribute("livre", livre);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listExposants", exposantRepository.findAll()); // Récupérer tous les exposants
        return "editLivre";
    }

}
