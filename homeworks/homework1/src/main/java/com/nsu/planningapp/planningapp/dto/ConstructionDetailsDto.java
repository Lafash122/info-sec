package com.nsu.planningapp.planningapp.dto;

import java.util.List;

public record ConstructionDetailsDto(List<ResourceRequirementDto> resources, int totalWorkdays) {}