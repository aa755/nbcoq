/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

/**
 *
 * @author Abhishek
 */
import java.awt.Color;
import java.lang.ref.WeakReference;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.mimelookup.MimeRegistrations;
import org.netbeans.api.editor.settings.AttributesUtilities;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.ZOrder;
import org.netbeans.spi.editor.highlighting.support.OffsetsBag;

@MimeRegistrations({
 //   @MimeRegistration(mimeType = "text/coq", service = HighlightsLayerFactory.class),
    @MimeRegistration(mimeType = "text/coq", service = HighlightsLayerFactory.class)
})
public class CoqHighlighter implements HighlightsLayerFactory {
    /*
    public static MarkHTMLOccurrencesHighlighter getMarkOccurrencesHighlighter(Document doc) {
        MarkHTMLOccurrencesHighlighter highlighter =
               (MarkHTMLOccurrencesHighlighter) doc.getProperty(MarkHTMLOccurrencesHighlighter.class);
        if (highlighter == null) {
            doc.putProperty(MarkHTMLOccurrencesHighlighter.class,
               highlighter = new MarkHTMLOccurrencesHighlighter(doc));
        }
        return highlighter;
    }
*/
    
    static cqDataObject getDataObj(Context context)
    {
        return (cqDataObject) context.getDocument().getProperty(cqDataObject.class);
    }
    

    
    @Override
    public HighlightsLayer[] createLayers(Context context) {
    
       // cqDataObject  dataObj=getDataObj(context);
    
        
        
           
        WeakReference<Document> weakDoc;
              weakDoc = new WeakReference<Document>(context.getDocument());
             cqDataObject dobj = (cqDataObject) NbEditorUtilities.getDataObject(weakDoc.get());
    //         dobj.setHighlighter(this);
             if(dobj.getRetb()==null)
                   dobj.setRetb(new OffsetsBag(context.getDocument()));
           // context.getDocument().putProperty(MarkHTMLOccurrencesHighlightsLayerFactory.class, this);
//            retb.addHighlight(context.getDocument().getStartPosition().getOffset(), context.getDocument().getEndPosition().getOffset(), compiledCodeAttr);
            return new HighlightsLayer[]{
                    HighlightsLayer.create(
                    "coq compiled",
                    ZOrder.CARET_RACK.forPosition(2000),
                    true,
                    dobj.getRetb())
                };
        
/*        
        return new HighlightsLayer[]{
                    HighlightsLayer.create(
                    "coq compiled",
                    ZOrder.CARET_RACK.forPosition(2000),
                    true,
                    dataObj.getCompiledArea())
                };
    */
    }
}