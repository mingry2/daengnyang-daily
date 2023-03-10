package com.daengnyangffojjak.dailydaengnyang.repository;

import com.daengnyangffojjak.dailydaengnyang.domain.entity.Record;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

	Page<Record> findAllByIsPublicTrue(Pageable pageable);

	Page<Record> findAllByIsPublicTrueAndPetId(Pageable pageable, Long petId);

	boolean existsByTagId(Long tagId);
	List<Record> findAllByCreatedAtBetweenAndPetId (Sort sort, LocalDateTime start, LocalDateTime end, Long petId);
}

