package uax.madm.devops.campaigns_demo.infrastructure.dto;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import org.mapstruct.factory.Mappers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import uax.madm.devops.campaigns_demo.domain.model.Campaign;
import uax.madm.devops.campaigns_demo.domain.model.Task;
import uax.madm.devops.campaigns_demo.domain.model.Worker;
import uax.madm.devops.campaigns_demo.domain.model.enums.TaskStatus;
import uax.madm.devops.campaigns_demo.infrastructure.mapper.NewWorkerMapper;

public record TaskDto( // @formatter:off
                       Long id,

                       String uuid,

                       @NotBlank(groups = { TaskUpdateValidationData.class, Default.class },
                               message = "name can't be blank")
                       String name,

                       @Size(groups = { TaskUpdateValidationData.class, Default.class },
                               min = 10, max = 50,
                               message = "description must be between 10 and 50 characters long")
                       String description,

                       @Future(message = "dueDate must be in the future")
                       LocalDate dueDate,

                       @NotNull(groups = { TaskUpdateStatusValidationData.class, TaskUpdateValidationData.class },
                               message = "status can't be null")
                       TaskStatus status,

                       @NotNull(groups = { TaskUpdateValidationData.class, Default.class },
                               message = "campaignId can't be null")
                       Long campaignId,

                       @NotNull(groups = { TaskUpdateValidationData.class, Default.class },
                               message = "workerId can't be null")
                       Long workerId,

                       @Valid
                       @ConvertGroup(from = Default.class, to = WorkerOnTaskCreateValidationData.class)
                       NewWorkerDto assignee,

                       Integer estimatedHours) { // @formatter:on

    public static class Mapper {

        protected static NewWorkerMapper newWorkerMapper = Mappers.getMapper(NewWorkerMapper.class);

        public static Task toModel(TaskDto dto) {
            if (dto == null)
                return null;

            Campaign campaign = new Campaign();
            campaign.setId(dto.campaignId());

/*            Task model = new Task(dto.name(), dto.description(), dto.dueDate(), campaign, dto.status(), newWorkerMapper.toModel(dto.assignee()), dto.uuid(), Optional.ofNullable(dto.estimatedHours())
                    .orElse(0));*/

            // Added - BEGIN
            Worker worker = new Worker();
            worker.setId(dto.workerId());
            Task model = new Task(dto.name(), dto.description(), dto.dueDate(), campaign, dto.status(), worker, dto.uuid(), Optional.ofNullable(dto.estimatedHours())
                    .orElse(0));
            //Added - END

            if (!Objects.isNull(dto.id())) {
                model.setId(dto.id());
            }

            // we won't allow creating or modifying Campaigns via a Task
            return model;
        }

        public static TaskDto toDto(Task model) {
            if (model == null)
                return null;
            TaskDto dto = new TaskDto(model.getId(), model.getUuid(), model.getName(), model.getDescription(), model.getDueDate(), model.getStatus(), model.getCampaign()
                    .getId(), model.getAssignee().getId(), newWorkerMapper.toDto(model.getAssignee()), model.getEstimatedHours());
            return dto;
        }
    }

    public interface TaskUpdateValidationData {
    }

    public interface TaskUpdateStatusValidationData {
    }

    public interface TaskUpdateAssigneeValidationData {
    }

    public interface WorkerOnTaskCreateValidationData {
    }
}
