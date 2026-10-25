package com.nsu.planningapp.planningapp.dto;

record BuildingBlueprintDto(int id,
			String name,
			int serviceLife,
			int numberOfWorkdays,
			float dailyWaterConsumption,
			float dailyEnergyConsumption,
			String blueprintType) {
}
