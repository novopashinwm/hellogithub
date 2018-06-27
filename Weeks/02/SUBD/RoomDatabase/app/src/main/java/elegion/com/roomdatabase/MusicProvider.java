package elegion.com.roomdatabase;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import elegion.com.roomdatabase.database.Album;
import elegion.com.roomdatabase.database.AlbumSong;
import elegion.com.roomdatabase.database.MusicDao;
import elegion.com.roomdatabase.database.MusicDatabase;
import elegion.com.roomdatabase.database.Song;

public class MusicProvider extends ContentProvider {

    private static final String TAG = MusicProvider.class.getSimpleName();

    private static final String AUTHORITY = "com.elegion.roomdatabase.musicprovider";
    private static final String TABLE_ALBUM = "album";
    private static final String TABLE_SONG = "song";
    private static final String TABLE_ALBUM_SONG ="albumsong" ;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ALBUM_TABLE_CODE = 100;
    private static final int ALBUM_ROW_CODE = 101;

    private static final int SONG_TABLE_CODE = 200;
    private static final int SONG_ROW_CODE = 201;

    private static final int ALBUM_SONG_TABLE_CODE = 300;
    private static final int ALBUM_SONG_ROW_CODE = 301;
    static {
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM, ALBUM_TABLE_CODE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM + "/*", ALBUM_ROW_CODE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_SONG, SONG_TABLE_CODE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_SONG + "/*", SONG_ROW_CODE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM_SONG, SONG_TABLE_CODE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_SONG + "/*", SONG_ROW_CODE);
    }

    private MusicDao mMusicDao;

    public MusicProvider() {
    }

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            mMusicDao = Room.databaseBuilder(getContext().getApplicationContext(), MusicDatabase.class, "music_database")
                    .build()
                    .getMusicDao();
            return true;
        }

        return false;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case ALBUM_TABLE_CODE:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_ALBUM;
            case ALBUM_ROW_CODE:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_ALBUM;
            case SONG_TABLE_CODE:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_SONG;
            case SONG_ROW_CODE:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_SONG;
            case ALBUM_SONG_TABLE_CODE:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_ALBUM_SONG;
            case ALBUM_SONG_ROW_CODE:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_ALBUM_SONG;
            default:
                throw new UnsupportedOperationException("not yet implemented");
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int code = URI_MATCHER.match(uri);

        if (code != ALBUM_ROW_CODE && code != ALBUM_TABLE_CODE  &&
                code != SONG_ROW_CODE && code != SONG_TABLE_CODE  &&
                code != ALBUM_SONG_ROW_CODE && code != ALBUM_SONG_TABLE_CODE)
            return null;

        Cursor cursor = null;
        if (code == ALBUM_SONG_TABLE_CODE) {
            cursor = mMusicDao.getAlbumSongsCursor();
        } else if (code == ALBUM_SONG_ROW_CODE) {
            cursor = mMusicDao.getAlbumSongWithIdCursor((int) ContentUris.parseId(uri));
        }

        else if (code == SONG_TABLE_CODE) {
            cursor = mMusicDao.getSongsCursor();
        } else if (code == SONG_ROW_CODE) {
            cursor = mMusicDao.getSongWithIdCursor((int) ContentUris.parseId(uri));
        }

        else
        if (code == ALBUM_TABLE_CODE) {
            cursor = mMusicDao.getAlbumsCursor();
        } else if (code == ALBUM_ROW_CODE) {
            cursor = mMusicDao.getAlbumWithIdCursor((int) ContentUris.parseId(uri));
        }


        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Integer id;
        if (URI_MATCHER.match(uri) == ALBUM_TABLE_CODE && isAlbumValuesValid(values)) {
            id = insertAlbumID(values);
            return ContentUris.withAppendedId(uri, id);
        } else if (URI_MATCHER.match(uri) == SONG_TABLE_CODE && isSongValuesValid(values)) {
            id = insertSongID(values);
            return ContentUris.withAppendedId(uri, id);
        }
         else if (URI_MATCHER.match(uri) == ALBUM_SONG_TABLE_CODE && isAlbumSongValuesValid(values)) {
            id = insertAlbumSongID(values);
            return ContentUris.withAppendedId(uri, id);
        }
        else {
            throw new IllegalArgumentException("cant add multiple items");
        }
    }

    private Integer insertAlbumSongID(ContentValues values) {
        AlbumSong albumSong = new AlbumSong();
        Integer id = values.getAsInteger("id");
        albumSong.setId(id);
        albumSong.setAlbumId(values.getAsInteger("albumId"));
        albumSong.setSongId(values.getAsInteger("songId"));
        mMusicDao.insertAlbumSong(albumSong);
        return id;
    }

    private boolean isAlbumSongValuesValid(ContentValues values) {
        return values.containsKey("id") && values.containsKey("albumId")
                && values.containsKey("songId");
    }

    private Integer insertSongID(ContentValues values) {
        Song song = new Song();
        Integer id = values.getAsInteger("id");
        song.setId(id);
        song.setName(values.getAsString("name"));
        song.setDuration(values.getAsString("duration"));
        mMusicDao.insertSong(song);
        return id;
    }

    private boolean isSongValuesValid(ContentValues values) {
        return values.containsKey("id") && values.containsKey("name") && values.containsKey("duration");
    }

    @NonNull
    private Integer insertAlbumID(ContentValues values) {
        Album album = new Album();
        Integer id = values.getAsInteger("id");
        album.setId(id);
        album.setName(values.getAsString("name"));
        album.setReleaseDate(values.getAsString("release"));
        mMusicDao.insertAlbum(album);
        return id;
    }

    private boolean isAlbumValuesValid(ContentValues values) {
        return values.containsKey("id") && values.containsKey("name") && values.containsKey("release");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (URI_MATCHER.match(uri) == ALBUM_ROW_CODE && isAlbumValuesValid(values)) {
            return updateAlbum(uri, values);
        } else if (URI_MATCHER.match(uri) == SONG_ROW_CODE && isSongValuesValid(values)) {
            return updateSong(uri, values);
        } else if (URI_MATCHER.match(uri) == ALBUM_SONG_ROW_CODE && isAlbumSongValuesValid(values)) {
            return updateAlbumSong(uri, values);
        }
        else {
            throw new IllegalArgumentException("cant add multiple items");
        }

    }

    private int updateAlbumSong(Uri uri, ContentValues values) {
        AlbumSong albumSong = new AlbumSong();
        int id = (int) ContentUris.parseId(uri);
        albumSong.setId(id);
        albumSong.setAlbumId(values.getAsInteger("albumId"));
        albumSong.setSongId(values.getAsInteger("songId"));
        mMusicDao.insertAlbumSong(albumSong);
        return  id;
    }

    private int updateSong(@NonNull  Uri uri, ContentValues values) {
        Song song = new Song();
        int id = (int) ContentUris.parseId(uri);
        song.setId(id);
        song.setName(values.getAsString("name"));
        song.setDuration(values.getAsString("duration"));
        return mMusicDao.updateSongInfo(song);
    }

    private int updateAlbum(@NonNull Uri uri, ContentValues values) {
        Album album = new Album();
        int id = (int) ContentUris.parseId(uri);
        album.setId(id);
        album.setName(values.getAsString("name"));
        album.setReleaseDate(values.getAsString("release"));
        return  mMusicDao.updateAlbumInfo(album);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        if (URI_MATCHER.match(uri) == ALBUM_ROW_CODE) {
            int id = (int) ContentUris.parseId(uri);
            return mMusicDao.deleteAlbumById(id);
        } else if (URI_MATCHER.match(uri) == SONG_ROW_CODE) {
            int id = (int) ContentUris.parseId(uri);
            return mMusicDao.deleteSongById(id);
        } else if (URI_MATCHER.match(uri) == ALBUM_SONG_ROW_CODE) {
            int id = (int) ContentUris.parseId(uri);
            return mMusicDao.deleteAlbumSongById(id);
        } else {
            throw new IllegalArgumentException("cant add multiple items");
        }
    }
}
