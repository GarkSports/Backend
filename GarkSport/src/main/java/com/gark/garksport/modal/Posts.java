package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String subtitle;
    private String body;
    private String author;
    private String category;
    private String imageUrl;
    private Integer views;
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "posts")
    private Academie academie;



}
