package com.stevewedig.blog.symbol.format;

import java.util.Map;

import com.stevewedig.blog.symbol.Symbol;
import com.stevewedig.blog.symbol.SymbolMap;
import com.stevewedig.blog.translate.Format;
import com.stevewedig.blog.translate.Translator;

public interface SymbolTranslator extends Translator<Map<String, String>, SymbolMap>, SymbolParser,
    SymbolWriter {

  interface Builder {

    SymbolTranslator build();

    <Value> Builder add(Symbol<Value> symbol, Format<Value> format);

    Builder add(Symbol<String> symbol);
  }

}

