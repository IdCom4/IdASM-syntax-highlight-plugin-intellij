package com.idcom4.idasmsyntaxhighlight;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IdASMFileType extends LanguageFileType {
    public static final IdASMFileType INSTANCE = new IdASMFileType();

    private IdASMFileType() {
        super(IdASMLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "IdASM";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "IdASM assembly source file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "idasm";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return IdASMIcon.FILE;
    }
}