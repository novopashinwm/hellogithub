package elegion.com.roomdatabase.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Azret Magometov
 */

@Entity
public class Album {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "release")
    private String mReleaseDate;

    public Album() {
    }

    public Album(int id, String name, String releaseDate) {
        mId = id;
        mName = name;
        mReleaseDate = releaseDate;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Album{" + "mId=" + mId +
                ", mName='" + mName +
                ", mReleaseDate='" + mReleaseDate + '}';
    }

    public static List<Album> createAlbums() {
        List<Album> albums = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            albums.add(new Album(i, "album " + i, "release" + System.currentTimeMillis()));
        }
        return albums;
    }

}
