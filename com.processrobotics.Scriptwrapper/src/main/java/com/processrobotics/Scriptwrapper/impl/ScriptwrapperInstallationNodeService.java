package com.processrobotics.Scriptwrapper.impl;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class ScriptwrapperInstallationNodeService implements SwingInstallationNodeService<ScriptwrapperInstallationNodeContribution, ScriptwrapperInstallationNodeView> {

    @Override
    public String getTitle(Locale locale) {
        return "Script Wrapper";
    }

    @Override
    public ScriptwrapperInstallationNodeView createView(ViewAPIProvider apiProvider) {
        return new ScriptwrapperInstallationNodeView(apiProvider);
    }

    @Override
    public ScriptwrapperInstallationNodeContribution createInstallationNode(InstallationAPIProvider	apiProvider, ScriptwrapperInstallationNodeView view, DataModel model, CreationContext context) {
        return new ScriptwrapperInstallationNodeContribution(apiProvider, view, model);
    }

    @Override
    public void configureContribution(ContributionConfiguration configuration) {
        // Perform any configuration or initialization of the contribution here
    }
}
