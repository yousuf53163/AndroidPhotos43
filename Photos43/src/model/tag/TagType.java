/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package model.tag;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class TagType implements Serializable
{
    private static final long serialVersionUID = -76435272961213564L;
    private String name;
    private boolean canHaveMultiple;

    /**
     * Build tag type
     * @param name
     * @param canHaveMultiple
     */
    public TagType(String name, boolean canHaveMultiple)
    {
        this.name = name;
        this.canHaveMultiple = canHaveMultiple;
    }

    /**
     * @return value of name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return value of canHaveMultiple
     */
    public boolean isCanHaveMultiple()
    {
        return canHaveMultiple;
    }

    /**
     * Load tag types
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream ois)
        throws IOException, ClassNotFoundException
    {
        this.name = (String) ois.readObject();
        this.canHaveMultiple = ois.readBoolean();
    }

    /**
     * Save tag types
     * @param oos
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream oos)
        throws IOException
    {
        oos.writeObject(this.name);
        oos.writeBoolean(this.canHaveMultiple);
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
