package io.teamway.model;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Worker")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Worker {

  @Id
  @GeneratedValue
  private UUID id;

  private String name;

  private String surname;
}
