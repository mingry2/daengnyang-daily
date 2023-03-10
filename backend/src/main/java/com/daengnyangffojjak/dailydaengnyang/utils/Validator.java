package com.daengnyangffojjak.dailydaengnyang.utils;

import com.daengnyangffojjak.dailydaengnyang.domain.entity.Comment;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Disease;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Group;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Monitoring;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Notification;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.NotificationUser;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Pet;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Record;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.RecordFile;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Tag;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.User;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.UserGroup;
import com.daengnyangffojjak.dailydaengnyang.exception.CommentException;
import com.daengnyangffojjak.dailydaengnyang.exception.DiseaseException;
import com.daengnyangffojjak.dailydaengnyang.exception.ErrorCode;
import com.daengnyangffojjak.dailydaengnyang.exception.FileException;
import com.daengnyangffojjak.dailydaengnyang.exception.GroupException;
import com.daengnyangffojjak.dailydaengnyang.exception.MonitoringException;
import com.daengnyangffojjak.dailydaengnyang.exception.NotificationException;
import com.daengnyangffojjak.dailydaengnyang.exception.PetException;
import com.daengnyangffojjak.dailydaengnyang.exception.RecordException;
import com.daengnyangffojjak.dailydaengnyang.exception.RecordFileException;
import com.daengnyangffojjak.dailydaengnyang.exception.TagException;
import com.daengnyangffojjak.dailydaengnyang.exception.UserException;
import com.daengnyangffojjak.dailydaengnyang.repository.CommentRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.DiseaseRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.GroupRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.MonitoringRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.NotificationRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.NotificationUserRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.PetRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.RecordFileRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.RecordRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.TagRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.UserGroupRepository;
import com.daengnyangffojjak.dailydaengnyang.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class Validator {

	private final UserRepository userRepository;
	private final UserGroupRepository userGroupRepository;
	private final GroupRepository groupRepository;
	private final PetRepository petRepository;
	private final MonitoringRepository monitoringRepository;
	private final RecordRepository recordRepository;
	private final TagRepository tagRepository;
	private final DiseaseRepository diseaseRepository;
	private final RecordFileRepository recordFileRepository;

	private final CommentRepository commentRepository;
	private final NotificationRepository notificationRepository;
	private final NotificationUserRepository notificationUserRepository;

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserException(ErrorCode.INVALID_REQUEST));
	}

	public User getUserByUserName(String userName) {
		return userRepository.findByUserName(userName)
				.orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND));
	}

	public Group getGroupById(Long groupId) {    //?????? ???????????? ?????? ??????, ????????? ?????? ??????
		return groupRepository.findById(groupId)
				.orElseThrow(() -> new GroupException(ErrorCode.GROUP_NOT_FOUND));
	}

	public Pet getPetById(Long petId) {
		return petRepository.findById(petId)
				.orElseThrow(() -> new PetException(ErrorCode.PET_NOT_FOUND));
	}

	public Monitoring getMonitoringById(Long monitoringId) {
		return monitoringRepository.findById(monitoringId)
				.orElseThrow(() -> new MonitoringException(ErrorCode.MONITORING_NOT_FOUND));
	}

	public Tag getTagById(Long tagId) {
		return tagRepository.findById(tagId)
				.orElseThrow(() -> new TagException(ErrorCode.TAG_NOT_FOUND));
	}

	public Disease getDiseaseById(Long diseaseId) {
		return diseaseRepository.findById(diseaseId)
				.orElseThrow(() -> new DiseaseException(ErrorCode.DISEASE_NOT_FOUND));
	}

	public RecordFile getRecordFileById(Long recordFileId) {
		return recordFileRepository.findById(recordFileId)
				.orElseThrow(() -> new RecordFileException(ErrorCode.RECORDFILE_NOT_FOUND));
	}

	//Pet??? username??? User??? ?????? ???????????? Pet??? ??????
	public Pet getPetWithUsername(Long petId, String username) {
		Pet pet = getPetById(petId);
		List<UserGroup> userGroupList = getUserGroupListByUsername(pet.getGroup(),
				username);
		return pet;
	}

	public Record getRecordById(Long recordId) {
		return recordRepository.findById(recordId)
				.orElseThrow(() -> new RecordException(ErrorCode.RECORD_NOT_FOUND));
	}

	/**
	 * User??? Group??? ?????? ????????? UserGroupList ?????? ?????? ADMMIN??? ???????????? ?????? ??????
	 **/
	public List<UserGroup> getUserGroupListByUsername(Group group, String username) {
		User user = getUserByUserName(username);     //?????? ??????
		//???????????? ?????? ??? ?????? ????????????
		List<UserGroup> userGroupList = userGroupRepository.findAllByGroup(group);
		//???????????? ????????? ?????? ??? ???????????? ?????? -> ?????? ??? ????????? ????????? ?????? ??????
		if (userGroupList.stream()
				.noneMatch(userGroup -> username.equals(userGroup.getUser().getUsername()))) {
			throw new GroupException(ErrorCode.INVALID_PERMISSION);
		}
		return userGroupList;
	}

	public Comment getCommentById(Long commentId) {
		return  commentRepository.findById(commentId)
				.orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
	}

	// ??? ????????? ????????? ????????? ????????? ????????? ????????? ???
	public void validateFile(List<MultipartFile> multipartFiles) {
		for (MultipartFile multipartFile : multipartFiles) {
			if (multipartFile.isEmpty()) {
				throw new FileException(ErrorCode.FILE_NOT_FOUND);
			}
		}
	}

	//??????????????? : ?????? ??? ?????? -> ??? ??????
	public Map<Long, String> makeMapWithRoleAndId(List<UserGroup> userGroupList) {
		Map<Long, String> roleIdMap = new HashMap<>();
		for (UserGroup userGroup : userGroupList) {
			Long userId = userGroup.getUser().getId();
			String role = userGroup.getRoleInGroup();

			roleIdMap.put(userId, role);
		}
		return roleIdMap;
	}

	public Notification getNotificationById(Long notificationId) {
		return notificationRepository.findById(notificationId)
				.orElseThrow(() -> new NotificationException(ErrorCode.NOTIFICATION_NOT_FOUND));
	}

	public NotificationUser validateNotificationUser(Long notificationId, Long loginUserId){
		return notificationUserRepository.findByNotificationIdAndUserId(
						notificationId, loginUserId)
				.orElseThrow(() -> new NotificationException(ErrorCode.INVALID_PERMISSION));
	}


}
