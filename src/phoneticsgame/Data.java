package phoneticsgame;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.nio.file.Paths;

class Data {
    private ObjectProperty<Game> game;
    private ObjectProperty<Preferences> preferences;
    private ObjectProperty<Profile> profile;

    Data() {
        this.game = null;
        try {
            setPreferences((Preferences) IOManager.deserialize(new File(IOManager.PREFERENCES)));
            getPreferences().setLanguage(getPreferences().getLanguage()); // Restoring transient fields.
        } catch (Exception e) {
            setPreferences(new Preferences());
        }
        try {
            setProfile((Profile) IOManager.deserialize(new File(IOManager.LASTPROFILE)));
        } catch (Exception readException) {
            profileObjectProperty();
        }
    }

    public ObjectProperty<Game> gameObjectProperty() {
        if (game == null)
            game = new SimpleObjectProperty<>(new Game(getPreferences()));
        return game;
    }

    public ObjectProperty<Profile> profileObjectProperty() {
        if (profile == null)
            profile = new SimpleObjectProperty<>(null);
        return profile;
    }

    public ObjectProperty<Preferences> preferencesObjectProperty() {
        if (preferences == null)
            preferences = new SimpleObjectProperty<>(new Preferences());
        return preferences;
    }

    public Game getGame() {
        return gameObjectProperty().get();
    }

    void setGame(Game game) {
        gameObjectProperty().set(game);
    }

    public Preferences getPreferences() {
        return preferencesObjectProperty().get();
    }

    public void setPreferences(Preferences preferences) {
        preferencesObjectProperty().set(preferences);
    }

    public Profile getProfile() {
        return profileObjectProperty().get();
    }

    public void setProfile(Profile profile) {
        profileObjectProperty().set(profile);
    }

    public void newGame() {
        setGame(new Game(getPreferences()));
    }

    public void saveGame() {
        if (game == null || getProfile() == null)
            return;
        File folder = new File(IOManager.PROFILEDIR + getProfile().getName());
        File file = new File(Paths.get(folder.getPath(),
                (IOManager.getLastId(folder) + 1) + ".game").toString());
        try {
            IOManager.serialize(getGame(), file);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void savePreferences() {
        try {
            IOManager.serialize(getPreferences(), new File(IOManager.PREFERENCES));
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void saveProfile() {
        try {
            IOManager.serialize(getProfile(), new File(IOManager.LASTPROFILE));
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

}
