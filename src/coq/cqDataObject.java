/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@Messages({
    "LBL_cq_LOADER=Files of cq"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_cq_LOADER",
        mimeType = "text/coq",
        extension = {"v"})
@DataObject.Registration(
        mimeType = "text/coq",
        iconBase = "coq/1372847281_ok_16x16.gif",
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
public class cqDataObject extends MultiDataObject {

    private CoqTopXMLIO coqtop;
    public String dbugcontents;
    private int compiledOffset;
    private EditorCookie editor;
    private final int DOWN_BUTTON_STEP=10000;
    private boolean initialized;   
    private CoqHighlighter highlighter;
    private static final String comment_reg="(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)";
    private static final String command_reg="([^\\s]*\\.[\\s])";
    private static final String command_start="([\\s]*[^\\(\\-])";
    private static final String command_end="";
    private static final String comment_start="([\\s]*\\(\\*)";
    private static final Pattern coq=Pattern.compile(command_reg+"|"+comment_reg);
    private static final Pattern coqStart=Pattern.compile(command_start+"|"+comment_start);
    private static final Pattern coqCommandEnd=Pattern.compile("(\\.[\\s])");
    private static final Pattern coqComment=Pattern.compile("(\\(\\*)|(\\*\\))");
    public cqDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader); 
        initialized=false;
        registerEditor("text/coq", true);
        coqtop=new CoqTopXMLIO(pf.getParent());
        compiledOffset=0;    
    //    initialize();
    }

    void updateCompiledOffset(int change)
    {
        compiledOffset=compiledOffset+change;
        highlighter.setHighlight(0, compiledOffset);
    }
    
    final void initialize()
    {
        initialized=true;
        assignCookie();
        /*
         StyleContext sc = StyleContext.getDefaultStyleContext();
        // StyleConstants.setBackground((MutableAttributeSet)SimpleAttributeSet.EMPTY, Color.red);
        compiledCodeAttr = sc.addAttribute(
                SimpleAttributeSet.EMPTY,
                StyleConstants.CharacterConstants.Background, Color.GREEN);
        compiledCodeAttr = sc.addAttribute(
                compiledCodeAttr,
                StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
                * */
        //getDocument().set
       // setHighlighter((MarkHTMLOccurrencesHighlightsLayerFactory) getDocument().getProperty(MarkHTMLOccurrencesHighlightsLayerFactory.class));
        assert(highlighter!=null);
        
     }
    
    private StyledDocument getDocument()
    {
        return editor.getDocument();
    }
    int getOffsetToSend()
    {
        try {
            int offset=0;
            int endPos=getDocument().getEndPosition().getOffset();        
            String code=getDocument().getText(compiledOffset, endPos-compiledOffset);
            Matcher commandEndMatcher = coqStart.matcher(code);
            if(commandEndMatcher.find())
            {
                int cstart=commandEndMatcher.group().indexOf("(*");
                if(cstart!=-1)
                {  /* the next segment is a pure comment*/
                    offset=code.indexOf("*)", endPos)+1;
                    //char at offset is )
                }
                else
                {
                    boolean done=false;
                    int unmatchedComLeft=0;
                    int unmatchedStrLift=0;
                        commandEndMatcher=coqCommandEnd.matcher(code);
                        while (commandEndMatcher.find())
                        {
                            Matcher comments=coqComment.matcher(code.substring(0, commandEndMatcher.end()));
                            while (comments.find())
                            {
                                if(comments.group().equals("*)"))
                                    unmatchedComLeft=unmatchedComLeft-1;
                                    
                                if(comments.group().equals("(*"))
                                    unmatchedComLeft=unmatchedComLeft+1;
                            }
                            if(unmatchedComLeft==0)
                            {
                                offset=commandEndMatcher.end()-1;
                                // code[offset]='.'
                                break;
                            }
                        }
                }
            }
            
            
            return offset;
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
            assert(false);
            return -1;
        }
        
        
    }
    
    synchronized void  handleDownButton() {
        if(!initialized)
            initialize();
        
        int dotOffset=getOffsetToSend();
        String sendtocoq="";
        try {
            sendtocoq = getDocument().getText(compiledOffset, dotOffset);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
            assert(false);
        }
        
        CoqTopXMLIO.CoqRecMesg rec=coqtop.interpret(sendtocoq);
        
        dbugcontents="sent: "+sendtocoq+" received "+rec.nuDoc.toXML();
        
        if(rec.success)
        {
                updateCompiledOffset (dotOffset+1);
        }  
        //System.out.println("compiled area:"+getCompiledArea());
        //compiledArea=new OffsetsBag(getDocument());
       // getCompiledArea().clear();
        //getCompiledArea().addHighlight(0, compiledOffset, compiledCodeAttr);
       
        //getDocument().setCharacterAttributes(0, compiledOffset, compiledCodeAttr, false);
    }

    /**
     * final because it is called in the constructor
     */
    final void assignCookie()
    {
        editor=getLookup().lookup(EditorCookie.class);
        assert(editor!=null);
    }
    
    @Override
    protected int associateLookup() {
        return 1;
    }

    void getContents() {
        
            dbugcontents= "successfully started CoqTop version: \n" +coqtop.getVersion();
            
        
    }
    /*
    
     @Override
     protected Node createNodeDelegate() {
     return new DataNode(
     this,
     Children.create(new AbcChildFactory(this), true),
     getLookup());
     }

     private static class AbcChildFactory extends ChildFactory<String> {

     private final cqDataObject dObj;

     public AbcChildFactory(cqDataObject dObj) {
     this.dObj = dObj;
     }

     @Override
     protected boolean createKeys(List list) {
     FileObject fObj = dObj.getPrimaryFile();
     try {
     List<String> dObjContent = fObj.asLines();
     list.addAll(dObjContent);
     } catch (IOException ex) {
     Exceptions.printStackTrace(ex);
     }
     return true;
     }

     @Override
     protected Node createNodeForKey(String key) {
     Node childNode = new AbstractNode(Children.LEAF);
     childNode.setDisplayName(key);
     return childNode;
     }

     }
     */
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
    public void setHighlighter(CoqHighlighter highlighter) {
        this.highlighter = highlighter;
    }

    /**
     * @return the compiledArea
     */
}
