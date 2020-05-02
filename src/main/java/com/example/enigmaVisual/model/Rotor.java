package com.example.enigmaVisual.model;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Andy Jiang
 */
public class Rotor {
    /** A rotor named NAME whose permutation is given by PERM. */
    public Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = alphabet().toInt(cposn);
    }

    /** Change setting to reflect ringsetting.
     * @param posn character in the alphabet.
     **/
    void ringSet(int posn) {
        set(permutation().wrap((setting() - posn)));
    }
    /** Change setting to reflect ringsetting.
     * @param cposn character in the alphabet.
     **/
    void ringSet(char cposn) {
        set(permutation().wrap(setting() - alphabet().toInt(cposn)));
    }

    /** Return the conversion of P(an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int num = (p + setting()) % size();
        int position = permutation().permute(num) - setting();
        if (position >= 0) {
            return position;
        } else {
            return size() + position;
        }

    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int num = (e + setting()) % size();
        int position = permutation().invert(num) - setting();
        if (position >= 0) {
            return position;
        } else {
            return size() + position;
        }
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Get the current notch.
     * @return char array.
     * */
    char[] getNotch() {
        return new char[0];
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;
    /** Setting of the rotor. */
    private int _setting;
}
