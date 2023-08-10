package ma.emsi.db_livre.web;

import jakarta.validation.Valid;
import ma.emsi.db_livre.entities.Exposant;
import ma.emsi.db_livre.entities.Livre;
import ma.emsi.db_livre.repositories.ExposantRepository;
import ma.emsi.db_livre.repositories.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MesLivresController {

    @Autowired
    LivreRepository livreRepository;

    @Autowired
    ExposantRepository exposantRepository;

    @GetMapping("/formLivres/{idExposant}")
    public String formLivre(@PathVariable Long idExposant, Model model) {
        Livre livre = new Livre();
        Exposant exposant = exposantRepository.findById(idExposant).orElse(null);

        livre.setExposant(exposant);
        model.addAttribute("livre", livre);
        model.addAttribute("listExposants", exposantRepository.findAll()); // Récupérer tous les exposants
        return "formLivres";
    }

    @PostMapping("/saveMesLivres")
    public String saveLivre(Model model, @Valid Livre livre, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listExposants", exposantRepository.findAll());
            return "formLivres";
        }

        // Récupérer l'ID de l'exposant à partir du formulaire
        Long exposantId = livre.getExposant().getExposantId();
        Exposant exposant = exposantRepository.findById(exposantId).orElse(null);
        if (exposant == null) {
            // Gérer le cas où l'ID de l'exposant n'est pas valide
            return "redirect:/error";
        }

        // Associer l'exposant au livre avant de le sauvegarder dans la base de données
        livre.setExposant(exposant);
        livreRepository.save(livre);

        // Rediriger vers la bonne URL en fonction de l'ID de l'utilisateur
        return "redirect:/mesLivres/" + exposantId;
    }


    @GetMapping(path = "/mesLivres/{userId}")
    public String mesLivres(Model model, @PathVariable Long userId) {
        // Récupérer les livres de l'utilisateur avec l'ID spécifié
        List<Livre> livres = livreRepository.findByExposant_ExposantId(userId);

        for (Livre livre : livres) {
            if (livre.getTitre() == null || livre.getTitre().isEmpty()) {
                livre.setTitre(null);
            }
            if (livre.getAuteur() == null || livre.getAuteur().isEmpty()) {
                livre.setAuteur(null);
            }
            if (livre.getEditeur() == null || livre.getEditeur().isEmpty()) {
                livre.setEditeur(null);
            }
            // Add more fields if needed
        }

        // Ajouter la liste des livres à l'attribut du modèle pour l'affichage dans la vue
        model.addAttribute("mesLivres", livres);

        return "mesLivres";
    }

    @GetMapping("/mesLivresdetails")
    public String mesLivresdetails(@RequestParam(name = "id") Long id, Model model) {
        Livre livre = livreRepository.findById(id).orElse(null);
        if (livre == null) {
            return "redirect:/mesLivres";
        }
        model.addAttribute("livre", livre);
        return "mesLivresdetails";
    }

    @GetMapping("/editMesLivres")
    public String editMesLivres(Long id, Model model){
        Livre livre = livreRepository.findById(id).orElse(null);
        if (livre == null) throw new RuntimeException("Livre introuvable");
        model.addAttribute("livre", livre);
        model.addAttribute("listExposants", exposantRepository.findAll()); // Récupérer tous les exposants
        return "editMesLivres";
    }

    @GetMapping("/deleteMesLivres")
    public String deleteMesLivres(@RequestParam(name = "id") Long id) {
        Optional<Livre> livreOptional = livreRepository.findById(id);
        if (livreOptional.isPresent()) {
            Livre livre = livreOptional.get();
            Long exposantId = livre.getExposant().getExposantId();
            livreRepository.deleteById(id);
            return "redirect:/mesLivres/" + exposantId;
        } else {
            // Handle the case where the Livre with the specified ID is not found
            return "redirect:/error"; // Or any other appropriate error handling
        }
    }


}

