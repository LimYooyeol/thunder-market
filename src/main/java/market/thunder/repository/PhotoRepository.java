package market.thunder.repository;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Photo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class PhotoRepository {

    private final EntityManager em;

    // 사진 정보 저장
    public String save(Photo photo){
        em.persist(photo);

        return photo.getSaveName();
    }

    // 파일명으로 사진 조회
    public Photo findBySaveName(String saveName){
        return em.find(Photo.class, saveName);
    }


}
