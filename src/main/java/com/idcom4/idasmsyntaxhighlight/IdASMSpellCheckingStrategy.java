package com.idcom4.idasmsyntaxhighlight;

import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class IdASMSpellCheckingStrategy extends SpellcheckingStrategy {
    @Override
    public @NotNull Tokenizer<?> getTokenizer(PsiElement element) {
        IElementType type = element.getNode().getElementType();

        if (
                type == IdASMTokens.KEYWORDS ||
                type == IdASMTokens.STATIC_ADDRESS
        ) {
            return EMPTY_TOKENIZER;
        }


        return super.getTokenizer(element);
    }
}