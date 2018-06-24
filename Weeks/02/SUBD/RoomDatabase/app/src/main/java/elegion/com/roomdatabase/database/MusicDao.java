package elegion.com.roomdatabase.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

/**
 * @author Azret Magometov
 */

@Dao
public interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbum(Album albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSong(Song song);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbumSongs(List<AlbumSong> albumSongs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbumSong(AlbumSong albumSong);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLinksAlbumSongs(List<AlbumSong> linksAlbumSongs);

    @Query("select * from album")
    List<Album> getAlbums();

    @Query("select * from album")
    Cursor getAlbumsCursor();

    @Query("select * from album where id = :albumId")
    Cursor getAlbumWithIdCursor(int albumId);

    @Query("select * from song")
    List<Song> getSongs();

    @Query("select * from song")
    Cursor getSongsCursor();

    @Query("select * from song where id = :songId")
    Cursor getSongWithIdCursor(int songId);

    @Query("select * from albumsong")
    List<AlbumSong> getAlbumSongs();

    @Query("select * from albumsong")
    Cursor getAlbumSongsCursor();

    @Query("select * from albumsong where id = :albumSongId")
    Cursor getAlbumSongWithIdCursor(int albumSongId);

    @Delete
    void deleteAlbum(Album album);

    @Delete
    void deleteSong(Song song);

    @Delete
    void  deleteAlbumSong(AlbumSong albumSong);

    //получить список песен переданного id альбома
    @Query("select * from song inner join albumsong on song.id = albumsong.song_id where album_id = :albumId")
    List<Song> getSongsFromAlbum(int albumId);

    //обновить информацию об альбоме
    @Update
    int updateAlbumInfo(Album album);

    //удалить альбом по id
    @Query("DELETE FROM album where id = :albumId")
    int deleteAlbumById(int albumId);

}
