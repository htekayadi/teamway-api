package io.teamway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class WorkerShiftDto {

  private final UUID id;
  private final UUID workerId;
  private final String shiftType;
  private final String shiftDate;
}
