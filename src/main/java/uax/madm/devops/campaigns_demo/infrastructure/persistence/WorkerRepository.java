package uax.madm.devops.campaigns_demo.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import uax.madm.devops.campaigns_demo.domain.model.Worker;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Worker findByUserEmail(String email);
}
