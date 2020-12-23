/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package model;

import model.tag.Tag;
import model.user.User;
import model.user.UserDataStore;

import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Photo implements Serializable
{
    private static final long serialVersionUID = -2287964197101407032L;
    private String filePath;
    private String caption;
    private Date date;
    private List<Tag> tags;

    /**
     *
     * @param file
     */
    public Photo(File file)
    {
        this(file, "", new ArrayList<>());
    }

    /**
     *
     * @param photo
     */
    public Photo(Photo photo)
    {
        super();
        this.filePath = photo.filePath;
        this.caption = photo.caption;
        this.date = photo.date;
        this.tags = photo.tags;
    }

    /**
     *
     * @param file
     * @param caption
     * @param tags
     */
    public Photo(File file, String caption, List<Tag> tags)
    {
        this.filePath = file.getAbsolutePath();
        this.caption = caption;
        this.date = new Date(file.lastModified());
        this.tags = tags;
    }

    /**
     *
     * @return file path of photo
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * @return value of caption
     */
    public String getCaption()
    {
        return caption;
    }

    /**
     * @param caption caption
     */
    public void setCaption(String caption)
    {
        this.caption = caption;
        // TODO: update file
    }

    /**
     * @return value of date
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * @param date date
     */
    public void setDate(Date date)
    {
        this.date = date;
        // TODO: update file
    }

    /**
     * @return value of tags
     */
    public List<Tag> getTags()
    {
        return tags;
    }

    /**
     * @param tag tag
     */
    public void addTag(User user, Tag tag)
    {
        this.tags.add(tag);
        UserDataStore.save(user);
    }

    /**
     * @param tag tag
     */
    public void removeTag(User user, Tag tag)
    {
        this.tags.removeIf((tempTag) -> {
            String tagName1 = tempTag.getTagType().getName();
            String tagName2 = tag.getTagType().getName();
            String tag1 = tempTag.getValue();
            String tag2 = tag.getValue();
            return ((tagName1.equals(tagName2)) && (tag1.equals(tag2)));
        });

        UserDataStore.save(user);
    }

    /**
     * Loads photo
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream ois)
        throws IOException, ClassNotFoundException
    {
        this.filePath = (String) ois.readObject();
        this.caption = (String) ois.readObject();
        this.date = (Date) ois.readObject();
        this.tags = (List<Tag>) ois.readObject();
    }

    /**
     * Saves photo
     * @param oos
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream oos)
        throws IOException
    {
        oos.writeObject(filePath);
        oos.writeObject(caption);
        oos.writeObject(date);
        oos.writeObject(tags);
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