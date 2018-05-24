package phoneticsgame;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Preferences implements Serializable {
    private int maxTime;
    private int maxRounds;
    private double volume;
    private String language;
    private transient LinkedList<Sound> soundPool = new LinkedList<>();
    private transient IntegerProperty availableSounds;

    Preferences() {
        setMaxTime(10);
        setVolume(0.5);
        setLanguage(Language.GERMAN);
        setMaxRounds(soundPool.size() / 2);
    }

    Preferences(Preferences other) {
        maxTime = other.maxTime;
        maxRounds = other.maxRounds;
        volume = other.volume;
        setLanguage(other.getLanguage());
    }

    Preferences(int maxTime, int maxRounds, String language) {
        this.volume = 0.5;
        setMaxTime(maxTime);
        setMaxRounds(maxRounds);
        setLanguage(language);
    }

    Preferences(int maxTime, int maxRounds, String language, double volume) {
        setMaxTime(maxTime);
        setMaxRounds(maxRounds);
        setVolume(volume);
        setLanguage(language);
    }

    public IntegerProperty availableSoundsIntegerProperty() {
        if (availableSounds == null)
            availableSounds = new SimpleIntegerProperty();
        return availableSounds;
    }

    public final int getAvailableSounds() {
        return availableSoundsIntegerProperty().get();
    }

    public final void setAvailableSounds(int value) {
        availableSoundsIntegerProperty().set(value);
    }

    public final String getLanguage() {
        return language;
    }

    public final void setLanguage(String language) {
        soundPool = Language.getLanguage(language);
        this.language = language;
        setAvailableSounds(soundPool.size());
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        if (maxTime < 1)
            throw new IllegalArgumentException("MaxTime must be greater than zero.");
        this.maxTime = maxTime;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        if (maxRounds < 1 || maxRounds >= soundPool.size())
            throw new IllegalArgumentException("MaxRounds must be greater than zero " +
                    "and less than the total number of sounds.");
        this.maxRounds = maxRounds;
    }

    public List<Sound> getRandPool(int poolSize) {
        if (poolSize < 1 || poolSize > soundPool.size())
            throw new IllegalArgumentException();
        Collections.shuffle(soundPool);
        return soundPool.stream().limit(poolSize).collect(Collectors.toList());
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        if (volume < 0 || volume > 1)
            throw new IllegalArgumentException("Volume must be greater than zero and less then one.");
        this.volume = volume;
    }
}
