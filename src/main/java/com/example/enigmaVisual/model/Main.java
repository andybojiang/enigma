package com.example.enigmaVisual.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static com.example.enigmaVisual.model.EnigmaException.*;

/** Enigma simulator.
 *  @author Andy Jiang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String plugsetting = "";
        String[] splitsettings = settings.split(" ");
        String[] rotors = new String[_rotornum];
        int inserted = 0;
        String setting = "";
        String ringSetting = "";
        for (String s : splitsettings) {
            if (s.toCharArray()[0] != '*' && s.toCharArray()[0] != '(') {
                if (inserted < rotors.length) {
                    rotors[inserted] = s;
                    inserted++;
                } else if (setting.equals("")) {
                    setting = s;
                } else {
                    ringSetting = s;
                }
            } else if (s.toCharArray()[0] == '(') {
                plugsetting += s;
            }
        }
        M.insertRotors(rotors);
        M.setRotors(setting);
        if (!ringSetting.equals("")) {
            M.setRings(ringSetting);
        }

        Permutation plugboardSetting = new Permutation(plugsetting, _alphabet);
        M.setPlugboard(plugboardSetting);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    public Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        _machine = readConfig();
        Machine copy = _machine;
        String message = "";

        if (!_input.hasNextLine()) {
            throw new EnigmaException("no input");
        } else if (!_input.hasNext("\\*")) {
            throw new EnigmaException("no setting at beginning of input");
        }
        while (_input.hasNextLine()) {
            if (_input.hasNext("\\*")) {
                String s = _input.nextLine();
                if (s.toCharArray().length > 0) {
                    _machine = copy;
                    setUp(_machine, s);
                } else {
                    printMessageLine("\n");
                }
            } else if (_input.hasNext("\\s")) {
                _input.next();
            } else if (_input.hasNext("\\n")) {
                _input.nextLine();
            } else {
                message += _input.nextLine();
                printMessageLine(_machine.convert(message));
                message = "";
            }
        }
    }

    /**
     * Read the alphabet portion of connfig.
     */
    private void readConfigAlpha() {
        try {
            _alphabet = new Alphabet();
            if (_config.hasNextLine()) {
                _alphabet = new Alphabet(_config.nextLine());
            } else {
                throw new EnigmaException("config file has no alphabet line");
            }
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }
    /**
     * Read the rotor number portion of connfig.
     */
    private void readConfigRotorNum() {
        if (_config.hasNextInt()) {
            _rotornum = _config.nextInt();
        } else {
            throw new EnigmaException("config file has no rotor number");
        }
    }
    /**
     * Read the pawl number portion of connfig.
     */
    private void readConfigPawlNum() {
        if (_config.hasNextInt()) {
            _pawlnum = _config.nextInt();
        } else {
            throw new EnigmaException("config file has no pawl number");
        }
    }
    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            readConfigAlpha();
            readConfigRotorNum();
            readConfigPawlNum();
            ArrayList<Rotor> rotorCollection = new ArrayList<>();
            while (_config.hasNextLine()) {
                Rotor r = readRotor();
                rotorCollection.add(r);
            }

            return new Machine(_alphabet, _rotornum, _pawlnum, rotorCollection);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next("[^ \\s]+");
            char[] typeandnotches = _config.next().toCharArray();
            char rotortype = typeandnotches[0];
            String notches = "";
            if (rotortype == 'M') {
                for (int i = 1; i < typeandnotches.length; i++) {
                    notches += typeandnotches[i];
                }
            }
            String cycles = "";
            while (_config.hasNext("(\\(.+\\))")) {
                cycles += (_config.nextLine());
            }
            Rotor r;
            Permutation p = new Permutation(cycles, _alphabet);
            if (rotortype == 'M') {
                r = new MovingRotor(name, p, notches);
            } else {
                r = new FixedRotor(name, p);
            }
            return r;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String result = "";
        char[] split = msg.toCharArray();
        int shift = 0;
        while (shift < msg.length()) {
            for (int i = 0; i < 5; i++) {
                if (shift + i < msg.length()) {
                    result += split[shift + i];
                }
            }
            result += " ";
            shift += 5;
        }

        _output.println(result.trim());
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Machine. */
    private Machine _machine;
    /** Number of rotors. */
    private int _rotornum;
    /** Number of pawls. */
    private int _pawlnum;
}
