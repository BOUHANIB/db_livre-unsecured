package ma.emsi.db_livre.web;

import jakarta.validation.Valid;
import ma.emsi.db_livre.entities.Exposant;
import ma.emsi.db_livre.entities.Livre;
import ma.emsi.db_livre.repositories.ExposantRepository;
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
public class ExposantController {

    @Autowired
    ExposantRepository exposantRepository;

    @GetMapping(path = "/listExposants")
    public String exposants(Model model,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "size", defaultValue = "5") int size,
                         @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Exposant> pageExposants = exposantRepository.findByNomContains(keyword, PageRequest.of(page, size));
        List<Exposant> exposants = pageExposants.getContent();

        // Pour chaque livre, vérifier les champs et attribuer "Indisponible" si nécessaire
        for (Exposant exposant : exposants) {
            if (exposant.getNom() == null || exposant.getNom().isEmpty()) {
                exposant.setNom("Indisponible");
            }
            if (exposant.getPays() == null || exposant.getPays().isEmpty()) {
                exposant.setPays("Indisponible");
            }
            if (exposant.getMail() == null || exposant.getMail().isEmpty()) {
                exposant.setMail("Indisponible");
            }
            if (exposant.getSpecialite() == null || exposant.getSpecialite().isEmpty()) {
                exposant.setSpecialite("Indisponible");
            }
            // Vous pouvez faire de même pour les autres champs si nécessaire
        }

        model.addAttribute("listExposants", exposants);
        model.addAttribute("pages", new int[pageExposants.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "exposants";
    }

    @GetMapping("/deleteExposant")
    public String deleteExposant(@RequestParam(name = "id") Long id, String keyword, int page){
        exposantRepository.deleteById(id);
        return "redirect:/listExposants?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/listExposants";
    }

    @GetMapping("/exposantdetails")
    public String exposantDetails(@RequestParam(name = "id") Long id, Model model) {
        // Récupérer l'exposant à partir de l'id
        Exposant exposant = exposantRepository.findById(id).orElse(null);

        // Vérifier si l'exposant a été trouvé
        if (exposant == null) {
            // Si l'exposant n'a pas été trouvé, vous pouvez gérer cela ici, par exemple, rediriger vers une page d'erreur.
            return "redirect:/listExposants"; // Remplacez "/error" par le chemin de votre page d'erreur personnalisée
        }

        // Ajouter l'exposant au modèle pour l'afficher dans la page "exposantdetails.html"
        model.addAttribute("exposant", exposant);

        // Rediriger vers la page "exposantdetails.html"
        return "exposantdetails";
    }

    @GetMapping("/formExposants")
    public String formExposant(Model model ){
        model.addAttribute("exposant",new Exposant());
        return "formExposants";
    }

    @PostMapping("/saveExposant")
    public String saveExposant(Model model, @Valid Exposant exposant, BindingResult bindingResult,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) return "formExposants";
        exposantRepository.save(exposant);
        return "redirect:/listExposants?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/editExposant")
    public String editExposant(Long id, Model model,String keyword,int page){
        Exposant exposant=exposantRepository.findById(id).orElse(null);
        if(exposant==null) throw new RuntimeException("Exposant introuvable");
        model.addAttribute("exposant",exposant);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editExposant";
    }


}
