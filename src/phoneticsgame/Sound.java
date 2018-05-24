package phoneticsgame;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class Sound implements Serializable {
    private String[] letterSequences;
    private String recordPath;

    public Sound(String recordPath, String sequences) {
        this.recordPath = recordPath;
        this.letterSequences = sequences.split(" ");
    }

    public Sound(Sound sound) {
        this.recordPath = sound.recordPath;
        this.letterSequences = sound.letterSequences;
    }

    public boolean isPresent(String sequence) {
        return Arrays.asList(letterSequences).contains(sequence);
    }

    public String getRecordPath() {
        return recordPath;
    }

    public String[] getLetterSequences() {
        return Arrays.copyOf(letterSequences, letterSequences.length);
    }

    public String getRndSequence() {
        return letterSequences[new Random().nextInt(letterSequences.length)];
    }

    @Override
    public String toString() {
        return recordPath;
    }
}
