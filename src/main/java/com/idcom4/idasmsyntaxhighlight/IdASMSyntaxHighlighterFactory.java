package com.idcom4.idasmsyntaxhighlight;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

public class IdASMSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {

    @Override
    protected @NotNull SyntaxHighlighter createHighlighter() {
        return new IdASMSyntaxHighlighter();
    }
}
