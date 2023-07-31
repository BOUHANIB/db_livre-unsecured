package ma.emsi.db_livre.service;

import ma.emsi.db_livre.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private UserService userService;

    @Secured({"ROLE_ADMIN"})
    public List<User> getAllUserAccounts() {
        return userService.findAll();
    }
}
