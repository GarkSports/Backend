package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Academie;

import java.util.Set;

public class AcademieRequest {
    private Academie academie;
    private Set<Integer> disciplineIds;

    public Academie getAcademie() {
        return academie;
    }

    public Set<Integer> getDisciplineIds() {
        return disciplineIds;
    }
}
