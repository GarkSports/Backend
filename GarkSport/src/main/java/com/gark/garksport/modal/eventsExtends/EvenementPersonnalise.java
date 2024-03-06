package com.gark.garksport.modal.eventsExtends;

import com.gark.garksport.modal.Evenement;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class EvenementPersonnalise extends Evenement {
}
