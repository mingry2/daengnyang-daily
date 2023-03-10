package com.daengnyangffojjak.dailydaengnyang.domain.dto.monitoring;

import com.daengnyangffojjak.dailydaengnyang.domain.entity.Monitoring;
import com.daengnyangffojjak.dailydaengnyang.domain.entity.Pet;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MntWriteRequest {

	@NotNull(message = "날짜를 입력하세요.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@PastOrPresent (message = "모니터링 날짜는 과거 또는 현재 날짜여야 합니다.")
	private LocalDate date;          //오늘 이전 날짜
	private Double weight;        //몸무게

	private Boolean vomit;        //구토
	private Boolean amPill;       //오전 투약
	private Boolean pmPill;       //오후 투약
	private Boolean customSymptom;           //custom symptom
	private String customSymptomName;        //custom symptom 이름

	private Integer feedToGram;        //식이량 (g)
	private Integer walkCnt;         //산책횟수 - 강아지
	private Integer playCnt;         //놀이횟수 - 고양이
	private Integer urination;        //배뇨 횟수
	private Integer defecation;       //배변 횟수
	private Integer respiratoryRate;  //호흡수
	private Integer customInt;        //custom 모니터링
	private String customIntName;

	private String notes;    //기타 특이사항
	public Monitoring toEntity(Pet pet) {
		return Monitoring.builder()
				.pet(pet)
				.date(this.date)
				.weight(this.weight)
				.vomit(this.vomit)
				.amPill(this.amPill)
				.pmPill(this.pmPill)
				.customSymptom(this.customSymptom)
				.customSymptomName(this.customSymptomName)
				.feedToGram(this.feedToGram)
				.walkCnt(this.walkCnt)
				.playCnt(this.playCnt)
				.urination(this.urination)
				.defecation(this.defecation)
				.respiratoryRate(this.respiratoryRate)
				.customInt(this.customInt)
				.customIntName(this.customIntName)
				.notes(this.notes)
				.build();
	}
}
