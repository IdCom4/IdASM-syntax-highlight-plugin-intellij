package com.idcom4.idasmsyntaxhighlight;


import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.tree.IElementType;
import kotlinx.html.COL;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class IdASMSyntaxHighlighter implements SyntaxHighlighter {

    /* custom colors
    private static final TextAttributesKey CUSTOM =
            TextAttributesKey.createTextAttributesKey("CUSTOM", new TextAttributes(Color.GREEN, null, Color.BLUE, EffectType.WAVE_UNDERSCORE, Font.ITALIC));
    */

    private static final TextAttributesKey KEYWORDS =
            TextAttributesKey.createTextAttributesKey("KEYWORDS", DefaultLanguageHighlighterColors.KEYWORD);
    private static final TextAttributesKey INSTRUCTION =
            TextAttributesKey.createTextAttributesKey("INSTRUCTION", new TextAttributes(new Color(220, 170, 0), null, null, null, Font.ITALIC));
    private static final TextAttributesKey STATIC_ADDRESS =
            TextAttributesKey.createTextAttributesKey("STATIC_ADDRESS", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
    private static final TextAttributesKey CHAR =
            TextAttributesKey.createTextAttributesKey("CHAR", DefaultLanguageHighlighterColors.STRING);
    private static final TextAttributesKey STRING =
            TextAttributesKey.createTextAttributesKey("STRING", DefaultLanguageHighlighterColors.STRING);
    private static final TextAttributesKey DIRECT_VALUE =
            TextAttributesKey.createTextAttributesKey("DIRECT_VALUE", new TextAttributes(new Color(255, 220, 220), null, null, null, Font.PLAIN));
    private static final TextAttributesKey LABEL =
            TextAttributesKey.createTextAttributesKey("LABEL", DefaultLanguageHighlighterColors.STATIC_FIELD);
    private static final TextAttributesKey COMMENT =
            TextAttributesKey.createTextAttributesKey("COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    private static final TextAttributesKey SYMBOL =
            TextAttributesKey.createTextAttributesKey("SYMBOL", DefaultLanguageHighlighterColors.IDENTIFIER);
    private static final TextAttributesKey BAD_TOKEN =
            TextAttributesKey.createTextAttributesKey("BAD_TOKEN", new TextAttributes(new Color(255, 100, 100), null, Color.RED, EffectType.WAVE_UNDERSCORE, Font.ITALIC));

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new IdASMLexer();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {

        if (tokenType == IdASMTokens.KEYWORDS) {
            return new TextAttributesKey[]{KEYWORDS};
        } else if (tokenType == IdASMTokens.INSTRUCTION) {
            return new TextAttributesKey[]{INSTRUCTION};
        } else if (tokenType == IdASMTokens.STATIC_ADDRESS) {
            return new TextAttributesKey[]{STATIC_ADDRESS};
        } else if (tokenType == IdASMTokens.CHAR) {
            return new TextAttributesKey[]{CHAR};
        } else if (tokenType == IdASMTokens.STRING) {
            return new TextAttributesKey[]{STRING};
        } else if (tokenType == IdASMTokens.DIRECT_VALUE) {
            return new TextAttributesKey[]{DIRECT_VALUE};
        } else if (tokenType == IdASMTokens.LABEL){
            return new TextAttributesKey[]{LABEL};
        } else if (tokenType == IdASMTokens.COMMENT){
            return new TextAttributesKey[]{COMMENT};
        } else if (tokenType == IdASMTokens.SYMBOL){
            return new TextAttributesKey[]{SYMBOL};
        } else if (tokenType == IdASMTokens.BAD_TOKEN){
            return new TextAttributesKey[]{BAD_TOKEN};
        } else {
            return TextAttributesKey.EMPTY_ARRAY;
        }
    }
}