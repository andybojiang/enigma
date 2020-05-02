package com.example.enigmaVisual.model;

import java.util.HashMap;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Andy Jiang
 */
public class Alphabet {
    /** A string used to contain the letters used in the alphabet.
     */
    private String _alph;

    /**
     * Size of the alphabet.
     */
    private int _size;
    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    public Alphabet(String chars) {
        char [] charsArr = chars.toCharArray();
        HashMap<Character, Integer> map = new HashMap<>();
        for (char  ch : charsArr) {
            if (map.containsKey(ch)) {
                int count = map.get(ch);
                map.put(ch, ++count);
            } else {
                map.put(ch, 1);
            }
        }
        for (char ch : map.keySet()) {
            if (map.get(ch) > 1) {
                throw new EnigmaException("Duplicate in alphabet");
            }
        }

        this._alph = chars;
        this._size = chars.length();

    }

    /** A default alphabet of all upper-case characters. */

    public Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _size;
    }

    @Override
    public String toString() {
        return _alph;
    }
    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        for (char c : this._alph.toCharArray()) {
            if (ch == c) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= this._size) {
            throw new EnigmaException("Index out of bounds");
        } else {
            return this._alph.toCharArray()[index];
        }
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        char[] alphArr = this._alph.toCharArray();

        if (!contains(ch)) {
            System.out.println(ch);
            throw new EnigmaException("Character is not in alphabet");
        } else {
            for (int i = 0; i < alphArr.length; i++) {
                if (alphArr[i] == ch) {
                    return i;
                }
            }
        }
        throw new EnigmaException("Something wrong with toInt");
    }

}
