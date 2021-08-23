package com.boldcomp.yournotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Notes> listNotes;
    private ArrayList<Notes> mArrayList;

    private SqliteDatabase mDatabase;

    public  NotesAdapter(Context context, ArrayList<Notes> listNotes){
        this.context = context;
        this.listNotes = listNotes;
        this.mArrayList = listNotes;
        mDatabase = new SqliteDatabase(context);
    }
    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_layout, parent, false);
        return new NotesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        final Notes notes = listNotes.get(position);


        holder.n_title.setText(notes.getN_title());
        holder.n_content.setText(notes.getN_content());

        holder.editNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(notes);
            }
        });
        holder.deleteNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete row from the database
                mDatabase.deleteNotes(notes.getId());

                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()){
                    listNotes = mArrayList;
                } else {
                    ArrayList<Notes> filteredList = new ArrayList<>();
                    for (Notes notes : mArrayList){
                        if (notes.getN_title().toLowerCase().contains(charString)){
                            filteredList.add(notes);
                        }
                    }
                    listNotes = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listNotes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                listNotes = (ArrayList<Notes>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    private void editTaskDialog(final Notes notes){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_notes_layout, null);

        final EditText titleField = subView.findViewById(R.id.text_title);
        final  EditText contentField = subView.findViewById(R.id.text_notes);

        if (notes != null){
            titleField.setText(notes.getN_title());
            contentField.setText(notes.getN_content());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Note");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT NOTE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String n_title = titleField.getText().toString();
                final String n_content = contentField.getText().toString();

                if (TextUtils.isEmpty(n_title)){
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    mDatabase.updateNotes(new Notes(notes.getId(), n_title, n_content));
                    //refresh the activity
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

}
