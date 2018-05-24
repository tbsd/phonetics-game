package phoneticsgame;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Game implements Serializable {
    private List<Turn> pastTurns;
    private Preferences preferences;
    private transient List<Sound> soundsPool;
    private transient boolean abortRequested = false;
    private transient ObjectProperty<Turn> currentTurn;
    private transient BooleanProperty started;
    private transient BooleanProperty ended;

    public Game(Preferences preferences) {
        pastTurns = new LinkedList<>();
        this.preferences = preferences;
        soundsPool = preferences.getRandPool(preferences.getMaxTurns() + 1); // + 1 for two options on last turn
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

    public ObjectProperty<Turn> currentTurnObjectProperty() {
        if (currentTurn == null)
            currentTurn = new SimpleObjectProperty<>(new Turn(preferences.getMaxTime(), popFromPool()));
        return currentTurn;
    }

    public String getLanguage() {
        return preferences.getLanguage();
    }

    public final Turn getCurrentTurn() {
        return currentTurnObjectProperty().get();
    }

    public final void setCurrentTurn(Turn value) {
        currentTurnObjectProperty().set(value);
    }

    public long getCorrectCount() {
        return pastTurns.stream().filter(Turn::isCorrect).count();
    }

    public double getAvgTime() {
        if (pastTurns.size() == 0) return 0;
        return pastTurns.stream().mapToDouble(Turn::getElapsedTime).sum() / pastTurns.size();
    }

    public double getAvgMistake() {
        if (pastTurns.size() == 0) return 0;
        return ((double) pastTurns.stream().filter(o -> !o.isCorrect()).count() / pastTurns.size());
    }

    public double getAvgCorrect() {
        if (pastTurns.size() == 0) return 0;
        return ((double) getCorrectCount() / pastTurns.size());
    }

    public double getAvgTimeOnCorrect() {
        if (getCorrectCount() == 0) return 0;
        return pastTurns.stream().filter(Turn::isCorrect).mapToDouble(Turn::getElapsedTime)
                .sum() / getCorrectCount();
    }

    public boolean isPerfect() {
        return getCorrectCount() == preferences.getMaxTurns();
    }

    public void startGame() {
        if (isStarted())
            return;
        run();
    }

    List<Sound> getSoundsPool() {
        LinkedList<Sound> result = new LinkedList<>();
        if (currentTurn == null) {
            result.addAll(soundsPool);
        } else {
            result.add(getCurrentTurn().getSound());
            result.addAll(soundsPool);
        }
        return result;
    }


    private void run() {
        if (!isStarted()) { // First turn
            setStarted(true);
        } else {
            endTurn();
            if (abortRequested)
                return;
            newTurn();
            if (soundsPool.isEmpty()) { // Last turn is fake it shouldn't be added to pastTurns
                setEnded(true);
                return;
            }
        }
        getCurrentTurn().startCountdown();
        getCurrentTurn().endedBooleanProperty().addListener((observableValue, aBoolean, t1) -> run());
    }

    public void abort() {
        abortRequested = true;
        // Last turn is fake it shouldn't be added to pastTurns
        if (!soundsPool.isEmpty() && !pastTurns.contains(getCurrentTurn()))
            endTurn(); // Current turn to pastTurns
        while (soundsPool.size() > 1) // Last turn is fake it shouldn't be added to pastTurns
            pastTurns.add(new Turn(preferences.getMaxTime(), popFromPool()));
        setEnded(true);
    }

    private Sound popFromPool() {
        return soundsPool.remove(0);
    }

    private void endTurn() {
        if (getCurrentTurn() == null)
            return;
        getCurrentTurn().end();
        pastTurns.add(getCurrentTurn());
    }

    private void newTurn() {
        if (currentTurn == null)
            currentTurnObjectProperty();
        else
            setCurrentTurn(new Turn(preferences.getMaxTime(), popFromPool()));
    }
}
