package com.kn.auth.integration_test;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kn.auth.enums.EmailError;
import com.kn.auth.enums.PasswordError;
import com.kn.auth.enums.ReturnPosition;
import com.kn.auth.enums.TableName;
import com.kn.auth.services.DatabaseAdminService;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private DatabaseAdminService databaseAdminService;
	private String email = "test@gmail.com";
	private String password = "!Test777";

	@BeforeEach
	void setUp() {
		try {
			String test_authentication_id = databaseAdminService.getValueByValue(TableName.AUTHENTICATION, "email",
					email,
					"id",
					ReturnPosition.FIRST);
			databaseAdminService.deleteValueByValue(TableName.BUYERS, "authentication_id", test_authentication_id);
			databaseAdminService.deleteValueByValue(TableName.AUTHENTICATION_ROLE, "authentication_id",
					test_authentication_id);
			databaseAdminService.deleteValueByValue(TableName.AUTHENTICATION, "email", email);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Test
	void authenticate() throws Exception {
		MvcResult previous_response;

		previous_response = mockMvc.perform(post("/public/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.token").isNotEmpty())
				.andDo(print())
				.andReturn();

		previous_response = mockMvc.perform(post("/public/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.token").isNotEmpty())
				.andDo(print())
				.andReturn();

		ObjectMapper objectMapper = new ObjectMapper();
		String responseContent = previous_response.getResponse().getContentAsString();
		JsonNode jsonNode = objectMapper.readTree(responseContent);
		String token = String.format("Bearer %s", jsonNode.get("data").get("token").asText());
		
		mockMvc.perform(get("/private/buyer/profile")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
				.content("{}"))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	void register() throws Exception {
		mockMvc.perform(post("/public/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error").value(nullValue()))
				.andDo(print());
	}

	@Test
	void registerCorrectly() throws Exception {
		mockMvc.perform(post("/public/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error").value(nullValue()))
				.andDo(print());
	}

	@Test
	void weakPasswords() throws Exception {
		Map<PasswordError, String> passwords = Map.of(
				PasswordError.EMPTY, "",
				PasswordError.TOO_SHORT, "a",
				PasswordError.MISSING_DIGIT, "!Aaaaaaaa",
				PasswordError.MISSING_SPECIAL_CHARACTER, "Aa1aaaaaa",
				PasswordError.MISSING_LOWERCASE_LETTER, "!A1AAAAAA",
				PasswordError.MISSING_UPPERCASE_LETTER, "!a1aaaaaa");

		for (Map.Entry<PasswordError, String> password_ : passwords.entrySet()) {
			mockMvc.perform(post("/public/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password_.getValue())))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.error.exceptionMessage").value(password_.getKey().getValue()))
					.andDo(print());
		}
	}

	@Test
	void weakEmails() throws Exception {
		Map<EmailError, String> emails = Map.of(
				EmailError.EMPTY, "",
				EmailError.WRONG_FORMAT, "a");

		for (Map.Entry<EmailError, String> email_ : emails.entrySet()) {
			mockMvc.perform(post("/public/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email_.getValue(), password)))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.error.exceptionMessage").value(email_.getKey().getValue()))
					.andDo(print());
		}
	}

	@Test
	void registerEmailRepeats() throws Exception {
		mockMvc.perform(post("/public/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error").value(nullValue()))
				.andDo(print());
		mockMvc.perform(post("/public/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password)))
				.andExpect(status().isConflict())
				.andDo(print());
	}

	@Test
	void unauthorizedToPrivate() throws Exception {
		mockMvc.perform(get("/private/hello-world!")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization",
						"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
				.content("{}"))
				.andExpect(status().isForbidden())
				.andDo(print());
	}

	@Test
	void authenticateWithToken() throws Exception {
		MvcResult previous_response;

		previous_response = mockMvc.perform(post("/public/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.token").isNotEmpty())
				.andDo(print())
				.andReturn();

		previous_response = mockMvc.perform(post("/public/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.token").isNotEmpty())
				.andDo(print())
				.andReturn();

		ObjectMapper objectMapper = new ObjectMapper();
		String responseContent = previous_response.getResponse().getContentAsString();
		JsonNode jsonNode = objectMapper.readTree(responseContent);
		String token = String.format("Bearer %s", jsonNode.get("data").get("token").asText());

		mockMvc.perform(get("/private/hello-world!")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
				.content("{}"))
				.andExpect(status().isOk())
				.andExpect(content().string("Hello, world! Private"))
				.andDo(print());
	}

}
