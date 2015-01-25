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

import java.util.*;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;


public class SJLanguageHierarchy extends LanguageHierarchy<SJTokenId> {

    private static List<SJTokenId> tokens;
    private static Map<Integer, SJTokenId> idToToken;


    private static void init() {
tokens = Arrays.asList(new SJTokenId[]{
 new SJTokenId("EOF", "whitespace", 0),
    new SJTokenId("WHITESPACE", "whitespace", 1),
    new SJTokenId("SINGLE_LINE_COMMENT", "comment", 4),
    new SJTokenId("FORMAL_COMMENT", "comment", 5),
    new SJTokenId("MULTI_LINE_COMMENT", "comment", 6),
    new SJTokenId("ABSTRACT", "keyword", 8),
    new SJTokenId("ASSERT", "keyword", 9),
    new SJTokenId("BOOLEAN", "keyword", 10),
    new SJTokenId("BREAK", "keyword", 11),
    new SJTokenId("BYTE", "keyword", 12),
    new SJTokenId("CASE", "keyword", 13),
    new SJTokenId("CATCH", "keyword", 14),
    new SJTokenId("CHAR", "keyword", 15),
    new SJTokenId("CLASS", "keyword", 16),
    new SJTokenId("CONST", "keyword", 17),
    new SJTokenId("CONTINUE", "keyword", 18),
    new SJTokenId("_DEFAULT", "keyword", 19),
    new SJTokenId("DO", "keyword", 20),
    new SJTokenId("DOUBLE", "keyword", 21),
    new SJTokenId("ELSE", "keyword", 22),
    new SJTokenId("ENUM", "keyword", 23),
    new SJTokenId("EXTENDS", "keyword", 24),
    new SJTokenId("FALSE", "keyword", 25),
    new SJTokenId("FINAL", "keyword", 26),
    new SJTokenId("FINALLY", "keyword", 27),
    new SJTokenId("FLOAT", "keyword", 28),
    new SJTokenId("FOR", "keyword", 29),
    new SJTokenId("GOTO", "keyword", 30),
    new SJTokenId("IF", "keyword", 31),
    new SJTokenId("IMPLEMENTS", "keyword", 32),
    new SJTokenId("IMPORT", "keyword", 33),
    new SJTokenId("INSTANCEOF", "keyword", 34),
    new SJTokenId("INT", "keyword", 35),
    new SJTokenId("INTERFACE", "keyword", 36),
    new SJTokenId("LONG", "keyword", 37),
    new SJTokenId("NATIVE", "keyword", 38),
    new SJTokenId("NEW", "keyword", 39),
    new SJTokenId("NULL", "keyword", 40),
    new SJTokenId("PACKAGE", "keyword", 41),
    new SJTokenId("PRIVATE", "keyword", 42),
    new SJTokenId("PROTECTED", "keyword", 43),
    new SJTokenId("PUBLIC", "keyword", 44),
    new SJTokenId("RETURN", "keyword", 45),
    new SJTokenId("SHORT", "keyword", 46),
    new SJTokenId("STATIC", "keyword", 47),
    new SJTokenId("STRICTFP", "keyword", 48),
    new SJTokenId("SUPER", "keyword", 49),
    new SJTokenId("SWITCH", "keyword", 50),
    new SJTokenId("SYNCHRONIZED", "keyword", 51),
    new SJTokenId("THIS", "keyword", 52),
    new SJTokenId("THROW", "keyword", 53),
    new SJTokenId("THROWS", "keyword", 54),
    new SJTokenId("TRANSIENT", "keyword", 55),
    new SJTokenId("TRUE", "keyword", 56),
    new SJTokenId("TRY", "keyword", 57),
    new SJTokenId("VOID", "keyword", 58),
    new SJTokenId("VOLATILE", "keyword", 59),
    new SJTokenId("WHILE", "keyword", 60),
    new SJTokenId("INTEGER_LITERAL", "literal", 61),
    new SJTokenId("DECIMAL_LITERAL", "literal", 62),
    new SJTokenId("HEX_LITERAL", "literal", 63),
    new SJTokenId("OCTAL_LITERAL", "literal", 64),
    new SJTokenId("FLOATING_POINT_LITERAL", "literal", 65),
    new SJTokenId("DECIMAL_FLOATING_POINT_LITERAL", "literal", 66),
    new SJTokenId("DECIMAL_EXPONENT", "number", 67),
    new SJTokenId("HEXADECIMAL_FLOATING_POINT_LITERAL", "literal", 68),
    new SJTokenId("HEXADECIMAL_EXPONENT", "number", 69),
    new SJTokenId("CHARACTER_LITERAL", "literal", 70),
    new SJTokenId("STRING_LITERAL", "literal", 71),
    new SJTokenId("IDENTIFIER", "identifier", 72),
    new SJTokenId("LETTER", "literal", 73),
    new SJTokenId("PART_LETTER", "literal", 74),
    new SJTokenId("LPAREN", "operator", 75),
    new SJTokenId("RPAREN", "operator", 76),
    new SJTokenId("LBRACE", "operator", 77),
    new SJTokenId("RBRACE", "operator", 78),
    new SJTokenId("LBRACKET", "operator", 79),
    new SJTokenId("RBRACKET", "operator", 80),
    new SJTokenId("SEMICOLON", "operator", 81),
    new SJTokenId("COMMA", "operator", 82),
    new SJTokenId("DOT", "operator", 83),
    new SJTokenId("AT", "operator", 84),
    new SJTokenId("ASSIGN", "operator", 85),
    new SJTokenId("LT", "operator", 86),
    new SJTokenId("BANG", "operator", 87),
    new SJTokenId("TILDE", "operator", 88),
    new SJTokenId("HOOK", "operator", 89),
    new SJTokenId("COLON", "operator", 90),
    new SJTokenId("EQ", "operator", 91),
    new SJTokenId("LE", "operator", 92),
    new SJTokenId("GE", "operator", 93),
    new SJTokenId("NE", "operator", 94),
    new SJTokenId("SC_OR", "operator", 95),
    new SJTokenId("SC_AND", "operator", 96),
    new SJTokenId("INCR", "operator", 97),
    new SJTokenId("DECR", "operator", 98),
    new SJTokenId("PLUS", "operator", 99),
    new SJTokenId("MINUS", "operator", 100),
    new SJTokenId("STAR", "operator", 101),
    new SJTokenId("SLASH", "operator", 102),
    new SJTokenId("BIT_AND", "operator", 103),
    new SJTokenId("BIT_OR", "operator", 104),
    new SJTokenId("XOR", "operator", 105),
    new SJTokenId("REM", "operator", 106),
    new SJTokenId("LSHIFT", "operator", 107),
    new SJTokenId("PLUSASSIGN", "operator", 108),
    new SJTokenId("MINUSASSIGN", "operator", 109),
    new SJTokenId("STARASSIGN", "operator", 110),
    new SJTokenId("SLASHASSIGN", "operator", 111),
    new SJTokenId("ANDASSIGN", "operator", 112),
    new SJTokenId("ORASSIGN", "operator", 113),
    new SJTokenId("XORASSIGN", "operator", 114),
    new SJTokenId("REMASSIGN", "operator", 115),
    new SJTokenId("LSHIFTASSIGN", "operator", 116),
    new SJTokenId("RSIGNEDSHIFTASSIGN", "operator", 117),
    new SJTokenId("RUNSIGNEDSHIFTASSIGN", "operator", 118),
    new SJTokenId("ELLIPSIS", "operator", 119),
    new SJTokenId("RUNSIGNEDSHIFT", "operator", 120),
    new SJTokenId("RSIGNEDSHIFT", "operator", 121),
    new SJTokenId("GT", "operator", 122)});
        idToToken = new HashMap<Integer, SJTokenId>();
        for (SJTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized SJTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<SJTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<SJTokenId> createLexer(LexerRestartInfo<SJTokenId> info) {
        return new SJLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/coq";
    }

}