package t9;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class T9Translator {

    private static final Map<Character, String> T9_MAP = new HashMap<>();

    static {
        T9_MAP.put('2', "abc");
        T9_MAP.put('3', "def");
        T9_MAP.put('4', "ghi");
        T9_MAP.put('5', "jkl");
        T9_MAP.put('6', "mno");
        T9_MAP.put('7', "pqrs");
        T9_MAP.put('8', "tuv");
        T9_MAP.put('9', "wxyz");
        T9_MAP.put('0', " ");
    }
    
    /**
     * Translate a stream of bytes containing only T9 keyboard characters to a
     * human-readable text. Only characters 2-9, 0, space are allowed as input
     * using standard layout representation: 2 -> abc 3 -> def 4 -> ghi 5 -> jkl
     * 6 -> mno 7 -> pqrs 8 -> tuc 9 -> wxyz 0 -> a space space -> a "pause" A
     * space is used to represent some time between two presses of the same
     * button. For instance, to write "hihi": "44 444 44 444" Pauses can be
     * repeated multiple time, including between two different key presses and
     * should not impact output
     */
    public String translate(InputStream stream) throws IOException {
        StringBuilder result = new StringBuilder();
        StringBuilder buffer = new StringBuilder();
        int prevChar = -1;

        int current;
        while ((current = stream.read()) != -1) {
            char digit = (char) current;

            if (digit == ' ') {
                if (buffer.length() > 0) {
                    result.append(getT9Character(buffer.toString()));
                    buffer.setLength(0);
                }
                prevChar = ' ';
                continue;
            }

            if (T9_MAP.containsKey(digit)) {
                if (prevChar == digit) {
                    buffer.append(digit); 
                } else {
                    if (buffer.length() > 0) {
                        result.append(getT9Character(buffer.toString()));
                        buffer.setLength(0);
                    }
                    buffer.append(digit);
                }
                prevChar = digit;
            } else {
                System.err.println("Invalid input detected: " + digit);
            }
        }

        if (buffer.length() > 0) {
            result.append(getT9Character(buffer.toString()));
        }

        return result.toString();
    }

    private static char getT9Character(String keyPresses) {
        char key = keyPresses.charAt(0);
        if (!T9_MAP.containsKey(key)) {
            System.err.println("Unexpected character: " + key);
            return '?';
        }

        String letters = T9_MAP.get(key);
        int index = keyPresses.length() - 1;
        return letters.charAt(index);
    }

}
