package phoneticsgame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

import java.io.Serializable;

public class Turn implements Serializable {
    private boolean correct;
    private Sound sound;
    private int maxTime;
    private int elapsedTime;
    private transient Timeline timeline;
    private transient IntegerProperty currentTime;
    private transient BooleanProperty ended;
    private transient BooleanProperty paused;


    public Turn(int maxTime, Sound sound) {
        elapsedTime = maxTime;
        this.maxTime = maxTime;
        setCurrentTime(maxTime);
        this.correct = false;
        this.ended = new SimpleBooleanProperty(false);
        this.sound = sound;
        setPaused(false);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (getCurrentTime() <= 0) {
                end();
            } else {
                setCurrentTime(getCurrentTime() - 1);
            }
        }));

    }

    public BooleanProperty pausedBooleanProperty() {
        if (paused == null)
            paused = new SimpleBooleanProperty(false);
        return paused;
    }

    public final boolean isPaused() {
        return pausedBooleanProperty().get();
    }

    public final void setPaused(boolean value) {
        pausedBooleanProperty().set(value);
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

    public IntegerProperty currentTimeIntegerProperty() {
        if (currentTime == null)
            currentTime = new SimpleIntegerProperty();
        return currentTime;
    }

    public final int getCurrentTime() {
        return currentTimeIntegerProperty().get();
    }

    public final void setCurrentTime(int value) {
        currentTimeIntegerProperty().set(value);
    }

    public Sound getSound() {
        return sound;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void startCountdown() {
        if (isPaused()) {
            resume();
            return;
        }
        timeline.setCycleCount(maxTime + 1);
        timeline.play();
    }

    public void pause() {
        timeline.stop();
        setPaused(true);
    }

    public void resume() {
        if (isPaused()) {
            timeline.setCycleCount(getCurrentTime() + 1);
            timeline.play();
            setPaused(false);
        }
    }

    public void end() {
        timeline.stop();
        elapsedTime = maxTime - getCurrentTime();
        setEnded(true);
    }

    @Override
    public String toString() {
        return this.sound.toString() + " " + correct;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }
}
