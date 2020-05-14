package cgh.community.community.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 图片存放工具类
 * @author Akuma
 * @date 2020/5/14 20:20
 */
public class ImgSave {
    // 设置文件存放路径
    private static final String SaveFilePath = "D:/testImg";
    // 设置静态文件服务器的ip:端口
    private static final String Host = "D:/testImg";

    /**
     * 保存文件
     * @param multipartFile 接收到的文件参数
     * @param fileName 保存到磁盘的文件名
     * @return 文件对外访问的url
     * @throws Exception
     */
    public static String saveFile(MultipartFile multipartFile, String fileName) throws Exception{
        // 文件全名
        String fileFullName = String.format("%s/%s",SaveFilePath,fileName);

        // 保存文件到磁盘
        File dest = new File(fileFullName);
        multipartFile.transferTo(dest);

        String url = String.format("%s/%s",Host,fileName);
        return url;
    }
}
