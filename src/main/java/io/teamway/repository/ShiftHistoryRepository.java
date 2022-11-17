package io.teamway.repository;

import io.teamway.model.ShiftHistory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.teamway.model.ShiftType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftHistoryRepository extends CrudRepository<ShiftHistory, UUID> {

  Optional<ShiftHistory> findOneByWorkerIdAndWorkedDate(UUID workerId, LocalDate workedDate);

  List<ShiftHistory> findAllByWorkedDateBetween(LocalDate startDate, LocalDate endDate);

  List<ShiftHistory> findAllByWorkedDate(LocalDate workedDate);
}
