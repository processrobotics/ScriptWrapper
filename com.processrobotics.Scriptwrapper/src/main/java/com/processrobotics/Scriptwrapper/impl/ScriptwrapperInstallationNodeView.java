package com.processrobotics.Scriptwrapper.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.*;

public class ScriptwrapperInstallationNodeView
		implements SwingInstallationNodeView<ScriptwrapperInstallationNodeContribution> {
	private static final String ENABLED_KEY = "enabled";
	private static final String SELECTED_FILE_PATH_KEY = "selectedFilePath";

	private ViewAPIProvider apiProvider;

	private final JButton enableButton;
	private final JButton selectFileButton;
	private final JButton saveScriptButton;
	private final JTextArea scriptTextArea;
	private final JScrollPane scrollPane;
	private String selectedFilePath;

	public ScriptwrapperInstallationNodeView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
		enableButton = new JButton("Enable");
		selectFileButton = new JButton("Select File...");
		saveScriptButton = new JButton("Save Script");
		scriptTextArea = new JTextArea();
		scrollPane = new JScrollPane(scriptTextArea);

		scrollPane.setPreferredSize(new Dimension(400, 300));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}

	public void enableTextArea(boolean isEnabled) {
		scriptTextArea.setEnabled(isEnabled);
	}

	private void selectFile(final ScriptwrapperInstallationNodeContribution contribution) {

		JFileChooser fileChooser = openFileChooser();

		// Add file filter for .script files
		FileNameExtensionFilter scriptFilter = new FileNameExtensionFilter("Script files (*.script)", "script");
		fileChooser.setFileFilter(scriptFilter);

		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				String script = readScriptFile(selectedFile);
				scriptTextArea.setText(script);
				selectedFilePath = selectedFile.getAbsolutePath();
				contribution.setSelectedFilePath(selectedFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveScript(final ScriptwrapperInstallationNodeContribution contribution) {
		JFileChooser fileChooser = openFileChooser();

		int returnValue = fileChooser.showSaveDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				writeScriptFile(selectedFile, scriptTextArea.getText());
				selectedFilePath = selectedFile.getAbsolutePath();
				contribution.setSelectedFilePath(selectedFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String readScriptFile(File file) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void writeScriptFile(File file, String content) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private JFileChooser openFileChooser(){
		JFileChooser fileChooser = new JFileChooser();
		String serialNumber = apiProvider.getSystemAPI().getRobotModel().getSerialNumber();
		File defaultDirectory = null;

		if ("20195599999".equals(serialNumber)) {
			defaultDirectory = new File("/home/ur/ursim-current");
		} else {
			defaultDirectory = new File("/programs");
		}

		fileChooser.setCurrentDirectory(defaultDirectory);

		// Add file filter for .script files
		FileNameExtensionFilter scriptFilter = new FileNameExtensionFilter("Script files (*.script)", "script");
		fileChooser.setFileFilter(scriptFilter);

		return fileChooser;
	}

	@Override
	public void buildUI(JPanel panel, final ScriptwrapperInstallationNodeContribution contribution) {
		panel.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(buildButtonPanel(contribution));

		panel.add(buttonPanel, BorderLayout.SOUTH);
		panel.add(buildScrollPane(contribution), BorderLayout.CENTER);
	}

	private JPanel buildButtonPanel(final ScriptwrapperInstallationNodeContribution contribution) {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		// Logo
		try {
			Image logoImage = ImageIO
					.read(getClass().getResource("/com/processrobotics/scriptwrapper/impl/img/logo.png"));
			ImageIcon logoIcon = new ImageIcon(logoImage);
			JLabel logoLabel = new JLabel(logoIcon);
			buttonPanel.add(logoLabel, BorderLayout.WEST);
		} catch (IOException e) {
			e.printStackTrace();
		}

		buttonPanel.add(buildInfo());
		buttonPanel.add(enableButton);
		buttonPanel.add(selectFileButton);
		buttonPanel.add(saveScriptButton);

		selectFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectFile(contribution);
			}
		});

		saveScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveScript(contribution);
			}
		});

		enableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// boolean isEnabled = contribution.getEnabledStatus();
				boolean isEnabled = enableButton.getText().startsWith("Enable");
				contribution.setEnabled(isEnabled);
				enableTextArea(isEnabled);
				enableButton.setText(isEnabled ? "Disable" : "Enable ");
			}
		});

		return buttonPanel;
	}

	private JScrollPane buildScrollPane(final ScriptwrapperInstallationNodeContribution contribution) {
		// scriptTextArea.getDocument().addDocumentListener(new DocumentListener() {
		// @Override
		// public void insertUpdate(DocumentEvent e) {
		// contribution.setScript(scriptTextArea.getText());
		// }

		// @Override
		// public void removeUpdate(DocumentEvent e) {
		// contribution.setScript(scriptTextArea.getText());
		// }

		// @Override
		// public void changedUpdate(DocumentEvent e) {
		// contribution.setScript(scriptTextArea.getText());
		// }
		// });

		scriptTextArea.setBorder(BorderFactory.createEtchedBorder()); // Add border
		return scrollPane;
	}

	public void setScriptText(String filePath) {
		if (!filePath.isEmpty()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(filePath));
				StringBuilder contents = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					contents.append(line).append("\n");
				}
				reader.close();
				scriptTextArea.setText(contents.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateEnabledButton(boolean b) {
		if (b) {
			enableButton.setText("Disable");
		} else {
			enableButton.setText("Enable ");
		}
	}

	private JPanel buildInfo() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JTextArea textArea = new JTextArea(
				"Enable or disable the Script Wrapper to selectably add the above script to your programs.");
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(325, 80));

		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(300, 0));
	}
}
