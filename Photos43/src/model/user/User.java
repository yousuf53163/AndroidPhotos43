/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package model.user;

import model.Album;
import model.tag.*;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable
{
    private static final long serialVersionUID = -3290945963173185209L;

    private String name;
    private List<Album> albums;
    public List<TagType> tagTypes;

    public User(String name)
    {
        this.name = name;
        this.albums = new ArrayList<>();
        this.tagTypes = new ArrayList<>();
        addTagType(new TagType("location", false));
        addTagType(new TagType("people", true));
    }

    public boolean isStock()
    {
        return "stock".equals(name);
    }

    public List<TagType> getTagTypes()
    {
        return tagTypes;
    }

    public void addTagType(TagType type) {
        this.tagTypes.add(type);
        UserDataStore.save(this);
    }
    /**
     * @return value of name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return value of albums
     */
    public List<Album> getAlbums()
    {
        return new ArrayList<>(albums);
    }

    public Album addAlbum(String name)
    {
        Album album = new Album(this, name);
        albums.add(album);
        UserDataStore.save(this);
        return album;
    }

    public Album findAlbum(String name) {
        return albums.stream().filter((album -> name.equals(album.getName()))).findFirst().get();
    }

    public void removeAlbum(String name)
    {
        albums.removeIf(album -> album.getName().equals(name));
        UserDataStore.save(this);
    }

    private void readObject(java.io.ObjectInputStream ois)
        throws IOException, ClassNotFoundException
    {
        this.name = (String) ois.readObject();
        this.albums = (List<Album>) ois.readObject();
        this.tagTypes = (List<TagType>) ois.readObject();
    }

    private void writeObject(java.io.ObjectOutputStream oos)
        throws IOException
    {
        oos.writeObject(name);
        oos.writeObject(albums);
        oos.writeObject(tagTypes);
    }
    private void readObjectNoData()
        throws ObjectStreamException
    {

    }
}
