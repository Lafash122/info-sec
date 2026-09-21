package com.nsu.planningapp.graphic;

import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseInitializer;
import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseConnection;
import com.nsu.planningapp.planningapp.service.QueryService;
import com.nsu.planningapp.planningapp.service.PreliminaryQueryService;
import com.nsu.planningapp.planningapp.service.InsertService;
import com.nsu.planningapp.planningapp.dto.*;

import java.sql.Connection;
import java.util.List;

public class DataBaseInformant implements DataBaseListener {
	private final QueryService queryService = new QueryService();
	//private final PreliminaryQueryService preliminaryService = new PreliminaryQueryService();
	private final InsertService insertService = new InsertService();

	@Override
	public String getDBName() {
		return DatabaseConnection.getDBName();
	}

	@Override
	public Connection getConnection() throws Exception {
		return DatabaseConnection.getConnection();
	}

	@Override
	public boolean areTablesExist(Connection connection) throws Exception {
		return DatabaseInitializer.areTablesPresent(connection);
	}

	@Override
	public void createAndFillDatabase(Connection connection) throws Exception {
		DatabaseInitializer.createTables(connection);
		DatabaseInitializer.createFromDefaultFiles(connection);
	}

	@Override
	public void createNewDatabase(Connection connection) throws Exception {
		DatabaseInitializer.dropTables(connection);
		createAndFillDatabase(connection);
	}


	// 13 вызов Error Handling метода
	public int callErrorHandlingMethod(Integer settlementId) throws Exception {
		return queryService.errorHandlingMethod(settlementId);
	}

	// 1
	@Override
	public int getTotalResidentCapacity(Integer settlementId) throws Exception {
		return queryService.getTotalResidentCapacity(settlementId);
	}

	// 2
	@Override
	public List<BuildingInfoDto> getBuildingsByType(String blueprintType, String settlementName) throws Exception {
		return queryService.getBuildingsByType(blueprintType, settlementName);
	}

	// 3
	@Override
	public int getMaxResourceProduction(Integer resourceId, Integer settlementId) throws Exception {
		return queryService.getMaxResourceProduction(resourceId, settlementId);
	}

	// 4
	@Override
	public int getMaxResourceConsumption(Integer resourceId, Integer cityId) throws Exception {
		return queryService.getMaxResourceConsumption(resourceId, cityId);
	}

	// 5
	@Override
	public JobsReportDto getJobsCount(Integer settlementId, Integer buildingId) throws Exception {
		return queryService.getJobsCount(settlementId, buildingId);
	}

	// 6
	@Override
	public double getTotalResourceStorage(Integer resourceId, Integer settlementId) throws Exception {
		return queryService.getTotalResourceStorage(resourceId, settlementId);
	}

	// 7
	@Override
	public ConstructionDetailsDto getBuildingConstructionCost(List<Integer> bBlueprintIds) throws Exception {
		return queryService.getBuildingConstructionCost(bBlueprintIds);
	}

	// 8
	@Override
	public ConstructionDetailsDto getTransportConstructionCost(List<Integer> tBlueprintIds) throws Exception {
		return queryService.getTransportConstructionCost(tBlueprintIds);
	}

	// 9
	@Override
	public List<BuildingInfoDto> getBuildingsList(Integer settlementId) throws Exception {
		return queryService.getBuildingsList(settlementId);
	}

	// 10
	@Override
	public double getDaysToFillStorage(Integer resourceId, Integer settlementId) throws Exception {
		return queryService.getDaysToFillStorage(resourceId, settlementId);
	}

	// 11
	@Override
	public int getTotalParkingSpaces(Integer settlementId, List<Integer> buildingIds) throws Exception {
		return queryService.getTotalParkingSpaces(settlementId, buildingIds);
	}

	// 12
	@Override
	public double getMaxStorageInNonStorageBuildings(Integer resourceId, Integer settlementId) throws Exception {
		return queryService.getMaxStorageInNonStorageBuildings(resourceId, settlementId);
	}


	@Override
	public List<String> getAllSettlements() throws Exception {
		return PreliminaryQueryService.getAllSettlements();
	}

	@Override
	public Integer getSettlementId(String cityName) throws Exception {
		return PreliminaryQueryService.getSettlementId(cityName);
	}

	@Override
	public List<String> getAllBlueprintTypes() throws Exception {
		return PreliminaryQueryService.getAllBlueprintTypes();
	}

	@Override
	public List<String> getAllResourcesNames() throws Exception {
		return PreliminaryQueryService.getAllResourcesNames();
	}

	@Override
	public Integer getResourceId(String resourceName) throws Exception {
		return PreliminaryQueryService.getResourceId(resourceName);
	}

	@Override
	public List<String> getAllBuildingBlueprintNames() throws Exception {
		return PreliminaryQueryService.getAllBuildingBlueprintNames();
	}

	@Override
	public Integer getBuildingBlueprintId(String name) throws Exception {
		return PreliminaryQueryService.getBuildingBlueprintId(name);
	}

	@Override
	public List<String> getAllTransportBlueprintNames() throws Exception {
		return PreliminaryQueryService.getAllTransportBlueprintNames();
	}

	@Override
	public Integer getTransportBlueprintId(String name) throws Exception {
		return PreliminaryQueryService.getTransportBlueprintId(name);
	}

	@Override
	public List<BuildingInfoDto> getBuildingsWithParking() throws Exception {
		return PreliminaryQueryService.getBuildingsWithParking();
	}


	@Override
	public void addSettlement(String name) throws Exception {
		insertService.addSettlement(name);
	}

	@Override
	public void addBuilding(String settlementName, String blueprintName) throws Exception {
		insertService.addBuilding(settlementName, blueprintName);
	}

	@Override
	public void addTransport(String blueprintName) throws Exception {
		insertService.addTransport(blueprintName);
	}
}