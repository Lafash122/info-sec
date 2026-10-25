package com.nsu.planningapp;

import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseConnection;
import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseInitializer;
import com.nsu.planningapp.planningapp.service.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.List;

public class Launch {
    // Test: 1) Connection + 2) Filling in tables
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // 0. Установка соединения
            // 1. Создание таблиц
            // 2. Заполнение таблиц данными

            Scanner scanner = new Scanner(System.in);
            boolean tablesExist = DatabaseInitializer.areTablesPresent(connection);
            System.out.println("\nТекущий статус: " + (tablesExist ? "База данных существует" : "База данных пуста"));
            System.out.println("1 - Создать БД (если есть - пересоздать)");
            System.out.println("2 - Использовать БД (если нет - создать)");
            System.out.println("3 - Выйти");
            System.out.print("\nВыберите 1, 2 или 3: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    if (!tablesExist) {
                        createNewDatabase(connection);
                        break;
                    }

                    System.out.println("\nВНИМАНИЕ! Это действие УДАЛИТ все существующие данные!");
                    System.out.print("Вы уверены? (Y/N): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("Y"))
                        createNewDatabase(connection);
                    else
                        System.out.println("\nДействие отменено.");

                    break;
                case "2":
                    if (!tablesExist) {
                        System.out.print("База данных пуста, создаём и заполняем... ");
                        createAndFillDatabase(connection);
                        System.out.println("База данных создана и заполнена.");
                    }
                    else 
                        System.out.println("Используем существующую базу данных.");

                    break;
                case "3":
                    System.out.println("Выход.");
                    return;
                default:
                    System.out.println("Нет такого варианта! Завершение работы.");
                    return;
            }
            System.out.print("Завершение работы. P. S. 12 запросы - в QueryService!");
            smallTest(connection);

            // 3. Запросы.
        } catch (SQLException e) {
            System.err.println("SQLException caught: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException caught: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception caught: " + e.getMessage());
        }
    }

    private static void createNewDatabase(Connection conn) throws Exception {
        System.out.print("Создание новой базы данных... ");
        DatabaseInitializer.dropTables(conn);
        createAndFillDatabase(conn);
        System.out.println("База данных создана заново и заполнена.");
    }

    public static void createAndFillDatabase(Connection conn) throws Exception {
        DatabaseInitializer.createTables(conn);
        DatabaseInitializer.createFromDefaultFiles(conn);
        System.out.println("Соединение установлено, данные добавлены в БД.");
    }

    public static void smallTest(Connection connection) throws Exception {
        //PreliminaryQueryService pqs = new PreliminaryQueryService();
        List<String> settlements = PreliminaryQueryService.getAllSettlements();
        System.out.println("\n\nВсего населённых пунктов: " + settlements.size());
        for (String settlement : settlements) {
            System.out.println("\n" + settlement);
    
            // SQL-запрос: для каждого здания в этом городе, которое является жилым (есть запись в RESIDENTIAL_BUILDING_BLUEPRINTS)
            String sql = 
                "SELECT bb.name AS building_name, rb.number_of_residents " +
                "FROM BUILDINGS b " +
                "JOIN BUILDING_BLUEPRINTS bb ON b.blueprint = bb.id " +
                "JOIN RESIDENTIAL_BUILDING_BLUEPRINTS rb ON bb.id = rb.id " +
                "JOIN SETTLEMENTS s ON b.settlement = s.id " +
                "WHERE s.name = ? " +
                "ORDER BY bb.name";
    
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, settlement);
                ResultSet rs = stmt.executeQuery();
                boolean hasResidential = false;
                while (rs.next()) {
                    hasResidential = true;
                    String buildingName = rs.getString("building_name");
                    int residents = rs.getInt("number_of_residents");
                    System.out.println(buildingName + " — " + residents + " жилых мест");
                }
                if (!hasResidential) {
                    System.out.println("   (нет жилых зданий)");
                }
                rs.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при запросе для города " + settlement + ": " + e.getMessage());
            }
        }
    }
}
