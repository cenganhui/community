package cgh.community.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Akuma
 * @date 2020/5/17 11:16
 */
@Controller
public class VideoController {

    @GetMapping("/videos")
    public String videos(){
        return "videos";
    }
    @GetMapping("/video")
    public String video(@RequestParam(name = "aid") String aid,
                        @RequestParam(name = "page",defaultValue = "1") String page,
                        Model model){
        model.addAttribute("aid",aid);
        model.addAttribute("page",page);
        return "video";
    }



}
