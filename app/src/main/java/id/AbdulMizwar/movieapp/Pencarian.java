package id.AbdulMizwar.movieapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

/**
 * Created by tanwir on 06/06/2016.
 */
public class Pencarian extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    private String[] daftar;
    protected Cursor cursor;
    private DataHelper dbcenter;
    public ListView lv;
    public SearchView search_view;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian);

        lv = (ListView) findViewById(R.id.list_view);
        search_view = (SearchView) findViewById(R.id.search_view);

        dbcenter = new DataHelper(this);
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tbsiswa", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(0).toString();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, daftar);
        lv.setAdapter(adapter);
        search_view.setOnQueryTextListener(this);
        lv.setSelected(true);
        ((ArrayAdapter) lv.getAdapter()).notifyDataSetInvalidated();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}