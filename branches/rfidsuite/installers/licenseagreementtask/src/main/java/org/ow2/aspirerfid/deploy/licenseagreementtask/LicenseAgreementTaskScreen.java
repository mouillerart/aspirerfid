/*
   Copyright 2005-2008, OW2 Aspire RFID project 
   
   This library is free software; you can redistribute it and/or modify it 
   under the terms of the GNU Lesser General Public License as published by 
   the Free Software Foundation (the "LGPL"); either version 2.1 of the 
   License, or (at your option) any later version. If you do not alter this 
   notice, a recipient may use your version of this file under either the 
   LGPL version 2.1, or (at his option) any later version.
   
   You should have received a copy of the GNU Lesser General Public License 
   along with this library; if not, write to the Free Software Foundation, 
   Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
   
   This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
   KIND, either express or implied. See the GNU Lesser General Public 
   License for the specific language governing rights and limitations.

   Contact: OW2 Aspire RFID project <X AT Y DOT org> (with X=aspirerfid and Y=ow2)

   LGPL version 2.1 full text http://www.gnu.org/licenses/lgpl-2.1.txt    
*/
package org.ow2.aspirerfid.deploy.licenseagreementtask;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JWindow;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;

/**
 * This class displays a license agreement screen
 * @author Didier Donsez
 */

public class LicenseAgreementTaskScreen extends LicenseAgreementScreen
	implements ActionListener , BuildListener
	{
    private static final int FONT_SIZE = 12;
    private JTextArea textarea;
    private JProgressBar progressBar;
	private LicenseAgreementTask licenseAgreementTask;
	
    private int total;
    private static final int MIN = 0;
    private static final int MAX = 200;

    public LicenseAgreementTaskScreen(ImageIcon img, String licenseText, boolean requireAgreement, final LicenseAgreementTask licenseAgreementTask) {

    	super(img, licenseText, requireAgreement, licenseAgreementTask);
    	
    	this.licenseAgreementTask=licenseAgreementTask;
    	
        JPanel panel1 = (JPanel) getContentPane();

        JLabel imageLabel = new JLabel(img);

        imageLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        textarea =new JTextArea(licenseText);
        textarea.setAlignmentX(JTextArea.LEFT_ALIGNMENT);
        textarea.setFont(new Font("Sans-Serif", Font.BOLD, FONT_SIZE));
        textarea.setBorder(BorderFactory.createEtchedBorder());

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());

        panel2.add(textarea, BorderLayout.NORTH);
        
        if(! requireAgreement){
	        progressBar = new JProgressBar(MIN, MAX);
	        progressBar.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
	        panel2.add(progressBar, BorderLayout.SOUTH);
        } else {
        	
        	JButton agree=new JButton("I agree");
        	agree.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent actionevent) {
					licenseAgreementTask.agree();
				}
        	});
        	
        	JButton dontagree=new JButton("I do not agree");
        	dontagree.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent actionevent) {
					licenseAgreementTask.dontagree();
				}
        	});
        	
            JPanel panel3 = new JPanel();
            panel3.setLayout(new FlowLayout());
        	panel3.add(agree);
        	panel3.add(dontagree);
	        panel2.add(panel3, BorderLayout.SOUTH);
        }
       
        panel1.setLayout(new BorderLayout());
        panel1.add(imageLabel, BorderLayout.CENTER);
        panel1.add(panel2, BorderLayout.SOUTH);

        panel1.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        pack();

        // center on the user screen
        Dimension size = getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - size.width) / 2;
        int y = (screenSize.height - size.height) / 2;
        setBounds(x, y, size.width, size.height);
    }

    public void actionPerformed(ActionEvent a) {
        if (total < MAX) {
            total++;
        } else {
            total = MIN;
        }
        progressBar.setValue(total);
    }

    
    public void buildStarted(BuildEvent event) {
        actionPerformed(null);
    }

    public void buildFinished(BuildEvent event) {
        actionPerformed(null);
    }
    
    /**
     * end the task when the target is started
     */
    public void targetStarted(BuildEvent event) {
    	if(event.getTarget().getName().equals(licenseAgreementTask.getOnStartedTarget())){
    		licenseAgreementTask.end();
    	} else {
            actionPerformed(null);
    	}
    }

    /**
     * end the task when the target is finished
     */
    public void targetFinished(BuildEvent event) {
    	if(event.getTarget().getName().equals(licenseAgreementTask.getOnFinishedTarget())){
    		licenseAgreementTask.end();
    	} else {
            actionPerformed(null);
    	}
    }

    public void taskStarted(BuildEvent event) {
        actionPerformed(null);
    }

    public void taskFinished(BuildEvent event) {
        actionPerformed(null);
    }

    public void messageLogged(BuildEvent event) {
        actionPerformed(null);
    }
    
}

