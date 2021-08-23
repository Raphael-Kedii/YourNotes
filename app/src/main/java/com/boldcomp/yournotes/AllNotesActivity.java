package com.boldcomp.yournotes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AllNotesActivity extends AppCompatActivity {
    private static final Class<AllNotesActivity> TAG = AllNotesActivity.class;

    private SqliteDatabase mDatabase;
    private ArrayList<Notes> allNotes = new ArrayList<>();
    private NotesAdapter mAdapter;
    Toolbar toolbar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        FrameLayout fLayout = findViewById(R.id.activity_to_do);

        RecyclerView notesView = findViewById(R.id.notes_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        notesView.setLayoutManager(linearLayoutManager);
        notesView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        allNotes = mDatabase.listNotes();

        if (allNotes.size()>0){
            notesView.setVisibility(View.VISIBLE);
            mAdapter = new NotesAdapter(this, allNotes);
            notesView.setAdapter(mAdapter);
        } else {
            notesView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no notes in the database. Start adding now", Toast.LENGTH_LONG).show();

        }


        fab = findViewById(R.id.floatingactionbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskDialog();
            }
        });
    }

    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_notes_layout, null);

        final EditText titleField = subView.findViewById(R.id.text_title);
        final EditText contentField = subView.findViewById(R.id.text_notes);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new NOTE");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD NOTE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String n_title = titleField.getText().toString();
                final String n_content = contentField.getText().toString();

                if (TextUtils.isEmpty(n_title)){
                    Toast.makeText(AllNotesActivity.this, "Something went went wrong", Toast.LENGTH_LONG).show();

                } else {
                    Notes newNote = new Notes(n_title, n_content);
                    mDatabase.addNotes(newNote);

                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AllNotesActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabase != null){
            mDatabase.close();
        }
    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.options_menu, menu);
            //MenuInflater inflater = getMenuInflater();
           // inflater.inflate(R.menu.options_menu, menu);
            MenuItem search = menu.findItem(R.id.search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
            search(searchView);
            return true;
        }
        private void search(SearchView searchView){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (mAdapter !=null)
                        mAdapter.getFilter().filter(newText);
                    return true;
                }
            });
        }
    }


