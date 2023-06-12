package com.processrobotics.Scriptwrapper.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScriptwrapperInstallationNodeContribution implements InstallationNodeContribution {
    private static final String ENABLED_KEY = "enabled";
    private static final String SELECTED_FILE_PATH_KEY = "selectedFilePath";

    private final DataModel model;
    private final ScriptwrapperInstallationNodeView view;

    public ScriptwrapperInstallationNodeContribution(InstallationAPIProvider apiProvider, ScriptwrapperInstallationNodeView view, DataModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void openView() {
		view.updateEnabledButton(getEnabledStatus());
		view.enableTextArea(getEnabledStatus());
        view.setScriptText(getSelectedFilePath());
    }

    public boolean getEnabledStatus() {
		return model.get(ENABLED_KEY, false);
	}

	public void setEnabled(boolean isEnabled) {
        model.set(ENABLED_KEY, isEnabled);
    }

	public String getSelectedFilePath() {
		return model.get(SELECTED_FILE_PATH_KEY, "");
	}

	@Override
    public void closeView() {
        // Called when the installation node view is closed
    }

    public boolean isDefined() {
        // Called to check if the installation node is defined
        return true;
    }

    @Override
    public void generateScript(ScriptWriter writer) {
        // Called to generate the script for the installation node

        boolean isEnabled = getEnabledStatus();
        if (isEnabled) {
            String selectedFilePath = model.get(SELECTED_FILE_PATH_KEY, "");
            if (!selectedFilePath.isEmpty()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(selectedFilePath));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.appendLine(line);
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setSelectedFilePath(String selectedFilePath) {
		System.out.println("Selected file path: " + selectedFilePath);
        model.set(SELECTED_FILE_PATH_KEY, selectedFilePath);
    }

	public void getSelectedFilePath(String selectedFilePath) {
		System.out.println("Selected file path: " + selectedFilePath);
		model.set(SELECTED_FILE_PATH_KEY, selectedFilePath);
	}

	public void setScriptText(String scriptText) {
		view.setScriptText(scriptText);
	}
}
