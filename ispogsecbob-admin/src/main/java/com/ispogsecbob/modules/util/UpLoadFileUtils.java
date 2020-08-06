package com.ispogsecbob.modules.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传util
 */
public class UpLoadFileUtils {
    public static boolean upLoad (String UPLOAD_FILES_PATH, String fileName, MultipartFile file) {
        if(file.isEmpty()){
            return false;
        }else{
            File dest = new File(UPLOAD_FILES_PATH  + "/" +  fileName );
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
