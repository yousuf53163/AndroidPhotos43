/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package model.user;

import model.Album;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserDataStore
{
    private static final String USER_STORE_PATH = System.getProperty("user.dir") + "/data/users";
    private static final String SUFFIX = ".user";

    public static List<User> loadUsers()
    {
        List<User> users = new ArrayList<>();
        File userDir = new File(USER_STORE_PATH);
        File[] userFileList = userDir.listFiles((dir, name) -> name.endsWith(".user"));

        if(userFileList == null)
        {
            return users;
        }

        for (File currentFile : userFileList)
        {
            String username = currentFile.getName().replace(".user", "");
            User user = load(username);
            // Link the albums to the user.
            for (Album album : user.getAlbums()) {
                album.setUser(user);
            }
            users.add(user);
        }

        return users;
    }

    public static User getUser(String name){
        List<User> allUsers = loadUsers();
        for (User user : allUsers) {
            if (name.equals(user.getName())) {
                return user;
            }
        }
        return null;
    }

    public static void save(User user)
    {
        try {
            File metaDataFile = getMetaDataFile(user.getName());
            FileOutputStream fos = new FileOutputStream(metaDataFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user);
            oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void delete(User user)
    {
        try {
            getMetaDataFile(user.getName()).delete();
        }
        catch (IOException e)
        {
        }
    }

    private static User load(String username)
    {
        try {
            File metaDataFile = getMetaDataFile(username);
            FileInputStream fis = new FileInputStream(metaDataFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (User) ois.readObject();
        }
        catch (IOException|ClassNotFoundException e)
        {
            return null;
        }
    }

    private static File getMetaDataFile(String username) throws IOException
    {
        File metaDataFile = new File(USER_STORE_PATH + "/" + username + SUFFIX);
        // Make sure meta data file exists.
        if (!metaDataFile.exists()) {
            metaDataFile.createNewFile();
        }
        return metaDataFile;
    }
}
