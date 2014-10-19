/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coq;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author abhishek
 */
public class CoqDocPostProc {

  /**
   * @param args the command line arguments
   */
  
  static String readFile(String path) 
  throws IOException 
  {
  byte[] encoded = Files.readAllBytes(Paths.get(path));
  return new String(encoded, Charset.defaultCharset());
  } 

/**
 * in some cases, the replacement for a regexp match
 * just needs to be unique. so it can be parametrized
 * by an index which is guaranteed by the caller to
 * be different for each replacement.
 * 
 * This is needed because div id's in HTML needs to
 * be unique.
 */
public interface IndexBasedReplace
{
  public String replacement (int index, String match);
}

static String replace (Pattern pat, IndexBasedReplace ir, String inp)
{
     Matcher m = pat.matcher(inp);
     int count=0;
     StringBuffer result=new StringBuffer();
      while (m.find())
      {
        m.appendReplacement(result, ir.replacement(count, m.group()));
        count=count+1;
      }
      
      m.appendTail(result);
      return result.toString();
}


public static class BeginHideReplace implements IndexBasedReplace
{
    @Override
    public String replacement (int index, String match)
    {
        String name = "ShowHide"+ (2323+index);
        return "<div class=\"toggleproof\" "
                + "onclick=\"toggleDisplay('"+name+"')\">"+name+"</div>\n" +
               "<div class=\"hidableCoqSegment849328\" id=\""+name+"\">";
        
    }
}

public static class ProofReplace implements IndexBasedReplace
{
    @Override
    public String replacement (int index, String match)
    {
        String name = "Proof"+(4343+index);
        return "<span class=\"id\" type=\"keyword\""
               + "onclick=\"toggleDisplay('"+name+"')\">"+name+"</span>"
                + "<div class=\"hidableCoqSegment849328\" id=\""+name+"\">";
        
    }
}

public static class InsertJavaScriptIncl implements IndexBasedReplace
{
    @Override
    public String replacement (int index, String match)
    {
        return match
                + "<script type=\"text/javascript\" src=\"coq2html.js\"> </script>";
    }
}

public static class BodyOnLoad implements IndexBasedReplace
{
    @Override
    public String replacement (int index, String match)
    {
        return "<body onload=\"hideAll('hidableCoqSegment849328')\">";
    }
}

public static class EndHideReplace implements IndexBasedReplace
{
    @Override
    public String replacement (int index, String match)
    {
        return "</div>";
    }
}

public static class QedReplace implements IndexBasedReplace
{
    @Override
    public String replacement (int index, String match)
    {
        return match+"</div>";
    }
}

public static void main(String[] args) throws IOException {
    String filename = args[0];
    String fileStr = readFile (filename);
    String pass;
    {
      Pattern begHide = Pattern.compile("<div class=\"doc\">\n" +
                        "begin hide \n" +
                        "</div>");
      pass = replace(begHide, new BeginHideReplace(), fileStr);
    }
    
    {
      Pattern endHide = Pattern.compile("<div class=\"doc\">\n" +
                        "end hide \n" +
                        "</div>");
      pass = replace(endHide, new EndHideReplace(), pass);
    }
  
    {
      Pattern head = Pattern.compile("<head>");
      pass = replace(head, new InsertJavaScriptIncl(), pass);
    }
    
    {
      Pattern proof = Pattern.compile("<span class=\"id\" type=\"keyword\">Proof</span>");
      pass = replace(proof, new ProofReplace(), pass);
    }
    
    {
      Pattern qed = Pattern.compile("<span class=\"id\" type=\"keyword\">Qed</span>");
      pass = replace(qed, new QedReplace(), pass);
    }

    {
      Pattern defined = Pattern.compile("<span class=\"id\" type=\"keyword\">Defined</span>");
      pass = replace(defined, new QedReplace(), pass);
    }

    {
      Pattern head = Pattern.compile("<body>");
      pass = replace(head, new BodyOnLoad(), pass);
    }

    System.out.println(pass);
  }
  
}
