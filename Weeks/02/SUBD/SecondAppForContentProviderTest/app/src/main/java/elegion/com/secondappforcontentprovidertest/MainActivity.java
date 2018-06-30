package elegion.com.secondappforcontentprovidertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Spinner spinnerMusicType;
    Spinner spinnerQuery;
    EditText editText;
    EditText etItem1;
    EditText etItem2;
    EditText etItem3;
    Button btnRun;

    private static final String AUTHORITY = "com.elegion.roomdatabase.musicprovider";
    private static final String TABLE_ALBUM = "album";
    private static final String TABLE_SONG = "song";
    private static final String TABLE_ALBUM_SONG ="albumsong" ;

    private static final int ALBUM_TABLE_CODE = 100;
    private static final int ALBUM_ROW_CODE = 101;

    private static final int SONG_TABLE_CODE = 200;
    private static final int SONG_ROW_CODE = 201;

    private static final int ALBUM_SONG_TABLE_CODE = 300;
    private static final int ALBUM_SONG_ROW_CODE = 301;


    private View.OnClickListener mbtnRunClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {

            String queryText = spinnerQuery.getSelectedItem().toString();
            switch (queryText){
                case "update":
                    if (isEditTextEmptyOrWrong())                        
                        break;
                    updateData();
                    Toast.makeText(MainActivity.this, "Запись обновлена!", Toast.LENGTH_SHORT).show();
                    break;
                case "delete":
                    if (isEditTextEmptyOrWrong())
                        break;
                    deleteData();
                    Toast.makeText(MainActivity.this, "Запись удалена!", Toast.LENGTH_SHORT).show();
                    break;
                case "query":
                    showQueryData();
                    break;
                case "insert":
                    insertData();
                    Toast.makeText(MainActivity.this, "Запись добавлена!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void updateData() {
        ContentValues values = new ContentValues();
        String table = getTablebyMusicType();
        values.put(getItemName1(),getItemValue1());
        values.put(getItemName2(),getItemValue2());
        values.put(getItemName3(),getItemValue3());
        String sId = editText.getText().toString();
        String contentString = "content://" + AUTHORITY + "/" + table +"/" +sId;
        getContentResolver().update(Uri.parse(contentString),values,null,null);
    }

    private void insertData() {
        ContentValues values = new ContentValues();
        String table = getTablebyMusicType();
        values.put(getItemName1(),getItemValue1());
        values.put(getItemName2(),getItemValue2());
        values.put(getItemName3(),getItemValue3());
        String contentString = "content://" + AUTHORITY + "/" + table ;

        getContentResolver().insert(Uri.parse(contentString),values);
    }

    private String getItemValue3() {
        return etItem3.getText().toString();
    }

    private String getItemName3() {
        switch (getTablebyMusicType()) {
            case TABLE_ALBUM: return "release";
            case TABLE_SONG:  return "duration";
            case TABLE_ALBUM_SONG: return "songId";
        }
        return null;
    }

    private String getItemValue2() {
        return etItem2.getText().toString();
    }

    private String getItemName2() {
        if (getTablebyMusicType()==TABLE_ALBUM_SONG)
            return "albumId";
        return "name";
    }

    private String getItemValue1() {
        return etItem1.getText().toString();
    }

    private String getItemName1() {
        return "id";
    }

    private void deleteData() {
        String table = getTablebyMusicType();
        String sId = editText.getText().toString();
        String contentString = "content://" + AUTHORITY + "/" + table + "/"  + sId;
        getContentResolver().delete(Uri.parse(contentString), null, null);
    }

    private String getTablebyMusicType() {
        String table = spinnerMusicType.getSelectedItem().toString();
        switch (table) {
            case "Albums": return TABLE_ALBUM;
            case "Songs": return TABLE_SONG;
            case "AlbumSongs": return TABLE_ALBUM_SONG;
        }
        return null;
    }

    private boolean isEditTextEmptyOrWrong() {
        String text =editText.getText().toString();
        if ( text == null ||text.isEmpty() || ! text.matches("\\d+") ) {
            Toast.makeText(getApplicationContext()
                    , "Для данного типа запроса нужен id записи."
                    , Toast.LENGTH_LONG).show();
            return  true;
        }
        return  false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showQueryData() {
        String musicType = spinnerMusicType.getSelectedItem().toString();
        int id = getId(musicType);
        Bundle bundle = new Bundle();
        bundle.putString("id", editText.getText().toString());
        getSupportLoaderManager().restartLoader(id,bundle, this);
    }

    private int getId(String musicType) {
        int id=0;
        switch (musicType) {
            case "Albums":        id= ALBUM_TABLE_CODE;      break;
            case "Songs":         id= SONG_TABLE_CODE;       break;
            case "AlbumSongs":    id= ALBUM_SONG_TABLE_CODE; break;
        }
        return id;
    }

    private AdapterView.OnItemSelectedListener OnItemSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String musicText = spinnerMusicType.getSelectedItem().toString();
            switch (musicText) {
                case "Albums":
                    etItem2.setHint("name");
                    etItem3.setHint("release date");
                    break;
                case "Songs":
                    etItem2.setHint("name");
                    etItem3.setHint("duration");
                    break;
                case "AlbumSongs":
                    etItem2.setHint("albumId");
                    etItem3.setHint("songId");
                    break;
            }
            
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }

    private void initControls() {
        spinnerMusicType = findViewById(R.id.music_types_spinner);

        spinnerMusicType.setOnItemSelectedListener(OnItemSelectListener);
        spinnerQuery = findViewById(R.id.query_types_spinner);
        editText = findViewById(R.id.edit_text);
        btnRun = findViewById(R.id.btnRun);
        btnRun.setOnClickListener(mbtnRunClickListener);
        etItem1 = findViewById(R.id.etItem1);
        etItem2 = findViewById(R.id.etItem2);
        etItem3 = findViewById(R.id.etItem3);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String table = TABLE_ALBUM_SONG;
        switch (id) {
            case ALBUM_TABLE_CODE:
                table= TABLE_ALBUM;
                break;
            case SONG_TABLE_CODE:
                table = TABLE_SONG;
                break;
            case ALBUM_SONG_TABLE_CODE:
                table = TABLE_ALBUM_SONG;
                break;
        }
        String sid = args.getString("id");
        String contentString = "content://" + AUTHORITY + "/" + table;

        if (sid !=null && !sid.isEmpty())
            contentString += "/" + sid;

        return new CursorLoader(this,
                Uri.parse(contentString),
                null,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            StringBuilder builder = new StringBuilder();
            do {
                for (int i =0; i< data.getColumnCount(); i++) {
                    if (i>0)
                        builder.append(", ");
                    builder.append(data.getColumnName(i));
                    builder.append("=");
                    builder.append(data.getString(i));
                }
                builder.append("\n\n");
            } while (data.moveToNext());
            Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Ошибка id", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
