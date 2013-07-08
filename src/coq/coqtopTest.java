package coq;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package coq;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

//import org.openide.util.Exceptions;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Abhishek
 */
public class coqtopTest {

    /**
     * @param args the command line arguments
     */
private static Process process;
private static BufferedReader result;
private static BufferedReader err;
private static PrintWriter input;

    
    public static void main(String[] args) {
        // TODO code application logic here
                try {
            process = new ProcessBuilder("coqtop").start();

            input = new PrintWriter(new OutputStreamWriter(process.getOutputStream()), true);
            result = new BufferedReader(new InputStreamReader(process.getInputStream()));
            err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            process.get
            String message;
            char [] cbuf=new char[2];
            char [] cerr=new char[10];
            String line;// = new String();
            int count=0;
            while (result.ready() || count<10)
            {
                result.read();
                count++;
            }
            while (err.ready()) 
                err.read();
         while(true)
         {
             message=JOptionPane.showInputDialog("com");
             
            input.println(message);
            input.flush();
            //System.out.println(message);


          //  line = result.readLine();
            //    System.out.println(line);
//            while ((line = result.readLine()) != null) {
  //              System.out.println(line);        }
//            err.read(cerr, 0, 6);
  //          System.err.println(new String(cerr)); 
            
            while (!result.ready());

            while (result.ready()) 
                System.out.print(""+(char)result.read());  
            
             System.out.println("----------");
            }
            
        } catch (IOException ex) {
        }

    }
}
