package com.nikitin.roadmaps.roadmapsbackendspring.entity;


import com.nikitin.roadmaps.roadmapsbackendspring.utils.enums.CompetenceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "profile")
public class Profile extends BaseEntity {

	@Column(name = "picture", columnDefinition = "text")
	private String picture;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
	private String name;

	@Column(name = "last_name", nullable = false, columnDefinition = "varchar(50)")
	private String lastName;

	@Formula("CONCAT_WS( ' ', name, last_name)")
	private String fullName;

	@Column(name = "email", nullable = false, unique = true, columnDefinition = "varchar(50)")
	private String email;

	@Column(name = "competence", columnDefinition = "varchar(50)")
	@Enumerated(value = EnumType.STRING)
	private CompetenceType competence;

	@Column(name = "speciality", columnDefinition = "varchar(50)")
	private String speciality;

	@Column(name = "last_date_login", columnDefinition = "timestamptz")
	private Instant lastDateLogin;

	@Column(name = "birht_date", columnDefinition = "timestamptz")
	private Instant birthDate;

	@Column(name = "residential_address", columnDefinition = "varchar(100)")
	private String residentialAddress;

}
