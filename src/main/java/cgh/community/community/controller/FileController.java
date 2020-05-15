package cgh.community.community.controller;

import cgh.community.community.dto.FileDTO;
import cgh.community.community.util.ImgSave;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 文件上传（待）
 * @author Akuma
 * @date 2020/5/13 20:14
 */
@Controller
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("editormd-image-file");

        FileDTO fileDTO = new FileDTO();
        // 校验传入文件有效性
        if (multipartFile==null || multipartFile.isEmpty()){
            throw new RuntimeException("文件传入错误");
        }
        String fileName = String.format("%s_%s",new Date().getTime(),multipartFile.getOriginalFilename());
        // 保存图片
        String url= null;
        try {
            url = ImgSave.saveFile(multipartFile,fileName);
            System.out.println("url " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileDTO.setSuccess(1);
        fileDTO.setMessage("ok");
        fileDTO.setUrl(url);

        return fileDTO;
    }

}
