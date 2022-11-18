package io.teamway.service;

import io.teamway.dto.WorkerShiftDto;
import io.teamway.dto.WorkerShiftResponseDto;
import io.teamway.exceptions.ResourceNotFoundException;
import io.teamway.exceptions.ShiftAlreadyExistsException;
import io.teamway.model.ShiftHistory;
import io.teamway.model.ShiftType;
import io.teamway.model.Worker;
import io.teamway.repository.ShiftHistoryRepository;
import io.teamway.repository.WorkerRepository;
import io.teamway.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.teamway.util.Constants.DATE_FORMATTER;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
public class ShiftHistoryService {

    private final ShiftHistoryRepository shiftHistoryRepository;

    private final WorkerRepository workerRepository;

    @Autowired
    public ShiftHistoryService(ShiftHistoryRepository shiftHistoryRepository, WorkerRepository workerRepository) {
        this.shiftHistoryRepository = shiftHistoryRepository;
        this.workerRepository = workerRepository;
    }

    @Transactional(propagation = REQUIRES_NEW)
    public WorkerShiftResponseDto getShifts(LocalDate startDate, LocalDate endDate) {
        if (endDate != null) {
            return WorkerShiftResponseDto.builder()
                    .response(shiftHistoryRepository.findAllByWorkedDateBetween(startDate, endDate)
                            .stream()
                            .map(this::createShiftHistoryDto)
                            .collect(Collectors.toList()))
                    .build();
        }

        return WorkerShiftResponseDto.builder()
                .response(shiftHistoryRepository.findAllByWorkedDate(startDate)
                        .stream()
                        .map(this::createShiftHistoryDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional(propagation = REQUIRES_NEW)
    public WorkerShiftResponseDto addShift(WorkerShiftDto workerShiftDto) throws ShiftAlreadyExistsException, ResourceNotFoundException {
        Optional<Worker> worker = workerRepository.findById(workerShiftDto.getWorkerId());

        if(worker.isEmpty()) {
            throw new ResourceNotFoundException("Worker not found!");
        }

        Optional<ShiftHistory> shift = shiftHistoryRepository.findOneByWorkerIdAndWorkedDate(workerShiftDto.getWorkerId(), LocalDate.parse(workerShiftDto.getShiftDate(), DATE_FORMATTER));

        if(shift.isPresent()) {
          throw new ShiftAlreadyExistsException("Shift already exists!");
        }

        ShiftHistory shiftHistory = createShiftHistoryEntity(workerShiftDto);
        return WorkerShiftResponseDto.builder()
                .response(Collections.singletonList(
                        createShiftHistoryDto(shiftHistoryRepository.save(shiftHistory))))
                .build();
    }

    @Transactional(propagation = REQUIRES_NEW)
    public WorkerShiftResponseDto getShift(UUID shiftId) throws ResourceNotFoundException {
        Optional<ShiftHistory> shiftHistoryOptional = shiftHistoryRepository.findById(shiftId);
        if (shiftHistoryOptional.isPresent()) {
            return WorkerShiftResponseDto.builder()
                    .response(Collections.singletonList(createShiftHistoryDto(shiftHistoryOptional.get())))
                    .build();
        }

        throw new ResourceNotFoundException(String.format("No shift with id=%s was found!", shiftId));
    }

    @Transactional(propagation = REQUIRES_NEW)
    public WorkerShiftResponseDto updateShift(UUID shiftId, WorkerShiftDto workerShiftDto)
            throws ResourceNotFoundException {
        Optional<ShiftHistory> shiftHistoryOptional = shiftHistoryRepository.findById(shiftId);
        if (shiftHistoryOptional.isPresent()) {
            ShiftHistory shiftHistory = shiftHistoryOptional.get();
            updateWorkerShift(workerShiftDto, shiftHistory);
            return WorkerShiftResponseDto.builder()
                    .response(Collections.singletonList(
                            createShiftHistoryDto(shiftHistoryRepository.save(shiftHistory))))
                    .build();
        }

        throw new ResourceNotFoundException(String.format("No shift with id=%s was found!", shiftId));
    }

    private WorkerShiftDto createShiftHistoryDto(ShiftHistory it) {
        return WorkerShiftDto.builder()
                .workerId(it.getWorkerId())
                .shiftType(it.getShiftType().name())
                .shiftDate(it.getWorkedDate().format(Constants.DATE_FORMATTER))
                .build();
    }

    private void updateWorkerShift(WorkerShiftDto workerShiftDto, ShiftHistory shiftHistory) {
        if (StringUtils.isNotEmpty(workerShiftDto.getShiftType())) {
            shiftHistory.setShiftType(ShiftType.valueOf(workerShiftDto.getShiftType()));
        }
        if (StringUtils.isNotEmpty(workerShiftDto.getShiftDate())) {
            shiftHistory.setWorkedDate(LocalDate.parse(workerShiftDto.getShiftDate(), Constants.DATE_FORMATTER));
        }
    }

    private ShiftHistory createShiftHistoryEntity(WorkerShiftDto workerShiftDto) {
        ShiftHistory shiftHistory = new ShiftHistory();
        shiftHistory.setWorkerId(workerShiftDto.getWorkerId());
        shiftHistory.setShiftType(ShiftType.valueOf(workerShiftDto.getShiftType()));
        shiftHistory.setWorkedDate(LocalDate.parse(workerShiftDto.getShiftDate(), Constants.DATE_FORMATTER));
        return shiftHistory;
    }
}
