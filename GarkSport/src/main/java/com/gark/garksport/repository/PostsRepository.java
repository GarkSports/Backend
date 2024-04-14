package com.gark.garksport.repository;

import com.gark.garksport.modal.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts,Integer> {

    List<Posts> findAllByOrderByCreatedAtDesc();
}
