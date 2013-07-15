/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Abhishek
 */
public class ProofSubgoal {
    
    public static JLabel getLabelOfWidth(String text, int width)
    {
        String html="<html> <body width='"+width+"px'> "+org.apache.commons.lang3.StringEscapeUtils.escapeHtml3(text)+"</body></html>";
        return new JLabel(html);        
    }
    
    static class Hypothesis
    {
        ArrayList<String> vars;
        String type;

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
        
        JPanel getPanel(JPanel parent)
        {
            JPanel ret=new JPanel();
           // FlowLayout fl=new FlowLayout(FlowLayout.LEFT,0,0);
            BoxLayout gl=new BoxLayout(ret, BoxLayout.X_AXIS);
            //
            ret.setLayout(gl);
            JPanel buttonPanel=new JPanel(new FlowLayout());
            for(int i=0;i<vars.size();i++)
               buttonPanel.add(new JButton(vars.get(i)));
            ret.add(buttonPanel);
            
            
            JTextArea typeAr=new JTextArea(type);
            //typeAr.setPreferredSize();
            typeAr.setWrapStyleWord(true);
            typeAr.setEditable(false);                    
           
            ret.add(typeAr);
            return ret;            
        }
        
    }
    
    static class Conclusion
    {
        String type;

        public Conclusion(String type) {
            this.type = type;
        }        
        
        JPanel getPanel(JPanel container)
        {
            JPanel ret=new JPanel();
            ret.setLayout(new GridLayout(1,1));
            
            
            JTextArea typeAr=new JTextArea(type);
            typeAr.setWrapStyleWord(true);
            typeAr.setEditable(false);
            //typeAr.setRows(1);
            //typeAr.setColumns(container.getWidth()/typeAr.getC);           
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
    }
    
    void showSubgoal(JPanel jp)
    {
        jp.removeAll();
        for(int i=0;i<hypothesis.size();i++)
        {
            jp.add(hypothesis.get(i).getPanel(jp));
        }
        jp.add(concl.getPanel(jp));
        jp.validate();
    }
}
