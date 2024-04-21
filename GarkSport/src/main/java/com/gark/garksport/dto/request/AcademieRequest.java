package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Academie;
import lombok.Data;

import java.util.Set;

@Data
public class AcademieRequest {
    private Academie academie;
    private Set<Integer> disciplineIds;

    public Academie getAcademie() {
        return academie;
    }
}
