package com.nsu.planningapp.graphic;

import com.nsu.planningapp.planningapp.dto.*;

import java.sql.Connection;
import java.util.List;

public interface DataBaseListener {
	public String getDBName();
	public Connection getConnection() throws Exception;
	public boolean areTablesExist(Connection connection) throws Exception;
	public void createAndFillDatabase(Connection connection) throws Exception;
	public void createNewDatabase(Connection connection) throws Exception;

	// 13 вызов Error Handling метода
	public int callErrorHandlingMethod(Integer settlementId) throws Exception;
	// 1
	public int getTotalResidentCapacity(Integer settlementId) throws Exception;
	// 2
	public List<BuildingInfoDto> getBuildingsByType(String blueprintType, String settlementName) throws Exception;
	// 3
	public int getMaxResourceProduction(Integer resourceId, Integer settlementId) throws Exception;
	// 4
	public int getMaxResourceConsumption(Integer resourceId, Integer cityId) throws Exception;
	// 5
	public JobsReportDto getJobsCount(Integer settlementId, Integer buildingId) throws Exception;
	// 6
	public double getTotalResourceStorage(Integer resourceId, Integer settlementId) throws Exception;
	// 7
	public ConstructionDetailsDto getBuildingConstructionCost(List<Integer> bBlueprintIds) throws Exception;
	// 8
	public ConstructionDetailsDto getTransportConstructionCost(List<Integer> tBlueprintIds) throws Exception;
	// 9
	public List<BuildingInfoDto> getBuildingsList(Integer settlementId) throws Exception;
	// 10
	public double getDaysToFillStorage(Integer resourceId, Integer settlementId) throws Exception;
	// 11
	public int getTotalParkingSpaces(Integer settlementId, List<Integer> buildingIds) throws Exception;
	// 12
	public double getMaxStorageInNonStorageBuildings(Integer resourceId, Integer settlementId) throws Exception;

	public List<String> getAllSettlements() throws Exception;
	public Integer getSettlementId(String cityName) throws Exception;
	public List<String> getAllBlueprintTypes() throws Exception;
	public List<String> getAllResourcesNames() throws Exception;
	public Integer getResourceId(String resourceName) throws Exception;
	public List<String> getAllBuildingBlueprintNames() throws Exception;
	public Integer getBuildingBlueprintId(String name) throws Exception;
	public List<String> getAllTransportBlueprintNames() throws Exception;
	public Integer getTransportBlueprintId(String name) throws Exception;
	public List<BuildingInfoDto> getBuildingsWithParking() throws Exception;

	void addSettlement(String name) throws Exception;
	void addBuilding(String settlementName, String blueprintName) throws Exception;
	void addTransport(String blueprintName) throws Exception;
}