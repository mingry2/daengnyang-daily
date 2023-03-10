package com.daengnyangffojjak.dailydaengnyang.domain.dto.pet;

import com.daengnyangffojjak.dailydaengnyang.domain.entity.Pet;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.enums.Sex;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.enums.Species;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PetShowResponse {

	private Long id;
	private String name;
	private Species species;
	private String breed;
	private Sex sex;
	private LocalDate birthday;
	private Double weight;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime lastModifiedAt;

	public static PetShowResponse showFrom(Pet pet) {
		return PetShowResponse.builder()
				.id(pet.getId())
				.name(pet.getName())
				.species(pet.getSpecies())
				.breed(pet.getBreed())
				.sex(pet.getSex())
				.birthday(pet.getBirthday())
				.weight(pet.getWeight())
				.createdAt(pet.getCreatedAt())
				.lastModifiedAt(pet.getLastModifiedAt())
				.build();
	}

}
