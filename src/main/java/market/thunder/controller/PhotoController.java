package market.thunder.controller;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Photo;
import market.thunder.service.PhotoService;
import market.thunder.utils.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    // 사진 업로드
    @PostMapping("photos/upload")
    @ResponseBody
    public String uploadPhoto(MultipartFile photo) throws IOException {
        String path = FileUtils.photoPath;
        String saveName = UUID.randomUUID() + ".png";

        // 서버 저정소에 저장하기
        FileUtils.save(photo, path, saveName);

        // DB에 정보 저장하기
        Photo savePhoto = new Photo();
        savePhoto.setOriginalName(photo.getOriginalFilename());
        savePhoto.setSaveName(saveName);
        photoService.save(savePhoto);

        // 파일 정보 JSON으로 반환
        JSONObject response = new JSONObject();
        response.put("originalName", savePhoto.getOriginalName());
        response.put("saveName", savePhoto.getSaveName());

        return response.toString();
    }

    // 사진 불러오기
    @GetMapping("photos/{saveName}")
    public void getPhoto(@PathVariable("saveName")String saveName, HttpServletResponse response) throws IOException {

        File photo = new File(FileUtils.photoPath + saveName);

        FileInputStream fileInputStream = new FileInputStream(photo);

        // response에 사진 담아서 전달
        IOUtils.copy(fileInputStream, response.getOutputStream());
        fileInputStream.close();
    }

}
