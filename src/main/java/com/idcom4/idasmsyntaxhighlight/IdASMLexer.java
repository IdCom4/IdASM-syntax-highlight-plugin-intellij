package com.idcom4.idasmsyntaxhighlight;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdASMLexer extends LexerBase {

    public static final List<String> SYMBOLS = List.of(
            "(",
            ")",
            "==",
            "<=",
            ">=",
            "<",
            ">",
            "{",
            "}",
            ",",
            "*",
            "[",
            "]"
    );

    public static final List<String> STATIC_ADDRESSES = List.of(
            "R0",
            "R1",
            "R2",
            "R3",
            "ACC0",
            "ACC1",
            "FLAGS",
            "SPTR",
            "EXPTR",
            "IN",
            "OUT",
            "MEMEXT",
            "MEM",
            "INTRC",
            "STACK"
    );

    public static final List<String> KEYWORDS = List.of(
            "IF",
            "LOOP",
            "CALL",
            "RET",
            "EXIT",
            "FUNC"
    );

    public static final List<String> INSTRUCTIONS = List.of(
            "ADD",
            "SUB",
            "DIV",
            "MUL",
            "MOD",
            "AND",
            "OR",
            "XOR",
            "LSHFT",
            "RSHFT",
            "JMPE",
            "JMPL",
            "JMPLE",
            "JMPG",
            "JMPGE",
            "INCR",
            "DECR",
            "MOVE",
            "INTR",
            "GOTO",
            "PUSH",
            "POP",
            "PRINT",
            "GETC",
            "BGETC"
    );



    private enum ESates {
        DEFAULT(0),
        PARAM(1),
        STRING(2),
        CHAR(3),
        MEMORY(4),
        COMMENT(5);

        public final int value;

        ESates(int value) { this.value = value; }
        public static ESates fromValue(int value) throws Exception {
            for (ESates state : ESates.values()) {
                if (state.value == value) return state;
            }

            throw new Exception("unknown state code: " + value);
        }
    }

    private ESates state = ESates.DEFAULT;

    private CharSequence buffer;
    private int endOffset;
    private int position;
    private IElementType tokenType;

    private String bufferSlice;

    private int tokenStart = 0;

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.endOffset = endOffset;
        this.position = startOffset;
        try {
            this.state = ESates.fromValue(initialState);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        advance();
    }

    @Override
    public int getState() {
        return this.state.value;
    }

    @Override
    public IElementType getTokenType() {
        return tokenType;
    }

    @Override
    public int getTokenStart() {
        return tokenStart;
    }

    @Override
    public int getTokenEnd() {
        return position;
    }

    @Override
    public void advance() {
        tokenStart = position;
        tokenType = null;

        if (position >= endOffset) {
            return;
        }

        // skip empty space
        while (position < endOffset && Character.isWhitespace(buffer.charAt(position))) {
            position++;
        }

        // handle white spaces
        if (position > tokenStart) {
            this.tokenType = TokenType.WHITE_SPACE;
            return ;
        }


        int nextEmptyChar = position;
        while (nextEmptyChar < buffer.length() && !Character.isWhitespace(buffer.charAt(nextEmptyChar)))
            nextEmptyChar++;

        bufferSlice = buffer.subSequence(position, nextEmptyChar).toString();
        String symbol;

        // if in params ()
        if (this.state.equals(ESates.PARAM)) {
            if (!this.tryParseParam()) this.tokenType = TokenType.BAD_CHARACTER;
        }
        // if in memory addressing []
        else if (this.state.equals(ESates.MEMORY)) {
            if (!this.tryParseMemory()) this.tokenType = TokenType.BAD_CHARACTER;
        }
        else {
            // check if param
            if (buffer.charAt(position) == '(') {
                tokenType = IdASMTokens.SYMBOL;
                this.state = ESates.PARAM;
                position++;
            }
            // check if mem value
            else if (buffer.charAt(position) == '[') {
                tokenType = IdASMTokens.SYMBOL;
                this.state = ESates.MEMORY;
                position++;
            }
            // check if string
            else if (buffer.charAt(position) == '"') {
                if (!this.tryParseString()) this.tokenType = IdASMTokens.BAD_TOKEN;
            }
            // check if char
            else if (buffer.charAt(position) == '\'') {
                if (!this.tryParseChar()) this.tokenType = IdASMTokens.BAD_TOKEN;
            }
            // check if comment
            else if (buffer.charAt(position) == '#') {
                this.tokenType = IdASMTokens.COMMENT;
                // skip until next line
                while (position < buffer.length() && buffer.charAt(position) != '\n')
                    position++;
            }

            // check if static address
            else if (tryParseStaticAddress()) {
                this.tokenType = IdASMTokens.STATIC_ADDRESS;
            }
            // check if keyword
            else if (KEYWORDS.contains(bufferSlice.toUpperCase())) {
                this.tokenType = IdASMTokens.KEYWORDS;
                this.position = nextEmptyChar;
            }
            // check if instruction
            else if (INSTRUCTIONS.contains(bufferSlice.toUpperCase())) {
                this.tokenType = IdASMTokens.INSTRUCTION;
                this.position = nextEmptyChar;
            }
            // check if symbol
            else if ((symbol = SYMBOLS.stream().filter(bufferSlice::startsWith).findFirst().orElse(null)) != null) {
                this.tokenType = IdASMTokens.SYMBOL;
                this.position += symbol.length();
            }
            // check if label
            else if (tryParseLabel()) {
                tokenType = IdASMTokens.LABEL;
            }
            // check if value
            else if (tryParseValue()) {
                tokenType = IdASMTokens.DIRECT_VALUE;
            }
            else {
                tokenType = IdASMTokens.BAD_TOKEN;
            }

        }

        // error handling
        if (position == tokenStart) position++;
        if (tokenType == null) tokenType = IdASMTokens.BAD_TOKEN;
    }

    private boolean tryParseStaticAddress() {
        String upperCaseSlice = bufferSlice.toUpperCase();
        String staticAddress = STATIC_ADDRESSES.stream().filter(upperCaseSlice::startsWith).findFirst().orElse(null);
        if (staticAddress == null)
            return false;

        if (
            staticAddress.length() == upperCaseSlice.length() ||
            SYMBOLS.stream().filter(upperCaseSlice.substring(staticAddress.length())::startsWith).findFirst().orElse(null) != null
        ) {
            this.tokenType = IdASMTokens.STATIC_ADDRESS;
            this.position += staticAddress.length();
            return true;
        }

        return false;
    }

    private boolean tryParseLabel() {
        Matcher matcher = Pattern.compile("^([a-zA-Z_]+\\w*)").matcher(bufferSlice);
        if (!matcher.find()) return false;

        tokenType = IdASMTokens.LABEL;
        position += matcher.group(0).length();
        return true;
    }

    private boolean tryParseMemory() {
        if (buffer.charAt(position) == ']') {
            this.state = ESates.DEFAULT;
            tokenType = IdASMTokens.SYMBOL;
            position++;
            return true;
        }

        if (buffer.charAt(position) == '*') {
            tokenType = IdASMTokens.SYMBOL;
            position++;
            return true;
        }

        return tryParseValue() || tryParseStaticAddress();
    }

    private boolean tryParseParam() {

        if (buffer.charAt(position) == ')') {
            this.state = ESates.DEFAULT;
            tokenType = IdASMTokens.SYMBOL;
            position++;
            return true;
        }
        if (buffer.charAt(position) == ',') {
            tokenType = IdASMTokens.SYMBOL;
            position++;
            return true;
        }

        if (buffer.charAt(position) == '*') {
            tokenType = IdASMTokens.SYMBOL;
            position++;
            return true;
        }

        return tryParseValue() || tryParseStaticAddress();
    }

    private boolean tryParseString() {
        Matcher matcher = Pattern.compile("^\"((?:[^\"\\\\]|\\\\[\"\\\\/bfnrt])*)\"").matcher(buffer.subSequence(position, buffer.length()));
        if (!matcher.find()) return false;

        tokenType = IdASMTokens.STRING;
        position += matcher.group(0).length();
        return true;
    }

    private boolean tryParseChar() {
        if (buffer.charAt(position) != '\'') return false;
        position++;
        if (buffer.charAt(position) == '\\') position++;
        position++;
        if (buffer.charAt(position) != '\'') return false;
        position++;

        this.tokenType = IdASMTokens.CHAR;
        this.state = ESates.DEFAULT;
        return true;
    }

    private boolean tryParseValue() {
        Matcher matcher;
        // binary
        if ((matcher = Pattern.compile("^0b([0-1]+)").matcher(bufferSlice)).find()) {
            position += matcher.group(0).length();
            tokenType = IdASMTokens.DIRECT_VALUE;
            return true;
        }
        // hexadecimal
        if ((matcher = Pattern.compile("^0x([0-9a-zA-Z]+)").matcher(bufferSlice)).find()) {
            position += matcher.group(0).length();
            tokenType = IdASMTokens.DIRECT_VALUE;
            return true;
        }
        // decimal
        if ((matcher = Pattern.compile("^(-?[0-9]+)").matcher(bufferSlice)).find()) {
            position += matcher.group(0).length();
            tokenType = IdASMTokens.DIRECT_VALUE;
            return true;
        }

        return false;
    }

    @Override
    public @NotNull CharSequence getBufferSequence() {
        return buffer;
    }

    @Override
    public int getBufferEnd() {
        return endOffset;
    }
}