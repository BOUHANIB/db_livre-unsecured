package ma.emsi.db_livre.web;


import ma.emsi.db_livre.entities.Exposant;
import ma.emsi.db_livre.entities.User;
import ma.emsi.db_livre.repositories.ExposantRepository;
import ma.emsi.db_livre.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@SuppressWarnings("SameReturnValue")
@Controller
public class RegistrationController {

  @Autowired
  private UserService userService;

  @Autowired
  public ExposantRepository exposantRepository;

  @GetMapping("/register")
  public String getRegister(ModelMap model) {
    model.addAttribute("user", new User());
    return "register";
  }

  @PostMapping("/register")
  public String postCreateUser(ModelMap modelMap, User user) {
    user.setUsername(user.getUsername().trim());

    if (userService.usernameExists(user.getUsername())) {
      modelMap.addAttribute("userExists", "Username not available");
      return "register";
    }

    if (userService.emailExists(user.getEmail())) {
      modelMap.addAttribute("emailExists", "Email already registered");
      return "register";
    }

    if (user.getPassword().length() < 6) {
      modelMap.addAttribute("badPassword", "Password must be 6+ characters long");
      return "register";
    }

    // Create the User entity
    userService.createUser(user);

    // Create the Exposant entity and set up the relationship with the User entity
    Exposant exposant = new Exposant();
    exposant.setUser(user); // Set the User entity in Exposant

    // Save the Exposant entity
    exposantRepository.save(exposant);

    return "redirect:/login";
  }

}
