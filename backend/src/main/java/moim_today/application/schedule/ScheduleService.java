package moim_today.application.schedule;

import moim_today.dto.schedule.ScheduleCreateRequest;
import moim_today.dto.schedule.ScheduleResponse;
import moim_today.dto.schedule.ScheduleUpdateRequest;
import moim_today.dto.schedule.TimeTableRequest;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;


public interface ScheduleService {

    List<ScheduleResponse> findAllByWeekly(final long memberId, final LocalDate startDate);

    List<ScheduleResponse> findAllByMonthly(final long memberId, final YearMonth yearMonth);

    void fetchTimeTable(final long memberId, final TimeTableRequest timeTableRequest);

    void createSchedule(final long memberId, final ScheduleCreateRequest scheduleCreateRequest);

    void updateSchedule(final long memberId, final ScheduleUpdateRequest scheduleUpdateRequest);

    void deleteSchedule(final long memberId, final long scheduleId);
}