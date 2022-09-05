package market.thunder.service;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Photo;
import market.thunder.domain.Post;
import market.thunder.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final PhotoRepository photoRepository;

    // 사진 저장
    @Transactional
    public String save(Photo photo){
        return photoRepository.save(photo);
    }

    // 사진 정보 조회
    public Photo findBySaveName(String saveName){
        return photoRepository.findBySaveName(saveName);
    }

    // 사진과 게시물 매칭
    @Transactional
    public void matchPost(String[] saveNames, Post post) {
        for(String saveName : saveNames){
            Photo photo = photoRepository.findBySaveName(saveName);
            photo.setPost(post);
        }
    }

    // 사진과 게시물 매칭 끊기
    @Transactional
    public void unMatch(String saveName) {
        Photo photo = photoRepository.findBySaveName(saveName);
        photo.setPost(null);
    }
}
