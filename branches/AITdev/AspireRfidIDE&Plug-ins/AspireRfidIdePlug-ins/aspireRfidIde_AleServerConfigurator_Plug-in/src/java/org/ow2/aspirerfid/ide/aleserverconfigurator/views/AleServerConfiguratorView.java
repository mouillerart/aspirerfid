/*
 * Copyright © 2008-2010, Aspire
 * 
 * Aspire is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License version 2.1 as published by
 * the Free Software Foundation (the "LGPL").
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library in the file COPYING-LGPL-2.1; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY
 * KIND, either express or implied. See the GNU Lesser General Public License
 * for the specific language governing rights and limitations.
 */

package org.ow2.aspirerfid.ide.aleserverconfigurator.views;

import java.io.CharArrayWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.accada.ale.util.DeserializerUtil;
import org.accada.ale.util.SerializerUtil;
import org.accada.ale.wsdl.ale.epcglobal.ALEServicePortType;
import org.accada.ale.wsdl.ale.epcglobal.ArrayOfString;
import org.accada.ale.wsdl.ale.epcglobal.Define;
import org.accada.ale.wsdl.ale.epcglobal.DuplicateNameExceptionResponse;
import org.accada.ale.wsdl.ale.epcglobal.DuplicateSubscriptionExceptionResponse;
import org.accada.ale.wsdl.ale.epcglobal.ECSpecValidationExceptionResponse;
import org.accada.ale.wsdl.ale.epcglobal.EmptyParms;
import org.accada.ale.wsdl.ale.epcglobal.GetECSpec;
import org.accada.ale.wsdl.ale.epcglobal.GetSubscribers;
import org.accada.ale.wsdl.ale.epcglobal.Immediate;
import org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse;
import org.accada.ale.wsdl.ale.epcglobal.InvalidURIExceptionResponse;
import org.accada.ale.wsdl.ale.epcglobal.NoSuchNameExceptionResponse;
import org.accada.ale.wsdl.ale.epcglobal.NoSuchSubscriberExceptionResponse;
import org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse;
import org.accada.ale.wsdl.ale.epcglobal.Subscribe;
import org.accada.ale.wsdl.ale.epcglobal.Undefine;
import org.accada.ale.wsdl.ale.epcglobal.Unsubscribe;
import org.accada.ale.xsd.ale.epcglobal.ECReports;
import org.accada.ale.xsd.ale.epcglobal.ECSpec;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.ow2.aspirerfid.ide.aleserverconfigurator.Activator;
import org.ow2.aspirerfid.ide.aleserverconfigurator.preferences.PreferenceConstants;
import org.ow2.aspirerfid.ide.aleserverconfigurator.views.View.ViewContentProvider;
import org.ow2.aspirerfid.ide.aleserverconfigurator.views.View.ViewLabelProvider;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.widgets.FileDialog;

import java.io.File;
import java.util.List;

import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author nkef@ait.edu.gr
 * 
 */

public class AleServerConfiguratorView extends ViewPart {

	private Text unnotificationUriText;
	private Combo unsubscribeECSpecListcombo;
	private Composite unsubscribeECSpecComposite;
	private Text notificationUriText;
	private Combo subscribeECSpecListcombo;
	private Composite subscribeECSpecComposite;
	private Combo getECSpecECSpecListcombo;
	private Composite getECSpecECSpecComposite;
	private Combo undefineECSpecListcombo;
	private Composite undefineECSpecComposite;
	private Text ecSpecNametext;
	private Combo ecSpecListcombo;
	// private FileDialog ecSpecLocationFileDialog;
	private Composite defineECSpecComposite;
	public static final String ID = "org.ow2.aspirerfid.ide.aleserverconfigurator.views.AleServerConfiguratorView"; //$NON-NLS-1$

	Composite container;

	/** ale proxy */
	private ALEServicePortType aleProxy = null;

	/** Returns the preference store for this UI plug-in */
	IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();

	/**
	 * ECSpec Configurator Result
	 */
	Object ecSpecConfiguratorResult = null;

	// ----------Console Initialization and Use----------------
	IOConsoleOutputStream aleServerConfiguratorOutputConsole;
	IOConsole aleServerConfiguratorConsole;
	IConsoleManager manager;
	IConsole[] consolesx;

	// --------------------------------------------------------

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);

		final TabItem ecspecConfiguratorTabItem = new TabItem(tabFolder, SWT.NONE);
		ecspecConfiguratorTabItem.setText("ECSpec Configurator");

		final ScrolledComposite ecspecConfiguratorScrolledComposite = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		ecspecConfiguratorTabItem.setControl(ecspecConfiguratorScrolledComposite);

		final Composite ecspecConfiguratorComposite = new Composite(ecspecConfiguratorScrolledComposite, SWT.NONE);
		ecspecConfiguratorComposite.setLocation(0, 0);

		final ToolBar toolBar = new ToolBar(ecspecConfiguratorComposite, SWT.NONE);
		toolBar.setBounds(0, 0, 1040, 25);

		final ToolItem defineECSpecToolItem = new ToolItem(toolBar, SWT.PUSH);
		defineECSpecToolItem.addSelectionListener(new DefineECSpecToolItemSelectionListener());
		defineECSpecToolItem.setText("Define");

		final ToolItem subscribeECSpecToolItem = new ToolItem(toolBar, SWT.PUSH);
		subscribeECSpecToolItem.addSelectionListener(new SubscribeECSpecToolItemSelectionListener());
		subscribeECSpecToolItem.setText("Subscribe");

		final ToolItem undefineECSpecToolItem = new ToolItem(toolBar, SWT.PUSH);
		undefineECSpecToolItem.addSelectionListener(new UndefineECSpecToolItemSelectionListener());
		undefineECSpecToolItem.setText("Undefine");

		final ToolItem unsubscribeECSpecToolItem = new ToolItem(toolBar, SWT.PUSH);
		unsubscribeECSpecToolItem.addSelectionListener(new UnsubscribeECSpecToolItemSelectionListener());
		unsubscribeECSpecToolItem.setText("Unsubscribe");

		final ToolItem getECSpecNamesToolItem = new ToolItem(toolBar, SWT.PUSH);
		getECSpecNamesToolItem.addSelectionListener(new GetECSpecNamesToolItemSelectionListener());
		getECSpecNamesToolItem.setText("Get ECSpec Names");

		final ToolItem getECSpecsToolItem = new ToolItem(toolBar, SWT.PUSH);
		getECSpecsToolItem.addSelectionListener(new GetECSpecsToolItemSelectionListener());
		getECSpecsToolItem.setText("Get ECSpecs");

		final ToolItem getVendorVersionToolItem = new ToolItem(toolBar, SWT.PUSH);
		getVendorVersionToolItem.addSelectionListener(new GetVendorVersionToolItemSelectionListener());
		getVendorVersionToolItem.setText("Get Vendor Version");

		defineECSpecComposite = new Composite(ecspecConfiguratorComposite, SWT.NONE);
		defineECSpecComposite.setVisible(false);
		defineECSpecComposite.setBounds(0, 31, 572, 64);

		// ecSpecLocationFileDialog = new
		// FileDialog(defineECSpecComposite.getShell(),SWT.NONE);
		// ecSpecLocationFileDialog.setFileName("test");
		// ecSpecLocationFileDialog.open();

		final Label setSpecNameLabel = new Label(defineECSpecComposite, SWT.NONE);
		setSpecNameLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		setSpecNameLabel.setText("Set Spec Name:");
		setSpecNameLabel.setBounds(5, 5, 105, 21);

		final Label setSpecNameLabel_1 = new Label(defineECSpecComposite, SWT.NONE);
		setSpecNameLabel_1.setBounds(5, 35, 105, 21);
		setSpecNameLabel_1.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		setSpecNameLabel_1.setText("Choose Spec:");

		final Button defineECSpecButton = new Button(defineECSpecComposite, SWT.NONE);
		defineECSpecButton.addSelectionListener(new DefineECSpecButtonSelectionListener());
		defineECSpecButton.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD));
		defineECSpecButton.setText("Execute");
		defineECSpecButton.setBounds(445, 5, 74, 51);

		ecSpecListcombo = new Combo(defineECSpecComposite, SWT.NONE);
		ecSpecListcombo.setBounds(115, 35, 325, 24);

		ecSpecNametext = new Text(defineECSpecComposite, SWT.BORDER);
		ecSpecNametext.setBounds(116, 5, 323, 24);

		undefineECSpecComposite = new Composite(ecspecConfiguratorComposite, SWT.NONE);
		undefineECSpecComposite.setBounds(0, 30, 572, 65);
		undefineECSpecComposite.setVisible(false);

		final Label setSpecNameLabel_1_1 = new Label(undefineECSpecComposite, SWT.NONE);
		setSpecNameLabel_1_1.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		setSpecNameLabel_1_1.setBounds(5, 5, 105, 21);
		setSpecNameLabel_1_1.setText("Choose Spec:");

		final Button undefineECSpecButton = new Button(undefineECSpecComposite, SWT.NONE);
		undefineECSpecButton.addSelectionListener(new UndefineECSpecButtonSelectionListener());
		undefineECSpecButton.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD));
		undefineECSpecButton.setBounds(445, 5, 74, 21);
		undefineECSpecButton.setText("Execute");

		undefineECSpecListcombo = new Combo(undefineECSpecComposite, SWT.NONE);
		undefineECSpecListcombo.setBounds(115, 5, 325, 21);

		getECSpecECSpecComposite = new Composite(ecspecConfiguratorComposite, SWT.NONE);
		getECSpecECSpecComposite.setBounds(0, 30, 572, 64);
		getECSpecECSpecComposite.setVisible(false);

		final Label setSpecNameLabel_1_1_1 = new Label(getECSpecECSpecComposite, SWT.NONE);
		setSpecNameLabel_1_1_1.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		setSpecNameLabel_1_1_1.setBounds(5, 5, 129, 21);
		setSpecNameLabel_1_1_1.setText("Choose Spec Name:");

		final Button getECSpecECSpecButton = new Button(getECSpecECSpecComposite, SWT.NONE);
		getECSpecECSpecButton.addSelectionListener(new GetECSpecECSpecButtonSelectionListener());
		getECSpecECSpecButton.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD));
		getECSpecECSpecButton.setBounds(470, 5, 74, 21);
		getECSpecECSpecButton.setText("Execute");

		getECSpecECSpecListcombo = new Combo(getECSpecECSpecComposite, SWT.NONE);
		getECSpecECSpecListcombo.setBounds(140, 5, 325, 21);

		subscribeECSpecComposite = new Composite(ecspecConfiguratorComposite, SWT.NONE);
		subscribeECSpecComposite.setBounds(0, 30, 572, 64);
		subscribeECSpecComposite.setVisible(false);

		final Label setSpecNameLabel_2 = new Label(subscribeECSpecComposite, SWT.NONE);
		setSpecNameLabel_2.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		setSpecNameLabel_2.setBounds(5, 5, 105, 21);
		setSpecNameLabel_2.setText("Notification URI:");

		final Label setSpecNameLabel_1_2 = new Label(subscribeECSpecComposite, SWT.NONE);
		setSpecNameLabel_1_2.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		setSpecNameLabel_1_2.setBounds(5, 35, 105, 21);
		setSpecNameLabel_1_2.setText("Choose Spec:");

		final Button subscribeECSpecButton = new Button(subscribeECSpecComposite, SWT.NONE);
		subscribeECSpecButton.addSelectionListener(new SubscribeECSpecButtonSelectionListener());
		subscribeECSpecButton.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD));
		subscribeECSpecButton.setBounds(445, 5, 74, 51);
		subscribeECSpecButton.setText("Execute");

		subscribeECSpecListcombo = new Combo(subscribeECSpecComposite, SWT.NONE);
		subscribeECSpecListcombo.setBounds(115, 35, 325, 21);

		notificationUriText = new Text(subscribeECSpecComposite, SWT.BORDER);
		notificationUriText.setBounds(116, 5, 323, 24);

		unsubscribeECSpecComposite = new Composite(ecspecConfiguratorComposite, SWT.NONE);
		unsubscribeECSpecComposite.setBounds(0, 30, 572, 64);
		unsubscribeECSpecComposite.setVisible(false);

		final Label setSpecNameLabel_2_1 = new Label(unsubscribeECSpecComposite, SWT.NONE);
		setSpecNameLabel_2_1.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		setSpecNameLabel_2_1.setBounds(5, 5, 105, 21);
		setSpecNameLabel_2_1.setText("Notification URI:");

		final Label setSpecNameLabel_1_2_1 = new Label(unsubscribeECSpecComposite, SWT.NONE);
		setSpecNameLabel_1_2_1.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		setSpecNameLabel_1_2_1.setBounds(5, 35, 105, 21);
		setSpecNameLabel_1_2_1.setText("Choose Spec:");

		final Button unsubscribeECSpecButton = new Button(unsubscribeECSpecComposite, SWT.NONE);
		unsubscribeECSpecButton.addSelectionListener(new UnsubscribeECSpecButtonSelectionListener());
		unsubscribeECSpecButton.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD));
		unsubscribeECSpecButton.setBounds(445, 5, 74, 51);
		unsubscribeECSpecButton.setText("Execute");

		unsubscribeECSpecListcombo = new Combo(unsubscribeECSpecComposite, SWT.NONE);
		unsubscribeECSpecListcombo.setBounds(115, 35, 325, 21);

		unnotificationUriText = new Text(unsubscribeECSpecComposite, SWT.BORDER);
		unnotificationUriText.setBounds(116, 5, 323, 24);
		ecspecConfiguratorComposite.setSize(647, 110);
		ecspecConfiguratorScrolledComposite.setContent(ecspecConfiguratorComposite);

		final TabItem lrspecConfiguratorTabItem = new TabItem(tabFolder, SWT.NONE);
		lrspecConfiguratorTabItem.setText("LRSpec Configurator");

		final ScrolledComposite lrspecConfiguratorScrolledComposite = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		lrspecConfiguratorTabItem.setControl(lrspecConfiguratorScrolledComposite);

		final Composite lrspecConfiguratorComposite = new Composite(lrspecConfiguratorScrolledComposite, SWT.NONE);
		lrspecConfiguratorComposite.setLocation(0, 0);

		final ToolBar toolBar_1 = new ToolBar(lrspecConfiguratorComposite, SWT.NONE);
		toolBar_1.setBounds(0, 0, 1029, 25);

		final ToolItem defineLRSpecToolItem = new ToolItem(toolBar_1, SWT.PUSH);
		defineLRSpecToolItem.setText("Define");

		final ToolItem setReadersToolItem = new ToolItem(toolBar_1, SWT.PUSH);
		setReadersToolItem.setText("Set");

		final ToolItem undefineLRSpecToolItem = new ToolItem(toolBar_1, SWT.PUSH);
		undefineLRSpecToolItem.setText("Undefine");

		final ToolItem removeReadersToolItem = new ToolItem(toolBar_1, SWT.PUSH);
		removeReadersToolItem.setText("Remove");
		lrspecConfiguratorComposite.setSize(1152, 480);
		lrspecConfiguratorScrolledComposite.setContent(lrspecConfiguratorComposite);
		createActions();
		initializeToolBar();
		initializeMenu();
		initiateConsole();
		initializeAleProxy();

	}

	// =======================Methods Definitions==============================

	/**
	 * Initiate the Ale Server Configurator Console
	 */
	private void initiateConsole() {
		manager = ConsolePlugin.getDefault().getConsoleManager();
		consolesx = manager.getConsoles();
		boolean exist = false;
		for (int i = 0; i < consolesx.length; i++) {
			if (consolesx[i].getName().equals("ALE Server Configurator"))
				aleServerConfiguratorConsole = (IOConsole) consolesx[i];
			exist = true;
		}
		if (!exist) {
			aleServerConfiguratorConsole = new IOConsole("ALE Server Configurator", null);
			manager.addConsoles(new IConsole[] { aleServerConfiguratorConsole });
		}
		manager.showConsoleView(aleServerConfiguratorConsole);
		aleServerConfiguratorConsole.clearConsole();
		aleServerConfiguratorOutputConsole = aleServerConfiguratorConsole.newOutputStream();
		// writeToConsole("welcome !!!");

	}

	/**
	 * Write to the IDE Console
	 * 
	 * @param message
	 */
	private void writeToConsole(String message) {

		try {
			aleServerConfiguratorOutputConsole.write(message + "\n");
		}
		catch (IOException e) {
			MessageDialog.openWarning(container.getShell(), "Console Writing Error", e.getMessage());
		}
	}

	private File[] getECSpecsPathFolderContents() {

		// IPreferenceStore preferences =
		// Activator.getDefault().getPreferenceStore();
		String folderPath = preferences.getString(PreferenceConstants.P_ECSpecsPATH);
		File f = new File(folderPath);
		File[] contents = f.listFiles();
		return contents;
	}

	/**
	 * Create the actions
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();

	}

	/**
	 * Initialize the menu
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	/**
	 * Gets the Defined ECSpec Names
	 * 
	 * @return List<String> of ECSpec Names or null if empty
	 */
	private List<String> getDefinedECSpecNames() {

		List<String> ecSpecNames = null;

		try {
			ecSpecNames = aleProxy.getECSpecNames(new EmptyParms()).getString();
		}
		catch (Exception e) {
			MessageDialog.openWarning(container.getShell(), "Error", e.getMessage());
		}
		if (ecSpecNames != null && ecSpecNames.size() > 0) {
			return ecSpecNames;
		}
		else {
			return null;

		}
	}

	/**
	 * Initialize the ALE Server Proxy
	 */
	private void initializeAleProxy() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(ALEServicePortType.class);
		String ecSpecEndPoint = preferences.getString(PreferenceConstants.P_ALEClientEndPointSTRING);
		if (ecSpecEndPoint.equals(null) || ecSpecEndPoint.equals("")) {
			MessageDialog.openWarning(container.getShell(), "Error", "No End Point Defined");
			return;
		}
		factory.setAddress(ecSpecEndPoint);
		aleProxy = (ALEServicePortType) factory.create();
	}

	/**
	 * This method loads the ec specification from a file.
	 * 
	 * @param filename
	 *            of ec specification file
	 * @return ec specification
	 * @throws Exception
	 *             if specification could not be loaded
	 */
	private ECSpec getECSpecFromFile() {
		FileInputStream inputStream;
		ECSpec eCSpec = null;
		String ecSpecsPath = null;
		ecSpecsPath = preferences.getString(PreferenceConstants.P_ECSpecsPATH);
		try {
			if (ecSpecsPath.endsWith("\\")) {
				ecSpecsPath = preferences.getString(PreferenceConstants.P_ECSpecsPATH);
			}
			else {
				ecSpecsPath = ecSpecsPath + "\\";
			}

			inputStream = new FileInputStream(ecSpecsPath + ecSpecListcombo.getText());
			eCSpec = DeserializerUtil.deserializeECSpec(inputStream);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			MessageDialog.openWarning(container.getShell(), "Error", "File Not Found");
			e.printStackTrace();
		}
		catch (Exception e) {
			MessageDialog.openWarning(container.getShell(), "Error", e.getMessage());

		}

		return eCSpec;

	}

	/**
	 * This method displays the result in the result text area.
	 * 
	 * @param result
	 *            to display
	 */
	private void showECSpecConfiguratorResult(Object result) {

		if (result instanceof String) {
			aleServerConfiguratorConsole.clearConsole();
			writeToConsole((String) result);

		}
		else if (result instanceof ArrayOfString) {
			aleServerConfiguratorConsole.clearConsole();
			ArrayOfString resultStringArray = (ArrayOfString) result;
			if (resultStringArray.getString().size() == 0) {
				writeToConsole("EmptyArray");
			}
			else {
				for (String s : resultStringArray.getString()) {
					writeToConsole(s);
				}
			}
		}
		else if (result instanceof ECSpec) {
			aleServerConfiguratorConsole.clearConsole();
			CharArrayWriter writer = new CharArrayWriter();
			try {
				SerializerUtil.serializeECSpec((ECSpec) result, writer);
			}
			catch (IOException e) {
				MessageDialog.openWarning(container.getShell(), "SerializationException", e.getMessage());
			}
			writeToConsole(writer.toString());

		}
		else if (result instanceof ECReports) {
			aleServerConfiguratorConsole.clearConsole();
			CharArrayWriter writer = new CharArrayWriter();
			try {
				SerializerUtil.serializeECReports((ECReports) result, writer);
			}
			catch (IOException e) {
				MessageDialog.openWarning(container.getShell(), "SerializationException", e.getMessage());
			}
			writeToConsole(writer.toString());

		}
	}

	// =========================Getters & Setters=============================

	public void setDefineECSpecCompositeVisible(boolean visible) {
		defineECSpecComposite.setVisible(visible);
	}

	public void setSubscribeECSpecCompositeVisible(boolean visible) {
		subscribeECSpecComposite.setVisible(visible);
	}

	public void setUndefineECSpecCompositeVisible(boolean visible) {
		undefineECSpecComposite.setVisible(visible);
	}

	public void setGetECSpecECSpecCompositeVisible(boolean visible) {
		getECSpecECSpecComposite.setVisible(visible);
	}

	public void setUnsubscribeECSpecCompositeVisible(boolean visible) {
		unsubscribeECSpecComposite.setVisible(visible);
	}

	// =====================Action Listeners================================

	private class DefineECSpecToolItemSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {

			setDefineECSpecCompositeVisible(true);
			setSubscribeECSpecCompositeVisible(false);

			ecSpecListcombo.removeAll();
			File[] contents = getECSpecsPathFolderContents();
			for (int i = 0; i < contents.length; i++) {
				if (contents[i].getName().endsWith(".xml")) {
					ecSpecListcombo.add(contents[i].getName());
				}
			}

		}
	}

	private class DefineECSpecButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {

			initializeAleProxy();

			List<String> ecSpecNames = getDefinedECSpecNames();

			if (ecSpecNames != null) {
				for (String specName : ecSpecNames) {
					if (ecSpecNametext.equals(specName)) {
						MessageDialog.openWarning(container.getShell(), "Error", "This ECSpec name already exists! \n Please choose a diferent one");
						return;
					}
				}
			}

			// ==================================

			if (ecSpecNametext.getText().equals("")) {
				MessageDialog.openWarning(container.getShell(), "Error", "Please spesify an ECSpec Name");
			}

			// get ecSpec
			ECSpec ecSpec = getECSpecFromFile();

			Define defineParms = new Define();
			defineParms.setSpecName(ecSpecNametext.getText());
			defineParms.setSpec(ecSpec);
			try {
				ecSpecConfiguratorResult = null;
				ecSpecConfiguratorResult = aleProxy.define(defineParms);
				showECSpecConfiguratorResult(ecSpecConfiguratorResult);
				MessageDialog.openConfirm(container.getShell(), "Confirmation", "The ECSpec Was Successfully Defined");
			}
			catch (ImplementationExceptionResponse e1) {
				MessageDialog.openWarning(container.getShell(), "Implementation Exception", e1.getMessage());
				e1.printStackTrace();
				return;
			}
			catch (SecurityExceptionResponse e1) {
				MessageDialog.openWarning(container.getShell(), "Security Exception", e1.getMessage());
				e1.printStackTrace();
				return;
			}
			catch (ECSpecValidationExceptionResponse e1) {
				MessageDialog.openWarning(container.getShell(), "ECSpec Validation Exception", e1.getMessage());
				e1.printStackTrace();
				return;
			}
			catch (DuplicateNameExceptionResponse e1) {
				MessageDialog.openWarning(container.getShell(), "Duplicate Name Exception", e1.getMessage());
				e1.printStackTrace();
				return;
			}

			// result = guiText.getString("SuccessfullyDefinedMessage");

			// Immediate immediateParms = new Immediate();
			// immediateParms.setSpec(ecSpec);
			// result = aleProxy.immediate(immediateParms);

			// ==================================

			ecSpecNametext.setText("");

		}
	}

	private class GetVendorVersionToolItemSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {
			initializeAleProxy();

			try {
				ecSpecConfiguratorResult = aleProxy.getVendorVersion(new EmptyParms());
			}
			catch (ImplementationExceptionResponse e1) {
				MessageDialog.openWarning(container.getShell(), "Error", "Implementation Exception");
				e1.printStackTrace();
			}

			showECSpecConfiguratorResult(ecSpecConfiguratorResult);

		}
	}

	private class GetECSpecNamesToolItemSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {
			ecSpecConfiguratorResult = null;
			try {
				ecSpecConfiguratorResult = aleProxy.getECSpecNames(new EmptyParms());
			}
			catch (ImplementationExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Implementation Exception", e1.getMessage());
				e1.printStackTrace();

			}
			catch (SecurityExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Security Exception", e1.getMessage());
				e1.printStackTrace();
			}
			showECSpecConfiguratorResult(ecSpecConfiguratorResult);

		}
	}

	private class GetECSpecECSpecButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {
			String specName = null;
			ecSpecConfiguratorResult = null;
			specName = getECSpecECSpecListcombo.getText();

			if (specName == null || "".equals(specName)) {
				MessageDialog.openWarning(container.getShell(), "Error", "Spec Name Not Specified");
				return;
			}

			GetECSpec getECSpecParms = new GetECSpec();
			getECSpecParms.setSpecName(specName);

			try {
				try {
					ecSpecConfiguratorResult = aleProxy.getECSpec(getECSpecParms);
				}
				catch (NoSuchNameExceptionResponse e1) {
					// TODO Auto-generated catch block
					MessageDialog.openWarning(container.getShell(), "No Such Name Exception", e1.getMessage());
					e1.printStackTrace();
				}
			}
			catch (ImplementationExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Implementation Exception", e1.getMessage());
				e1.printStackTrace();

			}
			catch (SecurityExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Security Exception", e1.getMessage());
				e1.printStackTrace();
			}

			showECSpecConfiguratorResult(ecSpecConfiguratorResult);

		}
	}

	private class UndefineECSpecButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {

			String specName = null;
			ecSpecConfiguratorResult = null;
			specName = undefineECSpecListcombo.getText();

			if (specName == null || "".equals(specName)) {
				MessageDialog.openWarning(container.getShell(), "Error", "Spec Name Not Specified");
				return;
			}
			Undefine undefineParms = new Undefine();
			undefineParms.setSpecName(specName);

			try {

				ecSpecConfiguratorResult = aleProxy.undefine(undefineParms);
				MessageDialog.openConfirm(container.getShell(), "Confirmation", "The ECSpec Was Successfully Undefined");

			}
			catch (NoSuchNameExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "No Such Name Exception", e1.getMessage());
				e1.printStackTrace();
			}
			catch (ImplementationExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Implementation Exception", e1.getMessage());
				e1.printStackTrace();

			}
			catch (SecurityExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Security Exception", e1.getMessage());
				e1.printStackTrace();
			}

			showECSpecConfiguratorResult(ecSpecConfiguratorResult);

		}
	}

	private class SubscribeECSpecButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {
			String notificationURI = null;
			String specName = null;
			ecSpecConfiguratorResult = null;

			specName = subscribeECSpecListcombo.getText();
			if (specName == null || "".equals(specName)) {
				MessageDialog.openWarning(container.getShell(), "Error", "Spec Name Not Specified");
				return;
			}

			// get notificationURI
			notificationURI = notificationUriText.getText();
			if (notificationURI == null || "".equals(notificationURI)) {
				MessageDialog.openWarning(container.getShell(), "Error", "Notification URI Not Specified");
				return;
			}

			Subscribe subscribeParms = new Subscribe();
			subscribeParms.setSpecName(specName);
			subscribeParms.setNotificationURI(notificationURI);

			try {

				ecSpecConfiguratorResult = aleProxy.subscribe(subscribeParms);

				MessageDialog.openConfirm(container.getShell(), "Confirmation", "The ECSpec Was Successfully Subscribed");

			}
			catch (DuplicateSubscriptionExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Duplicate Subscription Exception", e1.getMessage());
				e1.printStackTrace();
			}
			catch (InvalidURIExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Invalid URI Exception", e1.getMessage());
				e1.printStackTrace();
			}
			catch (NoSuchNameExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "No Such Name Exception", e1.getMessage());
				e1.printStackTrace();
			}
			catch (ImplementationExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Implementation Exception", e1.getMessage());
				e1.printStackTrace();

			}
			catch (SecurityExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Security Exception", e1.getMessage());
				e1.printStackTrace();
			}

			showECSpecConfiguratorResult(ecSpecConfiguratorResult);

		}
	}

	private class UnsubscribeECSpecButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {
			String notificationURI = null;
			String specName = null;
			ecSpecConfiguratorResult = null;

			specName = unsubscribeECSpecListcombo.getText();

			if (specName == null || "".equals(specName)) {
				MessageDialog.openWarning(container.getShell(), "Error", "Spec Name Not Specified");
				return;
			}

			// get notificationURI
			notificationURI = notificationUriText.getText();
			if (notificationURI == null || "".equals(notificationURI)) {
				MessageDialog.openWarning(container.getShell(), "Error", "Notification URI Not Specified");
				return;
			}

			Unsubscribe unsubscribeParms = new Unsubscribe();
			unsubscribeParms.setSpecName(specName);
			unsubscribeParms.setNotificationURI(notificationURI);

			try {

				ecSpecConfiguratorResult = aleProxy.unsubscribe(unsubscribeParms);
				MessageDialog.openConfirm(container.getShell(), "Confirmation", "The ECSpec Was Successfully Undefined");
			}
			catch (NoSuchSubscriberExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "NoSuch Subscriber Exception", e1.getMessage());
				e1.printStackTrace();
			}

			catch (InvalidURIExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Invalid URI Exception", e1.getMessage());
				e1.printStackTrace();
			}
			catch (NoSuchNameExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "No Such Name Exception", e1.getMessage());
				e1.printStackTrace();
			}
			catch (ImplementationExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Implementation Exception", e1.getMessage());
				e1.printStackTrace();

			}
			catch (SecurityExceptionResponse e1) {
				// TODO Auto-generated catch block
				MessageDialog.openWarning(container.getShell(), "Security Exception", e1.getMessage());
				e1.printStackTrace();
			}

			showECSpecConfiguratorResult(ecSpecConfiguratorResult);
		}
	}

	private class SubscribeECSpecToolItemSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {

			setDefineECSpecCompositeVisible(false);
			setGetECSpecECSpecCompositeVisible(false);
			setUndefineECSpecCompositeVisible(false);
			setUnsubscribeECSpecCompositeVisible(false);
			setSubscribeECSpecCompositeVisible(true);

			subscribeECSpecListcombo.removeAll();
			List<String> ecSpecNames = getDefinedECSpecNames();

			if (ecSpecNames != null) {
				for (String specName : ecSpecNames) {
					subscribeECSpecListcombo.add(specName);

				}
			}
		}
	}

	private class UndefineECSpecToolItemSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {

			setDefineECSpecCompositeVisible(false);
			setSubscribeECSpecCompositeVisible(false);
			setGetECSpecECSpecCompositeVisible(false);
			setUnsubscribeECSpecCompositeVisible(false);
			setUndefineECSpecCompositeVisible(true);

			undefineECSpecListcombo.removeAll();
			List<String> ecSpecNames = getDefinedECSpecNames();

			if (ecSpecNames != null) {
				for (String specName : ecSpecNames) {
					undefineECSpecListcombo.add(specName);

				}
			}

		}
	}

	private class GetECSpecsToolItemSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {
			setDefineECSpecCompositeVisible(false);
			setSubscribeECSpecCompositeVisible(false);
			setUndefineECSpecCompositeVisible(false);
			setUnsubscribeECSpecCompositeVisible(false);
			setGetECSpecECSpecCompositeVisible(true);

			getECSpecECSpecListcombo.removeAll();
			List<String> ecSpecNames = getDefinedECSpecNames();
			if (ecSpecNames != null) {
				for (String specName : ecSpecNames) {
					getECSpecECSpecListcombo.add(specName);

				}
			}

		}
	}

	private class UnsubscribeECSpecToolItemSelectionListener extends SelectionAdapter {
		public void widgetSelected(final SelectionEvent e) {

			setDefineECSpecCompositeVisible(false);
			setGetECSpecECSpecCompositeVisible(false);
			setUndefineECSpecCompositeVisible(false);
			setSubscribeECSpecCompositeVisible(false);
			setUnsubscribeECSpecCompositeVisible(true);

			subscribeECSpecListcombo.removeAll();
			List<String> ecSpecNames = getDefinedECSpecNames();
			unsubscribeECSpecListcombo.removeAll();
			if (ecSpecNames != null) {
				for (String specName : ecSpecNames) {
					unsubscribeECSpecListcombo.add(specName);

				}
			}
		}
	}

}
