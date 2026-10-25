package com.nsu.planningapp.graphic.UI;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollBarUI extends BasicScrollBarUI {
	private final ImageIcon upIcon;
	private final ImageIcon downIcon;
	private final ImageIcon leftIcon;
	private final ImageIcon rightIcon;

	private final Color customThumbColor;
	private final Color customTrackColor;

	private static final int BUTTON_SIZE = 16;

	public CustomScrollBarUI(String pathToIcons, Color thumbColor, Color trackColor) {
		this.upIcon = scaleIcon(pathToIcons + "upIcon.png", BUTTON_SIZE, BUTTON_SIZE);
		this.downIcon = scaleIcon(pathToIcons + "downIcon.png", BUTTON_SIZE, BUTTON_SIZE);
		this.leftIcon = scaleIcon(pathToIcons + "leftIcon.png", BUTTON_SIZE, BUTTON_SIZE);
		this.rightIcon = scaleIcon(pathToIcons + "rightIcon.png", BUTTON_SIZE, BUTTON_SIZE);

		this.customThumbColor = thumbColor;
		this.customTrackColor = trackColor;
	}

	private ImageIcon scaleIcon(String path, int w, int h) {
		ImageIcon original = new ImageIcon(path);
		Image scaled = original.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}

	@Override
	protected void configureScrollBarColors() {
        	this.thumbColor = customThumbColor;
        	this.trackColor = customTrackColor;
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return createCustomButton(orientation);
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return createCustomButton(orientation);
	}

	private JButton createCustomButton(int orientation) {
		JButton button = new JButton();
		button.setBorder(null);
		button.setFocusPainted(false);
		button.setBackground(customTrackColor);

		switch (orientation) {
			case SwingConstants.NORTH:
				button.setIcon(upIcon);
				break;
			case SwingConstants.SOUTH:
				button.setIcon(downIcon);
				break;
			case SwingConstants.WEST:
				button.setIcon(leftIcon);
				break;
			case SwingConstants.EAST:
				button.setIcon(rightIcon);
				break;
		}
        
		return button;
	}
}