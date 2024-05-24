package com.example.WeatherWise;

import com.example.WeatherWise.controller.UserController;
import com.example.WeatherWise.model.User;
import com.example.WeatherWise.model.Location;
import com.example.WeatherWise.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(UserController.class)
class WeatherWiseApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testRegister() throws Exception {
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword("password");

		when(userService.register(any(User.class))).thenReturn(user);

		mockMvc.perform(post("/user/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("test@example.com")));
	}

	@Test
	public void testLogin() throws Exception {
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword("password");

		when(userService.login(any(User.class))).thenReturn(user);

		mockMvc.perform(post("/user/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("test@example.com")));
	}

	@Test
	public void testAddLocation() throws Exception {
		Map<String, String> response = Map.of("message", "Success");

		when(userService.addLocation(anyString(), anyLong())).thenReturn(response);

		mockMvc.perform(post("/user/1/add_location")
						.contentType(MediaType.APPLICATION_JSON)
						.content("\"New York\""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("Success")));
	}

	@Test
	public void testGetLocations() throws Exception {
		Location location = new Location();
		location.setLocationName("New York");

		List<Location> locations = Collections.singletonList(location);

		when(userService.getLocations(anyLong())).thenReturn(locations);

		mockMvc.perform(get("/user/get_locations/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].locationName", is("New York")));
	}
}
