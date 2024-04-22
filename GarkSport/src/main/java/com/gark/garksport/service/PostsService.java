package com.gark.garksport.service;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Posts;
import com.gark.garksport.modal.User;
import com.gark.garksport.repository.AcademieRepository;
import com.gark.garksport.repository.PostsRepository;
import com.gark.garksport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor


public class PostsService {



    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private AcademieRepository academieRepository;
    private final UserRepository repository;

    public User getProfil(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<User> userOptional = repository.findById(user.getId());

        return userOptional.orElse(null);
    }
    // Create operation
    public Posts createPost(Posts post,Principal connectedUser) {
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
        User user = getProfil(connectedUser);
        Academie academie = academieRepository.findByManagerId(user.getId());

        post.setCreatedAt(new Date());
        post.setAuthor(user.getFirstname());
        post.setAcademie(academie);
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
