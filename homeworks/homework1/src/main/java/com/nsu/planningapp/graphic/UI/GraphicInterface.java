package com.nsu.planningapp.graphic.UI;

import com.nsu.planningapp.graphic.*;
import com.nsu.planningapp.planningapp.dto.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

public class GraphicInterface extends JFrame {
	private boolean isConnected = false;
	private int DEFAULT_TEXT_SIZE = 16;

	private static final Color menuColor = new Color(235, 235, 235);
	private static final Color buttonColor = new Color(220, 220, 220);
	private static final Color contourColor = new Color(195, 195, 195);
	private static final Color headerColor = new Color(175, 218, 252);	// Blue-blue frost
	private static final Color tableColor = new Color(221, 238, 255);	// Pale blue
	private static final Color hoveredColor = new Color(213, 213, 227);
	//private static final Color pressedColor = new Color(203, 203, 222);
	private static final Color fontColor = Color.BLACK;

	private static final Font menuFont = new Font("Arial", Font.PLAIN, 12);
	private static final Font toolTipFont = new Font("Dubai", Font.PLAIN, 12);
	private static final Font defaultTextFont = new Font("Arial", Font.BOLD, 14);
	private Font tableFont = new Font("Ubuntu", Font.PLAIN, DEFAULT_TEXT_SIZE);

	private JTable resultTable;
	private DefaultTableModel tableModel;

	private DataBaseListener dbListener;

	private void setGlobalStyle() {
		UIManager.put("Button.background", buttonColor);
		UIManager.put("Button.foreground", fontColor);
		UIManager.put("Button.font", menuFont);

		UIManager.put("Menu.foreground", fontColor);
		UIManager.put("Menu.font", menuFont);

		//UIManager.put("PopupMenu.background", auxiliaryColor);

		UIManager.put("MenuItem.background", menuColor);
		UIManager.put("MenuItem.foreground", fontColor);
		UIManager.put("MenuItem.font", menuFont);
		//UIManager.put("MenuItem.acceleratorFont", menuFont);
		//UIManager.put("MenuItem.acceleratorForeground", fontColor);
		//UIManager.put("MenuItem.acceleratorSelectionForeground", fontColor);

		//UIManager.put("TextArea.background", mainColor);
		//UIManager.put("TextArea.foreground", fontColor);
		//UIManager.put("TextArea.font", textFont);

		//UIManager.put("ScrollPane.background", backColor);
	}

	public GraphicInterface(DataBaseListener dbListener) {
		this.dbListener = dbListener;

		setGlobalStyle();
		setTitle("W&R:SR - data service");
		setSize(720, 480);
		setMinimumSize(new Dimension(720, 480));
		setIconImage((new ImageIcon("src/main/resources/ico64.png")).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setJMenuBar(createMenuBar());

		add(createMainPanel(), BorderLayout.CENTER);
	}

	private JMenuBar createMenuBar() {
		JMenuBar res = new JMenuBar();

		res.setBackground(Color.WHITE);
		res.setBorder(null);
		res.setBorderPainted(false);

		JMenu fileMenu = createFileMenu();
		JMenu dataHandlingMenu = createDataHandlingMenu();
		JMenu helpMenu = createHelpMenu();

		res.add(fileMenu);
		res.add(dataHandlingMenu);
		res.add(helpMenu);

		return res;
	}
	
	private JMenuItem createMenuItem(String title) {
		JMenuItem res = new JMenuItem(title);

		res.setBorderPainted(false);

		return res;
	}

	private JMenuItem createMenuItem(String title, String toolTip) {
		JMenuItem res = new JMenuItem(title) {
			@Override
			public JToolTip createToolTip() {
				JToolTip tt = super.createToolTip();
				tt.setBackground(menuColor);
				tt.setBorder(BorderFactory.createLineBorder(contourColor, 1));
				tt.setForeground(fontColor);
				tt.setFont(toolTipFont);

				return tt;
			}
		};

		res.setBorderPainted(false);
		res.setToolTipText(toolTip);

		return res;
	}

	private JMenu createFileMenu() {
		JMenu res = createMainMenu("File");

		JMenuItem connectDataItem = createMenuItem("Connect", "connect to database");
		connectDataItem.addActionListener(e -> showConnectionDialogs());

		JMenuItem disconnectDataItem = createMenuItem("Disconnect", "disconnect from database");
		disconnectDataItem.addActionListener(e -> showDisconnectionDialogs());

		JMenuItem exitProgramItem = createMenuItem("Exit");
		exitProgramItem.addActionListener(e -> dispose());

		res.add(connectDataItem);
		res.add(disconnectDataItem);
		res.add(exitProgramItem);

		return res;
	}

	private JMenu createDataHandlingMenu() {
		JMenu res = createMainMenu("Data");

		JMenu insertDataItem = createInsertMenu();

		JMenuItem editDataItem = createMenuItem("Edit", "edit existing data element");
		JMenuItem userRequestItem = createMenuItem("User request", "write your own request");
		userRequestItem.addActionListener(e -> showUserRequestDialogs());

		JMenu readyRequestItem = createReadyRequestsMenu();

		res.add(insertDataItem);
		res.add(editDataItem);
		res.add(userRequestItem);
		res.add(readyRequestItem);

		return res;
	}

	private JMenu createHelpMenu() {
		JMenu res = createMainMenu("Help");

		JMenuItem showHelpItem = createMenuItem("Show help");

		JMenuItem aboutProgramItem = createMenuItem("About");
		aboutProgramItem.addActionListener(e -> showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
										"Version v1.0.0",
										"information")
		);

		res.add(showHelpItem);
		res.add(aboutProgramItem);

		return res;
	}

	private JMenu createInsertMenu() {
		JMenu res = createSubMenu("Insert", "create new data element");

		JMenuItem addCityItem = createMenuItem("Добавить город");
		addCityItem.addActionListener(e -> showAddCityDialog());

		JMenuItem addBuildingItem = createMenuItem("Добавить здание");
		addBuildingItem.addActionListener(e -> showAddBuildingDialog());

		JMenuItem addTransportItem = createMenuItem("Добавить транспорт");
		addTransportItem.addActionListener(e -> showAddTransportDialog());

		res.add(addCityItem);
		res.add(addBuildingItem);
		res.add(addTransportItem);

		return res;
	}

	private JMenu createReadyRequestsMenu() {
		JMenu res = createSubMenu("Ready request", "choose ready request");

		JMenuItem residentCapacityItem = createMenuItem("1.\tКоличество жилья");
		residentCapacityItem.addActionListener(e -> showTotalResidentCapacityDialogs());

		JMenuItem buildingsByTypeItem = createMenuItem("2.\tПеречень зданий по типу");
		buildingsByTypeItem.addActionListener(e -> showBuildingsByTypeDialogs());

		JMenuItem resourceProductionItem = createMenuItem("3.\tМаксимальное производство");
		resourceProductionItem.addActionListener(e -> showResourceProductionDialogs());

		JMenuItem resourceConsumptionItem = createMenuItem("4.\tМаксимальное потребление");
		resourceConsumptionItem.addActionListener(e -> showResourceConsumptionDialogs());

		JMenuItem jobsCountItem = createMenuItem("5.\tРабочие места");
		jobsCountItem.addActionListener(e -> showJobsCountDialogs());

		JMenuItem resourceStorageItem = createMenuItem("6.\tМаксимальное хранение в хранилище");
		resourceStorageItem.addActionListener(e -> showTotalResourceStorageDialogs());

		JMenuItem buildingCostItem = createMenuItem("7.\tСтроительство зданий");
		buildingCostItem.addActionListener(e -> showBuildingConstructionCostDialogs());

		JMenuItem transportCostItem = createMenuItem("8.\tСборка транспорта");
		transportCostItem.addActionListener(e -> showTransportConstructionCostDialogs());

		JMenuItem buildingsListItem = createMenuItem("9.\tСписок зданий");
		buildingsListItem.addActionListener(e -> showBuildingsListDialogs());

		JMenuItem daysToFillItem = createMenuItem("10.\tВремя заполнения хранилищ");
		daysToFillItem.addActionListener(e -> showDaysToFillStorageDialogs());

		JMenuItem parkingSpacesItem = createMenuItem("11.\tЧисло служебных стояночных мест");
		parkingSpacesItem.addActionListener(e -> showTotalParkingSpacesDialogs());

		JMenuItem maxStorageNonStorageItem = createMenuItem("12.\tОбъём хранения не хранилищ");
		maxStorageNonStorageItem.addActionListener(e -> showMaxStorageInNonStorageDialogs());

		// добавляем кнопку для вызова метода с Error Handling
		JMenuItem errorHandlingItem = createMenuItem("13.\tОбъём хранения не хранилищ");
		errorHandlingItem.addActionListener(e -> showErrorHandlingDialogs());
		res.add(errorHandlingItem);

		res.add(residentCapacityItem);
		res.add(buildingsByTypeItem);
		res.add(resourceProductionItem);
		res.add(resourceConsumptionItem);
		res.add(jobsCountItem);
		res.add(resourceStorageItem);
		res.add(buildingCostItem);
		res.add(transportCostItem);
		res.add(buildingsListItem);
		res.add(daysToFillItem);
		res.add(parkingSpacesItem);
		res.add(maxStorageNonStorageItem);

		return res;
	}

	private JPanel createMainPanel() {
		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		resultTable = new JTable(tableModel);

		resultTable.getTableHeader().setBackground(headerColor);
		resultTable.getTableHeader().setForeground(fontColor);
		resultTable.getTableHeader().setReorderingAllowed(false);
		resultTable.getTableHeader().setResizingAllowed(false);
		resultTable.getTableHeader().setFont(tableFont);

		resultTable.setBackground(tableColor);
		resultTable.setForeground(fontColor);
		resultTable.setFont(tableFont);
		resultTable.setRowHeight(DEFAULT_TEXT_SIZE + 4);

		JScrollPane resultScroll = new JScrollPane(resultTable,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


		resultScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI("src/main/resources/", hoveredColor, menuColor));
		resultScroll.getHorizontalScrollBar().setUI(new CustomScrollBarUI("src/main/resources/", hoveredColor, menuColor));

		JPanel resultArea = new JPanel(new BorderLayout());
		resultArea.add(resultScroll, BorderLayout.CENTER);

		return resultArea;
	}

	private void showConnectionDialogs() {
		if (isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы уже подключены к базе данных: " + dbListener.getDBName(),
				"database connection");

			return;
		}

		try (Connection connection = dbListener.getConnection()) {
			Object message = "Добро пожаловать, Товарищ! ";

			JOptionPane connectionPane = new JOptionPane(
				null,
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.DEFAULT_OPTION,
				null,				//For Icon
				new Object[] {}
			);

			JDialog connectionDialog = connectionPane.createDialog(this, "W&R:SR - data service: database connection");

			Object[] buttons = null;

			JButton cancelButton = createButton("Отменить подключение");
			cancelButton.addActionListener(e -> connectionDialog.dispose());

			if (dbListener.areTablesExist(connection)) {
				message += "База данных уже существует.";

				JButton createDbButton = createButton("Пересоздать базу данных");
				createDbButton.addActionListener(e -> recreateBD(connection, connectionDialog));

				JButton useDbButton = createButton("Использовать существующую");
				useDbButton.addActionListener(e -> useBD(connection, connectionDialog));

				buttons = new Object[] { createDbButton, useDbButton, cancelButton };
			}
			else {
				message += "База данных пуста.";

				JButton createDbButton = createButton("Создать базу данных");
				createDbButton.addActionListener(e -> createBD(connection, connectionDialog));

				buttons = new Object[] { createDbButton, cancelButton };
			}


			connectionPane.setOptions(buttons);
			connectionPane.setMessage(message);
			connectionDialog.pack();
			connectionDialog.setVisible(true);

		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Возникли проблемы подключения к базе данных! Проверьте, что база данных "
				+ dbListener.getDBName() + "существует и к ней есть доступ.",
				"connection error");
		}
	}

	private void showDisconnectionDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"database disconnection");

			return;
		}

		Object message = "Вы действительно хотите отключиться от базы данных: " + dbListener.getDBName() + "?";

		JOptionPane disconnectionPane = new JOptionPane(
			message,
			JOptionPane.QUESTION_MESSAGE,
			JOptionPane.DEFAULT_OPTION,
			null,				//For Icon
			new Object[] {}
		);

		JDialog disconnectionDialog = disconnectionPane.createDialog(this, "W&R:SR - data service: database disconnection");

		JButton yesButton = createButton("Да");
		yesButton.addActionListener(e -> {
			isConnected = false;
			disconnectionDialog.dispose();
		});

		JButton noButton = createButton("Нет");
		noButton.addActionListener(e -> disconnectionDialog.dispose());

		disconnectionPane.setOptions(new Object[] { yesButton, noButton });
		disconnectionDialog.pack();
		disconnectionDialog.setVisible(true);
	}

	private void showUserRequestDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"user request");

			return;
		}

		JDialog userRequestDialog = new JDialog(this, "W&R:SR - data service: user request", true);

		userRequestDialog.setVisible(true);
	}

	private void showAddCityDialog() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Нет подключения к базе данных", "insert");

			return;
		}

		JPanel panel = createCustomPanel(new GridLayout(1, 2, 10, 10));
		JTextField nameField = new JTextField();
		nameField.setBackground(menuColor);
		nameField.setForeground(fontColor);
		nameField.setFont(defaultTextFont);
		panel.add(createTextLabel("Название города:"));
		panel.add(nameField);
		showCustomOkCancelOptionDialog(panel, "добавление города", () -> {
			String name = nameField.getText().trim();
			if (name.isEmpty()) {
				showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
					"Название не может быть пустым", "insert error");

				return;
			}
			try {
				dbListener.addSettlement(name);
				showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
					"Город добавлен", "succes");
			}
			catch (Exception ex) {
				showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
					"Ошибка добавления: " + ex.getMessage(), "insert error");
			}
		});
	}

	private void showAddBuildingDialog() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Нет подключения", "insert");

			return;
		}
		try {
			List<String> cities = dbListener.getAllSettlements();
			List<String> blueprints = dbListener.getAllBuildingBlueprintNames();
			if (cities.isEmpty() || blueprints.isEmpty()) {
				showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
					"Нет городов или чертежей", "insert error");

				return;
			}

			JComboBox<String> cityCombo = createCustomComboBox(cities.toArray(new String[0]));
			JComboBox<String> blueprintCombo = createCustomComboBox(blueprints.toArray(new String[0]));
			JPanel panel = createCustomPanel(new GridLayout(2, 2, 10, 10));
			panel.add(createTextLabel("Город:"));
			panel.add(cityCombo);
			panel.add(createTextLabel("Чертёж здания:"));
			panel.add(blueprintCombo);
			showCustomOkCancelOptionDialog(panel, "добавление здания", () -> {
				String city = (String) cityCombo.getSelectedItem();
				String blueprint = (String) blueprintCombo.getSelectedItem();
				if (city == null || blueprint == null)
					return;

				try {
					dbListener.addBuilding(city, blueprint);
					showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
						"Здание добавлено", "succes");
				}
				catch (Exception ex) {
					showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
						"Ошибка добавления: " + ex.getMessage(), "insert error");
				}
			});
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка добваления: " + e.getMessage(), "insert error");
		}
	}

	private void showAddTransportDialog() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Нет подключения", "inser");

			return;
		}
		try {
			List<String> blueprints = dbListener.getAllTransportBlueprintNames();
			if (blueprints.isEmpty()) {
				showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
					"Нет чертежей транспорта", "insert error");

				return;
			}

			JComboBox<String> blueprintCombo = createCustomComboBox(blueprints.toArray(new String[0]));
			JPanel panel = createCustomPanel(new GridLayout(1, 2, 10, 10));
			panel.add(createTextLabel("Чертёж транспорта:"));
			panel.add(blueprintCombo);
			showCustomOkCancelOptionDialog(panel, "добавление транспорта", () -> {
				String blueprint = (String) blueprintCombo.getSelectedItem();
				if (blueprint == null)
					return;

				try {
					dbListener.addTransport(blueprint);
					showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
						"Транспорт добавлен", "succes");
				}
				catch (Exception ex) {
					showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
						"Ошибка добавления: " + ex.getMessage(), "insert error");
				}
			});
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка добавления: " + e.getMessage(), "insert error");
		}
	}

	// вызов метода с Error Handling
	private void showErrorHandlingDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"ready request 1");

			return;
		}

		try {
			int f = dbListener.callErrorHandlingMethod(1);
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 13: " + e.getMessage(),
				"query error");
		}
	}

	private void showTotalResidentCapacityDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"ready request 1");

			return;
		}

		try {
			List<String> settlements = dbListener.getAllSettlements();
			String[] chooseOptions = new String[settlements.size() + 1];
			chooseOptions[0] = "Вся страна";
			for (int i = 1; i <= settlements.size(); i++)
				chooseOptions[i] = settlements.get(i - 1);

			JComboBox<String> settlementCombo = createCustomComboBox(chooseOptions);

			JPanel panel = createCustomPanel(new GridLayout(1, 2, 10, 10));
			panel.add(createTextLabel("Населенный пункт:"));
			panel.add(settlementCombo);

			showCustomOkCancelOptionDialog(panel, "количество жилья", () -> {
				String selectedSettlement = (String) settlementCombo.getSelectedItem();
				try {

					Integer settlementId = null;
					if (!selectedSettlement.equals("Вся страна")) {
						settlementId = dbListener.getSettlementId(selectedSettlement);
						if (settlementId == null) {
							showSingleNumberResult("Общая жилая вместимость", null);
							showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
								"Город " + selectedSettlement + " не найден в базе",
								"query error");
					
							return;
						}
					}

					int capacity = dbListener.getTotalResidentCapacity(settlementId);

					showSingleNumberResult("Общая жилая вместимость", capacity);
				}
				catch (Exception ex) {
					showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
						"Ошибка выполнения запроса 1: " + ex.getMessage(),
						"query error");
				}
			});
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 1: " + e.getMessage(),
				"query error");
		}
	}

	private void showBuildingsByTypeDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"ready request 2");

			return;
		}

		try {
			List<String> types = dbListener.getAllBlueprintTypes();
			if (types.isEmpty()) {
				showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
					"Нет типов зданий", "query error");

				return;
			}

			List<String> settlements = dbListener.getAllSettlements();
			String[] settlementOptions = new String[settlements.size() + 1];
			settlementOptions[0] = "Вся страна";
			for (int i = 1; i <= settlements.size(); i++)
				settlementOptions[i] = settlements.get(i - 1);

			JComboBox<String> typeCombo = createCustomComboBox(types.toArray(new String[0]));
			JComboBox<String> settlementCombo = createCustomComboBox(settlementOptions);

			JPanel panel = createCustomPanel(new GridLayout(2, 2, 10, 10));
			panel.add(createTextLabel("Тип здания:"));
			panel.add(typeCombo);
			panel.add(createTextLabel("Населенный пункт:"));
			panel.add(settlementCombo);

			showCustomOkCancelOptionDialog(panel, "перечень зданий по типу", () -> {
				try {
					String selectedType = (String) typeCombo.getSelectedItem();
					String selectedSettlement = (String) settlementCombo.getSelectedItem();
					if (selectedSettlement.equals("Вся страна"))
						selectedSettlement = null;

					List<BuildingInfoDto> buildings = dbListener.getBuildingsByType(selectedType, selectedSettlement);
					if (buildings.isEmpty()) {
						showBuildingDtoList(new ArrayList<>());

 						showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
							"Нет зданий типа '" + selectedType + "'" +
							(selectedSettlement == null ? " по всей стране" : " в городе " + selectedSettlement),
							"ready request 2");
					}
					else
						showBuildingDtoList(buildings);
				}
				catch (Exception ex) {
					showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
						"Ошибка выполнения запроса 2: " + ex.getMessage(),
						"query error");
				}
			});
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 2: " + e.getMessage(),
				"query error");
		}
	}

	private void showResourceProductionDialogs() {
		showResourceSettlementsQueryDialogs("Максимальное производство ресурса", 3,
			(resId, setlId) -> dbListener.getMaxResourceProduction(resId, setlId));
	}

	private void showResourceConsumptionDialogs() {
		showResourceSettlementsQueryDialogs("Максимальное потребление ресурса", 4,
			(resId, setlId) -> dbListener.getMaxResourceConsumption(resId, setlId));
	}

	private void showJobsCountDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.WARNING_MESSAGE,
				"Нет подключения к базе данных", "ready request 5");

			return;
		}

		JPanel typePanel = new JPanel(new GridLayout(2, 1, 10, 10));
		typePanel.setBackground(menuColor);
		JButton cityButton = createButton("По населённому пункту");
		JButton buildingButton = createButton("По конкретному зданию");
		typePanel.add(cityButton);
		typePanel.add(buildingButton);

		JOptionPane typePane = new JOptionPane(typePanel, JOptionPane.PLAIN_MESSAGE,
			JOptionPane.DEFAULT_OPTION, null, new Object[]{});
		JDialog typeDialog = typePane.createDialog(this, "W&R:SR - data service: выбор масштаба");
		typeDialog.setBackground(menuColor);
		typeDialog.getContentPane().setBackground(menuColor);

		final boolean[] choiceMade = { false };
		cityButton.addActionListener(e -> {
			choiceMade[0] = true;
			typeDialog.dispose();
			showJobsByCity();
		});

		buildingButton.addActionListener(e -> {
			choiceMade[0] = true;
			typeDialog.dispose();
			showJobsByBuilding();
		});

		typeDialog.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				if (!choiceMade[0])
					typeDialog.dispose();
			}
		});

		typeDialog.pack();
		typeDialog.setLocationRelativeTo(this);
		typeDialog.setVisible(true);
	}

	private void showTotalResourceStorageDialogs() {
		showResourceSettlementsQueryDialogs("Максимальное хранение в хранилище", 6,
			(resId, setlId) -> dbListener.getTotalResourceStorage(resId, setlId));
	}

	private void showBuildingConstructionCostDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Нет подключения к базе данных", "ready request 7");

			return;
		}
		try {
			List<String> blueprints = dbListener.getAllBuildingBlueprintNames();
			if (blueprints.isEmpty()) {
				showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
					"Нет чертежей зданий", "query error");

				return;
			}

			List<Integer> blueprintIds = showCheckBoxDialog(
				"Строительство зданий",
				blueprints,
				name -> dbListener.getBuildingBlueprintId(name)
			);
			if (blueprintIds == null)
				return;

			if (blueprintIds.isEmpty()) {
				showConstructionDetails(null);
				showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
					"Ни одно здание не выбрано",
					"ready request 7");

				return;
			}

			ConstructionDetailsDto details = dbListener.getBuildingConstructionCost(blueprintIds);
			showConstructionDetails(details);
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 7: " + e.getMessage(), "query error");
		}
	}

	private void showTransportConstructionCostDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Нет подключения к базе данных", "ready request 8");

			return;
		}
		try {
			List<String> blueprints = dbListener.getAllTransportBlueprintNames();
			if (blueprints.isEmpty()) {
				showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
					"Нет чертежей транспорта", "query error");

				return;
			}

			List<Integer> blueprintIds = showCheckBoxDialog(
				"Сборка транспорта",
				blueprints,
				name -> dbListener.getTransportBlueprintId(name)
			);
			if (blueprintIds == null)
				return;

			if (blueprintIds.isEmpty()) {
				showConstructionDetails(null);
				showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
					"Ни одно транспортное средство не выбрано",
					"ready request 8");

				return;
			}

			ConstructionDetailsDto details = dbListener.getTransportConstructionCost(blueprintIds);
			showConstructionDetails(details);
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 8: " + e.getMessage(), "query error");
		}
	}

	private void showBuildingsListDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"ready request 9");

			return;
		}

		try {
			List<String> settlements = dbListener.getAllSettlements();
			String[] settlementOptions = new String[settlements.size() + 1];
			settlementOptions[0] = "Вся страна";
			for (int i = 1; i <= settlements.size(); i++)
				settlementOptions[i] = settlements.get(i - 1);

			JComboBox<String> settlementCombo = createCustomComboBox(settlementOptions);

			JPanel panel = createCustomPanel(new GridLayout(2, 2, 10, 10));
			panel.add(createTextLabel("Населенный пункт:"));
			panel.add(settlementCombo);

			showCustomOkCancelOptionDialog(panel, "перечень всех зданий", () -> {
				try {
					String selectedSettlement = (String) settlementCombo.getSelectedItem();
					if (selectedSettlement == null)
						return;

					Integer settlementId = null;
					if (!selectedSettlement.equals("Вся страна")) {
						settlementId = dbListener.getSettlementId(selectedSettlement);
						if (settlementId == null) {
							showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
								"Город не найден", "query error");

							return;
						}
					}

					List<BuildingInfoDto> buildings = dbListener.getBuildingsList(settlementId);
					if (buildings.isEmpty()) {
						showBuildingDtoList(new ArrayList<>());

 						showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
							"Нет зданий " + (selectedSettlement == null ? " по всей стране" : " в городе "
							+ selectedSettlement), "ready request 9");
					}
					else
						showBuildingDtoList(buildings);
				}
				catch (Exception ex) {
					showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
						"Ошибка выполнения запроса 9: " + ex.getMessage(),
						"query error");
				}
			});
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 9: " + e.getMessage(),
				"query error");
		}
	}

	private void showDaysToFillStorageDialogs() {
		showResourceSettlementsQueryDialogs("Время заполнения хранилищ", 10,
			(resId, setlId) -> dbListener.getDaysToFillStorage(resId, setlId));
	}

	private void showTotalParkingSpacesDialogs() {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Нет подключения к базе данных", "ready request 11");

			return;
		}

		try {
			List<BuildingInfoDto> allBuildings = dbListener.getBuildingsWithParking();
			if (allBuildings.isEmpty()) {
				showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
					"Нет построенных зданий", "query error");

				return;
			}

			Map<String, Integer> nameToId = new LinkedHashMap<>();
			List<String> displayNames = new ArrayList<>();
			for (BuildingInfoDto b : allBuildings) {
				String display = b.blueprintName() + " (" + b.settlementName() + ")";
				displayNames.add(display);
				nameToId.put(display, b.id());
			}

			List<Integer> selectedIds = showCheckBoxDialog("Парковочные места", displayNames, nameToId::get);

			if (selectedIds == null)
				return;

			if (selectedIds.isEmpty()) {
				showSingleNumberResult("Общее количество парковочных мест", null);
				showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
					"Ни одно здание не выбрано", "ready request 11");

				return;
			}

			int parking = dbListener.getTotalParkingSpaces(null, selectedIds);
			showSingleNumberResult("Общее служебных парковочных мест", parking);
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 11: " + e.getMessage(), "query error");
		}
	}

	private void showMaxStorageInNonStorageDialogs() {
		showResourceSettlementsQueryDialogs("Максимальное хранение не в хранилищах", 12,
			(resId, setlId) -> dbListener.getMaxStorageInNonStorageBuildings(resId, setlId));
	}

	private List<Integer> showCheckBoxDialog(String title, List<String> items, IdResolver idResolver) {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.setBackground(menuColor);
		List<JCheckBox> checkBoxes = new ArrayList<>();
		for (String item : items) {
			JCheckBox cb = new JCheckBox(item);
			cb.setBackground(menuColor);
			cb.setForeground(fontColor);
			cb.setFont(defaultTextFont);
			panel.add(cb);
			checkBoxes.add(cb);
		}
		JScrollPane scroll = new JScrollPane(panel);
		scroll.setPreferredSize(new Dimension(400, 300));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(menuColor);
		JButton selectAllButton = createButton("Выбрать все");
		JButton clearAllButton = createButton("Снять все");
		buttonPanel.add(selectAllButton);
		buttonPanel.add(clearAllButton);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(menuColor);
		mainPanel.add(scroll, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		JOptionPane optionPane = new JOptionPane(mainPanel,
			JOptionPane.PLAIN_MESSAGE,
			JOptionPane.DEFAULT_OPTION,
			null,
			new Object[]{}
		);
		optionPane.setBackground(menuColor);

		JDialog dialog = optionPane.createDialog(this, "W&R:SR - data service: " + title);
		dialog.setBackground(menuColor);

		JButton okButton = createButton("Выполнить");
		JButton cancelButton = createButton("Отмена");
		optionPane.setOptions(new Object[]{okButton, cancelButton});

		final List<Integer>[] result = new List[]{ null };

		okButton.addActionListener(e -> {
			List<Integer> selectedIds = new ArrayList<>();
			for (int i = 0; i < checkBoxes.size(); i++) {
				if (checkBoxes.get(i).isSelected()) {
					try {
						Integer id = idResolver.resolve(items.get(i));
						if (id != null)
							selectedIds.add(id);
					}
					catch (Exception ex) {
						showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
							"Ошибка получения ID для " + items.get(i) + ": " + ex.getMessage(),
								"request error");

						return;
					}
				}
			}

			result[0] = selectedIds;
			dialog.dispose();
		});

		cancelButton.addActionListener(e -> {
			result[0] = null;
			dialog.dispose();
		});

		selectAllButton.addActionListener(e -> {
			for (JCheckBox cb : checkBoxes) cb.setSelected(true);
		});
		clearAllButton.addActionListener(e -> {
			for (JCheckBox cb : checkBoxes) cb.setSelected(false);
		});

		dialog.pack();
		dialog.setVisible(true);
		return result[0];
	}

	private void showResourceSettlementsQueryDialogs(String title, int qNumber, ResourceQuery query) {
		if (!isConnected) {
			showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
				"Вы не подключены к базе данных",
				"ready request " + qNumber);

			return;
		}

		try {
			List<String> resources = dbListener.getAllResourcesNames();
			if (resources.isEmpty()) {
				showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
					"Нет ресурсов", "query error");

				return;
			}

			List<String> settlements = dbListener.getAllSettlements();
			String[] settlementOptions = new String[settlements.size() + 1];
			settlementOptions[0] = "Вся страна";
			for (int i = 1; i <= settlements.size(); i++)
				settlementOptions[i] = settlements.get(i - 1);

			JComboBox<String> resourcesCombo = createCustomComboBox(resources.toArray(new String[0]));
			JComboBox<String> settlementCombo = createCustomComboBox(settlementOptions);

			JPanel panel = createCustomPanel(new GridLayout(2, 2, 10, 10));
			panel.add(createTextLabel("Ресурс:"));
			panel.add(resourcesCombo);
			panel.add(createTextLabel("Населенный пункт:"));
			panel.add(settlementCombo);

			showCustomOkCancelOptionDialog(panel, title, () -> {
				try {
					String selectedResource = (String) resourcesCombo.getSelectedItem();
					String selectedSettlement = (String) settlementCombo.getSelectedItem();
					if (selectedSettlement.equals("Вся страна"))
						selectedSettlement = null;

					Integer resourceId = null;
					resourceId = dbListener.getResourceId(selectedResource);
					if (resourceId == null) {
						showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
							"Ресурс " + resourceId + " не найден в базе",
							"query error");
					
						return;
					}

					Integer settlementId = null;
					if (selectedSettlement != null) {
						settlementId = dbListener.getSettlementId(selectedSettlement);
						if (settlementId == null) {
							showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
								"Город не найден", "query error");

							return;
						}
					}

					Number result = query.execute(resourceId, settlementId);
					showSingleNumberResult(title, result);
				}
				catch (Exception ex) {
					showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
						"Ошибка выполнения запроса " + qNumber + ": " + ex.getMessage(),
						"query error");
				}
			});
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 3: " + e.getMessage(),
				"query error");
		}
	}

	private void showCustomOkOptionDialog(int msgType, String message, String headMsg) {
		JOptionPane customPane = new JOptionPane(
			message,
			msgType,
			JOptionPane.OK_OPTION,
			null,				//For Icon
			new Object[] {}
		);

		JDialog customDialog = customPane.createDialog(this, "W&R:SR - data service: " + headMsg);

		JButton okButton = createButton("OK");
		okButton.addActionListener(e -> customDialog.dispose());

		customPane.setOptions(new Object[] { okButton });

		customDialog.pack();
		customDialog.setVisible(true); 
	}

	private void showCustomOkCancelOptionDialog(JPanel panel, String headMsg, Runnable onOk) {
		JOptionPane customPane = new JOptionPane(panel,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.DEFAULT_OPTION,
				null,
				new Object[]{});

		JDialog customDialog = customPane.createDialog(this, "W&R:SR - data service: " + headMsg);

		JButton okButton = createButton("Выполнить");
		JButton cancelButton = createButton("Отмена");
		customPane.setOptions(new Object[] { okButton, cancelButton });

		okButton.addActionListener(e -> {
			customDialog.dispose();
			if (onOk != null)
				onOk.run();
		});
		cancelButton.addActionListener(e -> customDialog.dispose());

		customDialog.pack();
		customDialog.setVisible(true);
	}

	private void showJobsByCity() {
		try {
			List<String> settlements = dbListener.getAllSettlements();
			String[] options = new String[settlements.size() + 1];
			options[0] = "Вся страна";
			for (int i = 0; i < settlements.size(); i++)
				options[i + 1] = settlements.get(i);

			JComboBox<String> combo = createCustomComboBox(options);
			JPanel panel = createCustomPanel(new GridLayout(1, 2, 10, 10));
			panel.add(createTextLabel("Населенный пункт:"));
			panel.add(combo);

			showCustomOkCancelOptionDialog(panel, "рабочие места по городу", () -> {
				String selected = (String) combo.getSelectedItem();
				if (selected == null)
					return;

				try {
					Integer settlementId = null;
					if (!selected.equals("Вся страна")) {
						settlementId = dbListener.getSettlementId(selected);
						if (settlementId == null) {
							showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
								"Город не найден", "query error");

							return;
						}
					}

					JobsReportDto report = dbListener.getJobsCount(settlementId, null);
					showJobsResult(report);
				}
				catch (Exception ex) {
					showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
						"Ошибка выполнения запроса 5: " + ex.getMessage(), "query error");
				}
			});
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Ошибка выполнения запроса 5: " + e.getMessage(), "query error");
		}
	}

	private void showJobsByBuilding() {
		try {
			List<String> settlements = dbListener.getAllSettlements();
			String[] cityOptions = new String[settlements.size() + 1];
			cityOptions[0] = "Вся страна";
			for (int i = 0; i < settlements.size(); i++)
				cityOptions[i + 1] = settlements.get(i);

			JComboBox<String> cityCombo = createCustomComboBox(cityOptions);
			JPanel cityPanel = createCustomPanel(new GridLayout(1, 2, 10, 10));
			cityPanel.add(createTextLabel("Населенный пункт (для фильтрации):"));
			cityPanel.add(cityCombo);

			showCustomOkCancelOptionDialog(cityPanel, "выбор города для зданий", () -> {
				String selectedCity = (String) cityCombo.getSelectedItem();
				if (selectedCity == null)
					return;

				try {
					Integer settlementId = null;
					if (!selectedCity.equals("Вся страна")) {
						settlementId = dbListener.getSettlementId(selectedCity);
						if (settlementId == null) {
							showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
								"Город не найден", "query error");

							return;
						}
					}

					List<BuildingInfoDto> buildings = dbListener.getBuildingsList(settlementId);
					if (buildings.isEmpty()) {
						showCustomOkOptionDialog(JOptionPane.INFORMATION_MESSAGE,
							"Нет зданий в выбранной локации", "ready request 5");

						return;
					}

					Integer buildingId = showSingleBuildingChooser("Выберите здание", buildings);
					if (buildingId == null)
						return;

					JobsReportDto report = dbListener.getJobsCount(null, buildingId);
					showJobsResult(report);
				}
				catch (Exception ex) {
					showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
						"Ошибка выполенния запроса 5: " + ex.getMessage(), "query error");
				}
			});
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
                		"Ошибка выполенния запроса 5: " + e.getMessage(), "query error");
		}
	}

	private Integer showSingleBuildingChooser(String title, List<BuildingInfoDto> buildings) {
		if (buildings == null || buildings.isEmpty())
			return null;

		List<String> names = new ArrayList<>();
		for (BuildingInfoDto b : buildings)
			names.add(b.blueprintName() + " (" + b.settlementName() + ")");

		JList<String> list = new JList<>(names.toArray(new String[0]));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(menuColor);
		list.setForeground(fontColor);
		list.setFont(defaultTextFont);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(400, 300));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(menuColor);
		JButton okButton = createButton("Выбрать");
		JButton cancelButton = createButton("Отмена");
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(menuColor);
		mainPanel.add(scroll, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		JOptionPane optionPane = new JOptionPane(mainPanel, JOptionPane.PLAIN_MESSAGE,
			JOptionPane.DEFAULT_OPTION, null, new Object[]{});

		optionPane.setBackground(menuColor);
		JDialog dialog = optionPane.createDialog(this, "W&R:SR - data service: " + title);
		dialog.setBackground(menuColor);

		final Integer[] result = { null };
		okButton.addActionListener(e -> {
			int idx = list.getSelectedIndex();
			if (idx >= 0) result[0] = buildings.get(idx).id();
			dialog.dispose();
		});

		cancelButton.addActionListener(e -> dialog.dispose());

		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);

		return result[0];
	}



	private JMenu createMainMenu(String title) {
		JMenu res = new JMenu(title);

		res.setBorderPainted(false);
		res.getPopupMenu().setOpaque(true);
		res.getPopupMenu().setBorder(BorderFactory.createLineBorder(contourColor, 1));

		return res;
	}

	private JMenu createSubMenu(String title, String toolTip) {
		JMenu res = new JMenu(title) {
			@Override
			public JToolTip createToolTip() {
				JToolTip tt = super.createToolTip();
				tt.setBackground(menuColor);
				tt.setBorder(BorderFactory.createLineBorder(contourColor, 1));
				tt.setForeground(fontColor);
				tt.setFont(toolTipFont);

				return tt;
			}
		};

		res.setToolTipText(toolTip);
		res.setBorderPainted(false);
		res.getPopupMenu().setOpaque(true);
		res.getPopupMenu().setBorder(BorderFactory.createLineBorder(contourColor, 1));

		return res;
	}

	private JButton createButton(String title) {
		JButton res = new JButton(title);

		res.setFocusPainted(false);

		return res;
	}

	private JComboBox<String> createCustomComboBox(String[] items) {
		JComboBox<String> res = new JComboBox<>(items);

		res.setBackground(menuColor);
		res.setForeground(fontColor);
		res.setFont(defaultTextFont);

		return res;
	}

	private JPanel createCustomPanel(LayoutManager layout) {
		JPanel res = new JPanel(layout);

		res.setBackground(menuColor);

		return res;
	}

	private JLabel createTextLabel(String title) {
		JLabel res = new JLabel(title);

		res.setBackground(menuColor);
		res.setForeground(fontColor);
		res.setFont(defaultTextFont);

		return res;
	}



	private void clearTable() {
		tableModel.setRowCount(0);
		tableModel.setColumnCount(0);
	}

	private void showSingleNumberResult(String title, Number value) {
		clearTable();

		tableModel.addColumn(title);

		if (value == null)
			return;

		tableModel.addRow(new Object[]{ value });
	}

	private void showBuildingDtoList(List<BuildingInfoDto> buildings) {
		clearTable();

		tableModel.addColumn("ID");
		tableModel.addColumn("Здание");
		tableModel.addColumn("Населенный пункт");
		tableModel.addColumn("Тип здания");

		for (BuildingInfoDto b : buildings)
			tableModel.addRow(new Object[]{
				b.id(),
				b.blueprintName(),
				b.settlementName(),
				b.blueprintType()
			});
	}

	private void showConstructionDetails(ConstructionDetailsDto details) {
		clearTable();
		tableModel.addColumn("Ресурс");
		tableModel.addColumn("Количество");

		if (details == null)
			return;

		for (ResourceRequirementDto res : details.resources())
			tableModel.addRow(new Object[]{res.resourceName(), res.totalQuantity()});

		tableModel.addRow(new Object[]{ "Итого трудодней", details.totalWorkdays() });
	}

	private void showJobsResult(JobsReportDto report) {
		clearTable();

		tableModel.addColumn("Показатель");
		tableModel.addColumn("Количество");

		tableModel.addRow(new Object[]{ "Общее количество рабочих мест", report.totalJobs() });
		tableModel.addRow(new Object[]{ "Рабочие места, требующие высшего образования", report.totalHigherEduJobs() });
	}


	@FunctionalInterface
	private interface IdResolver {
		Integer resolve(String name) throws Exception;
	}

	@FunctionalInterface
	private interface ResourceQuery {
		Number execute(Integer resourceId, Integer settlementId) throws Exception;
	}

	private void recreateBD(Connection connection, JDialog dialog) {
		try {
			dbListener.createNewDatabase(connection);
			isConnected = true;
			dialog.dispose();
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Возникли проблемы создания базы данных! Проверьте, что к базе данных есть доступ.",
				"create error");
		}
	}

	private void createBD(Connection connection, JDialog dialog) {
		try {
			dbListener.createAndFillDatabase(connection);
			isConnected = true;
			dialog.dispose();
		}
		catch (Exception e) {
			showCustomOkOptionDialog(JOptionPane.ERROR_MESSAGE,
				"Возникли проблемы создания базы данных! Проверьте, что к базе данных есть доступ.",
				"create error");
		}
	}

	private void useBD(Connection connection, JDialog dialog) {
		isConnected = true;
		dialog.dispose();
	}
}