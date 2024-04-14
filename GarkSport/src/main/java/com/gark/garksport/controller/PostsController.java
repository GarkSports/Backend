package com.gark.garksport.controller;

import com.gark.garksport.modal.Posts;
import com.gark.garksport.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private PostsService postsService;

    // Read operation
    @GetMapping
    public List<Posts> getAllPosts() {
        return postsService.getAllPosts();
    }

    // Create operation
    @PostMapping("/addpost")
    public ResponseEntity<?> createPost(@RequestBody Posts post) {
        if (post.getTitle() == null || post.getTitle().isEmpty() ||
                post.getSubtitle() == null || post.getSubtitle().isEmpty() ||
                post.getBody() == null || post.getBody().isEmpty() ||
                //TODO:ajouter nom de user connecté
                //post.getAuthor() == null || post.getAuthor().isEmpty() ||
                post.getCategory() == null || post.getCategory().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tous les champs doivent être remplis.");
        }

        // If imageUrl is empty, set it to null


        Posts createdPost = postsService.createPost(post);
        if (createdPost != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la création du post.");
        }
    }

            @GetMapping("/{id}")
    public Posts getPostById(@PathVariable Integer id) {
        return postsService.getPostById(id);
    }

    // Update operation
    @PutMapping("/updatepost/{id}")
    public Posts updatePost(@PathVariable Integer id, @RequestBody Posts post) {
        return postsService.updatePost(id, post);
    }

    // Delete operation
    @DeleteMapping("/deletepost/{id}")
    public void deletePost(@PathVariable Integer id) {
        postsService.deletePost(id);
    }
}
