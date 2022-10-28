package io.teamway.service;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import io.teamway.dto.WorkerDto;
import io.teamway.dto.WorkerResponseDto;
import io.teamway.model.Worker;
import io.teamway.repository.WorkerRepository;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkerService {

  private final WorkerRepository workerRepository;

  @Autowired
  public WorkerService(WorkerRepository workerRepository) {
    this.workerRepository = workerRepository;
  }


  @Transactional(propagation = REQUIRES_NEW)
  public WorkerResponseDto getAllWorkers() {
    return WorkerResponseDto.builder().response(
        StreamSupport.stream(workerRepository.findAll().spliterator(), false)
            .map(it -> WorkerDto.builder().id(it.getId())
                .name(it.getName())
                .surname(it.getSurname())
                .build())
            .collect(Collectors.toList()))
        .build();
  }

  @Transactional(propagation = REQUIRES_NEW)
  public WorkerResponseDto getWorker(UUID workerId) {
    Optional<Worker> optionalWorker = workerRepository.findById(workerId);
    if(optionalWorker.isPresent()){
      return getSingleWorkerResponse(optionalWorker.get());
    }

    return WorkerResponseDto.builder().build();
  }

  @Transactional(propagation = REQUIRES_NEW)
  public WorkerResponseDto addWorker(WorkerDto workerDto) {
    Worker worker = new Worker();
    worker.setName(workerDto.getName());
    worker.setSurname(workerDto.getSurname());

    return getSingleWorkerResponse(workerRepository.save(worker));
  }

  private WorkerResponseDto getSingleWorkerResponse(Worker worker) {
    return WorkerResponseDto.builder().response(
        Collections.singletonList(WorkerDto.builder().id(worker.getId())
            .name(worker.getName())
            .surname(worker.getSurname())
            .build())
    ).build();
  }
}
