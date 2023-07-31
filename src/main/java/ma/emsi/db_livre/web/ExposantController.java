package ma.emsi.db_livre.web;

import jakarta.validation.Valid;
import ma.emsi.db_livre.entities.Exposant;
import ma.emsi.db_livre.entities.User;
import ma.emsi.db_livre.repositories.ExposantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        for (Exposant exposant : exposants) {

            if (exposant.getPays() == null || exposant.getPays().isEmpty()) {
                exposant.setPays("Indisponible");
            }
            if (exposant.getTelephone() == null || exposant.getTelephone().isEmpty()) {
                exposant.setTelephone("Indisponible");
            }
            if (exposant.getSiteWeb() == null || exposant.getSiteWeb().isEmpty()) {
                exposant.setSiteWeb("Indisponible");
            }
            if (exposant.getAdresse() == null || exposant.getAdresse().isEmpty()) {
                exposant.setAdresse("Indisponible");
            }
            if (exposant.getResponsableSalle() == null || exposant.getResponsableSalle().isEmpty()) {
                exposant.setResponsableSalle("Indisponible");
            }
            if (exposant.getResponsable() == null || exposant.getResponsable().isEmpty()) {
                exposant.setResponsable("Indisponible");
            }
            if (exposant.getSpecialite() == null || exposant.getSpecialite().isEmpty()) {
                exposant.setSpecialite("Indisponible");
            }
            if (exposant.getLocalisation() == null || exposant.getLocalisation().isEmpty()) {
                exposant.setLocalisation("Indisponible");
            }
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

    @GetMapping("/exposantdetails/{id}")
    public String exposantDetails(@PathVariable(name = "id") Long id, Model model) {
        // Récupérer l'exposant à partir de l'id
        Exposant exposant = exposantRepository.findById(id).orElse(null);

        // Récupérer l'utilisateur associé à l'exposant (si vous avez une relation entre User et Exposant)
        User user = exposant.getUser();

        // Ajouter l'exposant et l'utilisateur au modèle pour les afficher dans la page "exposantdetails.html"
        model.addAttribute("exposant", exposant);
        model.addAttribute("user", user);

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
                               @RequestParam(defaultValue = "0") String page,
                               @RequestParam(defaultValue = "") String keyword,
                               @RequestParam(name = "newUsername", required = false) String newUsername) {

        if (bindingResult.hasErrors()) {
            return "formExposants";
        }

        // Fetch the existing Exposant from the database
        Exposant existingExposant = exposantRepository.findById(exposant.getExposantId()).orElse(null);
        if (existingExposant == null) {
            throw new RuntimeException("Exposant not found");
        }

        // Check if newUsername is provided and not empty
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            // Set the new username in the associated User entity (if exists)
            User user = existingExposant.getUser();
            if (user != null) {
                user.setUsername(newUsername.trim());
            }
        }

        // Update the fields of the existing Exposant with the new values
        existingExposant.setNom(exposant.getNom());
        existingExposant.setPays(exposant.getPays().isEmpty() ? null : exposant.getPays());
        existingExposant.setTelephone(exposant.getTelephone().isEmpty() ? null : exposant.getTelephone());
        existingExposant.setSiteWeb(exposant.getSiteWeb().isEmpty() ? null : exposant.getSiteWeb());
        existingExposant.setAdresse(exposant.getAdresse().isEmpty() ? null : exposant.getAdresse());
        existingExposant.setResponsableSalle(exposant.getResponsableSalle().isEmpty() ? null : exposant.getResponsableSalle());
        existingExposant.setResponsable(exposant.getResponsable().isEmpty() ? null : exposant.getResponsable());
        existingExposant.setSpecialite(exposant.getSpecialite().isEmpty() ? null : exposant.getSpecialite());
        existingExposant.setLocalisation(exposant.getLocalisation().isEmpty() ? null : exposant.getLocalisation());
        // Update other fields as needed...

        // Save the updated Exposant (including the User relationship) to the database
        exposantRepository.save(existingExposant);

        // Convert the page number from String to int
        int pageNumber = Integer.parseInt(page);

        // Redirect to the dashboard page after saving the changes
        return "redirect:/dashboard";
    }


    @GetMapping("/editExposant")
    public String editExposant(Long id, Model model, String keyword, String page){ // Modifier le type en String
        Exposant exposant = exposantRepository.findById(id).orElse(null);
        if (exposant == null) throw new RuntimeException("Exposant introuvable");

        String currentUsername = exposant.getUser() != null ? exposant.getUser().getUsername() : "";

        model.addAttribute("exposant", exposant);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentUsername", currentUsername); // Add the current username to the model


        return "editExposant";
    }


}
