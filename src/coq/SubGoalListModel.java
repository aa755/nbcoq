/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

/**
 *
 * @author Abhishek
 */
public class SubGoalListModel extends javax.swing.AbstractListModel {

    private int size;
    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Object getElementAt(int i) {
        return ""+i;
    }
    
    void setValues(nu.xom.Elements allGoals)
    {
        if(allGoals==null)
            size=0;
        else
            size=allGoals.size();
        fireContentsChanged(this, 0, size);
    }
}
