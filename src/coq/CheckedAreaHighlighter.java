/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

/**
 *
 * @author Abhishek
 */
import java.lang.ref.WeakReference;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.mimelookup.MimeRegistrations;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.ZOrder;
import org.netbeans.spi.editor.highlighting.support.OffsetsBag;

@MimeRegistrations({
    @MimeRegistration(mimeType = "text/coq", service = HighlightsLayerFactory.class)
})
public class CheckedAreaHighlighter implements HighlightsLayerFactory {
    
    static cqDataObject getDataObj(Context context)
    {
        return (cqDataObject) context.getDocument().getProperty(cqDataObject.class);
    }
    

    
    @Override
    public HighlightsLayer[] createLayers(Context context) {
        WeakReference<Document> weakDoc;
              weakDoc = new WeakReference<Document>(context.getDocument());
             cqDataObject dobj = (cqDataObject) NbEditorUtilities.getDataObject(weakDoc.get());
             
             if(dobj.getCheckedAreaHighlights()==null)
                   dobj.setCheckedAreaHighlights(new OffsetsBag(context.getDocument()));

             return new HighlightsLayer[]{
                    HighlightsLayer.create(
                    "coq compiled",
                    ZOrder.CARET_RACK.forPosition(2000),
                    true,
                    dobj.getCheckedAreaHighlights())
                };
        
    }
}