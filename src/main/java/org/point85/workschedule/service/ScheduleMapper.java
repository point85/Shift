package org.point85.workschedule.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.point85.workschedule.Break;
import org.point85.workschedule.NonWorkingPeriod;
import org.point85.workschedule.Rotation;
import org.point85.workschedule.RotationSegment;
import org.point85.workschedule.Shift;
import org.point85.workschedule.ShiftInstance;
import org.point85.workschedule.Team;
import org.point85.workschedule.TeamMember;
import org.point85.workschedule.WorkSchedule;
import org.point85.workschedule.dto.BreakDto;
import org.point85.workschedule.dto.NonWorkingPeriodDto;
import org.point85.workschedule.dto.RotationDto;
import org.point85.workschedule.dto.RotationSegmentDto;
import org.point85.workschedule.dto.ShiftDto;
import org.point85.workschedule.dto.ShiftInstanceDto;
import org.point85.workschedule.dto.TeamDto;
import org.point85.workschedule.dto.TeamMemberDto;
import org.point85.workschedule.dto.WorkScheduleDto;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

	public WorkSchedule toWorkSchedule(WorkScheduleDto dto) throws Exception {
		WorkSchedule ws = new WorkSchedule(dto.getName(),
				dto.getDescription() != null ? dto.getDescription() : dto.getName());

		// 1. Create shifts first (rotations reference them by name)
		for (ShiftDto shiftDto : dto.getShifts()) {
			Shift shift = ws.createShift(shiftDto.getName(),
					shiftDto.getDescription() != null ? shiftDto.getDescription() : shiftDto.getName(),
					shiftDto.getStart(), Duration.parse(shiftDto.getDuration()));

			// add breaks
			for (BreakDto breakDto : shiftDto.getBreaks()) {
				shift.createBreak(breakDto.getName(),
						breakDto.getDescription() != null ? breakDto.getDescription() : breakDto.getName(),
						breakDto.getStart(), Duration.parse(breakDto.getDuration()));
			}
		}

		// 2. Create rotations (reference shifts by name)
		for (RotationDto rotDto : dto.getRotations()) {
			Rotation rotation = ws.createRotation(rotDto.getName(),
					rotDto.getDescription() != null ? rotDto.getDescription() : rotDto.getName());

			for (RotationSegmentDto segDto : rotDto.getSegments()) {
				Shift shift = findShiftByName(ws, segDto.getShiftName());
				rotation.addSegment(shift, segDto.getDaysOn(), segDto.getDaysOff());
			}
		}

		// 3. Create teams (reference rotations by name)
		for (TeamDto teamDto : dto.getTeams()) {
			Rotation rotation = findRotationByName(ws, teamDto.getRotationName());
			Team team = ws.createTeam(teamDto.getName(),
					teamDto.getDescription() != null ? teamDto.getDescription() : teamDto.getName(), rotation,
					teamDto.getRotationStart());

			for (TeamMemberDto memberDto : teamDto.getMembers()) {
				TeamMember member = new TeamMember(memberDto.getName(),
						memberDto.getDescription() != null ? memberDto.getDescription() : memberDto.getName(),
						memberDto.getMemberID());
				team.addMember(member);
			}
		}

		// 4. Create non-working periods
		for (NonWorkingPeriodDto nwpDto : dto.getNonWorkingPeriods()) {
			ws.createNonWorkingPeriod(nwpDto.getName(),
					nwpDto.getDescription() != null ? nwpDto.getDescription() : nwpDto.getName(),
					nwpDto.getStartDateTime(), Duration.parse(nwpDto.getDuration()));
		}

		return ws;
	}

	public WorkScheduleDto toDto(WorkSchedule ws) {
		WorkScheduleDto dto = new WorkScheduleDto();
		dto.setName(ws.getName());
		dto.setDescription(ws.getDescription());

		// shifts
		for (Shift shift : ws.getShifts()) {
			dto.getShifts().add(toShiftDto(shift));
		}

		// rotations
		for (Rotation rotation : ws.getRotations()) {
			dto.getRotations().add(toRotationDto(rotation));
		}

		// teams
		for (Team team : ws.getTeams()) {
			dto.getTeams().add(toTeamDto(team));
		}

		// non-working periods
		for (NonWorkingPeriod nwp : ws.getNonWorkingPeriods()) {
			dto.getNonWorkingPeriods().add(toNonWorkingPeriodDto(nwp));
		}

		return dto;
	}

	public List<ShiftInstanceDto> toShiftInstanceDtos(List<ShiftInstance> instances) {
		List<ShiftInstanceDto> dtos = new ArrayList<>();
		for (ShiftInstance instance : instances) {
			ShiftInstanceDto dto = new ShiftInstanceDto();
			dto.setShiftName(instance.getShift().getName());
			dto.setTeamName(instance.getTeam().getName());
			dto.setStartTime(instance.getStartTime());
			dto.setEndTime(instance.getEndTime());
			dtos.add(dto);
		}
		return dtos;
	}

	private ShiftDto toShiftDto(Shift shift) {
		ShiftDto dto = new ShiftDto();
		dto.setName(shift.getName());
		dto.setDescription(shift.getDescription());
		dto.setStart(shift.getStart());
		dto.setDuration(shift.getDuration().toString());

		for (Break b : shift.getBreaks()) {
			BreakDto breakDto = new BreakDto();
			breakDto.setName(b.getName());
			breakDto.setDescription(b.getDescription());
			breakDto.setStart(b.getStart());
			breakDto.setDuration(b.getDuration().toString());
			dto.getBreaks().add(breakDto);
		}

		return dto;
	}

	private RotationDto toRotationDto(Rotation rotation) {
		RotationDto dto = new RotationDto();
		dto.setName(rotation.getName());
		dto.setDescription(rotation.getDescription());

		for (RotationSegment segment : rotation.getRotationSegments()) {
			RotationSegmentDto segDto = new RotationSegmentDto();
			segDto.setShiftName(segment.getStartingShift().getName());
			segDto.setDaysOn(segment.getDaysOn());
			segDto.setDaysOff(segment.getDaysOff());
			dto.getSegments().add(segDto);
		}

		return dto;
	}

	private TeamDto toTeamDto(Team team) {
		TeamDto dto = new TeamDto();
		dto.setName(team.getName());
		dto.setDescription(team.getDescription());
		dto.setRotationName(team.getRotation().getName());
		dto.setRotationStart(team.getRotationStart());

		for (TeamMember member : team.getAssignedMembers()) {
			TeamMemberDto memberDto = new TeamMemberDto();
			memberDto.setName(member.getName());
			memberDto.setDescription(member.getDescription());
			memberDto.setMemberID(member.getMemberID());
			dto.getMembers().add(memberDto);
		}

		return dto;
	}

	private NonWorkingPeriodDto toNonWorkingPeriodDto(NonWorkingPeriod nwp) {
		NonWorkingPeriodDto dto = new NonWorkingPeriodDto();
		dto.setName(nwp.getName());
		dto.setDescription(nwp.getDescription());
		dto.setStartDateTime(nwp.getStartDateTime());
		dto.setDuration(nwp.getDuration().toString());
		return dto;
	}

	private Shift findShiftByName(WorkSchedule ws, String name) throws Exception {
		for (Shift shift : ws.getShifts()) {
			if (shift.getName().equals(name)) {
				return shift;
			}
		}
		throw new Exception("Shift not found: " + name);
	}

	private Rotation findRotationByName(WorkSchedule ws, String name) throws Exception {
		for (Rotation rotation : ws.getRotations()) {
			if (rotation.getName().equals(name)) {
				return rotation;
			}
		}
		throw new Exception("Rotation not found: " + name);
	}
}
