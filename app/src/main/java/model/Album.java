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
public class Album implements Serializable{
    private static final long serialVersionUID = 1L;


    private String album_name;
    private ArrayList<Photo> photo_list;
    private int num_of_photo = 0;
    private Photo current_photo;

    public Album(String name)
    {
        this.album_name = name;
        this.photo_list = new ArrayList<Photo>();
    }


    /**
     * @param name
     */
    public void SetName(String name)
    {
        album_name = name;
    }

    /**
     * @return String
     */
    public String Getname()
    {
        return album_name;
    }

    /**
     * @param photo
     */
    public void add_photo(Photo photo)
    {
        for(Photo local : photo_list)
        {
            if(photo.getFilePath().equals(local.getFilePath()))
            {
                return;
            }
        }
        photo_list.add(photo);
        num_of_photo++;
    }


    /**
     * @param indx
     */
    public void remove_photo(int indx)
    {
        photo_list.remove(indx);
        num_of_photo--;
    }


    /**
     * @param indx
     * @param album
     */
    public void copy_photo(int indx, Album album)
    {
        album.add_photo(this.photo_list.get(indx));
    }





    /**
     * @param indx
     * @param album
     */
    public void move_photo(int indx, Album album)
    {
        album.add_photo(this.photo_list.get(indx));
        this.remove_photo(indx);
        num_of_photo--;
    }




    /**
     * @return int
     */
    public int photoCount()
    {
        return num_of_photo;
    }


    /**
     * @return ArrayList<Photo>
     */
    public ArrayList<Photo> getPhotos()
    {
        return photo_list;
    }

//
//    /**
//     * @return String
//     */
//    public String getFirstDate()
//    {
//        SimpleDateFormat dateFormatter = new SimpleDateFormat("E, M-d-y 'at' h:m:s a");
//        Date date = null;
//        String dateStr = "No Date";
//        if (!photo_list.isEmpty())
//        {
//            date = photo_list.get(0).get_date();
//            for (Photo photo: photo_list)
//            {
//                if (photo.get_date().before(date))
//                {
//                    date = photo.get_date();
//                }
//            }
//            dateStr = dateFormatter.format(date);
//        }
//
//        return dateStr;
//    }


//    /**
//     * @return String
//     */
//    public String getLastDate()
//    {
//        SimpleDateFormat dateFormatter = new SimpleDateFormat("E, M-d-y 'at' h:m:s a");
//        Date date = null;
//        String dateStr = "No Date";
//        if (!photo_list.isEmpty())
//        {
//            date = this.getPhotos().get(0).get_date();
//            for (Photo photo: photo_list)
//            {
//                if (photo.get_date().after(date))
//                {
//                    date = photo.get_date();
//                }
//            }
//            dateStr = dateFormatter.format(date);
//        }
//
//        return dateStr;
//    }



    /**
     * @return Photo
     */
    public Photo getCurrenPhoto()
    {
        return current_photo;
    }


    /**
     * @param photo
     */
    public void setCurrentPhoto(Photo photo)
    {
        this.current_photo = photo;
    }




}

