package com.example.enigmaVisual.model;

import java.util.ArrayList;

/** Class that represents a complete enigma machine.
 *  @author Andy Jiang
 */
public class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    public Machine(Alphabet alpha, int numRotors, int pawls,
            ArrayList<Rotor> allRotors) {
        _alphabet = alpha;
        if (numRotors <= 1) {
            throw new EnigmaException("Wrong number of rotors for machine");
        } else {
            _numRotors = numRotors;
        }
        if (pawls < 0 || pawls >= numRotors) {
            throw new EnigmaException("Wrong number of pawls for machine");
        } else {
            _numPawls = pawls;
        }

        if (allRotors.size() < _numRotors) {
            throw new EnigmaException("Wrong size for allRotors for machine");
        } else {
            _allRotors = allRotors;
        }
        _usedRotors = new ArrayList<Rotor>(numRotors);
        _plugboard = new Permutation("", alpha);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    public void insertRotors(String[] rotors) {
        if (rotors.length != numRotors()) {
            throw new EnigmaException("wrong number of rotors");
        }
        _usedRotors = new ArrayList<>();
        for (int i = 0; i < numRotors(); i++) {
            for (Rotor r : _allRotors) {
                if (r.name().equals(rotors[i])) {
                    _usedRotors.add(i, r);
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors() - 1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    public void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("wrong number of settings");
        }
        for (int i = 1; i < numRotors(); i++) {
            _usedRotors.get(i).set(setting.charAt(i - 1));
        }
    }

    /**
     * Adjust ring setting, which must be a string of
     * numRotors() - 1 characters in my alphabet.
     * @param setting String of numRotors() - 1 characters.
     */
    public void setRings(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("wrong number of settings");
        }
        for (int i = 1; i < numRotors(); i++) {
            _usedRotors.get(i).ringSet(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    public void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    public int convert(int c) {
        int curr = _plugboard.permute(c);

        for (int i = 0; i < _numRotors - 1; i++) {
            if (_usedRotors.get(i).rotates()) {
                if (_usedRotors.get(i + 1).rotates()) {
                    if (_usedRotors.get(i + 1).atNotch()) {
                        _usedRotors.get(i).advance();
                        if (i + 1 < _numRotors - 1) {
                            _usedRotors.get(i + 1).advance();
                        }
                    }
                }
            }
        }
        _usedRotors.get(_numRotors - 1).advance();

        for (int i = _numRotors - 1; i >= 0; i--) {
            curr = _usedRotors.get(i).convertForward(curr);
        }

        for (int i = 1; i < _numRotors; i++) {
            curr = _usedRotors.get(i).convertBackward(curr);
        }

        return _plugboard.permute(curr);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    public String convert(String msg) {
        String result = "";
        for (char c : msg.replace(" ", "").toCharArray()) {
            result += _alphabet.toChar(convert(_alphabet.toInt(c)));
        }
        return result;
    }
    /**
     * Getter function for all usable rotors.
     * @return All rotors.
     */
    public ArrayList<Rotor> allRotors() {
        return _allRotors;
    }
    /**
     * Getter function for all used rotors.
     * @return Return used rotors.
     */
    public ArrayList<Rotor> usedRotors() {
        return _usedRotors;
    }
    /**
     * Getter function for the plugboard permutation.
     * @return return Plugboard.
     */
    public Permutation plugboard() {
        return _plugboard;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of possible rotors. */
    private int _numRotors;
    /** Number of used rotors. */
    private int _numPawls;
    /** List of all rotors. */
    private ArrayList<Rotor> _allRotors;
    /** List of used rotors. */
    private ArrayList<Rotor> _usedRotors;
    /** Plugboard permutation. */
    private Permutation _plugboard;
}
