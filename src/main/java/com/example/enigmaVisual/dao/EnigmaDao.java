package com.example.enigmaVisual.dao;

import com.example.enigmaVisual.model.Alphabet;
import com.example.enigmaVisual.model.Machine;
import com.example.enigmaVisual.model.Rotor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("enigmaDao")
public class EnigmaDao {
    /** List of all available rotors to use.*/
    private static ArrayList<Rotor> ALL_ROTORS = new ArrayList<>();
    /** Alphabet consists of all uppercase letters.*/
    private static Alphabet ALPHABET = new Alphabet();
    /** Number of rotors a machine is allowed to have.*/
    private static int NUM_ROTORS = 3;
    /** Number of pawls a machine is allowed to have.*/
    private static int PAWLS = 3;
    /** Default machine.*/
    private static Machine MACHINE = new Machine(ALPHABET, NUM_ROTORS, PAWLS, ALL_ROTORS);
}
