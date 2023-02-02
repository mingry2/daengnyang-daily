package com.daengnyangffojjak.dailydaengnyang.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.daengnyangffojjak.dailydaengnyang.domain.dto.user.UserRole;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Group;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Monitoring;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Pet;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.User;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.UserGroup;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.enums.Sex;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.enums.Species;
import com.daengnyangffojjak.dailydaengnyang.exception.ErrorCode;
import com.daengnyangffojjak.dailydaengnyang.exception.GroupException;
import com.daengnyangffojjak.dailydaengnyang.exception.MonitoringException;
import com.daengnyangffojjak.dailydaengnyang.exception.PetException;
import com.daengnyangffojjak.dailydaengnyang.exception.UserException;
import com.daengnyangffojjak.dailydaengnyang.repository.GroupRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.MonitoringRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.PetRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.UserGroupRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ValidatorTest {

	private final UserRepository userRepository = mock(UserRepository.class);
	private final UserGroupRepository userGroupRepository = mock(UserGroupRepository.class);
	private final GroupRepository groupRepository = mock(GroupRepository.class);
	private final PetRepository petRepository = mock(PetRepository.class);
	private final MonitoringRepository monitoringRepository = mock(MonitoringRepository.class);
	User user = User.builder().id(1L).userName("user").password("password").email("@.")
			.role(UserRole.ROLE_USER).build();
	Group group = Group.builder().id(1L).name("그룹이름").user(user).build();
	Pet pet = Pet.builder().id(1L).birthday(LocalDate.of(2018, 3, 1)).species(Species.CAT)
			.name("hoon").group(group).sex(Sex.NEUTERED_MALE).build();

	private Validator validator;

	@BeforeEach
	void setUp() {
		validator = new Validator(userRepository, userGroupRepository, groupRepository,
				petRepository, monitoringRepository);
	}

	@Nested
	@DisplayName("유저 아이디로 유저 반환")
	class GetUserById {


		@Test
		@DisplayName("성공")
		void success() {
			given(userRepository.findById(1L)).willReturn(Optional.of(user));

			User result = assertDoesNotThrow(() -> validator.getUserById(1L));
			assertEquals(1L, result.getId());
			assertEquals("user", result.getUsername());
			assertEquals("@.", result.getEmail());
		}

		@Test
		@DisplayName("실패 - 없음")
		void fail() {
			given(userRepository.findById(1L)).willReturn(Optional.empty());

			UserException e = assertThrows(UserException.class, () -> validator.getUserById(1L));
			assertEquals(ErrorCode.INVALID_REQUEST, e.getErrorCode());
		}
	}

	@Nested
	@DisplayName("유저네임로 유저 반환")
	class GetUserByUsername {

		@Test
		@DisplayName("성공")
		void success() {
			given(userRepository.findByUserName("user")).willReturn(Optional.of(user));

			User result = assertDoesNotThrow(() -> validator.getUserByUserName("user"));
			assertEquals(1L, result.getId());
			assertEquals("user", result.getUsername());
			assertEquals("@.", result.getEmail());
		}

		@Test
		@DisplayName("실패 - 없음")
		void fail() {
			given(userRepository.findByUserName("user")).willReturn(Optional.empty());

			UserException e = assertThrows(UserException.class,
					() -> validator.getUserByUserName("user"));
			assertEquals(ErrorCode.USERNAME_NOT_FOUND, e.getErrorCode());
		}
	}

	@Nested
	@DisplayName("그룹 아이디로 그룹 반환")
	class GetGroupById {


		@Test
		@DisplayName("성공")
		void success() {
			given(groupRepository.findById(1L)).willReturn(Optional.of(group));

			Group result = assertDoesNotThrow(() -> validator.getGroupById(1L));
			assertEquals(1L, result.getId());
			assertEquals("그룹이름", result.getName());
		}

		@Test
		@DisplayName("실패 - 없음")
		void fail() {
			given(groupRepository.findById(1L)).willThrow(
					new GroupException(ErrorCode.GROUP_NOT_FOUND));

			GroupException e = assertThrows(GroupException.class,
					() -> validator.getGroupById(1L));
			assertEquals(ErrorCode.GROUP_NOT_FOUND, e.getErrorCode());
		}
	}

	@Nested
	@DisplayName("펫 등록번호로 펫 반환하기")
	class getPetById {

		@Test
		@DisplayName("성공")
		void success() {
			given(petRepository.findById(1L)).willReturn(Optional.of(pet));

			Pet pet = assertDoesNotThrow(
					() -> validator.getPetById(1L));
			assertEquals("hoon", pet.getName());
		}

		@Test
		@DisplayName("실패")
		void fail() {
			given(petRepository.findById(1L)).willThrow(
					new PetException(ErrorCode.PET_NOT_FOUND));

			PetException e = assertThrows(PetException.class,
					() -> validator.getPetById(1L));
			assertEquals(ErrorCode.PET_NOT_FOUND, e.getErrorCode());
		}
	}

	@Nested
	@DisplayName("모니터링 등록번호로 모니터링 반환하기")
	class getMonitoringById {

		@Test
		@DisplayName("성공")
		void success() {
			Monitoring monitoring = Monitoring.builder()
					.id(1L).pet(pet).date(LocalDate.of(2023, 1, 30)).weight(7.7).vomit(false)
					.amPill(true).pmPill(true).urination(3).defecation(2).notes("양치").build();
			given(monitoringRepository.findById(1L)).willReturn(Optional.of(monitoring));

			Monitoring response = assertDoesNotThrow(
					() -> validator.getMonitoringById(1L));
			assertEquals(1L, response.getId());
			assertFalse(response.isVomit());
		}

		@Test
		@DisplayName("실패")
		void fail() {
			given(monitoringRepository.findById(1L)).willThrow(
					new MonitoringException(ErrorCode.MONITORING_NOT_FOUND));

			MonitoringException e = assertThrows(MonitoringException.class,
					() -> validator.getMonitoringById(1L));
			assertEquals(ErrorCode.MONITORING_NOT_FOUND, e.getErrorCode());
		}
	}

	@Nested
	@DisplayName("유저가 그룹에 속하면 유저그룹리스트 반환하기")
	class getMemListByUserName {

		User user2 = User.builder().id(3L).userName("user2").build();
		User user3 = User.builder().id(4L).userName("user3").build();

		List<UserGroup> userGroupList = List.of(
				new UserGroup(1L, user, group, "mom"),
				new UserGroup(2L, user2, group, "dad"));

		@Test
		@DisplayName("성공")
		void success() {
			given(userRepository.findByUserName("user")).willReturn(Optional.of(user));
			given(userGroupRepository.findAllByGroup(group)).willReturn(userGroupList);

			List<UserGroup> list = assertDoesNotThrow(
					() -> validator.getUserGroupListByUsername(group, "user"));
			assertEquals(2, list.size());
		}

		@Test
		@DisplayName("실패")
		void fail() {
			List<UserGroup> userGroupList = List.of(
					new UserGroup(1L, user3, group, "mom"),
					new UserGroup(2L, user2, group, "dad"));
			given(userRepository.findByUserName("user")).willReturn(Optional.of(user));
			given(userGroupRepository.findAllByGroup(group)).willReturn(userGroupList);

			GroupException e = assertThrows(GroupException.class,
					() -> validator.getUserGroupListByUsername(group, "user"));
			assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
		}
	}

}