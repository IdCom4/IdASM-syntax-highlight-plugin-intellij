package com.idcom4.idasmsyntaxhighlight;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.annotations.NotNull;

public class IdASMFile extends PsiFileBase {
    public IdASMFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, IdASMLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return IdASMFileType.INSTANCE;
    }

    @NotNull
    @Override
    public IFileElementType getFileElementType() {
        return IdASMParserDefinition.FILE;
    }

    @Override
    public String toString() {
        return "IdASM File";
    }
}