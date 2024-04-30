package com.nikitin.roadmaps.roadmapsbackendspring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "pk_sequence"
	)
	@SequenceGenerator(name="pk_sequence",sequenceName="pk_sequence", allocationSize=1)
	private Long id;
}
