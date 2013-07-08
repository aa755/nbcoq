/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

/**
 *
 * @author Abhishek
 */
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.mimelookup.MimeRegistrations;
import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.ZOrder;

@MimeRegistrations({
 //   @MimeRegistration(mimeType = "text/coq", service = HighlightsLayerFactory.class),
    @MimeRegistration(mimeType = "text/coq", service = HighlightsLayerFactory.class)
})
public class MarkHTMLOccurrencesHighlightsLayerFactory implements HighlightsLayerFactory {

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
    
        cqDataObject  dataObj=getDataObj(context);
    
        
        if(dataObj==null)
            return null;
        
        
        return new HighlightsLayer[]{
                    HighlightsLayer.create(
                    "coq compiled",
                    ZOrder.CARET_RACK.forPosition(2000),
                    true,
                    dataObj.getCompiledArea())
                };
    }
}