package phoneticsgame;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

abstract class IOManager {
    public static final String USERDATA = System.getProperty("user.home") + "/.phonetics_game_data/";
    public static final String PROFILEDIR = USERDATA + "profiles/";
    public static final String PREFERENCES = USERDATA + "config.pref";
    public static final String LASTPROFILE = USERDATA + "last.profile";
    public static final String ICON = "res/icon.png";
    public static final String SOUNDDIR = "res/records/";
    public static final String LANGUAGESDIR = "data/languages/";
    public static final String README = "README.html";
    public static final String LICENSE = "LICENSE.html";
    public static final String ABOUT = "ABOUT.html";

    static int getLastId(File folder) {
        int lastID = 0;
        for (File file : folder.listFiles()) {
            if (file.isFile())
                if (file.getName().matches(".*\\.game")) {
                    int tmpID = Integer.parseInt(file.getName().replace(".game", ""));
                    if (tmpID > lastID)
                        lastID = tmpID;
                }
        }
        return lastID;
    }

    static void serialize(Object obj, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file.getPath());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

    static Object deserialize(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        Object obj = in.readObject();
        in.close();
        return obj;
    }

    static List<Game> getPastGames(String name, String language) {
        List<Game> result = new LinkedList<>();
        File folder = new File(Paths.get(PROFILEDIR, name).toString());
        int i;
        int lastId = getLastId(folder);
        for (i = 1; i <= lastId; i++) {
            try {
                FileInputStream fis = new FileInputStream(Paths.get(folder.getPath(), i + ".game").toString());
                ObjectInputStream oin = new ObjectInputStream(fis);
                result.add((Game) oin.readObject());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return result.stream().filter(game -> game.getLanguage().equals(language)).collect(Collectors.toList());
    }

    static void deleteDir(String dirPath) {
        File dir = new File(dirPath);
        File[] contents = dir.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f.getPath());
                }
            }
        }
        dir.delete();
    }
}
