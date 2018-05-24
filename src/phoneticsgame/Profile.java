package phoneticsgame;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable {
    public static final String AVALIBLENAMEREGEX =
            "[a-zA-Z0-9йцукенгшщзхъфывапролджэячсмитьбюёЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮЁ ]*";
    private String name;

    public Profile(String name) {
        File profileDir = new File(IOManager.PROFILEDIR + name);
        if (!profileDir.exists())
            if (!profileDir.mkdirs())
                System.err.println("User data directory was not created!");
        this.name = name;
    }

    public int getGamesCount(String language) {
        return IOManager.getPastGames(name, language).size();
    }

    public int getCompletelyCorrect(String language) {
        return (int) IOManager.getPastGames(name, language).stream().filter(Game::isPerfect).count();
    }

    public double getAvgCorrect(String language) {
        List<Game> pastGames = IOManager.getPastGames(name, language);
        if (pastGames.size() == 0) return 0;
        return pastGames.stream().mapToDouble(Game::getAvgCorrect).sum() / pastGames.size();
    }

    public double getAvgTimeOnCorrect(String language) {
        List<Game> pastGames = IOManager.getPastGames(name, language);
        if (pastGames.size() == 0) return 0;
        return pastGames.stream().mapToDouble(Game::getAvgTimeOnCorrect).sum() / pastGames.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.isEmpty())
            throw new IllegalArgumentException();
        this.name = name;
    }

}
