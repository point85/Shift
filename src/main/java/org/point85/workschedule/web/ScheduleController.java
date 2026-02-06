package org.point85.workschedule.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.point85.workschedule.dto.ShiftInstanceDto;
import org.point85.workschedule.dto.WorkScheduleDto;
import org.point85.workschedule.dto.WorkingTimeDto;
import org.point85.workschedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

	@Autowired
	private ScheduleService scheduleService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public WorkScheduleDto createSchedule(@Valid @RequestBody WorkScheduleDto dto) throws Exception {
		return scheduleService.createSchedule(dto);
	}

	@GetMapping
	public List<WorkScheduleDto> getAllSchedules() {
		return scheduleService.getAllSchedules();
	}

	@GetMapping("/{name}")
	public WorkScheduleDto getSchedule(@PathVariable String name) {
		return scheduleService.getSchedule(name);
	}

	@DeleteMapping("/{name}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteSchedule(@PathVariable String name) {
		scheduleService.deleteSchedule(name);
	}

	@GetMapping("/{name}/shifts/{date}")
	public List<ShiftInstanceDto> getShiftInstances(@PathVariable String name,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws Exception {
		return scheduleService.getShiftInstances(name, date);
	}

	@GetMapping("/{name}/shifts/at")
	public List<ShiftInstanceDto> getShiftInstancesForTime(@PathVariable String name,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) throws Exception {
		return scheduleService.getShiftInstancesForTime(name, dateTime);
	}

	@GetMapping("/{name}/working-time")
	public WorkingTimeDto calculateWorkingTime(@PathVariable String name,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) throws Exception {
		return scheduleService.calculateWorkingTime(name, from, to);
	}

	@PostMapping("/{name}/holidays/swedish/{year}")
	public WorkScheduleDto addSwedishHolidays(@PathVariable String name, @PathVariable int year) throws Exception {
		return scheduleService.addSwedishHolidays(name, year);
	}
}
