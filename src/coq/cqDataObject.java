/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
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

    private Process process;
    private BufferedReader result;
    private PrintWriter input;
    public String contents;
    private FileObject fob;
    public cqDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/coq", true);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    void getContents() {
        try {
            fob = getPrimaryFile();
            String fname = FileUtil.getFileDisplayName(fob);
            contents=fname+"\n---\n";
            process = new ProcessBuilder("coqtop").redirectErrorStream(true).start();

            input = new PrintWriter(new OutputStreamWriter(process.getOutputStream()), true);
            String message="Print nat.";
            input.println(message);
            //System.out.println(message);

            result = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;// = new String();

            line = result.readLine();
            //while ((line = result.readLine()) == null) {

                /* Some processing for the read line */

                contents=contents+"\n"+line;
            //}        
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
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
}
