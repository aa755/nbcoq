/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import javax.activation.DataSource;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import org.openide.util.Exceptions;

/**
 *
 * @author Abhishek
 */
public class ProofSubgoal {
    
//    static class StringTransferable implements Transferable {
//
//        public StringTransferable(Strin ) {
//        }
//
//        
//        @Override
//        public DataFlavor[] getTransferDataFlavors() {
//            return new DataFlavor[]
//            {
//                DataFlavor.stringFlavor  
//            };
//        }
//
//        @Override
//        public boolean isDataFlavorSupported(DataFlavor df) {
//            if(df.equals(DataFlavor.stringFlavor))
//                return true;
//            else
//                return false;
//        }
//
//        @Override
//        public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
//            return 
//        }
//        
//    }
    
     
    public static JLabel getLabelOfWidth(String text, int width)
    {
        String html="<html> <body width='"+width+"px'> "+org.apache.commons.lang3.StringEscapeUtils.escapeHtml3(text)+"</body></html>";
        return new JLabel(html);        
    }
    static interface CoqType{
        public String getType();
    }
            
    ArrayList<JTextArea> getTextAreas()
    {
        ArrayList<JTextArea> ret= new ArrayList<JTextArea>();
        ret.add(concl.typeAr);
        for(int i=0;i<hypothesis.size();i++)
          ret.add(hypothesis.get(i).typeAr);
        return ret;
    }
    static class Hypothesis
    {
        private static final Color COLOR=Color.RED;
        ArrayList<String> vars;
        String type;
        JTextArea typeAr;

        static class HypTransferHandler extends TransferHandler{
            String srcVar;
            cqDataObject dobj;

            public HypTransferHandler(String srcVar, cqDataObject dobj) {
                this.srcVar = srcVar;
                this.dobj = dobj;
            }
            

            @Override
            protected Transferable createTransferable(JComponent jc) {
                return new StringSelection(srcVar);
            }

            @Override
            public boolean canImport(TransferHandler.TransferSupport ts) {
                return ts.isDataFlavorSupported(DataFlavor.stringFlavor);
            }

            @Override
            public boolean importData(TransferSupport ts) {
                try {
                    String varOrLemmaName= (String) ts.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    if(varOrLemmaName!=null)
                    {
                        String command="apply "+varOrLemmaName+" in "+srcVar+".\n";
                        dobj.insertStringAtCompiledOffsetAndCompile(command);
                        return true;
                    }
                    else
                        return false;
                } catch (UnsupportedFlavorException ex) {
                    Exceptions.printStackTrace(ex);
                    return false;
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                    return false;
                }
            }

        }
        
        class ButtonMouseListener implements MouseListener
        {
            JPopupMenu popup;
            
            public ButtonMouseListener(JPopupMenu popup) {
                this.popup=popup;
            }
            

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
//                JButton button = (JButton)me.getSource();
//
//                TransferHandler handle = button.getTransferHandler();
//
//                handle.exportAsDrag(button, me, TransferHandler.COPY);
                // does not work: try the following?
                //http://www.dreamincode.net/forums/topic/209966-java-drag-and-drop-tutorial-part-1-basics-of-dragging/
                
                // in linux, popup is triggerred at mouse press
                if (e.isPopupTrigger()) {
                            popup.show(e.getComponent(),
                                       e.getX(), e.getY());
                }            
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                            popup.show(e.getComponent(),
                                       e.getX(), e.getY());
                }            
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
            
        }
        
        public Hypothesis(ArrayList<String> vars, String type) {
            this.vars = vars;
            this.type = type;
        }
        
        final void initFromVar(String var, String type)
        {
            this.vars = new ArrayList<String>();
            this.vars.add(var);
            this.type = type;            
        }
        public Hypothesis(String var, String type) {
            initFromVar(var, type);
        }

        public Hypothesis(String coqLine) {
             int splitIndex=coqLine.indexOf(" : ");
            initFromVar(coqLine.substring(0, splitIndex), coqLine.substring(splitIndex+2));
        }
        
        String getInsertString(String container , String vname)
        {
            return container.replace("(*s*)", vname);
        }
        
        JMenuItem getMenuItem(cqDataObject dobj, String container , String vname)
        {
            String label=getInsertString(container, vname);
            JMenuItem ret=new JMenuItem(label);
            ret.addActionListener(dobj.new InsertAndCompileActionListener(label));
            return ret;
        }
        
        static String [] getButtonContexts()
        {
            return new String [] {
                        "invertsn (*s*).\n",
                        "simpl in (*s*).\n",
                        "destruct (*s*).\n",
                        "dorn (*s*).\n",
                        "clear (*s*).\n",
                        "duplicate (*s*).\n",
                        "symmetry in (*s*).\n",
                        "apply (*s*).\n",
                        "exists (*s*).\n",
                        "rw (*s*).\n",
                        "rw <- (*s*).\n",
                        "revert (*s*).\n"
                                 };
        }
        
        String getVariable(int i)
        {
            return vars.get(i);
        }
        
        JPopupMenu getButtonPopupMenu(cqDataObject dobj,int index)
        {
            JPopupMenu popup;
            popup=new JPopupMenu();
            String [] containers = getButtonContexts();
            for (int i=0;i<containers.length;i++)
            {
                popup.add(getMenuItem(dobj, containers[i], getVariable(index) ));
            }
            return popup;
        }

        public static class DnDButton extends JButton implements DragGestureListener, DragSourceListener{

            DragSource source;
            public DnDButton(String string) {
                super(string);
                source = new DragSource();
                source.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
            }

            
            @Override
            public void dragGestureRecognized(DragGestureEvent dge) {
                getModel().setArmed(false);
                source.startDrag(dge, DragSource.DefaultMoveDrop, new StringSelection(getText()),  this);             
            }

            @Override
            public void dragEnter(DragSourceDragEvent dsde) {
            }

            @Override
            public void dragOver(DragSourceDragEvent dsde) {
            }

            @Override
            public void dropActionChanged(DragSourceDragEvent dsde) {
            }

            @Override
            public void dragExit(DragSourceEvent dse) {
            }

            @Override
            public void dragDropEnd(DragSourceDropEvent dsde) {
            }
            
        }
        
        JPanel getPanel(int viewPortWidth, cqDataObject dobj)
        {
            JPanel ret=new JPanel();
           // FlowLayout fl=new FlowLayout(FlowLayout.LEFT,0,0);
            BoxLayout gl=new BoxLayout(ret, BoxLayout.X_AXIS);
            Dimension dim=ret.getMaximumSize();
            dim.width=viewPortWidth;
            dim=ret.getMinimumSize();
            dim.width=viewPortWidth;
            ret.setMinimumSize(dim);
            ret.validate();
            
       //     ret.setBorder(BorderFactory.createLineBorder(Color.RED));
            //
            ret.setLayout(gl);
         //   int usedWidth=0;
          //  JPanel buttonPanel=new JPanel(new FlowLayout());
            Font fnt=dobj.getEditor().getOpenedPanes()[0].getFont();
            Font newF=fnt.deriveFont((float)(fnt.getSize() + dobj.getFontDelta()));
            for(int i=0;i<vars.size();i++)
            {
                DnDButton but=new DnDButton(vars.get(i));
                but.setMaximumSize(but.getMinimumSize());
       //         usedWidth=usedWidth+but.getMinimumSize().width;
                but.setAlignmentX(Component.LEFT_ALIGNMENT);
                ret.add(but);
                but.addActionListener(dobj.new InsertStringActionListener(vars.get(i)+" "));
                but.addMouseListener(new ButtonMouseListener(getButtonPopupMenu(dobj, i)));
                but.setTransferHandler(new HypTransferHandler(getVariable(i), dobj));
                but.setFont(fnt);
            }
            //ret.add(buttonPanel);
            
//               buttonPanel.add(new JButton(vars.get(i)));
//            ret.add(buttonPanel);
            
            typeAr=new JTextArea(type);
            //typeAr.setPreferredSize();
            typeAr.setWrapStyleWord(true);
            typeAr.setEditable(false);                    
            typeAr.setFont(newF);
         //   int availableWidth=viewPortWidth - usedWidth;
            Dimension maxSize=typeAr.getMaximumSize();
         //   maxSize.width=availableWidth;
            Dimension minSize=typeAr.getMinimumSize();
           // minSize.width=availableWidth;
            maxSize.height=minSize.height;
            typeAr.setMaximumSize(maxSize);
            typeAr.addKeyListener(dobj);
            typeAr.addMouseListener(dobj);
            typeAr.setDragEnabled(true);
           // typeAr.setMinimumSize(minSize);
            typeAr.setBackground(dobj.getEditor().getOpenedPanes()[0].getBackground());
            typeAr.setForeground(dobj.getEditor().getOpenedPanes()[0].getForeground());
            
            ret.add(typeAr);
            ret.setAlignmentX(Component.LEFT_ALIGNMENT);
            typeAr.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            return ret;            
        }

    }
    
    static class Conclusion
    {
        String type;
        //private static final Color COLOR=Color.RED;
        private static final float  FONT_SIZE=18;
        JTextArea typeAr;
        public Conclusion(String type) {
            this.type = type;
        }        
        
        JPanel getPanel(int viewPortWidth, cqDataObject dobj)
        {
            JPanel ret=new JPanel();
            ret.setLayout(new GridLayout(1,1));
            
            
            typeAr=new JTextArea(type);
//            typeAr.setWrapStyleWord(true);
            typeAr.setWrapStyleWord(false);
            typeAr.setEditable(false);
            //typeAr.setRows(1);
            //typeAr.setColumns(container.getWidth()/typeAr.getC);           
            ret.setAlignmentX(Component.LEFT_ALIGNMENT);
            ret.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            Dimension maxSize=typeAr.getMaximumSize();
         //   maxSize.width=availableWidth;
            Dimension minSize=typeAr.getMinimumSize();
           // minSize.width=availableWidth;
            maxSize.height=minSize.height;
            typeAr.setMaximumSize(maxSize);
            typeAr.addKeyListener(dobj);
            typeAr.addMouseListener(dobj);
            typeAr.setDragEnabled(true);
            Font fnt=dobj.getEditor().getOpenedPanes()[0].getFont();
            Font newF=fnt.deriveFont((float)(fnt.getSize() + dobj.getFontDelta()));
            typeAr.setFont(newF);
            typeAr.setBackground(dobj.getEditor().getOpenedPanes()[0].getBackground());
            typeAr.setForeground(dobj.getEditor().getOpenedPanes()[0].getForeground());
            ret.add(typeAr);
            return ret;            
        }
    }
    
    ArrayList<Hypothesis> hypothesis;
    Conclusion concl;

    public ProofSubgoal(nu.xom.Element goal) {
    //nu.xom.Element goal=allGoals.get(0);
    {
        nu.xom.Element conclusion=goal.getChildElements("string").get(1);
        concl=new Conclusion(conclusion.getValue());
    }
    
    nu.xom.Element hypslist=goal.getFirstChildElement("list");
    nu.xom.Elements hyps=hypslist.getChildElements("string");    
    hypothesis=new ArrayList<Hypothesis>(hyps.size());
    for(int i=0;i<hyps.size();i++)
         hypothesis.add(new Hypothesis(hyps.get(i).getValue()));
    compactHyps();
    }
    
    JPanel showSubgoal(JPanel jp, cqDataObject dobj, int viewPortWidth)
    {
        JPanel conclP=(concl.getPanel(viewPortWidth,dobj));
        jp.add(conclP);
        for(int i=hypothesis.size()-1; i>=0; i--)
        {
            jp.add(hypothesis.get(i).getPanel(viewPortWidth,dobj));           
        }
        //jp.validate();
    return conclP;
    }
    
    /**
     * final because it is called in constructor
     */
    final void compactHyps()
    {
        ConcurrentHashMap<String,Integer> typeLocs=new ConcurrentHashMap<String, Integer>();
        ArrayList<Hypothesis> compactedList = new ArrayList<Hypothesis>();
        
        for(int i=0;i<hypothesis.size();i++)
        {
           Hypothesis hyp=hypothesis.get(i);
           boolean typePresent=typeLocs.containsKey(hyp.type);
           if(typePresent)
           {
               int index=typeLocs.get(hyp.type);
               assert(hyp.vars.size()==1);
               compactedList.get(index).vars.add(hyp.vars.get(0));
           }
           else
           {
               compactedList.add(hyp);
               typeLocs.put(hyp.type, compactedList.size()-1);
           }
        }
        hypothesis=compactedList;
    }
}
