package com.gark.garksport.controller;

import com.gark.garksport.modal.Posts;
import com.gark.garksport.service.AdminService;
import com.gark.garksport.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostsController {
    private final AdminService adminService;

    @Autowired
    private PostsService postsService;



    @GetMapping("/getacademiePosts")
    public List<Posts> getacademiePosts(
            Principal connectedUser
    ) {
        return postsService.getAcademiePosts(connectedUser);
    }

    @GetMapping("/getadherentPosts")
    public List<Posts> getadherentPosts(
            Principal connectedUser
    ) {
        return postsService.getadherentPosts(connectedUser);
    }


    @GetMapping("/getpublicPosts")
    public List<Posts> getpublicPosts() {
        return postsService.getPublicPosts();
    }



    // Create operation
    //    @PreAuthorize("hasAuthority('ajouter_blog')")

    @PostMapping("/addpost")
    public ResponseEntity<?> createPost(@RequestBody Posts post,Principal connectedUser) {
        if (post.getTitle() == null || post.getTitle().isEmpty() ||
                post.getSubtitle() == null || post.getSubtitle().isEmpty() ||
                post.getBody() == null || post.getBody().isEmpty() ||
                post.getCategory() == null || post.getCategory().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tous les champs doivent être remplis.");
        }

        // If imageUrl is empty, set it to null


        Posts createdPost = postsService.createPost(post,connectedUser);
        if (createdPost != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la création du post.");
        }
    }

    @GetMapping("/{id}")
    public Posts getPostById(Principal connectedUser,@PathVariable Integer id) {
        return postsService.getPostById(connectedUser,id);
    }

    //    @PreAuthorize("hasAuthority('modifier_blog')")

    @PutMapping("/updatepost/{id}")
    public Posts updatePost(@PathVariable Integer id, @RequestBody Posts post) {
        return postsService.updatePost(id, post);
    }

    // @PreAuthorize("hasAuthority('supprimer_blog')")
    @DeleteMapping("/deletepost/{id}")
    public void deletePost(@PathVariable Integer id) {
        postsService.deletePost(id);
    }


}
