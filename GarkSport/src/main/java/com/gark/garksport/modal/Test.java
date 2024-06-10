package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String testName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academie_id")
    @JsonIgnore
    private Academie academie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adherent_id")
    @JsonIgnore
    private Adherent adherent;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("test")
    private List<Categorie> categories = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @PrePersist
    protected void onCreate() {
        creationDate = new Date();
    }

}
