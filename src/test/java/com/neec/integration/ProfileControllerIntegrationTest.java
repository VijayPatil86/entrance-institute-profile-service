package com.neec.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neec.dto.ProfileDTO;
import com.neec.enums.EnumGender;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileControllerIntegrationTest {
	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	static final private String JWT_TOKEN =
			"Bearer " +
			"eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbEFkZHJlc3MiOiJlbWFpbF8xM19hdWdfMDFAZ21haWwuY29tIiwicm9sZXMiOlsiQVBQTElDQU5UIl0sInN1YiI6IjEwMiIsImlhdCI6MTc1NTM0MjkxNiwiZXhwIjoxNzU1MzQzNTE2fQ.MtzcrHsEbHKQlIyW0szh-5TlMUgnAh0AU5dmc63zAX8";

	@Order(1)
	@Test
	void test_saveProfile_New() throws Exception {
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
		RequestEntity<Object> request = RequestEntity.post(URI.create("/api/v1/profiles"))
				.header("Authorization", JWT_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.body(toJsonString(dto));
		ResponseEntity<String> response = testRestTemplate.exchange(request, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		JsonNode jsonNode = toJsonNode(response.getBody());
		assertTrue(jsonNode.get("status").asText().equals("Student Profile is created"));
	}

	@Order(2)
	@Test
	void test_saveProfile_Again() throws Exception {
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
		RequestEntity<Object> request = RequestEntity.post(URI.create("/api/v1/profiles"))
				.header("Authorization", JWT_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.body(toJsonString(dto));
		ResponseEntity<String> response = testRestTemplate.exchange(request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JsonNode jsonNode = toJsonNode(response.getBody());
		assertTrue(jsonNode.get("error").asText().equals("Student profile with id " + userId + " already exists."));
	}

	private String toJsonString(ProfileDTO dto) throws JsonProcessingException {
		return objectMapper.writeValueAsString(dto);
	}

	private JsonNode toJsonNode(String jsonString) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readTree(jsonString);
	}
}
