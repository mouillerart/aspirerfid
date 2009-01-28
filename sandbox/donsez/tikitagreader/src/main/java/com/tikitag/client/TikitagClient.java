// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TikitagClient.java

package com.tikitag.client;

import com.tikitag.client.actionlauncher.ActionLauncher;
import com.tikitag.client.actionlauncher.Application;
import com.tikitag.client.actionlauncher.FallbackApplication;
import com.tikitag.client.actionlauncher.NoActionApplication;
import com.tikitag.client.actionlauncher.TagManagementApplication;
import com.tikitag.client.actionlauncher.file.DesktopApiFileApplication;
import com.tikitag.client.actionlauncher.url.DesktopApiUrlApplication;
import com.tikitag.client.actionlauncher.url.OstermillerUrlApplication;
import com.tikitag.client.actionlauncher.vcardmail.VCardDesktopMail;
import com.tikitag.client.factory.TikitagServerFactory;
import com.tikitag.client.gui.ActionFactory;
import com.tikitag.client.gui.LoginCredentialsGUI;
import com.tikitag.client.gui.SystemTrayGUI;
import com.tikitag.client.gui.TagServiceConfigurationGUI;
import com.tikitag.client.gui.TagToolsGUI;
import com.tikitag.client.tagservice.TagMonitor;
import com.tikitag.client.tagservice.TagService;
import com.tikitag.client.tagservice.TagServiceConfiguration;
import com.tikitag.client.tagservice.impl.TagServiceImpl;
import com.tikitag.ons.model.util.TagEvent;
import com.tikitag.util.HexFormatter;
import com.tikitag.util.PrefsKeyProvider;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.util.prefs.Preferences;
import javax.swing.*;

import org.ow2.aspirerfid.reader.remote.http.proxy.HttpAdapterProxy;

// Referenced classes of package com.tikitag.client:
//            ClientLifecycleHandler, Parameters, TikitagServer

public class TikitagClient
    implements ActionFactory
{
    public class TagServiceConfigurationAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            tagServiceConfigGUI.show();
        }

        private static final long serialVersionUID = 1L;
        private final TagServiceConfigurationGUI tagServiceConfigGUI;

        public TagServiceConfigurationAction()
        {
            super("Tag Service Configuration");
            tagServiceConfigGUI = new TagServiceConfigurationGUI(tagServiceConfiguration);
        }
    }

    private class LoginCredentialsAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            loginCredentialsGUI.show();
        }

        private static final long serialVersionUID = 1L;
        private final LoginCredentialsGUI loginCredentialsGUI = new LoginCredentialsGUI();

        public LoginCredentialsAction()
        {
            super("Login Credentials");
        }
    }

    private class KeyConfigurationAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            String newKey = JOptionPane.showInputDialog("Tikitag Shared Secret Key (HEX)", HexFormatter.toHexString(keyProvider.getKey()));
            if(newKey != null)
                keyProvider.setKey(newKey);
        }

        private static final long serialVersionUID = 1L;
        private final PrefsKeyProvider keyProvider = new PrefsKeyProvider();
        

        public KeyConfigurationAction()
        {
            super("Tikitag Key Configuration");
        }
    }

    public class ActionLauncherAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            if(isActionLauncherEnabled())
                tagService.removeTagMonitor(actionLauncher);
            else
                tagService.addTagMonitor(actionLauncher);
            setActionLauncherEnabled(!isActionLauncherEnabled());
        }

        public boolean isActionLauncherEnabled()
        {
            return prefs.getBoolean("actionLauncher.enabled", true);
        }

        public void setActionLauncherEnabled(boolean enabled)
        {
            prefs.putBoolean("actionLauncher.enabled", enabled);
        }

        private static final long serialVersionUID = 1L;
        private static final String ACTION_LAUNCHER_ENABLED_KEY = "actionLauncher.enabled";
        private Preferences prefs;
        

        public ActionLauncherAction()
        {
            super("Enable Action Launcher");
            prefs = Preferences.userNodeForPackage(com.tikitag.client.TikitagClient.class);
        }
    }

    private class ReadWriteTagction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            TagToolsGUI gui = new TagToolsGUI(tagService);
            gui.setVisible(true);
        }

        private static final long serialVersionUID = 1L;
        

        public ReadWriteTagction()
        {
            
            super("Tag Tools");
        }
    }

    private class ExitAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            stop();
            System.exit(0);
        }

        private static final long serialVersionUID = 1L;
        

        public ExitAction()
        {
            
            super("Exit");
        }
    }


    public TikitagClient(TikitagServer server)
    {
        this.server = server;
    }

    public static void main(String args[])
    {
        Parameters params = Parameters.parse(args);
        System.out.println((new StringBuilder()).append("Requesting connection to ").append(params.getInterfaceIdentifier()).toString());
        TikitagServer server = TikitagServerFactory.newServer(params.getInterfaceIdentifier(), params.getEndpointLocation());
        System.out.println((new StringBuilder()).append("Actually connecting to ").append(server.getActualConnectionUri()).toString());
        TikitagClient client = new TikitagClient(server);
        client.start();
    }

    public void start()
    {
        tagServiceConfiguration = new TagServiceConfiguration();
        tagService = new TagServiceImpl(tagServiceConfiguration);
        ClientLifecycleHandler clientLifeCycleHandler = new ClientLifecycleHandler(server, tagServiceConfiguration);
        tagService.addReaderMonitor(clientLifeCycleHandler);
        tagService.addTagMonitor(new TagMonitor() {

            public void onTagEvent(TagEvent tagEvent)
            {
                StringBuilder sb = new StringBuilder();
                sb.append((new StringBuilder()).append("\n  Action  Tag = ").append(tagEvent.getActionTag()).toString());
                sb.append((new StringBuilder()).append("\n  Context Tag = ").append(tagEvent.getContextTag()).toString());
                System.out.println((new StringBuilder()).append("Detected tags: ").append(sb.toString()).toString());
                
                try {
					HttpAdapterProxy.sendTag(
							"http://localhost:8080/HttpTagReader",
							HexFormatter.toHexString(tagEvent.getActionTag().getTagId().asByteArray()),
							HexFormatter.toHexString(tagEvent.getReaderId().getUid()));
				} catch (IOException e) {
					e.printStackTrace();
				}
            }

        }
);
        SystemTrayGUI gui = new SystemTrayGUI(this);
        clientLifeCycleHandler.addClientLifecycleListener(gui.getClientLifecycleListener());
        tagService.addReaderMonitor(gui.getReaderMonitor());
        actionLauncher = new ActionLauncher(server, new FallbackApplication(), gui.getUiNotifiction());
        try
        {
            actionLauncher.registerApplication(new DesktopApiUrlApplication());
        }
        catch(UnsupportedOperationException e)
        {
            actionLauncher.registerApplication(new OstermillerUrlApplication());
        }
        actionLauncher.registerApplication(new TagManagementApplication());
        actionLauncher.registerApplication(new NoActionApplication());
        actionLauncher.registerApplication(new VCardDesktopMail());
        try
        {
            actionLauncher.registerApplication(new DesktopApiFileApplication());
        }
        catch(UnsupportedOperationException e)
        {
            e.printStackTrace();
        }
        if(getActionLauncherAction().isActionLauncherEnabled())
            tagService.addTagMonitor(actionLauncher);
        gui.start();
        clientLifeCycleHandler.onTikitagClientStarted();
        tagService.start();
    }

    public void stop()
    {
        tagService.shutdown();
    }

    public void registerApplication(Application application)
    {
        actionLauncher.registerApplication(application);
    }

    public void unregisterApplication(Application application)
    {
        actionLauncher.unregisterApplication(application);
    }

    public Action getExitAction()
    {
        return new ExitAction();
    }

    public Action getReadWriteTagAction()
    {
        return new ReadWriteTagction();
    }

    public ActionLauncherAction getActionLauncherAction()
    {
        return new ActionLauncherAction();
    }

    public Action getKeyConfigurationAction()
    {
        return new KeyConfigurationAction();
    }

    public Action getLoginCredentialsAction()
    {
        return new LoginCredentialsAction();
    }

    public Action getTagServiceConfigurationAction()
    {
        return new TagServiceConfigurationAction();
    }

    private TagService tagService;
    private TikitagServer server;
    private ActionLauncher actionLauncher;
    private TagServiceConfiguration tagServiceConfiguration;



}
