/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

import java.io.IOException;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
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
    private final int DOWN_BUTTON_STEP=128;
    private boolean initialized;   
    private CoqHighlighter highlighter;
    public cqDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        initialized=false;
        registerEditor("text/coq", true);
        coqtop=new CoqTopXMLIO();
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
    
    void handleDownButton() {
        if(!initialized)
            initialize();
        String code="";
        /* start a binary search for endpoint*/
        int step_size = DOWN_BUTTON_STEP;
        int endPos=getDocument().getEndPosition().getOffset();
        if(endPos==compiledOffset)
        {
            dbugcontents="reached end of file";
            return;
        }
            try {
                code = getDocument().getText(compiledOffset, step_size);
            } catch (BadLocationException ex) {
            try {
                //Exceptions.printStackTrace(ex); this pops up an error message
                code=getDocument().getText(compiledOffset,endPos-compiledOffset);
//                System.out.println("step size="+step_size);
            } catch (BadLocationException ex1) {
                Exceptions.printStackTrace(ex1);
                assert(false); // this should never happen;
            }
            }
        
        
        int dotOffset=code.indexOf('.');
        if(dotOffset==-1)
        {
            dbugcontents="the next "+DOWN_BUTTON_STEP+" chars dont contain a dot. try breaking up ur next stament.";
            return;            
        }
        
        String sendtocoq=code.substring(0, dotOffset+1);
        
        CoqTopXMLIO.CoqRecMesg rec=coqtop.interpret(sendtocoq);
        
        dbugcontents="sent: "+sendtocoq+" received "+rec.nuDoc.toXML();
        
        if(rec.success)
        {
            updateCompiledOffset(dotOffset+1);
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
