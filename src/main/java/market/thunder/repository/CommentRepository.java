package market.thunder.repository;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    // 댓글 저장
    public Long save(Comment comment){
        em.persist(comment);

        return comment.getId();
    }

    // 게시물 번호로 게시물엗 달린 댓글들 조회
    public List<Comment> findByPostId(Long postId){

        List<Comment> comments = em.createQuery("select c from Comment c where c.post.id = :postId ORDER BY c.groupId, c.id", Comment.class)
                .setParameter("postId", postId)
                .getResultList();

        return comments;
    }

    // 댓글 ID로 댓글 조회
    public Comment findByCommentId(Long commentId) {
        return em.find(Comment.class, commentId);
    }

    // 댓글 삭제
    public void delete(Long commentId) {
        Comment comment = findByCommentId(commentId);
        em.remove(comment);
    }

    // 댓글에 달린 답글 조회
    public List<Comment> findReplies(Long commentId) {
        return em.createQuery("select c from Comment c where c.groupId =: commentId")
                .setParameter("commentId", commentId)
                .getResultList();
    }
}
