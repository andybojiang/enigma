package com.example.enigmaVisual.model;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Andy Jiang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _advanceable = false;
        _notches = notches.toCharArray();
        for (char c : _notches) {
            if (!alphabet().contains(c)) {
                throw new EnigmaException("Notches contain invalid character");
            }
        }
    }
    /** Return if this rotor rotates.
     */
    boolean rotates() {
        return true;
    }

    @Override
    void ringSet(int posn) {
        super.ringSet(posn);
        _ringSet = permutation().wrap(posn);
    }
    @Override
    void ringSet(char cposn) {
        super.ringSet(cposn);
        _ringSet = permutation().wrap(alphabet().toInt(cposn));
    }

    @Override
    void advance() {
        if (setting() < alphabet().size() - 1) {
            set(setting() + 1);
        } else {
            set(0);
        }
    }

    @Override
    boolean atNotch() {
        for (char c : _notches) {
            int set = permutation().wrap(alphabet().toInt(c) - _ringSet);
            if (set == setting()) {
                return true;
            }
        }
        return false;
    }

    /** Getter function for notches.
     * @return Char array of notches.
     */
    char[] getNotch() {
        return _notches;
    }

    /** Whether this rotor can advance.*/
    private boolean _advanceable;
    /** Notches.*/
    private char[] _notches;

    /**ringSet.*/
    private int _ringSet = 0;
}
