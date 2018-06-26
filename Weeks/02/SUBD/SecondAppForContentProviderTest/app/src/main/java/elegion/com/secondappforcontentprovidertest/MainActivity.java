package elegion.com.secondappforcontentprovidertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Spinner spinnerMusicType;
    Spinner spinnerQuery;
    EditText editText;

    Button btnRun;
    private View.OnClickListener mbtnRunClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (editText.getText().toString().isEmpty()) {
                if (spinnerQuery.getSelectedItem().toString().equals("update")
                        || spinnerQuery.getSelectedItem().toString().equals("delete")) {
                    Toast.makeText(getApplicationContext(), "Для данного типа запроса нужен id"
                            , Toast.LENGTH_LONG ).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 0);
                contentValues.put("name", "new Name");
                contentValues.put("release", "tomorrow");
                getContentResolver().update(Uri.parse("content://com.elegion.roomdatabase.musicprovider/album/1"), contentValues, null, null);
            }
        });

        getSupportLoaderManager().initLoader(12, null, this);
        initControls();

    }

    private void initControls() {
        spinnerMusicType= findViewById(R.id.music_types_spinner);
        spinnerQuery = findViewById(R.id.query_types_spinner);
        editText = findViewById(R.id.edit_text);
        btnRun = findViewById(R.id.btnRun);
        btnRun.setOnClickListener(mbtnRunClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Uri.parse("content://com.elegion.roomdatabase.musicprovider/album"),
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
                builder.append(data.getString(data.getColumnIndex("name"))).append("\n");
            } while (data.moveToNext());
            Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
