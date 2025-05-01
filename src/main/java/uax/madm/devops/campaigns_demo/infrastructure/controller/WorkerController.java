package uax.madm.devops.campaigns_demo.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import uax.madm.devops.campaigns_demo.application.services.WorkerService;
import uax.madm.devops.campaigns_demo.domain.model.Campaign;
import uax.madm.devops.campaigns_demo.domain.model.Worker;
import uax.madm.devops.campaigns_demo.infrastructure.dto.CampaignDto;
import uax.madm.devops.campaigns_demo.infrastructure.dto.NewWorkerDto;
import uax.madm.devops.campaigns_demo.infrastructure.dto.OldWorkerDto;
import uax.madm.devops.campaigns_demo.infrastructure.mapper.NewWorkerMapper;
import uax.madm.devops.campaigns_demo.infrastructure.mapper.OldWorkerMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/workers")
public class WorkerController {

    private WorkerService workerService;
    private NewWorkerMapper newWorkerMapper;
    private OldWorkerMapper oldWorkerMapper;

    public WorkerController(WorkerService workerService, NewWorkerMapper newWorkerMapper, OldWorkerMapper oldWorkerMapper) {
        this.workerService = workerService;
        this.newWorkerMapper = newWorkerMapper;
        this.oldWorkerMapper = oldWorkerMapper;
    }

    @GetMapping
    public List<NewWorkerDto> listWorkers() {
        List<Worker> models = workerService.findWorkers();
        List<NewWorkerDto> workerDtos = models.stream()
                .map(NewWorkerDto.Mapper::toDto)
                .collect(Collectors.toList());
        return workerDtos;
    }


    @GetMapping(value = "/{id}")
    public OldWorkerDto findOne(@PathVariable Long id) {
        Worker model = workerService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // return OldWorkerDto.Mapper.toDto(model);
        return oldWorkerMapper.toDto(model);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewWorkerDto create(@RequestBody @Valid NewWorkerDto newWorker) {
        // Worker model = OldWorkerDto.Mapper.toModel(newWorker);
        Worker model = newWorkerMapper.toModel(newWorker);
        Worker createdModel = this.workerService.save(model);
        // return OldWorkerDto.Mapper.toDto(createdModel);
        return newWorkerMapper.toDto(createdModel);
    }

/*    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OldWorkerDto create(@RequestBody @Valid OldWorkerDto newWorker) {
        // Worker model = OldWorkerDto.Mapper.toModel(newWorker);
        Worker model = oldWorkerMapper.toModel(newWorker);
        Worker createdModel = this.workerService.save(model);
        // return OldWorkerDto.Mapper.toDto(createdModel);
        return oldWorkerMapper.toDto(createdModel);
    }*/

    @PutMapping(value = "/{id}")
    public OldWorkerDto update(@PathVariable Long id, @RequestBody @Validated(OldWorkerDto.WorkerUpdateValidationData.class) OldWorkerDto updatedWorker) {
        // Worker model = OldWorkerDto.Mapper.toModel(updatedWorker);
        Worker model = oldWorkerMapper.toModel(updatedWorker);
        Worker createdModel = this.workerService.updateWorker(id, model)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // return OldWorkerDto.Mapper.toDto(createdModel);
        return oldWorkerMapper.toDto(createdModel);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public NewWorkerDto findOneNewStructure(@PathVariable Long id) {
        Worker model = workerService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // return NewWorkerDto.Mapper.toDto(model);
        return newWorkerMapper.toDto(model);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public NewWorkerDto createNewStructure(@RequestBody @Valid NewWorkerDto newWorker) {
        // Worker model = NewWorkerDto.Mapper.toModel(newWorker);
        Worker model = newWorkerMapper.toModel(newWorker);
        Worker createdModel = this.workerService.save(model);
        // return NewWorkerDto.Mapper.toDto(createdModel);
        return newWorkerMapper.toDto(createdModel);
    }

    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public NewWorkerDto updateNewStructure(@PathVariable Long id, @RequestBody @Validated(NewWorkerDto.WorkerUpdateValidationData.class) NewWorkerDto updatedWorker) {
        // Worker model = NewWorkerDto.Mapper.toModel(updatedWorker);
        Worker model = newWorkerMapper.toModel(updatedWorker);
        Worker createdModel = this.workerService.updateWorker(id, model)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // return NewWorkerDto.Mapper.toDto(createdModel);
        return newWorkerMapper.toDto(createdModel);
    }
}
