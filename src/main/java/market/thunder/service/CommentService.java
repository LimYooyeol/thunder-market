package market.thunder.service;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Comment;
import market.thunder.domain.CommentStatus;
import market.thunder.form.CommentForm;
import market.thunder.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 추가
    @Transactional
    public Long add(Comment comment){
        commentRepository.save(comment);
        if(comment.getGroupId() == 0){
            comment.setGroupId(comment.getId());
        }

        return comment.getId();
    }

    // 댓글 ID로 댓글 조회
    public Comment findByCommentId(Long commentId){
        return commentRepository.findByCommentId(commentId);
    }

    // 게시물 ID로 게시물에 달린 댓글 조회
    public List<Comment> findCommentsByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long commentId) {
        if(hasReply(commentId)){ // 답글이 있거나, 답글이면 상태를 deleted로 변경
            Comment comment = commentRepository.findByCommentId(commentId);
            comment.updateStatus(CommentStatus.DELETED);
        }
        else {                  // 단독으로 존재하면, 실제로 삭제
            commentRepository.delete(commentId);
        }
    }

    // 답글 유무 검사
    private boolean hasReply(Long commentId) {
        Comment comment = findByCommentId(commentId);
        if(comment.getGroupId() != comment.getId()){ // 검사하는 댓글 자체가 답글인 경우
            return true;
        }
        else{
            if(commentRepository.findReplies(commentId).size() > 1){ // 답글이 있는 경우
                return true;
            }
            return false; //답글이 없는 경우
        }
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, String newContent) {
        Comment comment = commentRepository.findByCommentId(commentId);
        comment.updateContent(newContent);
    }
}
