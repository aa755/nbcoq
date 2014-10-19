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

/**
 *
 * @author abhishek
 */
public class CoqDocPreProc {

  /**
   * @param args the command line arguments
   */
  
  static String readFile(String path) 
  throws IOException 
  {
  byte[] encoded = Files.readAllBytes(Paths.get(path));
  return new String(encoded, Charset.defaultCharset());
  } 
  
  public static void main(String[] args) throws IOException {
    String filename = args[0];
    String fileStr = readFile (filename);
    
    
  }
  
}
