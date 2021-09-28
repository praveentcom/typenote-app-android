package io.praveen.typenote.SQLite;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.praveen.typenote.R;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {
    private List<Note> filtered;
    private List<Note> fullNote;
    private List<Note> notes;

    public NoteAdapter(List<Note> list) {
        this.notes = list;
        this.fullNote = list;
    }

    public int impPos(int i) {
        int i2 = 0;
        int i3 = -1;
        for (Note note : this.fullNote) {
            if (note.getStar() == 1 && (i3 = i3 + 1) == i) {
                return i2;
            }
            i2++;
        }
        return i2;
    }

    @NonNull
    public Filter getFilter() {
        return new Filter() {
            /* class io.praveen.typenote.SQLite.NoteAdapter.AnonymousClass1 */

            /* access modifiers changed from: protected */
            @NonNull
            public Filter.FilterResults performFiltering(@NonNull CharSequence charSequence) {
                String charSequence2 = charSequence.toString();
                if (charSequence2.length() == 0) {
                    NoteAdapter.this.notes = new ArrayList(NoteAdapter.this.fullNote);
                } else {
                    ArrayList arrayList = new ArrayList();
                    for (Note note : NoteAdapter.this.fullNote) {
                        char c = 65535;
                        int hashCode = charSequence2.hashCode();
                        if (hashCode != 1107582) {
                            if (hashCode == 1115305 && charSequence2.equals("#IMP")) {
                                c = 1;
                            }
                        } else if (charSequence2.equals("#ALL")) {
                            c = 0;
                        }
                        switch (c) {
                            case 0:
                                arrayList.addAll(NoteAdapter.this.fullNote);
                                break;
                            case 1:
                                if (note.getStar() == 1) {
                                    arrayList.add(note);
                                    break;
                                } else {
                                    break;
                                }
                            default:
                                if (!note.getNote().toLowerCase().contains(charSequence2)) {
                                    if (!note.getNote().contains(charSequence2)) {
                                        if (!note.getTitle().toLowerCase().contains(charSequence2)) {
                                            if (note.getTitle().contains(charSequence2)) {
                                                arrayList.add(note);
                                                break;
                                            } else {
                                                break;
                                            }
                                        } else {
                                            arrayList.add(note);
                                            break;
                                        }
                                    } else {
                                        arrayList.add(note);
                                        break;
                                    }
                                } else {
                                    arrayList.add(note);
                                    break;
                                }
                        }
                    }
                    NoteAdapter.this.notes = arrayList;
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = NoteAdapter.this.filtered;
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence charSequence, @NonNull Filter.FilterResults filterResults) {
                NoteAdapter.this.filtered = (ArrayList) filterResults.values;
                NoteAdapter.this.notifyDataSetChanged();
            }
        };
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false));
    }

    @SuppressLint({"SetTextI18n"})
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Note note = this.notes.get(i);
        myViewHolder.setIsRecyclable(false);
        myViewHolder.text.setText(note.getNote());
        myViewHolder.date.setText(note.getDate());
        if (note.getTitle().length() == 0) {
            myViewHolder.title.setText("Untitled Note");
        } else {
            myViewHolder.title.setText(note.getTitle());
        }
        if (note.getStar() == 1) {
            myViewHolder.imp.setBackgroundColor(Color.parseColor("#0081E9"));
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.notes.size();
    }

    /* access modifiers changed from: package-private */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private LinearLayout imp;
        private TextView text;
        private TextView title;

        MyViewHolder(@NonNull View view) {
            super(view);
            this.text = (TextView) view.findViewById(R.id.text_note);
            this.date = (TextView) view.findViewById(R.id.text_date);
            this.title = (TextView) view.findViewById(R.id.text_title);
            this.imp = (LinearLayout) view.findViewById(R.id.important);
        }
    }
}
