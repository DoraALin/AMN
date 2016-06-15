package me.lin.amn.repository;

/**
 * Created by Lin on 5/24/16.
 */

import me.lin.amn.repository.model.AboutService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryResourceController {
    @RequestMapping("/about")
    public String about () {
        return new AboutService()
                .getVERSION();
    }
}
