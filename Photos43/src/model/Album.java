/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package model;

import model.user.User;
import model.user.UserDataStore;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class Album implements Serializable
{
    private static final long serialVersionUID = 8634548610593015945L;
    private User user;
    private String name;
    private List<Photo> photos;

    /**
     * Build album
     * @param user
     * @param name
     */
    public Album(User user, String name)
    {
        this.user = user;
        this.name = name;
        this.photos = new ArrayList<>();
    }

    /**
     * @return value of name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Earliest date of photos in album
     */
    public Date earliestPhoto()
    {
        if (this.photos.isEmpty()) {
            return null;
        }
        return Collections.min(this.photos, Comparator.comparing(Photo::getDate)).getDate();
    }

    /**
     * @return Latest date of photos in album
     */
    public Date latestPhoto()
    {
        if (this.photos.isEmpty()) {
            return null;
        }
        return Collections.max(this.photos, Comparator.comparing(Photo::getDate)).getDate();
    }

    /**
     * @return Number of photos in album
     */
    public int numberOfPhotos()
    {
        return photos.size();
    }

    /**
     * Adds photo to album
     * @param photo
     */
    public void addPhoto(Photo photo)
    {
        photos.add(photo);
        UserDataStore.save(user);
    }

    /**
     * Deleted photo from album
     * @param photoToDelete
     */
    public void removePhoto(Photo photoToDelete)
    {
        photos.removeIf(photo -> photo.getFilePath().equals(photoToDelete.getFilePath()));
        UserDataStore.save(user);
    }

    /**
     * @return value of photos
     */
    public List<Photo> getPhotos()
    {
        return new ArrayList<>(photos);
    }

    /**
     * @param user user
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * Loads album
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream ois)
        throws IOException, ClassNotFoundException
    {
        this.name = (String) ois.readObject();
        this.photos = (List<Photo>) ois.readObject();
    }

    /**
     * Saves album
     * @param oos
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream oos)
        throws IOException
    {
        oos.writeObject(name);
        oos.writeObject(photos);
    }

    /**
     *
     * @throws ObjectStreamException
     */
    private void readObjectNoData()
        throws ObjectStreamException
    {

    }
}
