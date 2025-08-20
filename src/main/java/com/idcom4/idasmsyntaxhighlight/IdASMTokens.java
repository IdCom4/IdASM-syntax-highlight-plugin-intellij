package com.idcom4.idasmsyntaxhighlight;
import com.intellij.psi.tree.IElementType;

public interface IdASMTokens {
    IElementType KEYWORDS = new IElementType("KEYWORDS", IdASMLanguage.INSTANCE);
    IElementType INSTRUCTION = new IElementType("INSTRUCTION", IdASMLanguage.INSTANCE);
    IElementType STATIC_ADDRESS = new IElementType("STATIC_ADDRESS", IdASMLanguage.INSTANCE);
    IElementType CHAR = new IElementType("CHAR", IdASMLanguage.INSTANCE);
    IElementType STRING = new IElementType("STRING", IdASMLanguage.INSTANCE);
    IElementType DIRECT_VALUE = new IElementType("DIRECT_VALUE", IdASMLanguage.INSTANCE);
    IElementType LABEL = new IElementType("LABEL", IdASMLanguage.INSTANCE);
    IElementType COMMENT = new IElementType("COMMENT", IdASMLanguage.INSTANCE);
    IElementType SYMBOL = new IElementType("SYMBOL", IdASMLanguage.INSTANCE);
    IElementType BAD_TOKEN = new IElementType("BAD_TOKEN", IdASMLanguage.INSTANCE);
}