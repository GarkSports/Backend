package com.gark.garksport.dto.request;

import com.gark.garksport.modal.enums.EvenementType;
import lombok.Data;

@Data
public class EventTypeRequest {
    private EvenementType type;
    private long count;

    public EventTypeRequest(EvenementType type, long count) {
        this.type = type;
        this.count = count;
    }
}
