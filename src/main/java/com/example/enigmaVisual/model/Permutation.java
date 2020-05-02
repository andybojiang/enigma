package com.example.enigmaVisual.model;
import java.util.HashMap;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Andy Jiang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String lc = alphabet.toString();
        String oneCycle = "";
        for (char c : cycles.toCharArray()) {
            if (c == ')') {
                addCycle(oneCycle);
            } else if (c == '(') {
                oneCycle = "";
            } else if (c != ' ') {
                if (_alphabet.contains(c)) {
                    if (lc.indexOf(c) >= 0) {
                        oneCycle += c;
                        String first = lc.substring(0, lc.indexOf(c));
                        lc = first + lc.substring(lc.indexOf(c) + 1);
                    } else {
                        throw new EnigmaException("Cycle is not in alphabet");
                    }
                } else {
                    throw new EnigmaException("Cycle is not in alphabet");
                }
            }
        }

        for (char c : lc.toCharArray()) {
            addCycle("" + c);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        char[] cycleArr = cycle.toCharArray();
        for (int i = 0; i < cycleArr.length - 1; i++) {
            if (_cycles.containsKey(cycleArr[i])) {
                throw new EnigmaException("Duplicate key");
            } else {
                _cycles.put(cycleArr[i], cycleArr[i + 1]);
                _inversecycles.put(cycleArr[i + 1], cycleArr[i]);
            }
        }

        _cycles.put(cycleArr[cycleArr.length - 1], cycleArr[0]);
        _inversecycles.put(cycleArr[0], cycleArr[cycleArr.length - 1]);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char letter = permute(_alphabet.toChar(p % size()));
        return _alphabet.toInt(letter);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char letter = invert(_alphabet.toChar(c % size()));
        return _alphabet.toInt(letter);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (_alphabet.contains(p)) {
            return _cycles.get(p);
        } else {
            throw new EnigmaException("Permuting character not in Alphabet");
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (_alphabet.contains(c)) {
            return _inversecycles.get(c);
        } else {
            throw new EnigmaException("Inverting character not in Alphabet");
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < size(); i++) {
            if (i == permute(i)) {
                return false;
            }
        }
        return true;
    }


    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Hashmap to represent cycle matching. */
    private HashMap<Character, Character> _cycles = new HashMap<>();
    /** Hashmap to represent inverse cycle matching. */
    private HashMap<Character, Character> _inversecycles = new HashMap<>();

}
