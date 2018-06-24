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
public class Song {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "duration")
    private String mDuration;

    public Song() {
    }

    public Song(int id, String name, String duration) {
        mId = id;
        mName = name;
        mDuration = duration;
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

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public static List<Song> createSongs() {
        List<Song> songs = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            songs.add(new Song(i, "song " + i, "duration " + System.currentTimeMillis()));
        }
        return songs;
    }
}
