package com.sweet_hjr.server.domain.project.controller;

import com.sweet_hjr.server.domain.project.dto.ProjectCreateRequest;
import com.sweet_hjr.server.domain.project.dto.ProjectDetailResponse;
import com.sweet_hjr.server.domain.project.dto.ProjectResponse;
import com.sweet_hjr.server.domain.project.dto.ProjectSummaryResponse;
import com.sweet_hjr.server.domain.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(@Valid @RequestBody ProjectCreateRequest request) {
        return projectService.createProject(request);
    }

    @GetMapping
    public List<ProjectSummaryResponse> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("/{projectId}")
    public ProjectDetailResponse getProject(@PathVariable Long projectId) {
        return projectService.getProject(projectId);
    }
}