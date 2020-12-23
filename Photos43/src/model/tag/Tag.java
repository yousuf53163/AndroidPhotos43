/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package model.tag;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class Tag implements Serializable
{
    private static final long serialVersionUID = -7876042005953464389L;
    private TagType tagType;
    private String value;

    /**
     * Tag constructor
     * @param tagType
     * @param value
     */
    public Tag(TagType tagType, String value)
    {
        this.tagType = tagType;
        this.value = value;
    }

    /**
     * @return value of tagType
     */
    public TagType getTagType()
    {
        return tagType;
    }

    /**
     * @return value of value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value value
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * Read saved tags
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream ois)
        throws IOException, ClassNotFoundException
    {
        this.tagType = (TagType) ois.readObject();
        this.value = (String) ois.readObject();
    }

    /**
     * Save tags
     * @param oos
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream oos)
        throws IOException
    {
        oos.writeObject(tagType);
        oos.writeObject(value);
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
