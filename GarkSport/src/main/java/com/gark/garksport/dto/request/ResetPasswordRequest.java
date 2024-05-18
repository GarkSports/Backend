package com.gark.garksport.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    // Getters and Setters
    public String getMotDePasseActuelle() {
        return currentPassword;
    }

    public void setMotDePasseActuelle(String motDePasseActuelle) {
        this.currentPassword = motDePasseActuelle;
    }

    public String getNouveauMotDePasse() {
        return newPassword;
    }

    public void setNouveauMotDePasse(String nouveauMotDePasse) {
        this.newPassword = nouveauMotDePasse;
    }

    public String getConfirmerMotDePasse() {
        return confirmPassword;
    }

    public void setConfirmerMotDePasse(String confirmerMotDePasse) {
        this.confirmPassword = confirmerMotDePasse;
    }
}
