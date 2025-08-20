package com.idcom4.idasmsyntaxhighlight;

import com.intellij.lang.Language;

public class IdASMLanguage extends Language {
    public static final IdASMLanguage INSTANCE = new IdASMLanguage();

    private IdASMLanguage() {
        super("IdASM");
    }
}