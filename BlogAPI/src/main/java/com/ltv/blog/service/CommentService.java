package com.ltv.blog.service;

import com.ltv.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {
    public CommentDto createComment(Long postId, CommentDto commentDto);
    public List<CommentDto> getCommentsByPostId(Long postId);

    public CommentDto getCommentById(Long commentId, Long postId);

    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest);

    void deleteComment(Long postId, Long commentId);
}
