/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coq;

//import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
//import nu.xom.ParsingException;
//import nu.xom.ValidityException;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.apache.commons.lang3.StringEscapeUtils;


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

    public CoqTopXMLIO(FileObject fob) throws IOException {
        
        
            System.out.println("coq path: "+fob.getPath());
            process = new ProcessBuilder("coqtop","-ideslave").directory(FileUtil.toFile(fob)).start();
//            process = new ProcessBuilder("coqtop","-ideslave  -I "+fob.getPath()).start();
//         process = new ProcessBuilder("coqtop","-ideslave  -I "+path).directory(File.)+start();
        
            input = new PrintWriter(new OutputStreamWriter(process.getOutputStream()), true);
            result=new BufferedReader(new InputStreamReader(process.getInputStream()));
            System.out.println("started coq");
    }
    
    public synchronized CoqRecMesg communicate(CoqSendMesg msg)
    {
        System.out.println("writing:" +msg.toXML());
        input.write(msg.toXML());
        input.flush();
        System.out.println("wrote:" +msg.toXML());
        CoqRecMesg ret= new CoqRecMesg();
        
        ret.parseXMLFromStream(result);
        return ret;        
    }

    public CoqRecMesg interpret(String code)
    {
        return communicate(new CoqSendMesg(code));
    }
    
    public String getVersion()
    {
        CoqRecMesg rec=communicate(CoqSendMesg.getVersionMesg());
        if(!rec.success)
            return "failure in init of CoqTop";
        else
            return rec.nuDoc.toXML();
    }
    
    public nu.xom.Document getGoal()
    {
        CoqRecMesg rec=communicate(CoqSendMesg.goalMessage());
        if(!rec.success)
            return null;
        else
            return rec.nuDoc;
    }

    public static class CoqSendMesg{
        public String mesg;
        public String type;


        static CoqSendMesg getVersionMesg()
        {
            return new CoqSendMesg("", "about");
        }
        
        public CoqSendMesg(String mesg, String type) {
            this.mesg = mesg;
            this.type = type;
        }

        public CoqSendMesg(String mesg) {
            this.mesg = mesg;
            this.type= "interp";
        }
        
        public static CoqSendMesg goalMessage()
        {
            return new CoqSendMesg("", "goal");
        }
        public String toXML()
        {            
            return "<call val=\""+ type +"\">"+ StringEscapeUtils.escapeXml(mesg)+"</call>";
        }
    }
    
    public static class CoqSendRewindMeg extends CoqSendMesg
    {

        int numSteps;
        
        public CoqSendRewindMeg(String mesg) {
            super(mesg, "rewind");
            numSteps=1;
        }

        public CoqSendRewindMeg( String mesg, int numSteps) {
            super(mesg, "rewind");
            this.numSteps = numSteps;
        }
               
        public String toXML()
        {            
            return "<call val=\"rewind\" steps="+numSteps+"/>";
        }        
    }
    
    public static class CoqRecMesg{
       nu.xom.Document nuDoc;
        Document doc;
        boolean success;
     //   Element contents;
        char [] buf=new char [20480];
        
        void trySleep(int milis)
        {
           try {
               Thread.sleep(milis);
           } catch (InterruptedException ex) {
               Exceptions.printStackTrace(ex);
           }
        }
        public void parseXMLFromStream(BufferedReader result) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                //  DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                nu.xom.Builder b = new nu.xom.Builder();
                String answer = "";
                int count = 0;
                while (!result.ready())
                    trySleep(10);
                //    Thread.sleep(1);
                while (count < 5) {
                    trySleep(2*(count+1));
                    while (result.ready()) {
                        result.read(buf);
                        answer = answer + new String(buf);
                     
                    }
                    try {
                       System.out.println("trying to parse:"+answer);
                        nuDoc = b.build(answer.trim(), null);
                        String status=nuDoc.getRootElement().getAttribute("val").getValue();
                        System.out.println("status="+status);
                        success=(status.equals("good"));
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