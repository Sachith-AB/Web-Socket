package com.realtimeposts.Repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.realtimeposts.Entities.Post;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    // Get post by ID
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }


    // Create new post
    public Post createPost(Post post) {
        Post savedPost = postRepository.save(post);
        
        // Send real-time notification
        messagingTemplate.convertAndSend("/topic/posts", savedPost);
        
        return savedPost;
    }

    public Post updatePost(Long id, Post postDetails) {
        Optional<Post> optionalPost = postRepository.findById(id);
        
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setTitle(postDetails.getTitle());
            post.setContent(postDetails.getContent());
            post.setAuthor(postDetails.getAuthor());
            
            Post updatedPost = postRepository.save(post);
            
            // Send real-time notification
            messagingTemplate.convertAndSend("/topic/posts/updated", updatedPost);
            
            return updatedPost;
        }
        
        throw new RuntimeException("Post not found with id: " + id);
    }

    // Delete post
    public void deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            
            // Send real-time notification
            messagingTemplate.convertAndSend("/topic/posts/deleted", id);
        } else {
            throw new RuntimeException("Post not found with id: " + id);
        }
    }

    public Post toggleLike(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setLikeCount(post.getLikeCount() + 1);
            
            Post updatedPost = postRepository.save(post);
            
            // Send real-time notification for like update
            messagingTemplate.convertAndSend("/topic/posts/liked", updatedPost);
            
            return updatedPost;
        }
        
        throw new RuntimeException("Post not found with id: " + id);
    }

    public List<Post> getPostsByAuthor(String author) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(author);
    }
    
    // Search posts by title
    public List<Post> searchPostsByTitle(String keyword) {
        return postRepository.findByTitleContaining(keyword);
    }
}
