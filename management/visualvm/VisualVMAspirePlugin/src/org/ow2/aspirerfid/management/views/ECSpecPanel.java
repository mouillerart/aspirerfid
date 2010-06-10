/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ECSpecPanel.java
 *
 * Created on 22 févr. 2010, 01:17:34
 */

package org.ow2.aspirerfid.management.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.AbstractListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import org.ow2.aspirerfid.management.model.PluginModel;

/**
 *
 * @author Kiev
 */
public class ECSpecPanel extends BasePanel implements PropertyChangeListener {
    /** Creates new form ECSpecPanel */
    public ECSpecPanel() {
        initComponents();
    }

    public ECSpecPanel(PluginModel model) {
        this.model = model;
        initComponents();
        initModel();
    }

    private void initModel() {
        model.addPropertyChangeListener(this);
        this.ecspecsList.setModel(new ECSpecsModel());
        this.subscribersList.setModel(new SubscribersListModel(null));
        this.ecspecsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String[] subscribers = null;
                    String ecSpec = (String)ecspecsList.getSelectedValue();
                    if (ecSpec != null) {
                        subscribers = model.getSubscribers(ecSpec);
                    }
                    subscribersList.setModel(new SubscribersListModel(subscribers));
                }
            }
        });
    }

    class ECSpecsModel extends AbstractListModel {
            public int getSize() {
                return model.getEcSpecs().length;
            }

            public Object getElementAt(int index) {
               return model.getEcSpecs()[index];
            }
        }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PluginModel.ECSPECS_PROPERTY)) {
            this.ecspecsList.setModel(new ECSpecsModel());
        }
    }

     class SubscribersListModel extends AbstractListModel {
            private String[] subscribers = new String[0];

            public SubscribersListModel(String[] subscribers) {
                if (subscribers == null) {
                    this.subscribers = new String[0];
                } else {
                    this.subscribers = subscribers;
                }
            }

            public int getSize() {
                return subscribers.length;
            }

            public Object getElementAt(int index) {
               return subscribers[index];
            }
        }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txEcSpec = new javax.swing.JTextField();
        txECSpecFile = new javax.swing.JTextField();
        btOK = new javax.swing.JButton();
        btBrowseECSpec = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        ecspecsList = new javax.swing.JList();
        btDeleteECSpec = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        subscribersList = new javax.swing.JList();
        txSubscriberURL = new javax.swing.JTextField();
        btAddSubscriber = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btUnsubscribe = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setOpaque(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.jPanel1.border.title"))); // NOI18N
        jPanel1.setOpaque(false);

        jLabel5.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.jLabel5.text")); // NOI18N

        jLabel6.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.jLabel6.text")); // NOI18N

        txEcSpec.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.txEcSpec.text")); // NOI18N

        txECSpecFile.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.txECSpecFile.text")); // NOI18N
        txECSpecFile.setEnabled(false);

        btOK.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.btOK.text")); // NOI18N
        btOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOKActionPerformed(evt);
            }
        });

        btBrowseECSpec.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.btBrowseECSpec.text")); // NOI18N
        btBrowseECSpec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBrowseECSpecActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addComponent(btOK))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txECSpecFile, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btBrowseECSpec))
                            .addComponent(txEcSpec, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txEcSpec, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txECSpecFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBrowseECSpec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btOK))
        );

        jScrollPane2.setOpaque(false);

        ecspecsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {  };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        ecspecsList.setPreferredSize(new java.awt.Dimension(200, 210));
        jScrollPane2.setViewportView(ecspecsList);

        btDeleteECSpec.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.btDeleteECSpec.text")); // NOI18N
        btDeleteECSpec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteECSpecActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.jPanel2.border.title"))); // NOI18N
        jPanel2.setOpaque(false);

        subscribersList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(subscribersList);

        txSubscriberURL.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.txSubscriberURL.text")); // NOI18N

        btAddSubscriber.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.btAddSubscriber.text")); // NOI18N
        btAddSubscriber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddSubscriberActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.jLabel2.text")); // NOI18N

        btUnsubscribe.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.btUnsubscribe.text")); // NOI18N
        btUnsubscribe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUnsubscribeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txSubscriberURL, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btAddSubscriber)
                        .addContainerGap())))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(btUnsubscribe)
                .addContainerGap(121, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btUnsubscribe)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txSubscriberURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btAddSubscriber))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13));
        jLabel1.setText(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(btDeleteECSpec)))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btDeleteECSpec))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btDeleteECSpec.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(ECSpecPanel.class, "ECSpecPanel.jButton1.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void btOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOKActionPerformed
        if ("".equals(txEcSpec.getText().trim()) || "".equals(txECSpecFile.getText())) {
             JOptionPane.showMessageDialog(null, "Please inform the name and path of the ECSpec",MESSAGE_TITLE,JOptionPane.WARNING_MESSAGE);
        } else {
            //TODO show message if error
            model.createECSpec(txEcSpec.getText(), txECSpecFile.getText());
        }
    }//GEN-LAST:event_btOKActionPerformed

    private void btBrowseECSpecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBrowseECSpecActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
            }

            public String getDescription() {
                return ".xml files";
            }
        });
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            txECSpecFile.setText(file.getAbsolutePath());
            txECSpecFile.setToolTipText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_btBrowseECSpecActionPerformed

    private void btDeleteECSpecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteECSpecActionPerformed
        String ecspec = (String)ecspecsList.getSelectedValue();

        if (ecspec != null) {
            model.deleteECSpec(ecspec);
        } else {
            JOptionPane.showMessageDialog(null, "Please select an ECSpec to be deleted",MESSAGE_TITLE,JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btDeleteECSpecActionPerformed

    private void btAddSubscriberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddSubscriberActionPerformed
        final String ecspec = (String)ecspecsList.getSelectedValue();
        String subscriberURL = txSubscriberURL.getText();

        if (ecspec != null && !"".equals(subscriberURL.trim())) {
            model.subscribeECSpec(ecspec,subscriberURL);
            //TODO change this for notifications from ALEServer
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                     subscribersList.setModel(new SubscribersListModel(model.getSubscribers((String)ecspecsList.getSelectedValue())));
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "Please select an ECSpec to subscribe",MESSAGE_TITLE,JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btAddSubscriberActionPerformed

    private void btUnsubscribeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUnsubscribeActionPerformed
        String ecspec = (String)ecspecsList.getSelectedValue();
        String subscriberURL = (String)subscribersList.getSelectedValue();

        if (ecspec != null && !"".equals(subscriberURL.trim())) {
            model.unsubscribeECSpec(ecspec,subscriberURL);
            //TODO change this for property change. Current problem is that there is no store value for that prop which is retrieved on demand
            subscribersList.setModel(new SubscribersListModel(model.getSubscribers((String)ecspecsList.getSelectedValue())));
        } else {
            JOptionPane.showMessageDialog(null, "Please select a subscriber to remove",MESSAGE_TITLE,JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btUnsubscribeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddSubscriber;
    private javax.swing.JButton btBrowseECSpec;
    private javax.swing.JButton btDeleteECSpec;
    private javax.swing.JButton btOK;
    private javax.swing.JButton btUnsubscribe;
    private javax.swing.JList ecspecsList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList subscribersList;
    private javax.swing.JTextField txECSpecFile;
    private javax.swing.JTextField txEcSpec;
    private javax.swing.JTextField txSubscriberURL;
    // End of variables declaration//GEN-END:variables

}
