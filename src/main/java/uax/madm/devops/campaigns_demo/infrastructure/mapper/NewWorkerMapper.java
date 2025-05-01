package uax.madm.devops.campaigns_demo.infrastructure.mapper;

import java.util.Optional;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import uax.madm.devops.campaigns_demo.domain.model.ServiceUser;
import uax.madm.devops.campaigns_demo.domain.model.Worker;
import uax.madm.devops.campaigns_demo.infrastructure.dto.NewWorkerDto;
import uax.madm.devops.campaigns_demo.infrastructure.dto.UserDto;

@Mapper(componentModel = "spring")
public interface NewWorkerMapper {

    NewWorkerDto toDto(Worker model);

    Worker toModel(NewWorkerDto dto);

    UserDto toUserDto(ServiceUser model);

    ServiceUser toUserModel(UserDto dto);

    @AfterMapping
    default void setWorker(@MappingTarget Worker worker) {
        Optional.ofNullable(worker.getUser())
                .ifPresent(it -> it.setWorker(worker));
    }
}
