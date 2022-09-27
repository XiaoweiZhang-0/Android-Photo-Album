package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * @author Xiaowei Zhang
 */
public class Photo implements Serializable{
    private static final long serialVersionUID = 1L;

    //private String caption;
    //private Date date;
    //private Calendar cal;
    //public boolean is_stock = false;
    private ArrayList<Tag> taglist;
    private File pic;

    //Path of the photo
    private String filepath;

    //person tag associated with the photo
    private ArrayList<String> persons;

    //Place tag associated with the photo
    private ArrayList<String> places;

    public Photo( String filepath)
    {
        this.filepath = filepath;
        //this.pic = pic;
        persons = new ArrayList<>();
        places = new ArrayList<>();
       this.taglist = new ArrayList<>();
//        this.cal = new GregorianCalendar();
//        cal.set(Calendar.MILLISECOND, 0);
//        this.date = cal.getTime();

    }


//    /**
//     * @return Date
//     */
//    public Date get_date()
//    {
//        return date;
//    }


//    /**
//     * @param caption
//     */
//    public void set_caption(String caption)
//    {
//        this.caption = caption;
//    }


//    /**
//     * @return String
//     */
//    public String get_caption()
//    {
//        return this.caption;
//    }


    /**
     * @param name
     * @param value
     */
    public void add_tag(String name, String value)
    {
        Tag tag1 = new Tag(name, value);
        if(this.exist(tag1) == -1)
        {
            this.taglist.add(tag1);
        }

    }


    /**
     * @param name
     * @param value
     */
    public void remove_tag(String name, String value)
    {
        Tag tag1 = new Tag(name, value);
        int indx = this.exist(tag1);
        if(indx != -1)
        {
            this.taglist.remove(indx);
        }
    }

    /**
     * @param tag1
     * @return int
     */
    private int exist(Tag tag1)
    {
        for(int i=0; i<taglist.size(); i++)
        {
            Tag tag = taglist.get(i);
            if(tag.tag_compare(tag1))
            {
                return i;
            }

        }
        return -1;
    }


    /**
     * @param pic
     */
    public void setPic(File pic)
    {
        this.pic = pic;
    }



    /**
     * @return File
     */
    public File getPic()
    {
        return this.pic;
    }



    /**
     * @return ArrayList<Tag>
     */
    public ArrayList<Tag> get_taglist()
    {
        return this.taglist;
    }



    /**
     * @param name
     * @param value
     * @return boolean
     */
    public boolean tagExists(String name, String value) {
        for(int i = 0; i < taglist.size(); i++) {
            Tag cur = taglist.get(i);
            if(cur.name().toLowerCase().equals(name.toLowerCase()) && cur.value().toLowerCase().equals(value.toLowerCase())) {
                return true;
            }
        }
        return false;

    }

    /**
     * @param fp
     */
    public void setFilePath(String fp) {
        this.filepath = fp;
    }


    /**
     * @return String
     */
    public String getFilePath() {
        return filepath;
    }


    /**
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o)
    {
        if(o == null || o instanceof Photo)
        {
            return false;
        }
        else
        {
            Photo photo = (Photo) o;
            return this.getFilePath().equals(photo.getFilePath());
        }

    }






}