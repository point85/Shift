package org.point85.workschedule.test.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.point85.workschedule.ShiftApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShiftApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ScheduleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private static final String SCHEDULE_JSON = "{\n"
			+ "  \"name\": \"Manufacturing\",\n"
			+ "  \"description\": \"Manufacturing 24/7 schedule\",\n"
			+ "  \"shifts\": [\n"
			+ "    { \"name\": \"Day\", \"description\": \"Day shift\", \"start\": \"07:00:00\", \"duration\": \"PT8H\", \"breaks\": [] },\n"
			+ "    { \"name\": \"Evening\", \"description\": \"Evening shift\", \"start\": \"15:00:00\", \"duration\": \"PT8H\", \"breaks\": [] },\n"
			+ "    { \"name\": \"Night\", \"description\": \"Night shift\", \"start\": \"23:00:00\", \"duration\": \"PT8H\", \"breaks\": [] }\n"
			+ "  ],\n"
			+ "  \"rotations\": [\n"
			+ "    { \"name\": \"DayRot\", \"description\": \"Day rotation\", \"segments\": [{ \"shiftName\": \"Day\", \"daysOn\": 5, \"daysOff\": 2 }] },\n"
			+ "    { \"name\": \"EveRot\", \"description\": \"Eve rotation\", \"segments\": [{ \"shiftName\": \"Evening\", \"daysOn\": 5, \"daysOff\": 2 }] },\n"
			+ "    { \"name\": \"NightRot\", \"description\": \"Night rotation\", \"segments\": [{ \"shiftName\": \"Night\", \"daysOn\": 5, \"daysOff\": 2 }] }\n"
			+ "  ],\n"
			+ "  \"teams\": [\n"
			+ "    { \"name\": \"TeamA\", \"description\": \"Team A\", \"rotationName\": \"DayRot\", \"rotationStart\": \"2026-01-05\", \"members\": [] },\n"
			+ "    { \"name\": \"TeamB\", \"description\": \"Team B\", \"rotationName\": \"EveRot\", \"rotationStart\": \"2026-01-05\", \"members\": [] },\n"
			+ "    { \"name\": \"TeamC\", \"description\": \"Team C\", \"rotationName\": \"NightRot\", \"rotationStart\": \"2026-01-05\", \"members\": [] }\n"
			+ "  ],\n"
			+ "  \"nonWorkingPeriods\": []\n"
			+ "}";

	@Test
	public void testCreateAndGetSchedule() throws Exception {
		// Create
		mockMvc.perform(post("/api/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SCHEDULE_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Manufacturing"));

		// Get
		mockMvc.perform(get("/api/schedules/Manufacturing"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Manufacturing"))
				.andExpect(jsonPath("$.shifts.length()").value(3))
				.andExpect(jsonPath("$.teams.length()").value(3));
	}

	@Test
	public void testGetAllSchedules() throws Exception {
		mockMvc.perform(post("/api/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SCHEDULE_JSON))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/api/schedules"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	public void testDeleteSchedule() throws Exception {
		mockMvc.perform(post("/api/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SCHEDULE_JSON))
				.andExpect(status().isCreated());

		mockMvc.perform(delete("/api/schedules/Manufacturing"))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/schedules/Manufacturing"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testGetShiftInstances() throws Exception {
		mockMvc.perform(post("/api/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SCHEDULE_JSON))
				.andExpect(status().isCreated());

		// Monday 2026-02-09 should have shifts
		mockMvc.perform(get("/api/schedules/Manufacturing/shifts/2026-02-09"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(3));
	}

	@Test
	public void testCalculateWorkingTime() throws Exception {
		mockMvc.perform(post("/api/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SCHEDULE_JSON))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/api/schedules/Manufacturing/working-time")
				.param("from", "2026-02-09T07:00:00")
				.param("to", "2026-02-09T15:00:00"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.workingTime").exists());
	}

	@Test
	public void testScheduleNotFound() throws Exception {
		mockMvc.perform(get("/api/schedules/NonExistent"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDuplicateSchedule() throws Exception {
		mockMvc.perform(post("/api/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SCHEDULE_JSON))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/api/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SCHEDULE_JSON))
				.andExpect(status().isConflict());
	}

	@Test
	public void testAddSwedishHolidays() throws Exception {
		mockMvc.perform(post("/api/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SCHEDULE_JSON))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/api/schedules/Manufacturing/holidays/swedish/2026"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nonWorkingPeriods.length()").value(16));
	}

	@Test
	public void testSwedishLocaleHeader() throws Exception {
		// Create schedule first
		mockMvc.perform(post("/api/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SCHEDULE_JSON))
				.andExpect(status().isCreated());

		// Request with Swedish locale header
		mockMvc.perform(get("/api/schedules/Manufacturing")
				.header("Accept-Language", "sv"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Manufacturing"));
	}
}
