package com.gark.garksport.modal.eventsExtends;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Evenement;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class MatchEntreNous extends Evenement {
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Adherent> equipe1;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Adherent> equipe2;
}
