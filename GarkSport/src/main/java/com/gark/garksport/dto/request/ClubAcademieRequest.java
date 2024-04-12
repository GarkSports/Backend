package com.gark.garksport.dto.request;

import lombok.Data;

@Data
public class ClubAcademieRequest {
    public long academieCount;
    public long clubCount;
    public long academiePercentage;
    public long clubPercentage;

    public ClubAcademieRequest() {
    }
    public ClubAcademieRequest(long academieCount, long clubCount, long academiePercentage, long clubPercentage) {
        this.academieCount = academieCount;
        this.clubCount = clubCount;
        this.academiePercentage = academiePercentage;
        this.clubPercentage = clubPercentage;
    }
}
