package com.daengnyangffojjak.dailydaengnyang.controller.rest;

import static com.daengnyangffojjak.dailydaengnyang.utils.RestDocsConfiguration.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.daengnyangffojjak.dailydaengnyang.domain.dto.MessageResponse;
import com.daengnyangffojjak.dailydaengnyang.domain.dto.tag.TagListResponse;
import com.daengnyangffojjak.dailydaengnyang.domain.dto.tag.TagWorkRequest;
import com.daengnyangffojjak.dailydaengnyang.domain.dto.tag.TagWorkResponse;
import com.daengnyangffojjak.dailydaengnyang.exception.ErrorCode;
import com.daengnyangffojjak.dailydaengnyang.exception.TagException;
import com.daengnyangffojjak.dailydaengnyang.service.TagService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(TagRestController.class)
class TagRestControllerTest extends ControllerTest {

	@MockBean
	protected TagService tagService;

	@Nested
	@DisplayName("?????? ??????")
	class TagCreate {

		TagWorkRequest request = new TagWorkRequest("??????");

		@Test
		@DisplayName("??????")
		void success() throws Exception {
			given(tagService.create(1L, request, "user")).willReturn(new TagWorkResponse(1L, "??????"));

			mockMvc.perform(
							post("/api/v1/groups/{groupId}/tags", 1L)
									.content(objectMapper.writeValueAsBytes(request))
									.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.resultCode").value("SUCCESS"))
					.andExpect(jsonPath("$.result.id").value(1L))
					.andDo(restDocs.document(
							pathParameters(
									parameterWithName("groupId").description("?????? ??????")
							),
							requestFields(fieldWithPath("name").description("?????? ??????")
									.attributes(field("constraints", "2~10??? ??????"))),
							responseFields(fieldWithPath("resultCode").description("????????????"),
									fieldWithPath("result.id").description("?????? ?????? ??????"),
									fieldWithPath("result.name").description("?????? ??????"))));
			verify(tagService).create(1L, request, "user");

		}
	}

	@Nested
	@DisplayName("?????? ??????")
	class TagModify {

		TagWorkRequest request = new TagWorkRequest("??????");

		@Test
		@DisplayName("??????")
		void success() throws Exception {
			given(tagService.modify(1L, 1L, request, "user")).willReturn(
					new TagWorkResponse(1L, "??????"));

			mockMvc.perform(
							put("/api/v1/groups/{groupId}/tags/{tagId}", 1L, 1L)
									.content(objectMapper.writeValueAsBytes(request))
									.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.resultCode").value("SUCCESS"))
					.andExpect(jsonPath("$.result.id").value(1L))
					.andDo(restDocs.document(
							pathParameters(
									parameterWithName("groupId").description("?????? ??????"),
									parameterWithName("tagId").description("?????? ??????")
							),
							requestFields(fieldWithPath("name").description("?????? ??????")
									.attributes(field("constraints", "2~10??? ??????"))),
							responseFields(fieldWithPath("resultCode").description("????????????"),
									fieldWithPath("result.id").description("?????? ?????? ??????"),
									fieldWithPath("result.name").description("?????? ??????"))));
			verify(tagService).modify(1L, 1L, request, "user");

		}
	}

	@Nested
	@DisplayName("?????? ??????")
	class TagDelete {

		@Test
		@DisplayName("??????")
		void success() throws Exception {
			given(tagService.delete(1L, 1L, "user")).willReturn(
					new MessageResponse("????????? ?????????????????????."));

			mockMvc.perform(
							delete("/api/v1/groups/{groupId}/tags/{tagId}", 1L, 1L))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.resultCode").value("SUCCESS"))
					.andExpect(jsonPath("$.result.msg").value("????????? ?????????????????????."))
					.andDo(restDocs.document(
							pathParameters(
									parameterWithName("groupId").description("?????? ??????"),
									parameterWithName("tagId").description("?????? ??????")
							),
							responseFields(fieldWithPath("resultCode").description("????????????"),
									fieldWithPath("result.msg").description("?????? ?????????"))));
			verify(tagService).delete(1L, 1L, "user");

		}

		@Test
		@DisplayName("?????? - ????????? ???????????? ?????? ???")
		void fail_???????????????() throws Exception {
			given(tagService.delete(1L, 1L, "user")).willThrow(
					new TagException(ErrorCode.INVALID_REQUEST));

			mockMvc.perform(
							delete("/api/v1/groups/{groupId}/tags/{tagId}", 1L, 1L))
					.andExpect(status().isConflict())
					.andExpect(jsonPath("$.resultCode").value("ERROR"))
					.andExpect(jsonPath("$.result.errorCode").value("INVALID_REQUEST"))
					.andDo(restDocs.document(
							pathParameters(
									parameterWithName("groupId").description("?????? ??????"),
									parameterWithName("tagId").description("?????? ??????")
							),
							responseFields(fieldWithPath("resultCode").description("????????????"),
									fieldWithPath("result.errorCode").description("????????????"),
									fieldWithPath("result.message").description("?????? ?????????"))));
			verify(tagService).delete(1L, 1L, "user");

		}
	}

	@Nested
	@DisplayName("??????????????? ??????")
	class TagListGet {

		@Test
		@DisplayName("??????")
		void success() throws Exception {
			given(tagService.getList(1L, "user")).willReturn(
					List.of(
							new TagListResponse(1L, "??????"),
							new TagListResponse(2L, "??????")));

			mockMvc.perform(
							get("/api/v1/groups/{groupId}/tags", 1L))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.resultCode").value("SUCCESS"))
					.andExpect(jsonPath("$.result").exists())
					.andDo(restDocs.document(
							pathParameters(
									parameterWithName("groupId").description("?????? ??????")
							),
							responseFields(fieldWithPath("resultCode").description("????????????"),
									fieldWithPath("result[].id").description("?????? ????????????"),
									fieldWithPath("result[].name").description("?????? ?????????"))));
			verify(tagService).getList(1L, "user");

		}
	}

}