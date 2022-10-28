package io.teamway.model;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Shift_History")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ShiftHistory {

  @Id
  @GeneratedValue
  private UUID id;

  private UUID workerId;

  @Enumerated(EnumType.STRING)
  private ShiftType shiftType;

  @Column(name = "worked_date", columnDefinition = "date")
  private LocalDate workedDate;

}
