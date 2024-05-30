package com.gark.garksport.dto.request;

import com.gark.garksport.modal.ConvocationEntrainement;
import com.gark.garksport.modal.Evenement;
import lombok.Data;

import java.util.List;

@Data
public class EntrainementRequest {
    private ConvocationEntrainement convocationEntrainement;
    private List<Integer> idAdherents;

    public ConvocationEntrainement getConvocationEntrainement() {
        return convocationEntrainement;
    }

    public List<Integer> getIdAdherents() {
        return idAdherents;
    }
}
