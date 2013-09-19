/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
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
    private Matcher regm;
    public static final boolean DARK=true;
    public static final Color DarkBack=Color.BLACK;
    public static final Color DarkFore=Color.GREEN;
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
        if(DARK)
        {
            jTextArea1.setBackground(DarkBack);
            jTextArea1.setForeground(DarkFore);
        }
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
        queryRegexp = new javax.swing.JTextField();
        highlightButton = new javax.swing.JButton();
        nextHButton = new javax.swing.JButton();
        prevHButton = new javax.swing.JButton();
        bottomButton = new javax.swing.JButton();
        queryCombo = new javax.swing.JComboBox();
        stopButton = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(goToCursorButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.goToCursorButton.text")); // NOI18N
        goToCursorButton.setToolTipText(org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.goToCursorButton.toolTipText")); // NOI18N
        goToCursorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToCursorButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(downButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.downButton.text")); // NOI18N
        downButton.setToolTipText(org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.downButton.toolTipText")); // NOI18N
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
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
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

        queryRegexp.setText(org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.queryRegexp.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(highlightButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.highlightButton.text")); // NOI18N
        highlightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(nextHButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.nextHButton.text")); // NOI18N
        nextHButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextHButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(prevHButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.prevHButton.text")); // NOI18N
        prevHButton.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(bottomButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.bottomButton.text")); // NOI18N
        bottomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bottomButtonActionPerformed(evt);
            }
        });

        queryCombo.setEditable(true);
        queryCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Print nat." }));
        queryCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queryComboActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(stopButton, org.openide.util.NbBundle.getMessage(ProofError.class, "ProofError.stopButton.text")); // NOI18N
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bottomButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(topButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jumpButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(highlightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(prevHButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nextHButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(followCompile, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(goalOption)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(queryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stopButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(queryRegexp, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 34, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(goToCursorButton)
                            .addComponent(downButton)
                            .addComponent(upButton)
                            .addComponent(topButton)
                            .addComponent(bottomButton)
                            .addComponent(jumpButton)
                            .addComponent(highlightButton)
                            .addComponent(prevHButton)
                            .addComponent(nextHButton)
                            .addComponent(followCompile, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(goalOption)
                                .addComponent(queryRegexp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(queryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(stopButton)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
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
        bottomButton.setEnabled(false);
    }
    
    public void enableCompileButtonsAndShowDbug()
    {
       downButton.setEnabled(true);
       goToCursorButton.setEnabled(true);
       bottomButton.setEnabled(true);
       jTextArea1.setText(editorDoc.getDbugcontents());
       regm=null;
       highlightButton.setText("H");
       if(isFollowCursorChecked())
            editorDoc.jumpToCompileOffest();
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

    void processQuery()
    {
        String text=getQuery();
        if(text==null||text.isEmpty())
        {
            setQuery("first enter query here");
            return;
        }
        if(!text.trim().endsWith("."))
        {
            setQuery(text+".");
        }
        editorDoc.handleQuery();

    }   
    
    public String getQuery()
    {
        //return queryField.getText();
        return (String) queryCombo.getSelectedItem();
    }

    void setQuery(String st)
    {
        queryCombo.setSelectedItem(st);
//        queryField.setText(st);
    }
    
    /**
     * saves a query in the dropdown list
     * @param s : a (successful) query
     */
    void saveQuery(String s)
    {
//        queryCombo.removeItem(s); // to avoid duplicates
//        if(queryCombo)
        queryCombo.insertItemAt(s, 0);
    }
    
    private void highlightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightButtonActionPerformed
       String regexp=queryRegexp.getText();
       if(!regexp.isEmpty())
       {
           Highlighter h=jTextArea1.getHighlighter();
           h.removeAllHighlights();
           Pattern search=Pattern.compile(regexp);
           regm = search.matcher(editorDoc.getDbugcontents());
           int count=0;
           while(regm.find())
           {
               try {
                   h.addHighlight(regm.start(), regm.end(), DefaultHighlighter.DefaultPainter );
               } catch (BadLocationException ex) {
                   Exceptions.printStackTrace(ex);
               }
               count++;
           }
        
           regm.reset();
           highlightButton.setText(""+count);
       }
        // TODO add your handling code here:
    }//GEN-LAST:event_highlightButtonActionPerformed

    private void nextHButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextHButtonActionPerformed
        // TODO add your handling code here:
        
        if(regm!=null && regm.find())
        {
            jTextArea1.setCaretPosition(regm.start());
        }
        if(regm.hitEnd())
            regm.reset();
    }//GEN-LAST:event_nextHButtonActionPerformed

    private void bottomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bottomButtonActionPerformed
        // TODO add your handling code here:
        editorDoc.handleBottomButton();
        disableCompileButtons();
    }//GEN-LAST:event_bottomButtonActionPerformed

    private void queryComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryComboActionPerformed
        // TODO add your handling code here:
        processQuery();
    }//GEN-LAST:event_queryComboActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        // TODO add your handling code here:
        editorDoc.stopRequest();
    }//GEN-LAST:event_stopButtonActionPerformed

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
     subGoalsList.ensureIndexIsVisible(slist.getMaxIndex());
  
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
    JPanel concl=curGoal.showSubgoal(proofRootPanel,editorDoc, maxWidth);
    validateScroll();
  //  proofRootPanel.revalidate();
//    proofRootPanel.repaint();
//    JScrollBar vs=proofScroll.getVerticalScrollBar();
//    vs.setValue(vs.getMaximum()-vs.getVisibleAmount()-1);
//    proofScroll.scrollRectToVisible(concl.getBounds());
    proofRootPanel.repaint();
    //https://forums.oracle.com/thread/2414508
    SwingUtilities.invokeLater(new Runnable() {
     @Override
     public void run() {
          proofScroll.getVerticalScrollBar().setValue(0);
     }
});
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
    private javax.swing.JButton bottomButton;
    private javax.swing.JButton downButton;
    private javax.swing.JRadioButton followCompile;
    private javax.swing.JButton goToCursorButton;
    private javax.swing.JRadioButton goalOption;
    private javax.swing.JButton highlightButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton jumpButton;
    private javax.swing.JButton nextHButton;
    private javax.swing.JButton prevHButton;
    private javax.swing.JScrollPane proofScroll;
    private javax.swing.JComboBox queryCombo;
    private javax.swing.JTextField queryRegexp;
    private javax.swing.JButton stopButton;
    private javax.swing.JList subGoalsList;
    private javax.swing.JButton topButton;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

    /**
     * @param editorDoc the editorDoc to set
     */
    public void setEditorDoc(cqDataObject editorDoc) {
        if(this.editorDoc!=null)
        {
            jTextArea1.removeKeyListener(this.editorDoc);
            jTextArea1.removeMouseListener(this.editorDoc);
        }
        this.editorDoc = editorDoc;
        editorDoc.setUiWindow(this);
        jTextArea1.addKeyListener(editorDoc);
        jTextArea1.addMouseListener(editorDoc);
                
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        if(!lse.getValueIsAdjusting())
        {
            displayGoal(subGoalsList.getSelectedIndex());
        }
    }
}
