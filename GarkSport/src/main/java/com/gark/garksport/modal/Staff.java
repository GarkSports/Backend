package com.gark.garksport.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Staff extends User {
}
