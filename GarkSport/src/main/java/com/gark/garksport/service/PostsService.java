package com.gark.garksport.service;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Posts;

import com.gark.garksport.repository.AcademieRepository;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor


public class PostsService {



    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private AcademieRepository academieRepository;

    @Autowired
    private AdherentRepository adherentRepository;
    private final UserService userService;


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
        if (post.getAuthorImageUrl() == null || post.getAuthorImageUrl().isEmpty()){
            post.setAuthorImageUrl("https://img.freepik.com/free-psd/3d-illustration-person-with-sunglasses_23-2149436188.jpg?size=338&ext=jpg&ga=GA1.1.1224184972.1713830400&semt=ais");

        }
        Academie academie = academieRepository.findByManagerId(userService.getUserId(connectedUser.getName()));

        post.setCreatedAt(new Date());
        post.setAuthor(userService.getUserFullName(connectedUser.getName()));
        post.setAcademie(academie);
        return postsRepository.save(post);
    }

    // web
    public List<Posts> getAcademiePosts(Principal connectedUser

    ) {

        Integer managerAcademieId = academieRepository.findByManagerId(userService.getUserId(connectedUser.getName())).getId();

        return postsRepository.findAllByAcademieIdOrderByCreatedAtDescIdDesc(managerAcademieId);
    }
    //mobile

    public List<Posts> getadherentPosts(Principal connectedUser

    ) {

        Integer AcademieId = academieRepository.findByAdherentsId(userService.getUserId(connectedUser.getName())).getId();

        return postsRepository.findAllByAcademieIdOrderByCreatedAtDescIdDesc(AcademieId);
    }
    public List<Posts> getPublicPosts() {

        return postsRepository.findAllByPublicAudienceIsTrueOrderByCreatedAtDescIdDesc();
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
            existingPost.setPublicAudience(post.getPublicAudience());
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
