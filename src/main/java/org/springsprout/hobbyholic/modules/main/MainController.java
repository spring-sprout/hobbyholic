package org.springsprout.hobbyholic.modules.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Keesun Baik
 */
@Controller
public class MainController {

    @RequestMapping("/main")
    public String main(){
        return "main";
    }


}
