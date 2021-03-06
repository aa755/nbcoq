/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

import java.io.IOException;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Abhishek
 */
public class CoqInteract extends javax.swing.JFrame {

    /**
     * Creates new form CoqInteract
     */
    CoqTopXMLIO coqtop;
    DefaultTreeModel model;
    public CoqInteract() {
        initComponents();
        try {
            model=new DefaultTreeModel(null);
            jTree1.setModel(model);
            coqtop=new CoqTopXMLIO(null);
            System.out.println("creation of coqtop succedd");
            System.out.flush();
        } catch (IOException ex) {
            //Exceptions.printStackTrace(ex);
            System.out.println("creation of coqtop failed");
            System.out.flush();
            ex.printStackTrace();
            //sendButton.setText("coqtop init failed");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(jTree1);

        jTextField1.setText("Print nat.");

        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "interp", "goal", "about" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
                    CoqTopXMLIO.CoqRecMesg rmesg;
            System.out.println("sending message");
            rmesg = coqtop.communicate(new CoqTopXMLIO.CoqSendMesg(jTextField1.getText(), jList1.getSelectedValue().toString()));
            System.out.println("sent message");
         resetTree(jTree1,rmesg.nuDoc);
            System.out.println("recd"+rmesg);
        } catch (Exception ex) {
            //Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CoqInteract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CoqInteract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CoqInteract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CoqInteract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CoqInteract().setVisible(true);
            }
        });
    }
    
public DefaultMutableTreeNode build(Node e, int depth) {
   DefaultMutableTreeNode result = new DefaultMutableTreeNode(e.toString());
  
    System.out.println("building:"+e);
    NodeList children=e.getChildNodes();
    System.out.println("nodelist"+children);
    System.out.println("length:"+children.getLength());
    
      if(depth==1)
          return result;
   for(int i=0;i< children.getLength();i++) {
      Element child = (Element) children.item(i);
    System.out.println("child:"+child);
      result.add(build(child,depth+1));
   }
   return result;         
}

   public void resetTree(JTree jt,Document doc) throws Exception {
     //SAXReader reader = new SAXReader();
       System.out.println("building started");
       doc.normalizeDocument();
       System.out.println("raw xml:"+doc);
       DefaultMutableTreeNode root=build(doc.getDocumentElement(),0);
       System.out.println("building completed");
      model.setRoot(root);
       System.out.println("setting completed");
       System.out.flush();
      model.reload();
             
      System.out.println("root:" + model.getRoot());

       System.out.println("flush in reset tree");
       System.out.flush();
      model.nodeStructureChanged((TreeNode) model.getRoot());
       //jt.setModel(new DefaultTreeModel());
             
}

public DefaultMutableTreeNode buildnu(nu.xom.Element e) {
   DefaultMutableTreeNode result = new DefaultMutableTreeNode(""+e.toXML());
  
    System.out.println("building:"+e);
    nu.xom.Elements children=e.getChildElements();
    System.out.println("nodelist"+children);
    System.out.println("length:"+children.size());
    
   for(int i=0;i< children.size();i++) {
      nu.xom.Element child =  children.get(i);
//    System.out.println("child:"+child);
      result.add(buildnu(child));
   }
   return result;         
}

public void resetTree(JTree jt,nu.xom.Document doc) throws Exception {
     //SAXReader reader = new SAXReader();
       System.out.println("building nu started");
       //doc.normalizeDocument();
       System.out.println("raw xml:"+doc);
       DefaultMutableTreeNode root=buildnu(doc.getRootElement());
       System.out.println("building completed");
      model.setRoot(root);
       System.out.println("setting completed");
       System.out.flush();
      model.reload();
             
      System.out.println("root:" + model.getRoot());

       System.out.println("flush in reset tree");
       System.out.flush();
      model.nodeStructureChanged((TreeNode) model.getRoot());
       //jt.setModel(new DefaultTreeModel());
             
}

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
