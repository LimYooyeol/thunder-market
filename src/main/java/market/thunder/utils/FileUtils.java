package market.thunder.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static String photoPath = "C:/thunder-image/"; // 서버 내 저장소

    // 이미지 파일 서버 저장소에 저장
    public static void save(MultipartFile photo, String path, String saveName) throws IOException {
        File file = new File(path, saveName);
        photo.transferTo(file);
    }
}
