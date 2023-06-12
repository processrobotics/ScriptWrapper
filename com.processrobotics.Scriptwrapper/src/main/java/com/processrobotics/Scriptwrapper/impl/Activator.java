package com.processrobotics.Scriptwrapper.impl;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution.*/
public class Activator implements BundleActivator {
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        System.out.println("Process Robotics ScriptWrapper says Hello World!");
		ScriptwrapperInstallationNodeService installationNodeService = new ScriptwrapperInstallationNodeService();

        // Register the installation node service
        bundleContext.registerService(SwingInstallationNodeService.class, installationNodeService, null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Process Robotics ScriptWrapper says Goodbye World!");
    }
}

