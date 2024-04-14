package com.gark.garksport.service;

import com.gark.garksport.modal.Posts;
import com.gark.garksport.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor


public class PostsService {


    @Autowired
    private PostsRepository postsRepository;

    // Create operation
    public Posts createPost(Posts post) {
        System.out.println("Inside createPost method");
        System.out.println("Original imageUrl: " + post.getImageUrl());
        System.out.println("Category: " + post.getCategory());

        if (post.getImageUrl() == null || post.getImageUrl().isEmpty()) {
            switch (post.getCategory().toLowerCase()) {
                case "football":
                    post.setImageUrl("https://c4.wallpaperflare.com/wallpaper/173/952/871/field-sport-football-stadium-wallpaper-preview.jpg");
                    break;
                case "basketball":
                    post.setImageUrl("https://images.freecreatives.com/wp-content/uploads/2015/10/basketball_pool_reflection.jpg");
                    break;
                case "tennis":
                    post.setImageUrl("https://www.mouratoglou.com/wp-content/uploads/2019/04/lacademie_courtscouverts-1-700x350.png");
                    break;

            }
        }
        System.out.println("Updated imageUrl: " + post.getImageUrl());
        post.setCreatedAt(new Date());

        return postsRepository.save(post);
    }

    // Read operation
    public List<Posts> getAllPosts() {
        return postsRepository.findAllByOrderByCreatedAtDesc();
    }

    public Posts getPostById(Integer id) {
        return postsRepository.findById(id).orElse(null);
    }

    // Update operation
    public Posts updatePost(Integer id, Posts post) {
        Posts existingPost = postsRepository.findById(id).orElse(null);
        if (existingPost != null) {
            existingPost.setTitle(post.getTitle());
            existingPost.setSubtitle(post.getSubtitle());
            existingPost.setBody(post.getBody());
            existingPost.setAuthor(post.getAuthor());
            existingPost.setAuthorImageUrl(post.getAuthorImageUrl());
            existingPost.setCategory(post.getCategory());
            existingPost.setImageUrl(post.getImageUrl());
            existingPost.setViews(post.getViews());
            existingPost.setCreatedAt(new Date());
            return postsRepository.save(existingPost);
        }
        return null;
    }

    // Delete operation
    public void deletePost(Integer id) {
        postsRepository.deleteById(id);
    }



}
