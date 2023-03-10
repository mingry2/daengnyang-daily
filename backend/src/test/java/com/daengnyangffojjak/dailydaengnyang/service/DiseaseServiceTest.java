package com.daengnyangffojjak.dailydaengnyang.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.daengnyangffojjak.dailydaengnyang.domain.dto.MessageResponse;
import com.daengnyangffojjak.dailydaengnyang.domain.dto.disease.DizGetResponse;
import com.daengnyangffojjak.dailydaengnyang.domain.dto.disease.DizWriteRequest;
import com.daengnyangffojjak.dailydaengnyang.domain.dto.disease.DizWriteResponse;
import com.daengnyangffojjak.dailydaengnyang.domain.dto.user.UserRole;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Disease;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Group;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Pet;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.User;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.enums.DiseaseCategory;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.enums.Sex;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.enums.Species;
import com.daengnyangffojjak.dailydaengnyang.exception.DiseaseException;
import com.daengnyangffojjak.dailydaengnyang.exception.ErrorCode;
import com.daengnyangffojjak.dailydaengnyang.fixture.GroupFixture;
import com.daengnyangffojjak.dailydaengnyang.repository.DiseaseRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.TagRepository;
import com.daengnyangffojjak.dailydaengnyang.utils.Validator;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

class DiseaseServiceTest {

	private final DiseaseRepository diseaseRepository = mock(DiseaseRepository.class);
	private final TagRepository tagRepository = mock(TagRepository.class);
	private final Validator validator = mock(Validator.class);

	Group group = GroupFixture.get();
	Pet pet = Pet.builder().id(1L).birthday(LocalDate.of(2018, 3, 1)).species(Species.CAT)
			.name("????????????").group(group).sex(Sex.NEUTERED_MALE).build();

	private DiseaseService diseaseService = new DiseaseService(diseaseRepository, tagRepository, validator);

	@Nested
	@DisplayName("?????? ??????")
	class CreateDisease {

		DizWriteRequest request = DizWriteRequest.builder().name("????????????")
				.category(DiseaseCategory.DERMATOLOGY)
				.startedAt(LocalDate.of(2023, 1, 1)).endedAt(LocalDate.of(2023, 1, 31)).build();
		Disease saved = Disease.builder().id(1L).pet(pet).name(request.getName())
				.category(request.getCategory())
				.startedAt(request.getStartedAt()).endedAt(request.getEndedAt()).build();

		@Test
		@DisplayName("??????")
		void success() {
			given(validator.getPetWithUsername(1L, "user")).willReturn(pet);
			given(diseaseRepository.save(request.toEntity(pet))).willReturn(
					saved);

			DizWriteResponse response = assertDoesNotThrow(
					() -> diseaseService.create(1L, request, "user"));
			assertEquals(1L, response.getId());
			assertEquals("????????????", response.getPetName());
			assertEquals("????????????", response.getName());
		}

		@Test
		@DisplayName("?????? - ?????? ????????? ???????????? ??????")
		void fail_????????????() {
			given(validator.getPetWithUsername(1L, "user")).willReturn(pet);
			given(diseaseRepository.save(request.toEntity(pet))).willReturn(
					saved);
			given(diseaseRepository.existsByPetIdAndName(1L, "????????????")).willReturn(
					true);

			DiseaseException e = assertThrows(DiseaseException.class,
					() -> diseaseService.create(1L, request, "user"));
			assertEquals(ErrorCode.DUPLICATED_DISEASE_NAME, e.getErrorCode());

		}
	}

	@Nested
	@DisplayName("?????? ??????")
	class ModifyDisease {

		DizWriteRequest request = DizWriteRequest.builder().name("????????????")
				.category(DiseaseCategory.DERMATOLOGY)
				.startedAt(LocalDate.of(2023, 1, 1)).endedAt(LocalDate.of(2023, 1, 31)).build();
		Disease saved = Disease.builder().id(1L).pet(pet).name("????????????")
				.category(request.getCategory())
				.startedAt(request.getStartedAt()).endedAt(request.getEndedAt()).build();
		Disease modified = Disease.builder().id(1L).pet(pet).name(request.getName())
				.category(request.getCategory())
				.startedAt(request.getStartedAt()).endedAt(request.getEndedAt()).build();

		@Test
		@DisplayName("??????")
		void success() {
			given(validator.getPetWithUsername(1L, "user")).willReturn(pet);
			given(validator.getDiseaseById(1L)).willReturn(saved);
			given(diseaseRepository.saveAndFlush(saved)).willReturn(
					modified);

			DizWriteResponse response = assertDoesNotThrow(
					() -> diseaseService.modify(1L, 1L, request, "user"));
			assertEquals(1L, response.getId());
			assertEquals("????????????", response.getPetName());
			assertEquals("????????????", response.getName());
		}

		@Test
		@DisplayName("?????? - ????????? ??? ????????? ??? ???????????? ?????? ??????")
		void fail_??????????????????() {
			Pet pet2 = Pet.builder().id(100L).birthday(LocalDate.of(2018, 3, 1))
					.species(Species.CAT)
					.name("hoon").group(group).sex(Sex.NEUTERED_MALE).build();
			given(validator.getPetWithUsername(100L, "user")).willReturn(pet2);
			given(validator.getDiseaseById(1L)).willReturn(saved);

			DiseaseException e = assertThrows(DiseaseException.class,
					() -> diseaseService.modify(100L, 1L, request, "user"));
			assertEquals(ErrorCode.INVALID_REQUEST, e.getErrorCode());
		}
	}


	@Nested
	@DisplayName("?????? ?????? ??????")
	class DeleteDisease {

		Disease saved = Disease.builder().id(1L).pet(pet).name("??????")
				.category(DiseaseCategory.DERMATOLOGY)
				.build();

		@Test
		@DisplayName("??????")
		void success() {
			given(validator.getPetWithUsername(1L, "user")).willReturn(pet);
			given(validator.getDiseaseById(1L)).willReturn(saved);

			MessageResponse response = assertDoesNotThrow(
					() -> diseaseService.delete(1L, 1L, "user"));
			assertEquals("?????? ????????? ?????????????????????.", response.getMsg());
		}
	}

	@Nested
	@DisplayName("?????? ?????? ??????")
	class GetDisease {

		Disease saved = Disease.builder().id(1L).pet(pet).name("??????")
				.category(DiseaseCategory.DERMATOLOGY)
				.build();

		@Test
		@DisplayName("??????")
		void success() {
			given(validator.getPetWithUsername(1L, "user")).willReturn(pet);
			given(validator.getDiseaseById(1L)).willReturn(saved);

			DizGetResponse response = assertDoesNotThrow(
					() -> diseaseService.getDisease(1L, 1L, "user"));
			assertEquals(1L, response.getId());
			assertEquals("??????", response.getName());
		}
	}

	@Nested
	@DisplayName("?????? ????????? ??????")
	class GetListDisease {

		Disease saved = Disease.builder().id(1L).pet(pet).name("??????")
				.category(DiseaseCategory.DERMATOLOGY).startedAt(LocalDate.of(2000, 1, 1))
				.build();

		@Test
		@DisplayName("??????")
		void success() {

			given(validator.getPetWithUsername(1L, "user")).willReturn(pet);
			given(diseaseRepository.findAllByPetId(Sort.by(Direction.DESC, "startedAt"), 1L)).willReturn(List.of(saved));

			List<DizGetResponse> response = assertDoesNotThrow(
					() -> diseaseService.getDiseaseList(1L, "user"));
			assertEquals(1, response.size());
			assertEquals("??????", response.get(0).getName());
			assertEquals(LocalDate.of(2000, 1, 1), response.get(0).getStartedAt());
		}
	}
}