package phoneticsgame;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Game implements Serializable {
    private List<Round> pastRounds;
    private Preferences preferences;
    private transient List<Sound> soundsPool;
    private transient boolean abortRequested = false;
    private transient ObjectProperty<Round> currentRound;
    private transient BooleanProperty started;
    private transient BooleanProperty ended;

    public Game(Preferences preferences) {
        pastRounds = new LinkedList<>();
        this.preferences = preferences;
        // + 1 for two options on last round
        soundsPool = preferences.getRandPool(preferences.getMaxRounds() + 1);
        setStarted(false);
        setEnded(false);
    }


    public BooleanProperty startedBooleanProperty() {
        if (started == null)
            started = new SimpleBooleanProperty(false);
        return started;
    }

    public final boolean isStarted() {
        return startedBooleanProperty().get();
    }

    public final void setStarted(boolean value) {
        startedBooleanProperty().set(value);
    }

    public BooleanProperty endedBooleanProperty() {
        if (ended == null)
            ended = new SimpleBooleanProperty(false);
        return ended;
    }

    public final boolean isEnded() {
        return endedBooleanProperty().get();
    }

    public final void setEnded(boolean value) {
        endedBooleanProperty().set(value);
    }

    public ObjectProperty<Round> currentRoundObjectProperty() {
        if (currentRound == null)
            currentRound = new SimpleObjectProperty<>(new Round(preferences.getMaxTime(), popFromPool()));
        return currentRound;
    }

    public String getLanguage() {
        return preferences.getLanguage();
    }

    public final Round getCurrentRound() {
        return currentRoundObjectProperty().get();
    }

    public final void setCurrentRound(Round value) {
        currentRoundObjectProperty().set(value);
    }

    public long getCorrectCount() {
        return pastRounds.stream().filter(Round::isCorrect).count();
    }

    public double getAvgTime() {
        if (pastRounds.size() == 0) return 0;
        return pastRounds.stream().mapToDouble(Round::getElapsedTime).sum() / pastRounds.size();
    }

    public double getAvgMistake() {
        if (pastRounds.size() == 0) return 0;
        return ((double) pastRounds.stream().filter(o -> !o.isCorrect()).count() / pastRounds.size());
    }

    public double getAvgCorrect() {
        if (pastRounds.size() == 0) return 0;
        return ((double) getCorrectCount() / pastRounds.size());
    }

    public double getAvgTimeOnCorrect() {
        if (getCorrectCount() == 0) return 0;
        return pastRounds.stream().filter(Round::isCorrect).mapToDouble(Round::getElapsedTime)
                .sum() / getCorrectCount();
    }

    public boolean isPerfect() {
        return getCorrectCount() == preferences.getMaxRounds();
    }

    public void startGame() {
        if (isStarted())
            return;
        run();
    }

    List<Sound> getSoundsPool() {
        LinkedList<Sound> result = new LinkedList<>();
        if (currentRound == null) {
            result.addAll(soundsPool);
        } else {
            result.add(getCurrentRound().getSound());
            result.addAll(soundsPool);
        }
        return result;
    }


    private void run() {
        if (!isStarted()) { // First turn
            setStarted(true);
        } else {
            endRound();
            if (abortRequested)
                return;
            newRound();
            if (soundsPool.isEmpty()) { // Last turn is fake it shouldn't be added to pastRounds
                setEnded(true);
                return;
            }
        }
        getCurrentRound().startCountdown();
        getCurrentRound().endedBooleanProperty().addListener((observableValue, aBoolean, t1) -> run());
    }

    public void abort() {
        abortRequested = true;
        // Last turn is fake it shouldn't be added to pastRounds
        if (!soundsPool.isEmpty() && !pastRounds.contains(getCurrentRound()))
            endRound(); // Current turn to pastRounds
        while (soundsPool.size() > 1) // Last turn is fake it shouldn't be added to pastRounds
            pastRounds.add(new Round(preferences.getMaxTime(), popFromPool()));
        setEnded(true);
    }

    private Sound popFromPool() {
        return soundsPool.remove(0);
    }

    private void endRound() {
        if (getCurrentRound() == null)
            return;
        getCurrentRound().end();
        pastRounds.add(getCurrentRound());
    }

    private void newRound() {
        if (currentRound == null)
            currentRoundObjectProperty();
        else
            setCurrentRound(new Round(preferences.getMaxTime(), popFromPool()));
    }
}
