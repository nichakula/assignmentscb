package com.assignment.api.unittest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootTestCase {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testAddUser() throws Exception {
		String json = "{\"username\":\"ton1\",\"password\":\"test1\",\"date_of_birth\":\"15/01/1985\"}";
		mockMvc.perform(post("/user").content(json).contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testLogin() throws Exception {
		String json = "{\"username\":\"ton1\",\"password\":\"test1\",\"date_of_birth\":\"15/01/1985\"}";
		mockMvc.perform(post("/login").content(json).contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testLoginIncorrect() throws Exception {
		String json = "{\"username\":\"ton22\",\"password\":\"test1\",\"date_of_birth\":\"15/01/1985\"}";
		mockMvc.perform(post("/login").content(json).contentType("application/json;charset=UTF-8"))
				.andExpect(status().isNotAcceptable());
	}

	@Test
	public void testAddOrder() throws Exception {
		String json = "{\"orders\":[1,4]}";
		mockMvc.perform(post("/users/orders").content(json))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testGetUser() throws Exception {
		mockMvc.perform(get("/users")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("name"));
	}
	
	@Test
	public void testGetBooks() throws Exception {
		mockMvc.perform(get("/books")).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		mockMvc.perform(delete("/users")).andExpect(status().isOk());
	}
}