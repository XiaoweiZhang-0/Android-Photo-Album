package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;




/**
 * @author Xiaowei Zhang
 */
public class Tag implements Serializable{
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;

    public Tag(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    /**
     * @return String
     */
    public String name()
    {
        return this.name;
    }


    /**
     * @return String
     */
    public String value()
    {
        return this.value;
    }


    /**
     * @param tag1
     * @return boolean
     */
    public boolean tag_compare(Tag tag1)
    {
        if(tag1.name.equals(this.name) && tag1.value.equals(this.value))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Tag)) {
            return false;
        }

        Tag tag = (Tag) o;
        return tag.name.equals(this.name) && tag.value.equals(this.value);
    }



}
