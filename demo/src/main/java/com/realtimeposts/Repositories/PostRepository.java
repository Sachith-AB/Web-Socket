package com.realtimeposts.Repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.realtimeposts.Entities.Post;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByAuthorOrderByCreatedAtDesc(String author);

     // Find posts with title containing keyword
    @Query("SELECT p FROM Post p WHERE p.title LIKE %?1% ORDER BY p.createdAt DESC")
    List<Post> findByTitleContaining(String keyword);
    
    // Count posts by author
    long countByAuthor(String author);
}
