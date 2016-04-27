package ru.dev2dev.notes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private ArrayList<Note> notes;


    public interface onCardViewClickListener {
        void onCardViewClick(Note note);
    }

    private onCardViewClickListener cardViewClickListener;

    public void setCardViewClickListener(onCardViewClickListener cardViewClickListener) {
        this.cardViewClickListener = cardViewClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView dateTextView;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardview);
            titleTextView = (TextView) view.findViewById(R.id.title);
            descriptionTextView = (TextView) view.findViewById(R.id.description);
            dateTextView = (TextView) view.findViewById(R.id.date);
        }
    }

    public NoteAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Note note = notes.get(position);

        holder.titleTextView.setText(note.getTitle());
        holder.descriptionTextView.setText(note.getDescription());
        holder.dateTextView.setText(note.getDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardViewClickListener!=null) {
                    cardViewClickListener.onCardViewClick(note);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}

