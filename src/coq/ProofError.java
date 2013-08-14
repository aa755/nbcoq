/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Highlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.openide.util.Exceptions;


/**
 *
 * @author Abhishek
 */
public class ProofError extends javax.swing.JPanel implements ListSelectionListener{

    private cqDataObject editorDoc;
    private final DefaultTreeModel model;
    nu.xom.Elements allGoals;
    ProofSubgoal curGoal;
    private final JPanel proofRootPanel;
    SubGoalListModel slist;
    
    final SubGoalListModel assignListModel()
    {
        slist=new SubGoalListModel();
        return slist;
    }
    /**
     * Creates new form ProofError
     */
    public ProofError() {
        initComponents();
        editorDoc=null;
        model=new DefaultTreeModel(null);
        jTree1.setModel(model);
        proofRootPanel=new JPanel();
        BoxLayout bl=new BoxLayout(proofRootPanel, BoxLayout.Y_AXIS);
        proofScroll.getViewport().add(proofRootPanel);
        proofScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        proofRootPanel.setLayout(bl);
        subGoalsList.getSelectionModel().addListSelectionListener(this);
        //proofRootPanel.setMaximumSize(new Dimension(this.getWidth(),Integer.MAX_VALUE ));
    }

    void validateScroll()
    {
        proofScroll.validate();
    }
    
    void setDebugMesg(String mesg)
    {
        jTextArea1.setText(mesg);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        goToCursorButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        followCompile = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jSplitPane1 = new javax.swing.JSplitPane();
        proofScroll = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        goalOption = new javax.swing.JRadioButton();
        jumpButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        topButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        subGoalsList = new javax.swing.JList();
        queryField = new javax.swing.JTextField();
        queryExecuteButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        org.openide.awt.Mnemonics.setLocalizedText(goToCursorButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.goToCursorButton.text")); // NOI18N
        goToCursorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToCursorButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(downButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.downButton.text")); // NOI18N
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(followCompile, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.followCompile.text")); // NOI18N
        followCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                followCompileActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jTree1);

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setLeftComponent(proofScroll);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(23, 100));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setMinimumSize(new java.awt.Dimension(4, 100));
        jScrollPane1.setViewportView(jTextArea1);

        jSplitPane1.setRightComponent(jScrollPane1);

        goalOption.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(goalOption, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.goalOption.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jumpButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.jumpButton.text")); // NOI18N
        jumpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumpButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(upButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.upButton.text")); // NOI18N
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(topButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.topButton.text")); // NOI18N
        topButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                topButtonActionPerformed(evt);
            }
        });

        subGoalsList.setModel(assignListModel());
        jScrollPane3.setViewportView(subGoalsList);

        queryField.setText(org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.queryField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(queryExecuteButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.queryExecuteButton.text")); // NOI18N
        queryExecuteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queryExecuteButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.jLabel1.text")); // NOI18N

        jTextField1.setText(org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.jTextField1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(goToCursorButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(downButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(upButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(topButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jumpButton)
                                .addGap(21, 21, 21))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(goalOption)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(queryField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(queryExecuteButton)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(jLabel1))
                                    .addComponent(followCompile)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jumpButton, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(goToCursorButton)
                                .addComponent(downButton)
                                .addComponent(upButton)
                                .addComponent(topButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(goalOption)
                            .addComponent(queryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(queryExecuteButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(followCompile, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void goToCursorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToCursorButtonActionPerformed
        // TODO add your handling code here:
        if(editorDoc==null)
        {
            setDebugMesg("upButton: no coq document is associated w/ this window. this could be a bug");
        }
       disableCompileButtons();
       editorDoc.handleDownToCursor();
        
    }//GEN-LAST:event_goToCursorButtonActionPerformed

    void disableCompileButtons()
    {
        downButton.setEnabled(false);
        goToCursorButton.setEnabled(false);
    }
    
    public void enableCompileButtonsAndShowDbug()
    {
       downButton.setEnabled(true);
       goToCursorButton.setEnabled(true);
       jTextArea1.setText(editorDoc.getDbugcontents());   
     //  Highlighter h=jTextArea1.getHighlighter();
    }
    
    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        // TODO add your handling code here:
        if(editorDoc==null)
        {
            setDebugMesg("downButton: no coq document is associated w/ this window. this could be a bug");
            
        }
        
       disableCompileButtons();
       editorDoc.handleDownButton();
    }//GEN-LAST:event_downButtonActionPerformed

    private void followCompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_followCompileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_followCompileActionPerformed

    private void jumpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumpButtonActionPerformed
        // TODO add your handling code here:
        editorDoc.jumpToCompileOffest();
    }//GEN-LAST:event_jumpButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        // TODO add your handling code here:
        editorDoc.handleUpButton();
    }//GEN-LAST:event_upButtonActionPerformed

    private void topButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_topButtonActionPerformed
        // TODO add your handling code here:
        editorDoc.handleUppButton();
    }//GEN-LAST:event_topButtonActionPerformed

    private void queryExecuteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryExecuteButtonActionPerformed
        // TODO add your handling code here:
        editorDoc.handleQuery();
    }//GEN-LAST:event_queryExecuteButtonActionPerformed

    public String getQuery()
    {
        return queryField.getText();
    }
    public DefaultMutableTreeNode buildnu(nu.xom.Element e) {   
   DefaultMutableTreeNode result = new DefaultMutableTreeNode(""+e.toXML());
  
    nu.xom.Elements children=e.getChildElements();
    
   for(int i=0;i< children.size();i++) {
      nu.xom.Element child =  children.get(i);
      result.add(buildnu(child));
   }
   return result;         
}

public void resetTree(nu.xom.Document doc) throws Exception {
    if(doc==null)
        return;
       DefaultMutableTreeNode root=buildnu(doc.getRootElement());
      model.setRoot(root);
       System.out.flush();
      model.reload();
      model.nodeStructureChanged((TreeNode) model.getRoot());
             
}
void setAllGoals(nu.xom.Document prg)
{
   
    allGoals=null;
    if(prg==null)
        return;
//    nu.xom.Element root=prg.getRootElement();
    nu.xom.Element option=prg.getRootElement().getFirstChildElement("option");
    if(option==null)
        return;
    nu.xom.Element goals =option.getFirstChildElement("goals");
    if(goals==null)
        return;
    nu.xom.Element list=goals.getFirstChildElement("list");
    allGoals=list.getChildElements("goal");    
}


void displayGoal(int index)
{
     proofRootPanel.removeAll();
         slist.setValues(allGoals);

    if(allGoals==null || allGoals.size()==0)
    {
        proofRootPanel.validate();
        proofRootPanel.repaint();
        return;
    }
    curGoal=new ProofSubgoal(allGoals.get(index));
    int maxWidth=proofScroll.getViewport().getWidth();
    Dimension dim=proofRootPanel.getMaximumSize();
    dim.width=maxWidth;
    proofRootPanel.setMaximumSize(dim);
    JPanel concl=curGoal.showSubgoal(proofRootPanel,maxWidth);
    validateScroll();
  //  proofRootPanel.revalidate();
//    proofRootPanel.repaint();
//    JScrollBar vs=proofScroll.getVerticalScrollBar();
//    vs.setValue(vs.getMaximum()-vs.getVisibleAmount()-1);
//    proofScroll.scrollRectToVisible(concl.getBounds());
    proofRootPanel.repaint();
}

public boolean isShowGoalChecked()
{
    return goalOption.isSelected();
}
public boolean isFollowCursorChecked()
{
    return followCompile.isSelected();
}

void showGoal()
{
try {
            //jTextArea1.setText(editorDoc.getGoal());
            //resetTree(editorDoc.getGoal());
            setAllGoals(editorDoc.getGoal());
            displayGoal(0);
          // JScrollBar vscr=proofScroll.getVerticalScrollBar();
           //vscr.setValue(vscr.getMaximum()-vscr.getVisibleAmount());
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }    
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton downButton;
    private javax.swing.JRadioButton followCompile;
    private javax.swing.JButton goToCursorButton;
    private javax.swing.JRadioButton goalOption;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton jumpButton;
    private javax.swing.JScrollPane proofScroll;
    private javax.swing.JButton queryExecuteButton;
    private javax.swing.JTextField queryField;
    private javax.swing.JList subGoalsList;
    private javax.swing.JButton topButton;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

    /**
     * @param editorDoc the editorDoc to set
     */
    public void setEditorDoc(cqDataObject editorDoc) {
        this.editorDoc = editorDoc;
        editorDoc.setUiWindow(this);
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        displayGoal(subGoalsList.getSelectedIndex());
    }
}
