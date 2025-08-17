package com.neec.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neec.config.SecurityConfig;
import com.neec.dto.CustomPrincipal;
import com.neec.dto.ProfileDTO;
import com.neec.enums.EnumGender;
import com.neec.service.ProfileService;
import com.neec.util.JwtUtil;

@WebMvcTest(controllers = {ProfileController.class})
@Import(value = {SecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
public class ProfileControllerTest {
	@MockitoBean
	private ProfileService mockProfileService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private JwtUtil mockJwtUtil;

	@Test
	void test_saveProfile_BlankRequestBody() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/profiles")
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request)
				.andDo(print())
				.andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		JsonNode jsonNode = toJsonNode(result.getResponse().getContentAsString());
		assertTrue(jsonNode.get("error").asText().contains("Required request body is missing"));
	}

	@Test
	void test_saveProfile_EmptyJson() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/profiles")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}");
		MvcResult result = mockMvc.perform(request)
				.andDo(print())
				.andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		JsonNode jsonNode = toJsonNode(result.getResponse().getContentAsString());
		assertNotNull(jsonNode.get("firstName"));
		assertNotNull(jsonNode.get("lastName"));
		assertNotNull(jsonNode.get("phoneNumber"));
		assertNotNull(jsonNode.get("yearOfPassing"));
		assertNotNull(jsonNode.get("gender"));
		assertNotNull(jsonNode.get("city"));
		assertNotNull(jsonNode.get("percentage"));
		assertNotNull(jsonNode.get("pinCode"));
		assertNotNull(jsonNode.get("addressLine1"));
		assertNotNull(jsonNode.get("dateOfBirth"));
		assertNotNull(jsonNode.get("state"));
		assertNotNull(jsonNode.get("schoolName"));
		assertNotNull(jsonNode.get("boardName"));
	}

	@Test
	void test_saveProfile_Invalid_Fields_Values() throws Exception {
		ProfileDTO dto = ProfileDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.dateOfBirth(LocalDate.of(2026, 10, 11))
				.gender(EnumGender.M)
				.phoneNumber("476543#1A")
				.addressLine1("123 Main St")
				.addressLine2("Apt 4B")
				.city("New York")
				.state("NY")
				.pinCode("2A45*")
				.schoolName("XYZ High School")
				.boardName("CBSE")
				.yearOfPassing((short)2030)
				.percentage(new BigDecimal(165.20))
				.build();
		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/profiles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJsonString(dto));
		MvcResult result = mockMvc.perform(request)
				.andDo(print())
				.andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		JsonNode jsonNode = toJsonNode(result.getResponse().getContentAsString());
		assertNotNull(jsonNode.get("dateOfBirth"));
		assertNotNull(jsonNode.get("phoneNumber"));
		assertNotNull(jsonNode.get("pinCode"));
		assertNotNull(jsonNode.get("yearOfPassing"));
		assertNotNull(jsonNode.get("percentage"));
	}

	@Test
	void test_saveProfile_New() throws Exception {
		CustomPrincipal customPrincipal = CustomPrincipal.builder()
				.emailAddress("email_13_aug_01@gmail.com")
				.subject("102")
				.build();
		TestingAuthenticationToken testingAuthenticationToken =
				new TestingAuthenticationToken(customPrincipal, null);
		SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
		ProfileDTO dto = ProfileDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.dateOfBirth(LocalDate.of(1985, 10, 11))
				.gender(EnumGender.M)
				.phoneNumber("9876543210")
				.addressLine1("123 Main St")
				.addressLine2("Apt 4B")
				.city("New York")
				.state("NY")
				.pinCode("415236")
				.schoolName("XYZ High School")
				.boardName("CBSE")
				.yearOfPassing((short)2000)
				.percentage(new BigDecimal(65.20))
				.build();
		doNothing().when(mockProfileService).saveProfile(anyLong(), any(ProfileDTO.class));
		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/profiles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJsonString(dto));
		MvcResult result = mockMvc.perform(request)
				.andDo(print())
				.andReturn();
		verify(mockProfileService, times(1)).saveProfile(anyLong(), any(ProfileDTO.class));
		assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
		JsonNode jsonNode = toJsonNode(result.getResponse().getContentAsString());
		assertTrue(jsonNode.get("status").asText().equals("Student Profile is created"));
	}

	@Test
	void test_saveProfile_Existing() throws Exception {
		CustomPrincipal customPrincipal = CustomPrincipal.builder()
				.emailAddress("email_13_aug_01@gmail.com")
				.subject("102")
				.build();
		TestingAuthenticationToken authenticationToken =
				new TestingAuthenticationToken(customPrincipal, null);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		Long userId = 102L;
		ProfileDTO dto = ProfileDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.dateOfBirth(LocalDate.of(1985, 10, 11))
				.gender(EnumGender.M)
				.phoneNumber("9876543210")
				.addressLine1("123 Main St")
				.addressLine2("Apt 4B")
				.city("New York")
				.state("NY")
				.pinCode("415236")
				.schoolName("XYZ High School")
				.boardName("CBSE")
				.yearOfPassing((short)2000)
				.percentage(new BigDecimal(65.20))
				.build();
		doThrow(new IllegalArgumentException("Student profile with id " + userId + " already exists."))
			.when(mockProfileService).saveProfile(anyLong(), any(ProfileDTO.class));
		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/profiles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJsonString(dto));
		MvcResult result = mockMvc.perform(request)
				.andDo(print())
				.andReturn();
		verify(mockProfileService).saveProfile(anyLong(), any(ProfileDTO.class));
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		JsonNode jsonNode = toJsonNode(result.getResponse().getContentAsString());
		assertTrue(jsonNode.get("error").asText().equals("Student profile with id " + userId + " already exists."));
	}

	private JsonNode toJsonNode(String jsonString) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readTree(jsonString);
	}

	private String toJsonString(ProfileDTO dto) throws JsonProcessingException {
		return objectMapper.writeValueAsString(dto);
	}
}
