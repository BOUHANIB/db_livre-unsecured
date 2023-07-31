package ma.emsi.db_livre.web;

import ma.emsi.db_livre.entities.User;
import ma.emsi.db_livre.security.ActiveUserStore;
import ma.emsi.db_livre.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {
  @Autowired
  private ActiveUserStore activeUserStore;
  @Autowired
  private AdminService adminService;

  @GetMapping("/admin")
  public String getAdmin(ModelMap modelMap) {
    List<User> allUserAccounts = adminService.getAllUserAccounts();
    modelMap.addAttribute("users", allUserAccounts);
    modelMap.addAttribute("activeUsers", activeUserStore.getUsers());
    modelMap.addAttribute("isLoggedIn", true);
    modelMap.addAttribute("isAdmin", true);
    return "admin";
  }

}