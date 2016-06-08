package ru.dev2dev.notes.adapters;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.dev2dev.notes.Note;
import ru.dev2dev.notes.R;
import ru.dev2dev.notes.data.NoteCursor;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Cursor cursor;

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public Cursor getItem(int position) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.moveToPosition(position);
        }
        return cursor;
    }

    private OnCardViewClickListener cardViewClickListener;

    public interface OnCardViewClickListener {
        void onCardViewClick(Note note);
    }

    public NoteAdapter(OnCardViewClickListener listener) {
        cardViewClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleTextView;
        TextView descriptionTextView;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardview);
            titleTextView = (TextView) view.findViewById(R.id.title);
            descriptionTextView = (TextView) view.findViewById(R.id.description);
        }
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

        Cursor item = getItem(position);

        final Note note = new NoteCursor(item).getNote();

        holder.titleTextView.setText(note.getTitle());
        holder.descriptionTextView.setText(note.getDescription());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewClickListener.onCardViewClick(note);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

}

