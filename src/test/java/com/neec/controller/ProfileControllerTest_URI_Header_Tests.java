package com.neec.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
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
import com.neec.filter.JwtAuthenticationFilter;
import com.neec.service.ProfileService;
import com.neec.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@WebMvcTest(controllers = {ProfileController.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
public class ProfileControllerTest_URI_Header_Tests {
	@MockitoBean
	private ProfileService mockProfileService;
	@MockitoBean
	private JwtUtil mockJwtUtil;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void test_saveProfile_NoAuthorizationHeader() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/profiles");
		MvcResult result = mockMvc.perform(request)
				.andDo(print())
				.andReturn();
		assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
		JsonNode jsonNode = toJsonNode(result.getResponse().getContentAsString());
		assertTrue(jsonNode.get("error").asText().equals("missing or invalid Authorization header"));
	}

	@Test
	void test_saveProfile_BlankAuthorizationHeader() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/profiles")
				.header("Authorization", "");
		MvcResult result = mockMvc.perform(request)
				.andDo(print())
				.andReturn();
		assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
		JsonNode jsonNode = toJsonNode(result.getResponse().getContentAsString());
		assertTrue(jsonNode.get("error").asText().equals("missing or invalid Authorization header"));
	}

	@Test
	void test_saveProfile_TokenString_Without_Bearer_Word() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/profiles")
				.header("Authorization", "token-without-Bearer-word");
		MvcResult result = mockMvc.perform(request)
				.andDo(print())
				.andReturn();
		assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
		JsonNode jsonNode = toJsonNode(result.getResponse().getContentAsString());
		assertTrue(jsonNode.get("error").asText().equals("missing or invalid Authorization header"));
	}

	@Test
	void test_saveProfile_Invalid_Token() throws Exception {
		when(mockJwtUtil.getJwtPayload(anyString())).thenThrow(new SignatureException("invalid token"));
		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/profiles")
			.header("Authorization", "Bearer invalid-token");
		MvcResult result = mockMvc.perform(request)
			.andDo(print())
			.andReturn();
		verify(mockJwtUtil, times(1)).getJwtPayload(anyString());
		assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
		JsonNode jsonNode = toJsonNode(result.getResponse().getContentAsString());
		assertTrue(jsonNode.get("error").asText().equals("invalid token"));
	}

	@Test
	void test_saveProfile_Valid_Expired_Token() throws Exception {
		when(mockJwtUtil.getJwtPayload(anyString())).
			thenThrow(new ExpiredJwtException(null, null, "token expired"));
		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/profiles")
				.header("Authorization", "Bearer expired-token");
		MvcResult result = mockMvc.perform(request)
				.andDo(print())
				.andReturn();
		verify(mockJwtUtil, times(1)).getJwtPayload(anyString());
		assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
		JsonNode jsonNode = toJsonNode(result.getResponse().getContentAsString());
		assertTrue(jsonNode.get("error").asText().equals("token expired"));
	}

	private JsonNode toJsonNode(String jsonString) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readTree(jsonString);
	}
}
