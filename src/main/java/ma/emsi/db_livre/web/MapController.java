package ma.emsi.db_livre.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {
    @GetMapping(path = "/map")
    public String map(){
        return "map";
    }

}
