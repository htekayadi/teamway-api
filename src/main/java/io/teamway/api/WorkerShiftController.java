package io.teamway.api;

import io.teamway.dto.WorkerShiftDto;
import io.teamway.dto.WorkerShiftResponseDto;
import io.teamway.exceptions.ResourceNotFoundException;
import io.teamway.exceptions.ShiftAlreadyExistsException;
import io.teamway.service.ShiftHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

import static io.teamway.util.Constants.DATE_FORMATTER;

@RestController
@RequestMapping("/api/shifts")
public class WorkerShiftController {

    private final ShiftHistoryService shiftHistoryService;

    @Autowired
    public WorkerShiftController(ShiftHistoryService shiftHistoryService) {
        this.shiftHistoryService = shiftHistoryService;
    }

    @PostMapping
    public WorkerShiftResponseDto addShift(@RequestBody WorkerShiftDto requestBody)
            throws ShiftAlreadyExistsException, ResourceNotFoundException {
        return shiftHistoryService.addShift(requestBody);
    }

    @GetMapping
    public WorkerShiftResponseDto getShiftsBetween(@RequestParam String startDate,
                                                   @RequestParam(required = false) String endDate) {
        if (StringUtils.isNotEmpty(endDate)) {
            return shiftHistoryService.getShifts(LocalDate.parse(startDate, DATE_FORMATTER),
                    LocalDate.parse(endDate, DATE_FORMATTER));
        }

        return shiftHistoryService.getShifts(LocalDate.parse(startDate, DATE_FORMATTER), null);
    }

    @GetMapping("/{shiftId}")
    public WorkerShiftResponseDto getShift(@PathVariable String shiftId)
            throws ResourceNotFoundException {
        return shiftHistoryService.getShift(UUID.fromString(shiftId));
    }

    @PatchMapping("/{shiftId}")
    public WorkerShiftResponseDto updateShift(@PathVariable String shiftId, @RequestBody
            WorkerShiftDto requestBody) throws ResourceNotFoundException {
        return shiftHistoryService.updateShift(UUID.fromString(shiftId), requestBody);
    }
}
