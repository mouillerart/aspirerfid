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

package org.ow2.aspirerfid.demos.warehouse.management.UI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigInteger;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;
import org.ow2.aspirerfid.demos.warehouse.connector.ConnectorManager;
import org.ow2.aspirerfid.demos.warehouse.connector.WarehouseManager;
import org.ow2.aspirerfid.demos.warehouse.management.beg.CaptureReport;
import org.ow2.aspirerfid.demos.warehouse.management.tools.DeliveryItem;
import org.ow2.aspirerfid.demos.warehouse.management.tools.Shipment;

/**
 * @author nkef (Nikos Kefalakis)
 * @author Nektarios Leontiadis
 *
 */
public class WarehouseManagement{

	private JTextField epcisRepositoryURLTextField;
	private JTextField aleListeningPortTextField;
	/**
	 * The logger.
	 */
	private static Log log = LogFactory.getLog(WarehouseManagement.class);

	private static JTextField offeringHourTextField;
	private static JTextField offeringDateTextField;
	private static JTextField entryHourTextField;
	private static JTextField entryDateTextField;
	private static JTextField userIDTextField;
	private static JTextField zoneIDTextField;
	private static JTextField warehouseIDTextField;
	private static JTextField invoiceIDTextField;
	private JFrame frame;
	private JTable deliveryTable;
	private static DefaultTableModel deliveryTableModel;
	int selectedRow;
	CaptureReport captureReport = null;

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
		    
			WarehouseManagement window = new WarehouseManagement();
			connectorClient = new WarehouseManager();
			window.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the application
	 */
	public WarehouseManagement() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame
	 */
	private void initialize() {

		final JLabel entryDateLabel;
		final JLabel entryDateLabel_1;
		final JLabel entryDateLabel_2;
		final JLabel entryDateLabel_3;
		final JLabel entryDateLabel_3_1;
		final JLabel entryDateLabel_3_2;
		final JLabel entryDateLabel_3_1_1;
		final JLabel entryDateLabel_3_1_2;
		final JScrollPane scrollPane;
		final JButton printReportButton;
		final JButton saveReportButton;
		final JButton activateDoorButton;
		final JButton deactivateDoorButton;
		final JButton clearReportButton;
		final JPanel panel;
		final JLabel entryDateLabel_3_3;
		final JLabel entryDateLabel_2_1;
		frame = new JFrame();
		frame.addWindowListener(new FrameWindowListener());
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setTitle("Warehouse Management");
		frame.setBounds(100, 100, 1011, 625);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabbedPane = new JTabbedPane();
		frame.getContentPane().add(tabbedPane);

		deliveryPanel = new JPanel();
		deliveryPanel.setLayout(null);
		tabbedPane.addTab("Delivery Counter", null, deliveryPanel, null);

		entryDateLabel = new JLabel();
		entryDateLabel.setText("Entry Date .........");
		entryDateLabel.setBounds(533, 23, 117, 16);
		deliveryPanel.add(entryDateLabel);

		entryDateLabel_1 = new JLabel();
		entryDateLabel_1.setText("User ID ................");
		entryDateLabel_1.setBounds(10, 94, 117, 16);
		deliveryPanel.add(entryDateLabel_1);

		entryDateLabel_2 = new JLabel();
		entryDateLabel_2.setText("Invoice ID ............");
		entryDateLabel_2.setBounds(10, 23, 117, 16);
		deliveryPanel.add(entryDateLabel_2);

		entryDateLabel_3 = new JLabel();
		entryDateLabel_3.setText("Warehouse ID....");
		entryDateLabel_3.setBounds(10, 45, 117, 16);
		deliveryPanel.add(entryDateLabel_3);

		entryDateLabel_3_1 = new JLabel();
		entryDateLabel_3_1.setText("Zone ID ................");
		entryDateLabel_3_1.setBounds(10, 67, 117, 16);
		deliveryPanel.add(entryDateLabel_3_1);

		entryDateLabel_3_2 = new JLabel();
		entryDateLabel_3_2.setText("Entry Hour .........");
		entryDateLabel_3_2.setBounds(533, 45, 117, 16);
		deliveryPanel.add(entryDateLabel_3_2);

		entryDateLabel_3_1_1 = new JLabel();
		entryDateLabel_3_1_1.setText("Offering Date ....");
		entryDateLabel_3_1_1.setBounds(533, 70, 117, 16);
		deliveryPanel.add(entryDateLabel_3_1_1);

		entryDateLabel_3_1_2 = new JLabel();
		entryDateLabel_3_1_2.setText("Offering Hour ....");
		entryDateLabel_3_1_2.setBounds(533, 94, 117, 16);
		deliveryPanel.add(entryDateLabel_3_1_2);

		invoiceIDTextField = new JTextField();
		invoiceIDTextField.setBounds(105, 19, 365, 20);
		deliveryPanel.add(invoiceIDTextField);

		warehouseIDTextField = new JTextField();
		warehouseIDTextField.setBounds(105, 43, 365, 20);
		deliveryPanel.add(warehouseIDTextField);

		zoneIDTextField = new JTextField();
		zoneIDTextField.setBounds(105, 66, 365, 20);
		deliveryPanel.add(zoneIDTextField);

		userIDTextField = new JTextField();
		userIDTextField.setBounds(105, 90, 365, 20);
		deliveryPanel.add(userIDTextField);

		entryDateTextField = new JTextField();
		entryDateTextField.setBounds(623, 19, 365, 20);
		deliveryPanel.add(entryDateTextField);

		entryHourTextField = new JTextField();
		entryHourTextField.setBounds(623, 41, 365, 20);
		deliveryPanel.add(entryHourTextField);

		offeringDateTextField = new JTextField();
		offeringDateTextField.setBounds(623, 66, 365, 20);
		deliveryPanel.add(offeringDateTextField);

		offeringHourTextField = new JTextField();
		offeringHourTextField.setBounds(623, 90, 365, 20);
		deliveryPanel.add(offeringHourTextField);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 129, 978, 355);
		deliveryPanel.add(scrollPane);

		deliveryTableModel = new DefaultTableModel();// All Clients Items
		deliveryTableModel.addColumn("Company");
		deliveryTableModel.addColumn("Item Code");
		deliveryTableModel.addColumn("Description");
		deliveryTableModel.addColumn("Quantity Delivered");
		deliveryTableModel.addColumn("Expected Quantity");
		deliveryTableModel.addColumn("Quantity Remain");
		deliveryTableModel.addColumn("Delivery Date");
		deliveryTableModel.addColumn("Measurement ID");
		deliveryTableModel.addColumn("Quantity");
		deliveryTable = new JTable(deliveryTableModel);
		deliveryTable.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		deliveryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setViewportView(deliveryTable);

		ListSelectionModel rowSM = deliveryTable.getSelectionModel();

		printReportButton = new JButton();
		printReportButton.setText("Print Report");
		printReportButton.setBounds(860, 520, 117, 26);
		deliveryPanel.add(printReportButton);

		saveReportButton = new JButton();
		saveReportButton.setText("Save Report");
		saveReportButton.setBounds(614, 520, 117, 26);
		deliveryPanel.add(saveReportButton);

		activateDoorButton = new JButton();
		activateDoorButton
				.addMouseListener(new ActivateDoorButtonMouseListener());
		activateDoorButton.setText("Activate Door");
		activateDoorButton.setBounds(50, 520, 117, 26);
		deliveryPanel.add(activateDoorButton);

		deactivateDoorButton = new JButton();
		deactivateDoorButton
				.addMouseListener(new DeactivateDoorButtonMouseListener());
		deactivateDoorButton.setText("Dectivate Door");
		deactivateDoorButton.setBounds(173, 520, 117, 26);
		deliveryPanel.add(deactivateDoorButton);

		clearReportButton = new JButton();
		clearReportButton
				.addMouseListener(new ClearReportButtonMouseListener());
		clearReportButton.setText("Clear Report");
		clearReportButton.setBounds(737, 520, 117, 26);
		deliveryPanel.add(clearReportButton);
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// Ignore extra messages.
				if (e.getValueIsAdjusting())
					return;

				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (lsm.isSelectionEmpty()) {
					// no rows are selected
				} else {
					selectedRow = lsm.getMinSelectionIndex();
					System.out.println("selectedRow = " + selectedRow);

				}
			}
		});

		shipmentPanel = new JPanel();
		shipmentPanel.setLayout(null);
		tabbedPane.addTab("Shipment", null, shipmentPanel, null);

		final JLabel shipmentGid96Label = new JLabel();
		shipmentGid96Label.setText("Shipment GID-96");
		shipmentGid96Label.setBounds(58, 189, 111, 15);
		shipmentPanel.add(shipmentGid96Label);

		tfGid = new JTextField();
		tfGid.setText("145.56");
		tfGid.setBounds(192, 187, 331, 19);
		shipmentPanel.add(tfGid);

		fromLabel = new JLabel();
		fromLabel.setText("From");
		fromLabel.setBounds(58, 108, 69, 15);
		shipmentPanel.add(fromLabel);

		final JLabel descriptionLabel = new JLabel();
		descriptionLabel.setText("Description");
		descriptionLabel.setBounds(58, 135, 75, 15);
		shipmentPanel.add(descriptionLabel);

		final JLabel quantityLabel = new JLabel();
		quantityLabel.setText("Quantity");
		quantityLabel.setBounds(58, 164, 69, 15);
		shipmentPanel.add(quantityLabel);

		tfFrom = new JTextField();
		tfFrom.setText("ALTERA");
		tfFrom.setBounds(192, 106, 331, 19);
		shipmentPanel.add(tfFrom);

		tfDescription = new JTextField();
		tfDescription.setText("Altera Microcontroler 8081");
		tfDescription.setBounds(192, 133, 331, 19);
		shipmentPanel.add(tfDescription);

		tfQuantity = new JTextField();
		tfQuantity.setText("4");
		tfQuantity.setBounds(192, 162, 331, 19);
		shipmentPanel.add(tfQuantity);

		final JLabel fromLabel_1 = new JLabel();
		fromLabel_1.setText("Invoice GID-96");
		fromLabel_1.setBounds(58, 83, 95, 15);
		shipmentPanel.add(fromLabel_1);

		tfInvoiceGid = new JTextField();
		tfInvoiceGid.setText("145.12");
		tfInvoiceGid.setBounds(192, 81, 331, 19);
		shipmentPanel.add(tfInvoiceGid);

		final JButton submitShipmentButton = new JButton();
		submitShipmentButton.addActionListener(new SubmitShipmentButtonActionListener());
		
		
		submitShipmentButton.setText("Submit");
		submitShipmentButton.setBounds(103, 254, 112, 25);
		shipmentPanel.add(submitShipmentButton);

		final JButton clearButton = new JButton();
		clearButton.setText("Clear");
		clearButton.setBounds(269, 254, 112, 25);
		shipmentPanel.add(clearButton);

		panel = new JPanel();
		panel.setLayout(null);
		tabbedPane.addTab("Door Config", null, panel, null);

		aleListeningPortTextField = new JTextField();
		aleListeningPortTextField.setText("9999");
		aleListeningPortTextField.setBounds(206, 42, 330, 20);
		panel.add(aleListeningPortTextField);

		epcisRepositoryURLTextField = new JTextField();
		epcisRepositoryURLTextField
				.setText("http://localhost:8080/aspire0.3.0EpcisRepository/capture");
		epcisRepositoryURLTextField.setBounds(206, 68, 330, 20);
		panel.add(epcisRepositoryURLTextField);

		entryDateLabel_3_3 = new JLabel();
		entryDateLabel_3_3.setText("EPCIS Rep. URL .........");
		entryDateLabel_3_3.setBounds(51, 67, 125, 15);
		panel.add(entryDateLabel_3_3);

		entryDateLabel_2_1 = new JLabel();
		entryDateLabel_2_1.setText("ALE Listening Port ....");
		entryDateLabel_2_1.setBounds(51, 43, 130, 15);
		panel.add(entryDateLabel_2_1);

		final JLabel epcisQueryUrlLabel = new JLabel();
		epcisQueryUrlLabel.setText("EPCIS query URL");
		epcisQueryUrlLabel.setBounds(51, 96, 105, 15);
		panel.add(epcisQueryUrlLabel);

		final JTextField queryUrl = new JTextField();
		queryUrl.setText("http://localhost:8080/epcis/query");
		queryUrl.setBounds(206, 94, 330, 19);
		panel.add(queryUrl);

	}

	/**
	 * @param String
	 *            invoiceID
	 */
	public static void setInvoiceIDTextField(String invoiceID) {
		invoiceIDTextField.setText(invoiceID);
	}

	/**
	 * @param String
	 *            warehouseID
	 */
	public static void setWarehouseIDTextField(String warehouseID) {
		warehouseIDTextField.setText(warehouseID);
	}

	/**
	 * @param String
	 *            zoneID
	 */
	public static void setZoneIDTextField(String zoneID) {
		zoneIDTextField.setText(zoneID);
	}

	/**
	 * @param String
	 *            userID
	 */
	public static void setUserIDTextField(String userID) {
		userIDTextField.setText(userID);
	}

	/**
	 * @param String
	 *            entryDate
	 */
	public static void setEntryDateTextField(String entryDate) {
		entryDateTextField.setText(entryDate);
	}

	/**
	 * @param String
	 *            entryHour
	 */
	public static void setEntryHourTextField(String entryHour) {
		entryHourTextField.setText(entryHour);
	}

	/**
	 * @param String
	 *            offeringDate
	 */
	public static void setOfferingDateTextField(String offeringDate) {
		offeringDateTextField.setText(offeringDate);
	}

	/**
	 * @param String
	 *            offeringHour
	 */
	public static void setOfferingHourTextField(String offeringHour) {
		offeringHourTextField.setText(offeringHour);
	}

	/**
	 * @return entryDateTextField
	 */
	public static String getEntryDateTextField() {
		return entryDateTextField.getText();
	}

	/**
	 * @return entryHourTextField
	 */
	public static String getEntryHourTextField() {
		return entryHourTextField.getText();
	}

	/**
	 * @param DeliveryItem
	 *            deliveredItem
	 */
	public static void updateDeliveryTableModel(DeliveryItem deliveredItem) {

		// deliveryTableModel.setNumRows(0);
		if (deliveryTableModel.getRowCount()==0){
			deliveryTableModel.addRow(new Object[] {
					deliveredItem.getCompany(),
					deliveredItem.getItemCode(),
					deliveredItem.getDescription(),
					deliveredItem.getQuantityDelivered(),
					deliveredItem.getExpectedQuantity(),
					deliveredItem.getQuantityRemain(),
					deliveredItem.getDeliveryDate(),
					deliveredItem.getMeasurementID(),
					deliveredItem.getQuantity() });
		}
		boolean included = false;
		for (int i = 0; i < deliveryTableModel.getRowCount(); i++) {
			if (deliveryTableModel.getValueAt(i, 1).equals(deliveredItem.getItemCode())) {
				included = true;
				deliveryTableModel.setValueAt(deliveredItem.getQuantityDelivered(), i, 3);
				deliveryTableModel.setValueAt(deliveredItem.getExpectedQuantity(), i, 4);
				deliveryTableModel.setValueAt(deliveredItem.getQuantityRemain(), i, 5);
				break;
			}
		}
			if(!included){
			deliveryTableModel.addRow(new Object[] {
					deliveredItem.getCompany(),
					deliveredItem.getItemCode(),
					deliveredItem.getDescription(),
					deliveredItem.getQuantityDelivered(),
					deliveredItem.getExpectedQuantity(),
					deliveredItem.getQuantityRemain(),
					deliveredItem.getDeliveryDate(),
					deliveredItem.getMeasurementID(),
					deliveredItem.getQuantity() });
			}
			
		
	}

	private class ActivateDoorButtonMouseListener extends MouseAdapter {
		public void mouseClicked(final MouseEvent arg0) {
			// ========Start Capture Application============

			URL log4jURL = this.getClass().getResource("/log4j.xml");
			DOMConfigurator.configure(log4jURL);
			log
					.debug("*******************Door Activated*********************\n");
			int port;
			String epcisRepository;
			if (!((aleListeningPortTextField.getText().equals("")) || (epcisRepositoryURLTextField
					.getText().equals("")))) {
				port = Integer.parseInt(aleListeningPortTextField.getText());
				epcisRepository = epcisRepositoryURLTextField.getText();
				log.debug("EPCIS REPOSITORY URL:"+ epcisRepositoryURLTextField.getText());
				log.debug("ALE Report Port:"+ aleListeningPortTextField.getText() + "\n");
				captureReport = new CaptureReport(port, epcisRepository);
				CaptureReport.setActivated(true);

			} else {
				return;
			}

			captureReport.start();

			// =============================================

		}
	}

	private class DeactivateDoorButtonMouseListener extends MouseAdapter {
		public void mouseClicked(final MouseEvent e) {
			CaptureReport.setActivated(false);

		}
	}

	private class ClearReportButtonMouseListener extends MouseAdapter {
		public void mouseClicked(final MouseEvent e) {
			deliveryTableModel.setNumRows(0);
			offeringHourTextField.setText("");
			offeringDateTextField.setText("");
			entryHourTextField.setText("");
			entryDateTextField.setText("");
			userIDTextField.setText("");
			zoneIDTextField.setText("");
			warehouseIDTextField.setText("");
			invoiceIDTextField.setText("");

		}
	}
	
	private class SubmitShipmentButtonActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
		    Shipment s = createShipment();
		    if(s == null)
			return;
		    
		    if(connectorClient.addShipmentInfo(s))
		    {
			//TODO Auto change tab here
		    }
		}
	}

	private class FrameWindowListener extends WindowAdapter {
		public void windowClosing(final WindowEvent arg0) {
		    ConnectorManager.getInstance().cancelAllSubscriptions();
		}
	}
	
	private Shipment createShipment()
	{
	    String empty = "";
	    String gids = tfGid.getText().trim();
	    String from = tfFrom.getText().trim();
	    String description = tfDescription.getText().trim();
	    String invoice = tfInvoiceGid.getText().trim();
	    String [] quantityStr;
	    int [] quantity;
	    String [] items;
	    DeliveryItem item;
	    
	    if(gids.equals(empty) || from.equals(empty) || description.equals(empty) || invoice.equals(empty))
	    {
		log.error("Shipment parameters not defined");
		return null;
	    }
	    try {
		quantityStr = tfQuantity.getText().trim().split(",");
		quantity = new int[quantityStr.length];
		for(int i=0; i<quantityStr.length; i++)
		{
		    quantity[i] = Integer.parseInt(quantityStr[i]);
		}
	    }catch (Exception e)
	    {
		log.error("Quantities should be defined as comma separated numerical values");
		return null;
	    }
	    items = gids.split(",");
	    Shipment s = new Shipment(tfInvoiceGid.getText().trim());
	    
	    for(int i=0; i<quantity.length; i++)
	    {
		item = new DeliveryItem();
		item.setCompany(from);
		item.setDescription(description);
		item.setExpectedQuantity(BigInteger.valueOf(quantity[i]));
		item.setItemCode(items[i]);
		s.addItem(item);
	    }
	    
	    return s;
	}

	private static WarehouseManager connectorClient;
	
	private JTextField tfInvoiceGid;
	private JTextField tfGid;
	private JLabel fromLabel;
	private JTextField tfQuantity;
	private JTextField tfFrom;
	private JTextField tfDescription;
	private static JTabbedPane tabbedPane;
	private static JPanel deliveryPanel;
	private static JPanel shipmentPanel;
}
