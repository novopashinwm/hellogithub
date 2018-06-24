package elegion.com.roomdatabase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import elegion.com.roomdatabase.database.Album;
import elegion.com.roomdatabase.database.AlbumSong;
import elegion.com.roomdatabase.database.MusicDao;
import elegion.com.roomdatabase.database.Song;

public class MainActivity extends AppCompatActivity {
    private Button mAddBtn;
    private Button mGetBtn;

    // добавить базу данных Room ----
    // вставить данные / извлечь данные ---
    // добавить контент провайдер над Room ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MusicDao musicDao = ((AppDelegate) getApplicationContext()).getMusicDatabase().getMusicDao();

        mAddBtn = (findViewById(R.id.add));
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicDao.deleteAlbumAll();
                musicDao.deleteSongAll();
                musicDao.deleteAlbumSongAll();
                musicDao.insertAlbums(Album.createAlbums());
                musicDao.insertSongs(Song.createSongs());
                musicDao.insertAlbumSongs(AlbumSong.createAlbumSongs());
            }
        });

        mGetBtn = findViewById(R.id.get);
        mGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                sb.append(getAlbumsString(musicDao.getAlbums())).append("\n");
                sb.append(getSongsString(musicDao.getSongs())).append("\n");
                sb.append(getAlbumSongsString(musicDao.getAlbumSongs())).append("\n");
                showToastAll(sb.toString());
            }
        });

    }

    private List<Album> createAlbums() {
        List<Album> albums = new ArrayList<>(10);
        for (int i = 0; i < 30; i++) {
            albums.add(new Album(i, "album " + i, "release" + System.currentTimeMillis()));
        }
        return albums;
    }

    private void showToastAll(String strAll) {
        Toast.makeText(this, strAll, Toast.LENGTH_SHORT).show();
    }

    private void showToast(List<Album> albums) {
        String strAlbums = getAlbumsString(albums);
        Toast.makeText(this, strAlbums, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    private String getAlbumsString(List<Album> albums) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, size = albums.size(); i < size; i++) {
            builder.append(albums.get(i).toString()).append("\n");
        }
        return builder.toString();
    }

    @NonNull
    private String getSongsString(List<Song> songs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, size = songs.size(); i < size; i++) {
            builder.append(songs.get(i).toString()).append("\n");
        }
        return builder.toString();
    }

    @NonNull
    private String getAlbumSongsString(List<AlbumSong> albumSongs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, size = albumSongs.size(); i < size; i++) {
            builder.append(albumSongs.get(i).toString()).append("\n");
        }
        return builder.toString();
    }

}
