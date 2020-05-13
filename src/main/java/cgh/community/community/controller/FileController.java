package cgh.community.community.controller;

import cgh.community.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 文件上传（待）
 * @author Akuma
 * @date 2020/5/13 20:14
 */
@Controller
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setMessage("ok");
        fileDTO.setUrl("/images/ro.png");
        return fileDTO;
    }
}
