/* The following code was generated by JFlex 1.6.0 */

package microLIR.parser;

import java_cup.runtime.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.0
 * from the specification file <tt>src/microLIR/parser/LIR.lex</tt>
 */
public class Lexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\1\1\2\1\72\1\13\1\2\22\0\1\11\1\10\1\14"+
    "\1\12\4\10\1\20\1\21\2\10\1\26\1\10\1\24\1\71\1\3"+
    "\11\3\1\25\2\10\1\27\3\10\1\34\1\4\1\60\1\52\1\67"+
    "\1\43\1\66\1\4\1\53\1\63\1\4\1\40\1\30\1\55\1\56"+
    "\2\4\1\6\1\47\1\64\1\4\1\70\1\4\1\57\2\4\1\22"+
    "\1\15\1\23\1\10\1\7\1\10\1\36\1\51\1\54\1\46\1\33"+
    "\1\5\1\41\1\42\1\44\2\5\1\45\1\61\1\17\1\31\1\62"+
    "\1\5\1\35\1\65\1\16\1\50\1\32\2\5\1\37\1\5\4\10"+
    "\6\0\1\72\u1fa2\0\1\72\1\72\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\udfe6\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\1\1\2"+
    "\1\1\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
    "\1\15\15\1\2\5\1\16\1\0\1\17\16\0\1\20"+
    "\5\0\1\5\1\0\1\21\1\22\1\23\1\0\1\24"+
    "\2\0\1\25\1\26\1\27\1\30\1\31\1\32\1\33"+
    "\3\0\1\2\1\5\1\34\4\0\1\35\1\0\1\5"+
    "\6\0\1\36\2\0\1\37\1\0\1\40\6\0\1\41"+
    "\2\0\1\42\4\0\1\43\1\0\1\44\10\0\1\45"+
    "\1\0\1\46\1\47\2\0\1\50\2\0\1\51\1\0"+
    "\1\52\1\53";

  private static int [] zzUnpackAction() {
    int [] result = new int[134];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\73\0\166\0\261\0\354\0\u0127\0\u0162\0\u019d"+
    "\0\u01d8\0\73\0\73\0\73\0\73\0\73\0\73\0\73"+
    "\0\73\0\u0213\0\u024e\0\u0289\0\u02c4\0\u02ff\0\u033a\0\u0375"+
    "\0\u03b0\0\u03eb\0\u0426\0\u0461\0\u049c\0\u04d7\0\u0512\0\u054d"+
    "\0\u0162\0\u01d8\0\73\0\u0588\0\u05c3\0\u05fe\0\u0639\0\u0674"+
    "\0\u06af\0\u06ea\0\u0725\0\u0760\0\u079b\0\u07d6\0\u0811\0\u084c"+
    "\0\u0887\0\73\0\u08c2\0\u08fd\0\u0938\0\u0973\0\u09ae\0\u09e9"+
    "\0\u0a24\0\73\0\73\0\73\0\u0a5f\0\73\0\u0a9a\0\u0ad5"+
    "\0\73\0\73\0\73\0\73\0\73\0\73\0\73\0\u0b10"+
    "\0\u0b4b\0\u0b86\0\73\0\u0bc1\0\u0bfc\0\u0c37\0\u0c72\0\u0cad"+
    "\0\u0ce8\0\u0d23\0\u0d5e\0\u0d99\0\u0dd4\0\u0e0f\0\u0e4a\0\u0e85"+
    "\0\u0ec0\0\u0efb\0\u0f36\0\u0f71\0\u0fac\0\u0fe7\0\u1022\0\u0512"+
    "\0\u105d\0\u1098\0\u10d3\0\u110e\0\u1149\0\u1184\0\73\0\u11bf"+
    "\0\u11fa\0\73\0\u1235\0\u1270\0\u12ab\0\u12e6\0\73\0\u1321"+
    "\0\73\0\u135c\0\u1397\0\u13d2\0\u140d\0\u1448\0\u1483\0\u14be"+
    "\0\u14f9\0\73\0\u1534\0\73\0\73\0\u156f\0\u15aa\0\73"+
    "\0\u15e5\0\u1620\0\73\0\u165b\0\73\0\73";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[134];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\2\3\1\4\1\2\1\5\1\6\1\7\1\2"+
    "\1\3\1\10\1\3\1\11\1\2\2\5\1\12\1\13"+
    "\1\14\1\15\1\16\1\17\1\20\1\21\1\22\3\5"+
    "\1\23\3\5\1\24\2\5\1\2\3\5\1\25\2\5"+
    "\1\26\1\27\1\5\1\30\1\31\1\32\1\33\2\5"+
    "\1\34\1\2\1\5\2\2\1\35\1\36\75\0\2\3"+
    "\6\0\1\3\1\0\1\3\62\0\1\4\72\0\5\5"+
    "\6\0\2\5\10\0\41\5\5\0\5\37\6\0\2\37"+
    "\10\0\3\37\1\40\35\37\5\0\5\41\6\0\2\41"+
    "\10\0\41\41\2\0\2\10\1\0\10\10\1\0\57\10"+
    "\3\0\10\42\1\0\1\43\1\44\54\42\32\0\1\45"+
    "\16\0\1\46\41\0\1\47\15\0\1\50\10\0\1\51"+
    "\70\0\1\52\44\0\1\53\31\0\1\54\55\0\1\55"+
    "\10\0\1\56\45\0\1\57\104\0\1\60\1\0\1\61"+
    "\74\0\1\62\66\0\1\63\72\0\1\64\111\0\1\65"+
    "\66\0\1\66\117\0\1\67\4\0\5\37\6\0\2\37"+
    "\10\0\41\37\5\0\5\37\6\0\1\70\1\37\10\0"+
    "\41\37\16\0\4\42\105\0\1\71\13\0\1\72\71\0"+
    "\1\73\73\0\1\74\61\0\1\75\103\0\1\76\75\0"+
    "\1\77\57\0\1\100\105\0\1\101\75\0\1\102\50\0"+
    "\1\103\114\0\1\104\34\0\1\105\115\0\1\106\66\0"+
    "\1\107\116\0\1\110\72\0\1\111\46\0\1\112\35\0"+
    "\2\67\1\113\10\67\1\0\56\67\4\0\5\37\6\0"+
    "\2\37\10\0\20\37\1\114\20\37\35\0\1\115\75\0"+
    "\1\116\71\0\1\117\53\0\1\120\136\0\1\121\72\0"+
    "\1\122\26\0\1\123\57\0\5\37\6\0\2\37\10\0"+
    "\5\37\1\124\33\37\36\0\1\125\6\0\1\126\66\0"+
    "\1\127\71\0\1\130\100\0\1\131\64\0\1\132\74\0"+
    "\1\133\2\0\1\134\20\0\1\135\1\0\1\136\54\0"+
    "\1\137\25\0\5\37\6\0\1\37\1\140\10\0\41\37"+
    "\37\0\1\141\101\0\1\142\66\0\1\143\67\0\1\144"+
    "\111\0\1\145\53\0\1\146\124\0\1\147\41\0\1\150"+
    "\71\0\1\151\124\0\1\152\41\0\1\153\71\0\1\154"+
    "\70\0\1\155\72\0\1\156\76\0\1\157\113\0\1\160"+
    "\45\0\1\161\104\0\1\162\75\0\1\163\67\0\1\164"+
    "\63\0\1\165\101\0\1\166\44\0\1\167\111\0\1\170"+
    "\121\0\1\171\40\0\1\172\117\0\1\173\51\0\1\174"+
    "\101\0\1\175\65\0\1\176\76\0\1\177\60\0\1\200"+
    "\75\0\1\201\52\0\1\202\121\0\1\203\72\0\1\204"+
    "\67\0\1\205\75\0\1\206\25\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5782];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\7\1\10\11\20\1\1\0\1\11\16\0"+
    "\1\11\5\0\1\1\1\0\3\11\1\0\1\11\2\0"+
    "\7\11\3\0\1\11\2\1\4\0\1\1\1\0\1\1"+
    "\6\0\1\1\2\0\1\1\1\0\1\1\6\0\1\11"+
    "\2\0\1\11\4\0\1\11\1\0\1\11\10\0\1\11"+
    "\1\0\2\11\2\0\1\11\2\0\1\11\1\0\2\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[134];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
	public int getLineNumber() { return yyline+1; }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 204) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;           
    int totalRead = 0;
    while (totalRead < requested) {
      int numRead = zzReader.read(zzBuffer, zzEndRead + totalRead, requested - totalRead);
      if (numRead == -1) {
        break;
      }
      totalRead += numRead;
    }

    if (totalRead > 0) {
      zzEndRead += totalRead;
      if (totalRead == requested) { /* possibly more input available */
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      return false;
    }

    // totalRead = 0: End of stream
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) throws RuntimeException {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new RuntimeException(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  throws RuntimeException {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public Token next_token() throws java.io.IOException, RuntimeException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          zzR = false;
          break;
        case '\r':
          yyline++;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
          }
          break;
        default:
          zzR = false;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 1: 
          { throw new RuntimeException("Illegal character at line " + (yyline+1) + " : '" + yytext() + "'");
          }
        case 44: break;
        case 2: 
          { 
          }
        case 45: break;
        case 3: 
          { return new Token(yyline, "NUMBER", sym.NUMBER, new Integer(yytext()));
          }
        case 46: break;
        case 4: 
          { return new Token(yyline, "VAR", sym.VAR, yytext());
          }
        case 47: break;
        case 5: 
          { return new Token(yyline, "REG", sym.REG, yytext());
          }
        case 48: break;
        case 6: 
          { return new Token(yyline, yytext(), sym.LP);
          }
        case 49: break;
        case 7: 
          { return new Token(yyline, yytext(), sym.RP);
          }
        case 50: break;
        case 8: 
          { return new Token(yyline, yytext(), sym.LB);
          }
        case 51: break;
        case 9: 
          { return new Token(yyline, yytext(), sym.RB);
          }
        case 52: break;
        case 10: 
          { return new Token(yyline, yytext(), sym.DOT);
          }
        case 53: break;
        case 11: 
          { return new Token(yyline, yytext(), sym.COLON);
          }
        case 54: break;
        case 12: 
          { return new Token(yyline, yytext(), sym.COMMA);
          }
        case 55: break;
        case 13: 
          { return new Token(yyline, yytext(), sym.ASSIGN);
          }
        case 56: break;
        case 14: 
          { return new Token(yyline, "Label", sym.LABEL, yytext());
          }
        case 57: break;
        case 15: 
          { return new Token(yyline, "String", sym.STRING, yytext());
          }
        case 58: break;
        case 16: 
          { return new Token(yyline, yytext(), sym.OR);
          }
        case 59: break;
        case 17: 
          { return new Token(yyline, yytext(), sym.MOD);
          }
        case 60: break;
        case 18: 
          { return new Token(yyline, yytext(), sym.MUL);
          }
        case 61: break;
        case 19: 
          { return new Token(yyline, yytext(), sym.AND);
          }
        case 62: break;
        case 20: 
          { return new Token(yyline, yytext(), sym.ADD);
          }
        case 63: break;
        case 21: 
          { return new Token(yyline, yytext(), sym.SUB);
          }
        case 64: break;
        case 22: 
          { return new Token(yyline, yytext(), sym.DEC);
          }
        case 65: break;
        case 23: 
          { return new Token(yyline, yytext(), sym.DIV);
          }
        case 66: break;
        case 24: 
          { return new Token(yyline, yytext(), sym.INC);
          }
        case 67: break;
        case 25: 
          { return new Token(yyline, yytext(), sym.NOT);
          }
        case 68: break;
        case 26: 
          { return new Token(yyline, yytext(), sym.NEG);
          }
        case 69: break;
        case 27: 
          { return new Token(yyline, yytext(), sym.XOR);
          }
        case 70: break;
        case 28: 
          { return new Token(yyline, yytext(), sym.MOVE);
          }
        case 71: break;
        case 29: 
          { return new Token(yyline, yytext(), sym.JUMP);
          }
        case 72: break;
        case 30: 
          { return new Token(yyline, yytext(), sym.JUMPL);
          }
        case 73: break;
        case 31: 
          { return new Token(yyline, yytext(), sym.JUMPG);
          }
        case 74: break;
        case 32: 
          { return new Token(yyline, yytext(), sym.RETURN);
          }
        case 75: break;
        case 33: 
          { return new Token(yyline, yytext(), sym.JUMPLE);
          }
        case 76: break;
        case 34: 
          { return new Token(yyline, yytext(), sym.JUMPGE);
          }
        case 77: break;
        case 35: 
          { return new Token(yyline, yytext(), sym.LIBRARY);
          }
        case 78: break;
        case 36: 
          { return new Token(yyline, yytext(), sym.COMPARE);
          }
        case 79: break;
        case 37: 
          { return new Token(yyline, yytext(), sym.JUMPTRUE);
          }
        case 80: break;
        case 38: 
          { return new Token(yyline, yytext(), sym.MOVEARRAY);
          }
        case 81: break;
        case 39: 
          { return new Token(yyline, yytext(), sym.MOVEFIELD);
          }
        case 82: break;
        case 40: 
          { return new Token(yyline, yytext(), sym.JUMPFALSE);
          }
        case 83: break;
        case 41: 
          { return new Token(yyline, yytext(), sym.STATICCALL);
          }
        case 84: break;
        case 42: 
          { return new Token(yyline, yytext(), sym.ARRAYLENGTH);
          }
        case 85: break;
        case 43: 
          { return new Token(yyline, yytext(), sym.VIRTUALLCALL);
          }
        case 86: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              {
                return new Token(yyline, "EOF", sym.EOF);
              }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}