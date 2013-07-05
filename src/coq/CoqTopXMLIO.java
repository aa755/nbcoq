/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

//import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import nu.xom.Builder;
//import nu.xom.ParsingException;
//import nu.xom.ValidityException;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Abhishek
 */
public class CoqTopXMLIO {
    
private  Process process;
private  BufferedReader result;
//private static BufferedReader err;
private  PrintWriter input;
//private static PrintWriter input;

    public CoqTopXMLIO() throws IOException {
            process = new ProcessBuilder("coqtop","-ideslave").start();
            input = new PrintWriter(new OutputStreamWriter(process.getOutputStream()), true);
            result=new BufferedReader(new InputStreamReader(process.getInputStream()));
    }
    
    public CoqRecMesg communicate(CoqSendMesg msg)
    {
        System.out.println("writing:" +msg.toXML());
        input.write(msg.toXML());
        input.flush();
        System.out.println("wrote:" +msg.toXML());
        CoqRecMesg ret= new CoqRecMesg();
        
        ret.parseXMLFromStream(result);
        return ret;        
    }

    
    public static class CoqSendMesg{
        public String mesg;

        public CoqSendMesg(String mesg) {
            this.mesg = mesg;
        }
        
        public String toXML()
        {            
            return "<call val=\"interp\">"+mesg+"</call>";
        }
    }
    
    public static class CoqRecMesg{
       nu.xom.Document nuDoc;
        Document doc;
        boolean success;
        Element contents;
        char [] buf=new char [256];
        
        
        public void parseXMLFromStream(BufferedReader result) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                //  DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                nu.xom.Builder b = new nu.xom.Builder();
                String answer = "";
                int count = 0;
                while (!result.ready());
                //    Thread.sleep(1);
                while (count < 10) {
                    while (result.ready()) {
                        result.read(buf);
                        answer = answer + new String(buf);
                        
                    }
                    try {
                       System.out.println("trying to parse:"+answer);
                        nuDoc = b.build(answer.trim(), null);
                        break;
                    } catch (Exception ex) {
                        //Exceptions.printStackTrace(ex);
                        System.err.println("parse error count=" + count);
                        count = count + 1;
                        continue;
                    }
                    /*
                     try {
                     System.out.println("trying to parse:"+answer);
                     doc = dBuilder.parse(new InputSource(new StringReader(answer.trim())));
                     break;
                     } catch (SAXException ex) {
                     //Exceptions.printStackTrace(ex);
                     System.err.println("parse error count="+count);
                     count=count+1;
                     continue;
                     }catch (ParserConfigurationException ex) {
                     Exceptions.printStackTrace(ex);
                     }
                     }
                     */

                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}