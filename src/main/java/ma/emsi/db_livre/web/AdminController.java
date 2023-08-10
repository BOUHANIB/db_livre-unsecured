package ma.emsi.db_livre.web;


import jakarta.validation.Valid;
import ma.emsi.db_livre.entities.Exposant;
import ma.emsi.db_livre.entities.User;
import ma.emsi.db_livre.repositories.ExposantRepository;
import ma.emsi.db_livre.repositories.UserRepository;
import ma.emsi.db_livre.security.ActiveUserStore;
import ma.emsi.db_livre.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {
  @Autowired
  private ActiveUserStore activeUserStore;

  @Autowired
  private AdminService adminService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ExposantRepository exposantRepository;

  @GetMapping("/admin")
  public String getAdmin(ModelMap modelMap) {
    List<User> allUserAccounts = adminService.getAllUserAccounts();
    modelMap.addAttribute("users", allUserAccounts);
    modelMap.addAttribute("activeUsers", activeUserStore.getUsers());
    modelMap.addAttribute("isLoggedIn", true);
    modelMap.addAttribute("isAdmin", true);
    return "admin";
  }


  @GetMapping("/admindetails/{id}")
  public String exposantDetails(@PathVariable(name = "id") Long id, Model model) {
    // Récupérer l'exposant à partir de l'id
    Exposant exposant = exposantRepository.findById(id).orElse(null);

    // Récupérer l'utilisateur associé à l'exposant (si vous avez une relation entre User et Exposant)
    User user = exposant.getUser();

    // Ajouter l'exposant et l'utilisateur au modèle pour les afficher dans la page "admindetails.html"
    model.addAttribute("exposant", exposant);
    model.addAttribute("user", user);

    // Rediriger vers la page "admindetails.html"
    return "admindetails";
  }

  @GetMapping("/editAdmin")
  public String editAdmin(Long id, Model model) {
    User admin = adminService.getAdminById(id);
    if (admin == null) {
      throw new RuntimeException("Admin not found");
    }

    model.addAttribute("user", admin); // Make sure the attribute name matches the one in your Thymeleaf template
    model.addAttribute("exposant", admin.getExposant()); // Vous devrez peut-être ajuster ceci en fonction de votre modèle de données
    model.addAttribute("isAdmin", true);

    return "editAdmin"; // Assurez-vous que vous avez une vue Thymeleaf appelée "editAdmin.html"
  }

    @PostMapping("/saveAdmin")
  public String saveAdmin(@Valid User user, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "home";
    }

    // Fetch the existing Exposant from the database
    User existingUser = userRepository.findById(user.getUserId()).orElse(null);
    if (existingUser == null) {
      throw new RuntimeException("User not found");
    }


    if (user != null) {
      existingUser.setUsername(user.getUsername());
      existingUser.setTelephone(user.getTelephone());
    }
      existingUser.setTelephone(user.getTelephone().isEmpty() ? null : user.getTelephone());

    // Save the updated Exposant (including the User relationship) to the database
      userRepository.save(existingUser);

    // Redirect to the dashboard page after saving the changes
    return "redirect:/dashboard";
  }

}