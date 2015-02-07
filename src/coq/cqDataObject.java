/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.editor.settings.AttributesUtilities;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.netbeans.spi.editor.highlighting.support.OffsetsBag;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.windows.TopComponent;
import com.github.tomtung.latex2unicode.DefaultLatexToUnicodeConverter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@Messages({
    "LBL_cq_LOADER=Files of cq"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_cq_LOADER",
        mimeType = "text/coq",
        extension = {"v"})
@DataObject.Registration(
        mimeType = "text/coq",
        iconBase = "coq/coq.gif",
        displayName = "#LBL_cq_LOADER",
        position = 300)
@ActionReferences({
    @ActionReference(
            path = "Loaders/text/coq/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/text/coq/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/text/coq/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/text/coq/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/text/coq/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/text/coq/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/text/coq/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/text/coq/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/text/coq/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class cqDataObject extends MultiDataObject implements KeyListener, UndoableEditListener, DocumentListener, MouseListener{

    /**
     * @return the compiledOffset
     */
    public int getCompiledOffset() {
        return compiledOffset.intValue();
    }

    /**
     * @return the uiWindow
     */
    public ProofError getUiWindow() {
        return uiWindow;
    }

    /**
     * @param uiWindow the uiWindow to set
     */
    public void setUiWindow(ProofError uiWindow) {
        this.uiWindow = uiWindow;
    }

    /**
     * @return the dbugcontents
     */
    public String getDbugcontents() {
        return dbugcontents;
    }

    /**
     * @param dbugcontents the dbugcontents to set
     */
    public void setDbugcontents(String dbugcontents) {
        this.dbugcontents = dbugcontents;
    }

    /**
     * @return the editor
     */
    public EditorCookie getEditor() {
        if(!initialized)
            initialize();
        return editor;
    }

    void stopRequest()
    {
        batchCompile.requestStopping();
    }
    /**
     * @return the goal
     */
    public nu.xom.Document getGoal() {
        return goal;
    }

    /**
     * @param goal the goal to set
     */
    public void setGoal(nu.xom.Document goal) {
        this.goal = goal;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        //JOptionPane.showMessageDialog(null, "NSA you edited!");
              //  highlighter.setHighlight(0, getCompiledOffset());

    }

    boolean isCompileToCursorShortcut(KeyEvent ke)
    {
        return (ke.isControlDown()&&ke.getKeyCode()==KeyEvent.VK_RIGHT);
    }
    
    boolean isBottomShortcut(KeyEvent ke)
    {
        return (ke.isControlDown()&&ke.getKeyCode()==KeyEvent.VK_LEFT);
    }

    boolean isUpShortcut(KeyEvent ke)
    {
        return (ke.isControlDown()&&ke.getKeyCode()==KeyEvent.VK_UP);
    }

    boolean isDownShortcut(KeyEvent ke)
    {
        return (ke.isControlDown()&&ke.getKeyCode()==KeyEvent.VK_DOWN);
    }

    boolean isSearchAboutShortcut(KeyEvent ke)
    {
        return (ke.isControlDown()&&ke.isAltDown()&&ke.getKeyCode()==KeyEvent.VK_L);
    }

    boolean isPrintShortcut(KeyEvent ke)
    {
        return (ke.isControlDown()&&ke.isAltDown()&&ke.getKeyCode()==KeyEvent.VK_P);
    }
    
    boolean isJumpDefnShortcut(KeyEvent ke)
    {
        return (ke.isControlDown()&&ke.isAltDown()&&ke.getKeyCode()==KeyEvent.VK_O);
    }
    
    boolean isLatex2UnicodeShortcut(KeyEvent ke)
    {
        return (ke.isControlDown()&&ke.isAltDown()&&ke.getKeyCode()==KeyEvent.VK_U);
    }

    String getSelectedWord()
    {
        try
        {
            return getEditor().getOpenedPanes()[0].getSelectedText();
        }
        catch(NullPointerException ex)
        {
            return "";
        }
    }
    
    static boolean partOFId(char ch)
    {
        return ('a'<=ch && ch<='z')||('A'<=ch && ch<='Z') || ('0'<=ch && ch<='9') || (ch=='_');
    }

    static String getFocussedWord(Object src)
    {
        String str=getSelectedWord(src);
        // if nothins is selected, return the word at cursor
        if(str.isEmpty())
            str=getWordAtCursor(src);
        
        return str;
    }
    
    String getTextSelectedInEditor()
    {
       return getEditor().getOpenedPanes()[0].getSelectedText();
    }
    
    void replaceSelectedTextInEditorWith( String rep)
    {
       getEditor().getOpenedPanes()[0].replaceSelection(rep);
    }

    void fixSelectedCode()
    {
      String prefix="((Definition)|(Fixpoint)|(Lemma)|(Theorem)|(Inductive))";
      Pattern pat=Pattern.compile(prefix+" ([\\w]*)");
      Matcher mat=pat.matcher(getEntireText());
      dbugcontents="";
      while(mat.find())
      {
          String [] frags=mat.group().trim().split(" ");
          String mesg="Print "+frags[1]+".";
          CoqTopXMLIO.CoqRecMesg query = getCoqtop().query(mesg);
          if(query.success)
              dbugcontents=dbugcontents+query.conciseReply;
          
      }
          uiWindow.enableCompileButtonsAndShowDbug();
/*      String sellection = getTextSelectedInEditor();
      String [] sp=sellection.split("[ :]", 3);
      String lemmaname = "";
      if(sp[0].equals("Lemma") ||sp[0].equals("Theorem"))
         lemmaname= sp[1].replace("lsubst","lsubst_aux");
                 
      insertStringAtCursor("Proof.\n"
              + "  intros. change_to_lsubst_aux2.\n"
              + "  apply "+lemmaname+";try(sp;fail);\n" +
"  try(apply disjoint_sub_as_flat_map;disjoint_reasoning).\n"
              + "Qed.\n"
              + "");
      
      String change=sellection.replace("lsubst","lsubst_aux");
      insertStringAtCursor(change);*/
      
      
    }
    static String getSelectedWord(Object src)
    {
        
        try
        {
            JTextComponent comp=(JTextComponent) src;
            return comp.getSelectedText();
        }
        catch(Exception ex) // cast exception
        {
            return "";
        }
    }
    
    static String getWordAtCursor(Object src)
    {
        
        try
        {
            JTextComponent comp=(JTextComponent) src;
            //return comp.getSelectedText()+":"+comp;
            int start=comp.getCaret().getDot();
            int pos=start;
            String ret="";
            // march forward
            try
            {
                char ch;
                while( partOFId(ch= comp.getText(pos, 1).charAt(0)))
                {
                    ret=ret+ch;
                    pos++;
                }
            }
            catch (Exception ex) // illegal position exception
            {
            }
            
            //march backward
            pos=start-1;
            try
            {
                char ch;
                while( partOFId(ch= comp.getText(pos, 1).charAt(0)))
                {
                    ret=ch+ret;
                    pos--;
                }
            }
            catch (Exception ex) 
            {
            }
            
            return ret;
        }
        catch(Exception ex) // cast exception
        {
            return "";
        }
    }
    
    @Override
    public void keyPressed(KeyEvent ke) {
        //keyboard shortcuts?
        if(isCompileToCursorShortcut(ke))
            handleDownToCursor();
        else if(isUpShortcut(ke))
            handleUpButton();
        else if(isBottomShortcut(ke))
            handleBottomButton();
        else if(isDownShortcut(ke))
            handleDownButton();
        else if(isSearchAboutShortcut(ke))
        {
            if(uiWindow!=null) 
            {
                String word=getWordAtCursor(ke.getSource());
                if(!word.isEmpty())
                {
                    uiWindow.setQuery("SearchAbout "+word+".");
                    handleQuery();
                }
            }
        }            
        else if(isPrintShortcut(ke))
        {
            if(uiWindow!=null) 
            {
                
                String word;
                String query;
                word=getSelectedWord(ke.getSource());
                /**
                 * the only reason when one 
                 * would take the pain to select
                 * and not just click at the word,
                 * is when the selection is more
                 * complicated than a single word.
                 * In this case, Print might not
                 * make sens
                 */
                if(word!=null && (!word.isEmpty()))
                {
                    query="Check ("+word+").";
                }
                else
                {
                    word= getWordAtCursor(ke.getSource());
                    query="Print "+word+".";
                    /**
                     * print already shows the info that Check shown
                     */
                }
                    
                if(!word.isEmpty())
                {
                    uiWindow.setQuery(query);
                    handleQuery();
                }
            }
        }
        else if(isJumpDefnShortcut(ke))
        {
            String word;
            String query;
            word= getWordAtCursor(ke.getSource());
            query="Locate "+word+".";
            uiWindow.setQuery(query);
            handleQuery();
            
        }
        else if(isLatex2UnicodeShortcut(ke))
        {
            String word = getTextSelectedInEditor();
            String unicode = DefaultLatexToUnicodeConverter.convert(word);
            replaceSelectedTextInEditorWith(unicode);
        }            
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    /**
     * @return the retb
     */
    public OffsetsBag getCheckedAreaHighlights() {
        return checkedAreaHighlights;
    }

    /**
     * @param retb the retb to set
     */
    public void setCheckedAreaHighlights(OffsetsBag retb) {
        this.checkedAreaHighlights = retb;
    }

    int  getDocLength()
    {
        return getDocument().getEndPosition().getOffset();
    }
    public /*synchronized*/ void setHighlight(int start, int end)
    {
        uiWindow.setProgressText(((float) getCompiledOffset())/ getDocLength() ); 
        checkedAreaHighlights.clear();
        if(ProofError.DARK)
            checkedAreaHighlights.addHighlight(start, end, compiledCodeAttrDark);
        else    
            checkedAreaHighlights.addHighlight(start, end, compiledCodeAttr);
    }        

/*    public synchronized void  setHighlightHelper(int start, int end)
    {
        try {
            //System.out.println(getDocument());
              this.getEditor().getOpenedPanes()[0].getHighlighter().removeAllHighlights();
              this.getEditor().getOpenedPanes()[0].getHighlighter().addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(Color.red));
              
    //        retb.clear();
    //        if(ProofError.DARK)
    //            retb.addHighlight(start, end, compiledCodeAttrDark);
    //            retb.addHighlight(start, end, compiledCodeAttr);
    //            retb.addHighlight(start, end, compiledCodeAttr);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }        
    }
    
        public void setHighlightBad(final int start, final int end)
    {
            SwingUtilities.invokeLater(new Runnable () {

                @Override
                public void run() {
                    setHighlightHelper(start, end);
                }
            });
        
    }
*/
    /**
     * retb already seems to have a lock; as it seems from debug
     * @param start
     * @param end 
     */
    public /*synchronized*/ void addErrorHighlight(int start, int end)
    {
        if(start<end)
            checkedAreaHighlights.addHighlight(start, end, errorCodeAttr);
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent uee) {
       // setHighlight(0, getCompiledOffset());
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
       // JOptionPane.showMessageDialog(null, "you inserted text");
        int offset=de.getOffset();
        if(offset<getCompiledOffset())
        {
            if(offset+1<de.getDocument().getEndPosition().getOffset())
                offset=offset+1; // to handle backspace before a dot
            handleCompileToOffset(offset);
            lastCharIsDot=false;
        }
        else
        {
            try {
                String insert=getDocument().getText(offset, de.getLength());
                if((uiWindow.isJumpToErrorChecked())&&lastCharIsDot&&Character.isWhitespace(insert.charAt(0)))
                {
                    if(offset+1<de.getDocument().getEndPosition().getOffset())
                        offset=offset+1; // to handle backspace before a dot
                    handleCompileToOffset(offset);                    
                }
                else
                {
                    setHighlight(0, getCompiledOffset());                    
                }
                lastCharIsDot = insert.equals(".");
            } catch (BadLocationException ex) {
                setHighlight(0, getCompiledOffset());
                lastCharIsDot=false;
                Exceptions.printStackTrace(ex);
            }
        }
        
        
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        int offset=de.getOffset();
        lastCharIsDot=false;
        if(offset+1<de.getDocument().getEndPosition().getOffset())
            offset=offset+1; // to handle backspace before a dot        
        if(offset<getCompiledOffset())
        {
            handleCompileToOffset(offset);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        de.getType(); // just to find out when it gets triggered
    }

  /**
   * @return the globSyntaxHighlights
   */
  public OffsetsBag getGlobSyntaxHighlights() {
    return globSyntaxHighlights;
  }

  /**
   * @param globSyntaxHighlights the globSyntaxHighlights to set
   */
  public void setGlobSyntaxHighlights(OffsetsBag globSyntaxHighlights) {
    this.globSyntaxHighlights = globSyntaxHighlights;
  }



    class OffsetTime implements Comparable<OffsetTime>
    {
        int offset;
        int duration;

        @Override
        public int compareTo(OffsetTime t) {
            return (int) (t.duration-duration);
        }
        
        boolean compileStepAndMeasureTime()
        {
            long startTime=System.nanoTime();
            boolean success=compileStep();
            offset=getCompiledOffset();
            duration=(int) ((System.nanoTime()/1000000L)-(startTime/1000000L));
            return success;
        }
    }
    
    ArrayList<OffsetTime> profInfo=null;

    void showProfilingInfo()
    {
        Collections.sort(profInfo);
        int size= 20;
        if(profInfo.size()<size)
            size=profInfo.size();
        Integer [] times=new Integer [size];        
        for(int i=0;i<size;i++)
        {
            times[i]=(profInfo.get(i).duration);
        }
        final JList list = new JList(times); //data has type Object[]
        list.setVisibleRowCount(-1);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {

          @Override
          public void valueChanged(ListSelectionEvent lse) {
              jumpToOffset(profInfo.get(list.getSelectedIndex()).offset);
          }
        });
        JDialog jd=new JDialog();
        jd.add(list);
        jd.pack();
        jd.setVisible(true);
    }
    
    /**
     * @return the coqtop
     */
    public CoqTopXMLIO getCoqtop() {
        if(coqtop==null)
        {
            try {
                coqtop=new CoqTopXMLIO(fileObj.getParent());
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
             //   uiWindow.disableCompileButtons();
                JOptionPane.showMessageDialog(null, "could not start [h|c]oqtop. Check path in Tools | Options| Misc| CoqOptions");
                uiWindow.setDebugMesg("could not start [h|c]oqtop. Check path in Tools | Options| Misc| CoqOptions");
                uiWindow.enableCompileButtonsAndShowDbug(); // down buttoon might have disabled it. this will allow user to fix problem
            }
        }
        
        return coqtop;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if(me.getButton()==MouseEvent.BUTTON2)
        {
            String selection=getFocussedWord(me.getSource());
            if(selection!=null && !selection.isEmpty())
            {
                insertStringAtCursor(selection);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

  /**
   * @return the fontDelta
   */
  public int getFontDelta() {
    return fontDelta;
  }

  public void incementFont() {
    fontDelta = fontDelta +1;
  }

  public void decrementFont() {
    fontDelta = fontDelta -1;
  }

  
    class BatchCompile implements Runnable{
        private AtomicInteger targetOffset;
        private AtomicInteger pendingSteps;
        private AtomicBoolean stopRequest; // flag to request it to stop
        private AtomicInteger lastActionRequest;
        
        public static final int NOP_ACTION=0; 
        public static final int MOVE_TO_CURSOR_ACTION=1; 
        public static final int UP_DOWN_ACTION=2; 
        public static final int QUERY_ACTION=3; 
        
        
        public void resetPendingSteps()
        {
          //  pendingSteps=0;
            pendingSteps.set(0);
        }

        void decrementPendingSteps(int n)
        {
            pendingSteps.addAndGet(0-n);
            lastActionRequest.set(UP_DOWN_ACTION);
        }
        
        public void incrementPendingSteps()
        {
            pendingSteps.incrementAndGet();
            lastActionRequest.set(UP_DOWN_ACTION);
        }
                
        public void requestStopping()
        {
            stopRequest.set(true);
        }

        public BatchCompile(int targetOffset) {
            pendingSteps=new AtomicInteger(0);
            this.targetOffset=new AtomicInteger(targetOffset);
            stopRequest=new AtomicBoolean(false);
            lastActionRequest=new AtomicInteger(NOP_ACTION);
        }
        
        @Override
        public void run() {
            int lastAc=lastActionRequest.intValue();
            lastActionRequest.set(NOP_ACTION); // actions take time to execute
            
            
            boolean change=false;
            getUiWindow().disableCompileButtons();
            if(lastAc==UP_DOWN_ACTION)
                change=handleSteps();
            else if(lastAc==MOVE_TO_CURSOR_ACTION)
                change=handleCompileToTargetPos();
            else if(lastAc==QUERY_ACTION)
                handleQuery();
            getUiWindow().enableCompileButtonsAndShowDbug();
            if(change)
            {
                if(uiWindow.isShowGoalChecked())
                {
                                    SwingUtilities.invokeLater(new Runnable () {

                                        @Override
                                        public void run() {
                    updateGoal();
                    uiWindow.showGoal();                    
                                        }
                                    });
                }
            }
        }

        boolean handleSteps()
        {
            if(pendingSteps.intValue()<0)
                return handleRewind(0-pendingSteps.intValue());
            
                boolean change=false;
            while(pendingSteps.intValue()>0)
            {
                if(compileStep())
                {
                   pendingSteps.decrementAndGet();
                   change=true;
                }
                else
                {
                    pendingSteps.set(0);
                    break;
                }
            }
            if(lastError!=null)
            {
                lastError.highlight();
            }
            return change;
        }
        
        boolean handleRewind(int nofSteps)
        {
            
             int rewSteps=(rewindCoqtop(nofSteps));
             resetPendingSteps();
             return rewSteps>0;
              
        }
        
        boolean handleUpCursor()
        {
            int curOffset=compiledOffset.intValue();
            int countPops=0;
            int lastElem=offsets.size()-1;
            while(targetOffset.intValue()<curOffset)
            {
                //offsets.
                curOffset=offsets.get(lastElem-countPops); // binary search can be done here
                // do not pop here. handleRewind does that.
                countPops=countPops+1;
            }
            if(countPops>0)
                return handleRewind(countPops);
            else 
                return false;
        }
        
        boolean handleCompileToTargetPos()
        {   
            boolean change = false;
            if (getCompiledOffset() > targetOffset.intValue()) {
                change = handleUpCursor();
            } 
            else {
                boolean profile= (uiWindow.isProfilingEnabled());
                if(profile)
                {
                    if(profInfo==null)
                        profInfo=new ArrayList<OffsetTime>();
                    else
                      profInfo.clear();
                }
                while (getCompiledOffset() < targetOffset.intValue() && (!stopRequest.get())) {
                    boolean success;
                    if(!profile)
                    {
                        success=compileStep();
                    }
                    else
                    {
                        OffsetTime ot = new OffsetTime();
                        success=ot.compileStepAndMeasureTime();
                        profInfo.add(ot);
                    }
                    if (success) {
                        change = true;
                    } else {
                        break;
                    }
                }
                
                if(profile)
                {
                    showProfilingInfo();
                }
                
                if(lastError!=null)
                {
                    lastError.highlight();
                }

            }
            stopRequest.set(false);
            targetOffset.set(0);
            return change;
        }
        
        /**
         * 
         * @return Top.v if it is in current file.
         * full source filename otherwise
         */
        public String getFileName(String name)
        {
            String [] parts=name.split(".");
            if(parts.length==2) //TODO: handle other cases
            {
                return parts[0]+".v";
            }
            else
                return null;
        }
        
        cqDataObject getDataObject(String filename)
        {
            FileObject fob=fileObj.getParent().getFileObject(filename,"v");
            //FileObject fob = FileUtil.toFileObject(FileUtil.normalizeFile(gifFile)); 
            if (fob != null) { 
                try {
                    cqDataObject dob = (cqDataObject) DataObject.find (fob);
                    return dob;
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            return null;
//            OpenCookie oc = (OpenCookie) dob.getCookie (OpenCookie.class); 
//            if (oc != null) 
//            oc.open(); 
//            }             
        }
        void handleQuery() {
            String sendtocoq = uiWindow.getQuery();
            boolean setCommand=(sendtocoq.trim().startsWith("Set") || sendtocoq.trim().startsWith("Unset"));
            
            CoqTopXMLIO.CoqRecMesg rec;
            if(setCommand)
              rec=getCoqtop().setOption(sendtocoq);
            else
              rec= getCoqtop().interpret(sendtocoq);
            
            if (rec.success) {
                String reply=rec.nuDoc.getRootElement().getFirstChildElement("string").getValue();
                String creply=reply;
                String warnMesg="Warning: query commands should not be inserted in scripts";
                if(reply.startsWith(warnMesg))
                    creply=reply.substring(warnMesg.length());
                            //+ "this might make IDE's estimation of coqtop's state inconsistent."
                            //+ "you might want to save the file and restart the IDE");
                setDbugcontents(creply);
                
                if(!setCommand)
                {
                  rewindCoqtopForQuery();                
                  uiWindow.saveQuery(sendtocoq);
                }
                
                if(!sendtocoq.trim().startsWith("Locate"))
                  return;
                
                String ctreply=creply.trim();
               // ctreply.sp
                String [] frags=ctreply.split("[\\s]");
                String prefix="";
               if(frags.length>1)
               {
                   if(frags[0].equals("Inductive"))
                   {
                       prefix=frags[0];
                   }
                   else if(frags[0].equals("Constant"))
                   {
                       prefix="((Definition)|(Fixpoint)|(Lemma)|(Theorem))";
                   }
                   else if(sendtocoq.startsWith("Locate"))
                    {
                      // this part is useful if the definition is the current file and
                      // is a tactic or outside the compiled region
                      prefix = "((Definition)|(Fixpoint)|(Lemma)|(Notation)|(Theorem)|(Ltac)|(Tactic[\\s]*Notation[\\s]*\"))";
                      frags=new String[2];
                      frags[0]="";
                      String[] parts = sendtocoq.split(" ");
                      String obname = parts[1];
                      frags[1]="Top."+obname;
                    }
                   
                   if(!prefix.isEmpty())
                   {
                        String [] parts=frags[1].split("\\.");
                        final cqDataObject target;
                        if(parts.length>=2) //TODO: handle other cases
                        {
                            String suffix=parts[parts.length-1]; 
                            // if the jump target is in a section in the current file,
                            // parts.length-1 is better than 2
                            // However, this simplistic implementation
                            // ignores section names, i.e it might jump to
                            // a definition of same name in a prev. section.
                            String query=prefix+"[\\s]*"+suffix+"[\\s:]";
                            if(parts[0].equals("Top"))
                            {
                                target=getThisDataObject();
                            }
                            else
                            {
                                String filename=parts[0];
                                target=getDataObject(filename);
                            }
                            
                            if(target!=null)
                            {
                                target.getLookup().lookup(Openable.class).open();
                                //target.getDocument().gette
                                final Matcher mjump=Pattern.compile(query).matcher(target.getEntireText());
                                if(mjump.find())
                                {
                                    SwingUtilities.invokeLater(new Runnable () {

                                        @Override
                                        public void run() {
                                        target.jumpToOffset(mjump.start());
                                        }
                                    });
                                    
                                    //target.get
                                }
                                
                            }
                            
                        }
                       
                   }
               }
            } else {
                String error= "probably too large output from Coq. If so, please ask the plugin developer to increase "
                        + "CoqRecMesg.BUF_SIZE and/or CoqRecMesg.NUM_TRIALS";
                if(rec.nuDoc!=null)
                {
                    error=rec.nuDoc.toXML();
                }
                setDbugcontents("sent: " + sendtocoq + " received " + error);
            }
        }
       
        public void requestQuery()
        {
            lastActionRequest.set(QUERY_ACTION);
        }
        
        public /*synchronized*/ void setTargetOffset(int targetOffset) {
            if(targetOffset==0)
            {
                System.out.println("received 0 as targetOffset");
                this.targetOffset.set(0);
                lastActionRequest.set(MOVE_TO_CURSOR_ACTION);
            }
            else
            {
                this.targetOffset.set(targetOffset);
                lastActionRequest.set(MOVE_TO_CURSOR_ACTION);
            }
            
        }
    }
    
     private RequestProcessor.Task batchCompileTask;
    private CoqTopXMLIO coqtop;
    private String dbugcontents;
    private AtomicInteger compiledOffset;
    private EditorCookie editor;
    private boolean initialized;   
    //private CoqHighlighter highlighter;
    private final RequestProcessor rp;
    private static final Pattern coqCommandEnd=Pattern.compile("([^.]\\.[\\s])");
    private static final Pattern coqComment=Pattern.compile("(\\(\\*)|(\\*\\))");
    private BatchCompile batchCompile;
    private ProofError uiWindow;
    private nu.xom.Document goal;
    private Stack<Integer> offsets;
    private int fontDelta=0;
    private OffsetsBag checkedAreaHighlights;
    private OffsetsBag globSyntaxHighlights;
    private static final AttributeSet compiledCodeAttr =
            AttributesUtilities.createImmutable(StyleConstants.Background,
            new Color(200, 255, 200));
    private static final AttributeSet compiledCodeAttrDark =
            AttributesUtilities.createImmutable(StyleConstants.Background,
            new Color(10, 20, 30));
    private static final AttributeSet errorCodeAttr =
            AttributesUtilities.createImmutable(StyleConstants.Background,
            new Color(255, 100, 100));
    private static final AttributeSet defnAttr =
            AttributesUtilities.createImmutable(StyleConstants.Foreground,
            new Color(50, 255, 50));
    private static final AttributeSet indAttr =
            AttributesUtilities.createImmutable(StyleConstants.Foreground,
            new Color(150, 150, 255));
    private static final AttributeSet varAttr =
            AttributesUtilities.createImmutable(StyleConstants.Foreground,
            new Color(250, 250, 0));
    private static final AttributeSet constrAttr =
            AttributesUtilities.createImmutable(StyleConstants.Foreground,
            new Color(200, 0, 100));
    private static final AttributeSet thmAttr =
            AttributesUtilities.createImmutable(StyleConstants.Foreground,
            new Color(0, 200, 0));
    private static final AttributeSet notationAttr =
            AttributesUtilities.createImmutable(StyleConstants.Foreground,
            new Color(255, 50, 50));
    
    final String keywords="([^a-zA-Z]((Definition)|(Fixpoint)|(Lemma)|(Theorem)|(Section)"
            + "|(Inductive)|(Qed)|(Defined)|(Require)|(Record)|(Instance)"
            + "|(Export)|(Import)|(Notation)|(CoFixpoint)"
            + "|(CoInductive)|(match)|(end)|(Coercion)|(end))[^a-zA-Z0-9])";
    final Pattern keywordPat=Pattern.compile(keywords);
    boolean lastCharIsDot;
    private CoqError lastError;
    private static final String indentStrs = "-+*";
    /**
     * file object denoting this file
     */
    private final FileObject fileObj;
    class CoqError{
        public int startLoc;
        public int endLoc;
        public String errorMesg;
        
        public void highlight()
        {
            addErrorHighlight(startLoc, endLoc);
            
            //if(uiWindow.isJumpToErrorChecked())
              jumpToOffset(startLoc);
        }
    }
    
    String getEntireText()
    {
        try {
            return getDocument().getText(0, getDocument().getLength());
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
            return "";
        }
    }
    
    public cqDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader); 
        initialized=false;
        registerEditor("text/coq", true);
        fileObj=pf;
        compiledOffset=new AtomicInteger(0);
        rp = new RequestProcessor(cqDataObject.class);
        batchCompile=new BatchCompile(0);
        batchCompileTask=rp.create(batchCompile, true);
        offsets=new Stack<Integer>();
        checkedAreaHighlights=null;
        lastCharIsDot=false;
        keyListenerAssigned=false;
    //    initialize();
    }

    void setKeyboardListener()
    {
        if(!keyListenerAssigned)
        {
            getEditor().getOpenedPanes()[0].addKeyListener(this);
          //  getEditor().getOpenedPanes()[0].addMouseListener(this);
        }
        keyListenerAssigned=true;
    }
    void scheduleCompilation()
    {
        batchCompileTask.schedule(10);
    }

    void handleCompileOffsetChange()
    {
        setHighlight(0, getCompiledOffset());        
    }
    /**
     * this should only be called after coq compilation/rewind.
     * else it can cause inconsistency between coqtop's and editor's state
     * @param change 
     */
    synchronized void addToCompiledOffset(int change)
    {
        assert(change>0);
        offsets.push(compiledOffset.intValue());
        compiledOffset.addAndGet(change);
        handleCompileOffsetChange();
    }
    
    synchronized void unwindOffsets(int n)
    {
        int newOffset=compiledOffset.intValue();
        for(int i=0;i<n;i++)
        {
            newOffset=offsets.pop();
        }
        compiledOffset.set(newOffset);
        handleCompileOffsetChange();
    }
    
    void jumpToOffset(int offset)
    {
        getEditor().getOpenedPanes()[0].getCaret().setDot(offset);
    }
    void jumpToCompileOffest()
    {        
         jumpToOffset(compiledOffset.intValue());
    }    
    
    final void initialize()
    {
        initialized=true;
        assignCookie();
     }
    
    public class InsertStringActionListener implements ActionListener
    {
        String str;

        public InsertStringActionListener(String str) {
            this.str=str;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            insertStringAtCursor(str);
        }
        
    }
        
    public class InsertAndCompileActionListener implements ActionListener
    {
        String str;

        public InsertAndCompileActionListener(String str) {
            this.str=str;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            insertStringAtCompiledOffsetAndCompile(str);
            
        }
        
    }
     
    public StyledDocument getDocument()
    {
        return getEditor().getDocument();
    }
    /*synchronized*/ void updateGoal()
    {
        setGoal(getCoqtop().getGoal());
    }
    
    void insertStringAtCursor(String str)
    {
        try {        
            getDocument().insertString(getEditor().getOpenedPanes()[0].getCaret().getDot(), str, errorCodeAttr);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    void insertStringAndCompile(int startIndex, String str)
    {
        try {        
            getDocument().insertString(startIndex, str, errorCodeAttr);
            getEditor().getOpenedPanes()[0].getCaret().setDot(startIndex+str.length());
            handleDownToCursor();
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    void insertStringAtCursorAndCompile(String str)
    {
        insertStringAndCompile(getEditor().getOpenedPanes()[0].getCaret().getDot(), str);
    }

    void insertStringAtCompiledOffsetAndCompile(String str)
    {
        insertStringAndCompile(getCompiledOffset(), str);
    }

    int getOffsetToSend() {

        int offset = 0;
        int endPos = getDocument().getEndPosition().getOffset();
        String code="";
        try {
            code = getDocument().getText(getCompiledOffset(), endPos - getCompiledOffset());
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
            assert(false);
        }
        int unmatchedComLeft = 0;
        //int unmatchedStrLift = 0;
        Matcher commandEndMatcher = coqCommandEnd.matcher(code);
        int start=0;
        while (commandEndMatcher.find()) {
            if ((start==0))
            {
                String segment=code.substring(0, commandEndMatcher.end());
                String seg_trim=segment.trim();
                // if the segment to next dot begins with - / * / + , send only to that part
                for(int i=0;i<indentStrs.length();i++)
                {
                    String ich=indentStrs.substring(i, i+1)+" ";
                    if(seg_trim.startsWith(ich))
                    {
                        offset= segment.indexOf(ich)+1;
                        return offset;
                    }
                }
            }
            
            Matcher comments = coqComment.matcher(code.substring(start, commandEndMatcher.end()));
            start=commandEndMatcher.end();
            while (comments.find()) {
                if (comments.group().equals("*)")) {
                    unmatchedComLeft = unmatchedComLeft - 1;
                }

                if (comments.group().equals("(*")) {
                    unmatchedComLeft = unmatchedComLeft + 1;
                }
            }
            if (unmatchedComLeft == 0) {
                offset = commandEndMatcher.end() - 1;
                // code[offset]='.'
                break;
            }
        }




        return offset;


    }
    
    /* synchronized */ boolean  compileStep() {
        if(!initialized)
            initialize();
        
        int dotOffset=getOffsetToSend();
        String sendtocoq="";
        try {
            sendtocoq = getDocument().getText(getCompiledOffset(), dotOffset);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
            assert(false);
        }
        
        CoqTopXMLIO.CoqRecMesg rec=getCoqtop().interpret(sendtocoq);
        if(rec.success)
        {
      //          setDbugcontents(""+rec.nuDoc.toXML());
            setDbugcontents("received "+rec.nuDoc.toXML()+"sent: "+sendtocoq);
            addToCompiledOffset (dotOffset+1);
            lastError=null;
        }
        else
        {
            if(rec.nuDoc!=null)
            {
//                setDbugcontents("received "+rec.nuDoc.toXML()+"sent: "+sendtocoq);
                setDbugcontents(rec.nuDoc.getRootElement().getValue());
                nu.xom.Element root=rec.nuDoc.getRootElement();
                try
                {
                    int startOffset=Integer.parseInt(root.getAttributeValue("loc_s"));
                    int endOffset=Integer.parseInt(root.getAttributeValue("loc_e"));
                    lastError=new CoqError();
                    lastError.startLoc= compiledOffset.intValue() + startOffset ;
                    lastError.endLoc= compiledOffset.intValue() + endOffset;
                }catch(NumberFormatException ex)
                {
                    lastError=new CoqError();
                    lastError.startLoc= compiledOffset.intValue() ;
                    lastError.endLoc= compiledOffset.intValue() + dotOffset;
                    
                }
                
            }
            else
            {
                setDbugcontents("received null, sent: "+sendtocoq);
            }
        }
        return rec.success;
    }

    void handleDownButton()
    {
        setKeyboardListener();
        batchCompile.incrementPendingSteps();
        scheduleCompilation();
    }
    
    void handleQuery()
    {
        batchCompile.requestQuery();
        scheduleCompilation();
    }
    
    void handleCompileToOffset(int offset)
    {
        setKeyboardListener();
        batchCompile.setTargetOffset(offset);
        scheduleCompilation();
    }
    
    void handleDownToCursor()
    {
        int curPos=getEditor().getOpenedPanes()[0].getCaretPosition();
        handleCompileToOffset(curPos);
    }
    
    void handleUpButton()
    {
        batchCompile.decrementPendingSteps(1);
        scheduleCompilation();
    }
    void handleUppButton()
    {
        batchCompile.decrementPendingSteps(offsets.size());
        scheduleCompilation();
    }

    void handleBottomButton()
    {
        handleCompileToOffset(getDocument().getEndPosition().getOffset());
    }
    /**
     * 
     * @param nofSteps
     * @return the number of steps actually rewound
     *  INCLUDING the ones asked for(and extra steps)
     */
    int rewindCoqtop (int nofSteps)
    {
        CoqTopXMLIO.CoqRecMesg rec=getCoqtop().rewind(nofSteps);
        if(rec.success)
        {
            int actualSteps= rec.getExtraRewoudSteps()+nofSteps;
            unwindOffsets(actualSteps);
            return actualSteps;
                    
        }
        else
            return 0;
    }
    /**
     * same as rewindCoqtop, but since it is a query
     * entered externally(w.r.t the file editor), there
     * us no need to change editor state(highlighting)
     * @param nofSteps
     * @return the number of steps actually rewound
     *  INCLUDING the ones asked for(and extra steps)
     */
    void rewindCoqtopForQuery ()
    {
        CoqTopXMLIO.CoqRecMesg rec=getCoqtop().rewind(1);
        assert(rec.success);
        assert(rec.getExtraRewoudSteps()==0);
    }
    

    DebugUnivInconst dbgC;
    void showTopUnivs()
    {
        CoqTopXMLIO.CoqRecMesg rec= getCoqtop().query("Print Universes.");        
        if(rec.success)
        {
            String constraints= rec.conciseReply;
            setDbugcontents(constraints);
            dbgC.showTopUnivs(constraints);
        }
    }
    
  static String readFile(String path) 
  throws IOException 
  {
  byte[] encoded = Files.readAllBytes(Paths.get(path));
  return new String(encoded, Charset.defaultCharset());
  } 
        
  void clearSyntaxHighlight()
    {
      globSyntaxHighlights.clear();
    }
  
     void SyntaxHighlight()
    {
      globSyntaxHighlights.clear();
      Matcher kwMatcher = keywordPat.matcher(getEntireText());
      while(kwMatcher.find())
      {
        globSyntaxHighlights.addHighlight(kwMatcher.start(), kwMatcher.end(), notationAttr);
        //some of these will match other identifiers which have a keyword
        // as a substring. these will get fixed anyway by the code below.
      }

      FileObject glob=fileObj.getParent().getFileObject(fileObj.getName(),"glob");

        try
        {
          String globContents = readFile(glob.getPath());
          String [] lines= globContents.split("\n");
          final String entText=getEntireText();
          int [] byteToCPOffset = new int[entText.getBytes().length]; 
          final int entLen = entText.length();
          int byteOffset=0;
          String entTextB = entText;
          for (int cpOffset=0;cpOffset<entLen;cpOffset++)
          {
            int byteDelta= entTextB.substring(0,1).getBytes().length;
            for(int i=0;i<byteDelta;i++)
            {
              byteToCPOffset[byteOffset+i]=cpOffset;
            }
            byteOffset=byteOffset+byteDelta;
            entTextB=entTextB.substring(1);
          }
          int globLineCount=0;
          for (String line:lines)
          {
            globLineCount++;
            if(globLineCount<3) continue;
            String category;
            int startIndexCP= 0;
            int endIndexCP= 0;
            try {

              if (line.startsWith("R")) { // reference to already defined thing
                line = line.substring(1);
                int colonIndex = line.indexOf(":");
                int spaceIndex = line.indexOf(" ");
                startIndexCP = Integer.parseInt(line.substring(0, colonIndex));
                endIndexCP = 1 + Integer.parseInt(line.substring(colonIndex + 1, spaceIndex));
                String[] words = line.split(" ");
                String lastWord = words[words.length - 1];
                category = lastWord.trim();
              } else { // new definition
                int spaceIndex = line.indexOf(" ");
                category = line.substring(0, spaceIndex + 1).trim();
                line = line.substring(spaceIndex).trim();
                int colonIndex = line.indexOf(":");
                spaceIndex = line.indexOf(" ");
                startIndexCP = Integer.parseInt(line.substring(0, colonIndex));
                endIndexCP = startIndexCP + 1; // in some cases, 
                //the .glob file omits end index, specially for 1-character notations.
                try {
                  endIndexCP = 1 + Integer.parseInt(line.substring(colonIndex + 1, spaceIndex));
                } catch (NumberFormatException e) {
                }
              }
            } catch (NumberFormatException ex) {
              continue;
            }

            if(0<startIndexCP && startIndexCP<endIndexCP) {
              int startIndex=byteToCPOffset[startIndexCP];
              int endIndex=byteToCPOffset[endIndexCP];
              if(category.equals("def")) // there could be a \r at end
                globSyntaxHighlights.addHighlight(startIndex, endIndex, defnAttr);
              else if(category.equals("constr"))
                globSyntaxHighlights.addHighlight(startIndex, endIndex, constrAttr);
              else if(category.equals("not"))
                globSyntaxHighlights.addHighlight(startIndex, endIndex, notationAttr);
              else if(category.equals("thm"))
                globSyntaxHighlights.addHighlight(startIndex, endIndex, thmAttr);
              else if(category.equals("proj"))
                globSyntaxHighlights.addHighlight(startIndex, endIndex, defnAttr);
              else if(category.equals("ind"))
                globSyntaxHighlights.addHighlight(startIndex, endIndex, indAttr);
              else if(category.equals("rec"))
                globSyntaxHighlights.addHighlight(startIndex, endIndex, indAttr);
              else if(category.equals("var"))
                globSyntaxHighlights.addHighlight(startIndex, endIndex, varAttr);
              }
              
            } 
            
            
        }
        catch(IOException ex)
        {
          JOptionPane.showMessageDialog(uiWindow, "could not read glob file");
          
        }
        
    }

void debugUnivInconsistency()
    {        
        Pattern pat = Pattern.compile("\\(cannot enforce ([\\w_.]*)[ \\n]<[=]?[ \\n]([\\w_.]*)\\)");
        Matcher mat = pat.matcher(dbugcontents);
        DebugUnivInconst.Constraint violatedConstr;
        
        
        if(mat.find())
        {
            String toParse=mat.group().substring("(cannot enforce".length());
            violatedConstr=new DebugUnivInconst.Constraint(mat.group(1),mat.group(2),mat.group());
//            violatedConstr.addHelpfulNames(helpfulConstrNames);
        }
        else
        {
          setDbugcontents("could not parse the error message in the constraint. aborting");
          return;
        }
        
        CoqTopXMLIO.CoqRecMesg rec= getCoqtop().query("Print Universes.");        
        if(rec.success)
        {
            String constraints= rec.conciseReply;
            setDbugcontents(constraints);
            // strict equality of edge is true
            ArrayList<DebugUnivInconst.Constraint> cts = dbgC.parseConstraints(constraints);
            cts.add(violatedConstr);
            HashSet<String> highlightNodes=new HashSet<String>();
            highlightNodes.add(violatedConstr.lhs);
            highlightNodes.add(violatedConstr.rhs);
            int index=dbgC.debugUnivInconsistency(cts,highlightNodes);
            
        }

    }
    /**
     * final because it is called in the constructor
     */
    boolean keyListenerAssigned;
    final void assignCookie()
    {
        editor=getLookup().lookup(EditorCookie.class);
        assert(getEditor()!=null);
        //assert(getEditor().openDocument()!=null);
        StyledDocument doc;
        try {
            doc = getEditor().openDocument();
            assert(doc!=null);
            doc.addDocumentListener(this);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
  //      getEditor().getDocument().add
        //getEditor().getDocument().addUndoableEditListener(this);
        //getEditor().getDocument().add
    }
    
    @Override
    protected int associateLookup() {
        return 1;
    }

    cqDataObject getThisDataObject()
    {
        return this;
    }
    void getContents() {
        
            setDbugcontents("successfully started CoqTop version: \n" +getCoqtop().getVersion());
            
        
    }
    @MultiViewElement.Registration(
            displayName = "#LBL_cq_EDITOR",
            iconBase = "coq/1372847281_ok_16x16.gif",
            mimeType = "text/coq",
            persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
            preferredID = "cq",
            position = 1000)
    @Messages("LBL_cq_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }

    /**
     * @param highlighter the highlighter to set
     */
//    public void setHighlighter(CoqHighlighter highlighter) {
//        this.highlighter = highlighter;
//    }

}
