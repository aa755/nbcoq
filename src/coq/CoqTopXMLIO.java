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
import java.util.prefs.Preferences;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
//import nu.xom.ParsingException;
//import nu.xom.ValidityException;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.apache.commons.lang3.StringEscapeUtils;
import org.openide.util.NbPreferences;


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

    static String getCoqTopPath()
    {
        Preferences pref = NbPreferences.forModule(CoqOptionsPanel.class);
        return pref.get(CoqOptionsPanel.COQPATH_KEY, "");
    }
    
    public CoqTopXMLIO(FileObject fob) throws IOException {
        
        
            System.out.println("coq path: "+fob.getPath());
            String command=getCoqTopPath();
            process = new ProcessBuilder(command,"-ideslave").directory(FileUtil.toFile(fob)).start();
//            process = new ProcessBuilder("coqtop","-ideslave  -I "+fob.getPath()).start();
//         process = new ProcessBuilder("coqtop","-ideslave  -I "+path).directory(File.)+start();
        
            input = new PrintWriter(new OutputStreamWriter(process.getOutputStream()), true);
            result=new BufferedReader(new InputStreamReader(process.getInputStream()));
            System.out.println("started coq");
    }
    
    public synchronized CoqRecMesg communicate(CoqSendMesg msg)
    {
   //     System.out.println("writing:" +msg.toXML());
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
    
    public CoqRecMesg rewind(int steps)
    {
        return communicate(new CoqSendRewindMeg(steps));
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
        
        public CoqSendRewindMeg() {
            super("", "rewind");
            numSteps=1;
        }

        public CoqSendRewindMeg( int numSteps) {
            super("", "rewind");
            this.numSteps = numSteps;
        }
               
        @Override
        public String toXML()
        {            
            return "<call val=\"rewind\" steps=\""+numSteps+"\"/>";
        }        
    }
    
    public static class CoqRecMesg{
       nu.xom.Document nuDoc;
        //Document doc;
        boolean success;
     //   Element contents;
        private static final int BUF_SIZE=262144;
        private static final int NUM_TRIALS=128;
        
        char [] buf=new char [BUF_SIZE];
        
        void trySleep(int milis)
        {
           try {
               Thread.sleep(milis);
           } catch (InterruptedException ex) {
               Exceptions.printStackTrace(ex);
           }
        }
        
        int getExtraRewoudSteps()
        {
            nu.xom.Element intStr=nuDoc.getRootElement().getFirstChildElement("int");
            return Integer.parseInt(intStr.getValue());
        }
        
        public void parseXMLFromStream(BufferedReader result) {
            try {
                nu.xom.Builder b = new nu.xom.Builder();
                String answer = "";
                int count = 0;
                while (!result.ready())
                    trySleep(10);
                //    Thread.sleep(1);
                while (count < NUM_TRIALS) {
                    while (result.ready()) {
                        int numRead=result.read(buf,0,BUF_SIZE);
                        if(numRead>0)
                        {
                           answer = answer + new String(buf,0,numRead);
                        }
                    }
                    try {
                       System.out.println("trying to parse:"+answer);
                        nuDoc = b.build(answer.trim(), null);
                        String status=nuDoc.getRootElement().getAttribute("val").getValue();
                      //  System.out.println("status="+status);
                        success=(status.equals("good"));
                        break;
                    }
                    catch (Exception ex) {
                        //Exceptions.printStackTrace(ex);
                        System.err.println("parse error count=" + count +"size o/p:"+answer.length());
                        count = count + 1;
                        trySleep(2*(count));
                        continue;

                    }
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    
}