package com.nsu.planningapp;

import com.nsu.planningapp.graphic.*;
import com.nsu.planningapp.graphic.UI.*;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		DataBaseListener dataBaseConnector = new DataBaseInformant();
		GraphicInterface gf = new GraphicInterface(dataBaseConnector);
		SwingUtilities.invokeLater(() -> gf.setVisible(true));
	}
}