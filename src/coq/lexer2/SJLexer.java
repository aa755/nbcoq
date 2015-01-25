/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coq.lexer2;

/**
 *
 * @author Abhishek
 */
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import coq.lexer.JavaCharStream;
import coq.lexer.JavaParserTokenManager;
import coq.lexer.Token;
import org.netbeans.spi.lexer.LexerInput;

class SJLexer implements Lexer<SJTokenId> {

    private LexerRestartInfo<SJTokenId> info;
    private JavaParserTokenManager javaParserTokenManager;

    SJLexer(LexerRestartInfo<SJTokenId> info) {
        this.info = info;
//        JavaCharStream stream = new JavaCharStream(info.input());
    //    info.input().
  //     javaParserTokenManager = new JavaParserTokenManager(stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<SJTokenId> nextToken() {
        //Token token = javaParserTokenManager.getNextToken();
        //if (info.input().readLength() < 1) {
          //  return null;
        //}
      int c = info.input().read();
      if (c== LexerInput.EOF)
        return null;
      else
        return info.tokenFactory().createToken(SJLanguageHierarchy.getToken(
          72));
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }

}
