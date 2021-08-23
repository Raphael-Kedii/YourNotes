package com.boldcomp.yournotes;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class NotesViewHolder extends RecyclerView.ViewHolder {
    public TextView n_title, n_content;
    public ImageView editNotes, deleteNotes;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        n_title = itemView.findViewById(R.id.title_name);
        n_content = itemView.findViewById(R.id.notes_content);
        editNotes = itemView.findViewById(R.id.edit_notes);
        deleteNotes = itemView.findViewById(R.id.delete_notes);
    }
}
