package com.sweet_hjr.server.domain.project.repository;

import com.sweet_hjr.server.domain.project.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = {"influencer", "createdBy"})
    List<Project> findAllByOrderByIdDesc();

    @EntityGraph(attributePaths = {"influencer", "createdBy"})
    Optional<Project> findById(Long id);
}