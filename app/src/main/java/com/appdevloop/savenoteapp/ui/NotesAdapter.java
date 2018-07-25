package com.appdevloop.savenoteapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdevloop.savenoteapp.R;
import com.appdevloop.savenoteapp.database.NoteEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AppDevloop on 24/07/2018.
 */
public class NotesAdapter extends RecyclerView.Adapter {

    private final List<NoteEntity> mNotes;
    private final Context mContext;

    public NotesAdapter(List<NoteEntity> mNotes, Context mContext) {
        this.mNotes = mNotes;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.note_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_note)
        TextView tv_note;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(int position) {
            tv_note.setText(mNotes.get(position).getText());
        }
    }
}
