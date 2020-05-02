package com.example.enigmaVisual.model;

import static com.example.enigmaVisual.model.EnigmaException.*;
import static com.example.enigmaVisual.model.EnigmaException.error;

/** Class that represents a reflector in the enigma.
 *  @author Andy Jiang
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!permutation().derangement()) {
            throw new EnigmaException("Permutation is not Deranged");
        }
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

}
