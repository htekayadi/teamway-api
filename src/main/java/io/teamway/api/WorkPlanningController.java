package io.teamway.api;

import io.teamway.dto.WorkPlanResponseDto;
import io.teamway.service.WorkPlanningService;
import io.teamway.util.Constants;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
public class WorkPlanningController {

  private final WorkPlanningService workPlanningService;

  @Autowired
  public WorkPlanningController(WorkPlanningService workPlanningService) {
    this.workPlanningService = workPlanningService;
  }

  @GetMapping("/generate")
  public WorkPlanResponseDto generateWeeklyWorkPlan(@RequestParam String startDate) {
    return workPlanningService.generateWeeklyWorkPlan(LocalDate.parse(startDate, Constants.DATE_FORMATTER));
  }

  @PostMapping
  public ResponseEntity<?> saveWorkPlan(@RequestBody WorkPlanResponseDto requestBody) {
    workPlanningService.saveWorkPlan(requestBody.getWorkPlan());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
