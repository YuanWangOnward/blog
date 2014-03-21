package com.stevewedig.foundation.etc;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;


public abstract class StrLib {

  // http://stackoverflow.com/questions/275944/how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string

  // ============================================================================
  // countOccurrences
  // ============================================================================

  public static int countOccurences(String str, char subChar) {
    return countOccurences(str, "" + subChar);
  }

  public static int countOccurences(String str, String subStr) {
    return str.length() - str.replace(subStr, "").length();
  }

  // ============================================================================
  // containsInterpolation
  // ============================================================================

  public static boolean containsInterpolation(String str, char openDelim, char closeDelim) {

    int openCount = countOccurences(str, openDelim);
    int closeCount = countOccurences(str, closeDelim);

    if (openCount > 1)
      throw new RuntimeException("can only contain one open delimiter '" + openDelim + "', got: " + str);
    
    if (closeCount > 1)
      throw new RuntimeException("can only contain one close delimiter '" + closeDelim + "', got: " + str);

    if (openCount != closeCount)
      throw new RuntimeException("open delimiter '" + openDelim + "' and close delimiter '" + closeDelim + "' must always accompany each other, got: " + str);
    
    return openCount > 0;
  }
  
  // ============================================================================
  // splitInterpolation
  // ============================================================================

  public static String[] splitInterpolation(String str, char openDelim, char closeDelim) {
    
    int openDelimIndex = str.indexOf(openDelim);
    int closeDelimIndex = str.indexOf(closeDelim);
    
    if (openDelimIndex > closeDelimIndex)
      throw new RuntimeException(openDelim + " must precede " + closeDelim + ", got: " + str);
    
    return splitInterpolation(str, openDelimIndex, closeDelimIndex);
  }
  
  public static String[] splitInterpolation(String str, int openIndex, int closeIndex) {
    
    String prefix = str.substring(0, openIndex);
    String middle = str.substring(openIndex + 1, closeIndex);
    String suffix = str.substring(closeIndex + 1);
    
    String[] triple = {prefix, middle, suffix};
    
    return triple;
  }

  // ============================================================================
  // ============================================================================
  // old below
  // ============================================================================
  // ============================================================================
  // ============================================================================

  public static String strip_tags(String markup) {
    return markup.replaceAll("<[^>]+>", " ");
  }

  // ============================================================================

  public static String trim_words(String input, int max_count) {

    String[] words = input.split("\\s+");

    String output = "";
    boolean was_trimmed = false;

    for (int i = 0; i < words.length; i++) {
      String next = output + " " + words[i];
      if (next.length() > max_count) {
        was_trimmed = true;
        break;
      }
      output = next;
    }

    if (was_trimmed)
      output += "...";

    return output;
  }

  // ============================================================================

  // http://stackoverflow.com/questions/1086123/titlecase-conversion
  public static String title_case(String input) {
    String output = "";

    boolean nextTitleCase = true;

    for (int i = 0; i < input.length(); i++) {
      String c = input.substring(i, i + 1);

      if (c.equals(" "))
        nextTitleCase = true;
      else if (nextTitleCase) {
        c = c.toUpperCase();
        nextTitleCase = false;
      }

      output += c;
    }

    return output;
  }

  // ============================================================================
  // ============================================================================

  public static String pluralize(String base, int count) {
    return base + (count == 1 ? "" : "s");
  }

  // ============================================================================
  // blanks
  // ============================================================================

  public static boolean isBlank(String str) {
    return str.isEmpty() || str.matches("^\\s*$");
  }

  // ============================================================================
  // slice
  // ============================================================================

  public static char last(String str) {
    return str.charAt(str.length() - 1);
  }

  public static String suffix(String str) {
    return str.substring(1);
  }

  public static String prefix(String str) {
    return str.substring(0, str.length() - 1);
  }

  // ============================================================================
  // strip
  // ============================================================================

  public static String lstrip(String str, char ch) {
    while (!str.isEmpty() && str.charAt(0) == ch) {
      str = suffix(str);
    }
    return str;
  }

  public static String rstrip(String str, char ch) {
    while (!str.isEmpty() && last(str) == ch) {
      str = prefix(str);
    }
    return str;
  }

  public static String strip(String str, char ch) {
    str = lstrip(str, ch);
    str = rstrip(str, ch);
    return str;
  }

  // ============================================================================
  // split
  // ============================================================================

  // TODO replace with guava
//  public static List<String> splitEmpty(String str, char ch) {
//    if (str.length() == 0)
//      return new ArrayList<String>();
//    else
//      return split(str, ch);
//  }

  public static Iterable<String> split(String str, char ch) {
    return Splitter.on(ch).split(str);
  }

  // TODO replace with guava
  public static Iterable<String> stripSplit(String str, char ch) {
    str = strip(str, ch);

    if (isBlank(str))
      return new ArrayList<String>();

    return split(str, ch);
  }

  // ============================================================================
  // ============================================================================

  // public static Opt< String > parse_opt( String str ) {
  // if( is_blank( str ) ) return m.none();
  // else return m.some( str );
  // }
  //
  // public static Array< String > parse_comma_array( String str ) {
  // return parse_array( str, ',' );
  // }
  //
  // public static Set< String > parse_comma_set( String str ) {
  // return parse_set( str, ',' );
  // }
  //
  // public static Set< String > parse_newline_set( String str ) {
  // return parse_set( str, '\n' );
  // }
  //
  // public static Set< String > parse_set( String str, char delim ) {
  // return to_set.unroll(
  // parse_array( str, delim )
  // );
  // }
  //
  // public static Array< String > parse_array( String str, char delim ) {
  //
  // Path path = split( str, delim );
  //
  // Array_fluid< String > items = to_array_fluid.apply();
  //
  // for( String item : path )
  // if( ! is_blank( item ) )
  // items.add_(
  // strip( strip( item, ' ' ), '\t' )
  // );
  //
  // return items.freeze();
  // }

  // ============================================================================
  // join
  // ============================================================================

  // public static String join( String empty, String delim, Container_ice< String > items ) {
  // if( items.is_empty() ) return empty;
  // else return join( delim, items );
  // }

  public static String join(String delim, Iterable<String> items) {
    Iterator<String> parts = items.iterator();

    String output = "";
    while (parts.hasNext())
      output += parts.next() + delim;

    if (output.length() == 0)
      return output;

    return output.substring(0, output.length() - delim.length());
  }

  // ============================================================================
  // startswith
  // ============================================================================

  // @untested
  public static boolean startsWith(String str, String sub) {
    if (sub.length() > str.length())
      return false;
    return str.substring(0, sub.length()).equals(sub);
  }

  public static boolean endsWith(String str, String sub) {
    if (sub.length() > str.length())
      return false;
    return str.substring(str.length() - sub.length()).equals(sub);
  }

  // ============================================================================
  // startswith
  // ============================================================================

  // ============================================================================
  // ============================================================================

  public static Optional<String> blank__optional(String s) {
    if (StrLib.isBlank(s))
      return Optional.absent();
    else
      return Optional.of(s);
  }

  public static String upperFirstLetter(String s) {
    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }
}