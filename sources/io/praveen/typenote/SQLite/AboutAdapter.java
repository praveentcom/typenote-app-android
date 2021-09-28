package io.praveen.typenote.SQLite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.praveen.typenote.R;
import java.util.List;

public class AboutAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<String> notes;

    public AboutAdapter(List<String> list) {
        this.notes = list;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.about_list_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.text.setText(this.notes.get(i));
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.notes.size();
    }

    /* access modifiers changed from: package-private */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        MyViewHolder(@NonNull View view) {
            super(view);
            this.text = (TextView) view.findViewById(R.id.about_text);
        }
    }
}
