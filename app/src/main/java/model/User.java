package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;




/**
 * @author Xiaowei Zhang
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String storeDir = "/data/data/com.example.photo_album/files";
    public static final String storeFile = "data.dat";

    private ArrayList<Album> albums;
    private Album curAlbum;


    public User() {
        albums = new ArrayList<Album>();
        this.curAlbum = null;
    }


    /**
     * @param name
     */
    public void create_album(String name) {
        Album alb = new Album(name);
        albums.add(alb);
    }


    /**
     * @param album
     */
    public void add_album(Album album) {
        albums.add(album);
    }


    /**
     * @param indx
     */
    public void delete_album(int indx) {
        albums.remove(indx);
    }


    /**
     * @return ArrayList<Album>
     */
    public ArrayList<Album> get_albums() {
        return albums;
    }


    /**
     * @param name
     * @return boolean
     */
    public boolean album_exist(String name) {
        for (Album album : albums) {
            if (album.Getname().equals(name)) {
                return true;
            }
        }
        return false;
    }


//    /**
//     * @param fromDate
//     * @param toDate
//     * @return ArrayList<Photo>
//     */
//    public ArrayList<Photo> getPhotosInRange(LocalDate fromDate, LocalDate toDate){
//        ArrayList<Photo> inrange = new ArrayList<Photo>();
//        for(Album album : albums) {
//            for(Photo photo : album.getPhotos()) {
//                Date date = photo.get_date();
//                Calendar pDate = Calendar.getInstance();
//                pDate.setTime(date);
//                int year = pDate.get(Calendar.YEAR);
//                int month = pDate.get(Calendar.MONTH)+1;
//                int dateOfMonth = pDate.get(Calendar.DAY_OF_MONTH);
//
//                // today.set(year, month, dateOfMonth);
//                if(year >= fromDate.getYear() && year <= toDate.getYear())
//                {
//                    if(month >= fromDate.getMonthValue() && month <= toDate.getMonthValue())
//                    {
//                        if(dateOfMonth >= fromDate.getDayOfMonth() && dateOfMonth <= toDate.getDayOfMonth())
//                        {
//                            if(!inrange.contains(photo))
//                            {
//                                inrange.add(photo);
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//        return inrange;
//    }


//    /**
//     * @param taggedlist
//     * @return ArrayList<Photo>
//     */
//    public ArrayList<Photo> getOrTaggedPhotos(ArrayList<Tag> taggedlist) {
//        ArrayList<Photo> photolist = new ArrayList<Photo>();
//        HashSet<Photo> check = new HashSet<Photo>();
//        for (Tag tag : taggedlist) {
//            for (Album album : albums) {
//                for (Photo photo : album.getPhotos()) {
//                    if (photo.tagExists(tag.name(), tag.value())) {
//                        if (!check.contains(photo)) {
//                            check.add(photo);
//                        }
//                    }
//                }
//
//            }
//        }
//        photolist.addAll(check);
//        return photolist;
//    }


//    /**
//     * @param taggedlist
//     * @return ArrayList<Photo>
//     */
//    public ArrayList<Photo> getAndTaggedPhotos(ArrayList<Tag> taggedlist) {
//        ArrayList<Photo> photolist = new ArrayList<Photo>();
//        HashSet<Photo> check = new HashSet<Photo>();
//        for (Album album : albums) {
//            for (Photo photo : album.getPhotos()) {
//                if (photo.get_taglist().containsAll(taggedlist)) {
//                    if (!check.contains(photo)) {
//                        check.add(photo);
//                    }
//                }
//            }
//        }
//        photolist.addAll(check);
//        return photolist;
//    }


    /**
     * @param user
     * @throws IOException
     */
    public static void save(User user) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(user);
        oos.close();
    }


    /**
     * @return User
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static User load() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(storeDir + File.separator + storeFile));
        User user = (User) ois.readObject();
        ois.close();
        return user;
    }


    /**
     * @param album
     */
    public void setCurrentAlbum(Album album) {
        curAlbum = album;
    }


    /**
     * @return Album
     */
    public Album getCurrentAlbum() {
        return curAlbum;
    }

    public ArrayList<Photo> searchCertainTags(String tagkey, String tagvalue) {
        //Used to make sure no duplicates
        HashSet<Photo> check = new HashSet<Photo>();
        for (Album album : albums) {
            for (Photo photo : album.getPhotos()) {
                for (Tag tag : photo.get_taglist()) {

                    System.out.println(tagvalue);
                    System.out.println(tag.value().toLowerCase().contains(tagvalue.toLowerCase()));
                    if (tag.value().toLowerCase().contains(tagvalue.toLowerCase()) && tag.name().toLowerCase().contains(tagkey.toLowerCase())) {
                        check.add(photo);
                    }
                }
            }

        }

        return new ArrayList<Photo>(check);
    }

    //Search All
    public ArrayList<Photo> searchAllTags(String tagvalue) {
        //Used to make sure no duplicates
        HashSet<Photo> check = new HashSet<Photo>();
        for (Album album : albums) {
            for (Photo photo : album.getPhotos()) {
                for (Tag tag : photo.get_taglist()) {
                    System.out.println(tagvalue);
                    System.out.println(tag.value().toLowerCase().contains(tagvalue.toLowerCase()));
                    if (tag.value().toLowerCase().contains(tagvalue.toLowerCase())) {
                        check.add(photo);
                    }
                }
            }

        }

        return new ArrayList<Photo>(check);
    }
}