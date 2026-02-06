package org.point85.workschedule.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.point85.workschedule.ShiftInstance;
import org.point85.workschedule.WorkSchedule;
import org.point85.workschedule.dto.NonWorkingPeriodDto;
import org.point85.workschedule.dto.ShiftInstanceDto;
import org.point85.workschedule.dto.WorkScheduleDto;
import org.point85.workschedule.dto.WorkingTimeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

	private final Map<String, WorkSchedule> schedules = new ConcurrentHashMap<>();

	@Autowired
	private ScheduleMapper mapper;

	@Autowired
	private SwedishHolidayService swedishHolidayService;

	public WorkScheduleDto createSchedule(WorkScheduleDto dto) throws Exception {
		if (schedules.containsKey(dto.getName())) {
			throw new IllegalArgumentException("Schedule already exists: " + dto.getName());
		}

		WorkSchedule ws = mapper.toWorkSchedule(dto);
		schedules.put(ws.getName(), ws);
		return mapper.toDto(ws);
	}

	public WorkScheduleDto getSchedule(String name) {
		WorkSchedule ws = findSchedule(name);
		return mapper.toDto(ws);
	}

	public List<WorkScheduleDto> getAllSchedules() {
		List<WorkScheduleDto> result = new ArrayList<>();
		for (WorkSchedule ws : schedules.values()) {
			result.add(mapper.toDto(ws));
		}
		return result;
	}

	public void deleteSchedule(String name) {
		findSchedule(name);
		schedules.remove(name);
	}

	public List<ShiftInstanceDto> getShiftInstances(String scheduleName, LocalDate date) throws Exception {
		WorkSchedule ws = findSchedule(scheduleName);
		List<ShiftInstance> instances = ws.getShiftInstancesForDay(date);
		return mapper.toShiftInstanceDtos(instances);
	}

	public List<ShiftInstanceDto> getShiftInstancesForTime(String scheduleName, LocalDateTime dateTime)
			throws Exception {
		WorkSchedule ws = findSchedule(scheduleName);
		List<ShiftInstance> instances = ws.getShiftInstancesForTime(dateTime);
		return mapper.toShiftInstanceDtos(instances);
	}

	public WorkingTimeDto calculateWorkingTime(String scheduleName, LocalDateTime from, LocalDateTime to)
			throws Exception {
		WorkSchedule ws = findSchedule(scheduleName);

		Duration working = ws.calculateWorkingTime(from, to);
		Duration nonWorking = ws.calculateNonWorkingTime(from, to);

		WorkingTimeDto dto = new WorkingTimeDto();
		dto.setFrom(from);
		dto.setTo(to);
		dto.setWorkingTime(working.toString());
		dto.setNonWorkingTime(nonWorking.toString());
		return dto;
	}

	public WorkScheduleDto addSwedishHolidays(String scheduleName, int year) throws Exception {
		WorkSchedule ws = findSchedule(scheduleName);
		List<NonWorkingPeriodDto> holidays = swedishHolidayService.getHolidays(year);

		for (NonWorkingPeriodDto holiday : holidays) {
			ws.createNonWorkingPeriod(holiday.getName(), holiday.getDescription(), holiday.getStartDateTime(),
					Duration.parse(holiday.getDuration()));
		}

		return mapper.toDto(ws);
	}

	private WorkSchedule findSchedule(String name) {
		WorkSchedule ws = schedules.get(name);
		if (ws == null) {
			throw new ScheduleNotFoundException("Schedule not found: " + name);
		}
		return ws;
	}
}
