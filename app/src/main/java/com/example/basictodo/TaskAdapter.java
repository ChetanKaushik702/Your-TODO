package com.example.basictodo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> tasks;
    private LongItemSelected activity;
    public static View v;

    public interface LongItemSelected {
        void onLongItemSelected(int id, String title);
    }

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        this.tasks = tasks;
        activity = (LongItemSelected) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(tasks.get(position));
        holder.tvTitle.setText(tasks.get(position).getTitle());
        holder.tvDate.setText(tasks.get(position).getDate());
        holder.tvTime.setText(tasks.get(position).getTime());
        holder.tvDescription.setText(tasks.get(position).getDescription());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                activity.onLongItemSelected(ApplicationClass.tasks.indexOf((Task)view.getTag()), holder.tvTitle.getText().toString());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView tvTitle, tvDate, tvTime, tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        }
    }

    public static int getPosition() {
        return ApplicationClass.tasks.indexOf((Task)(v.getTag()));
    }

    public static void uncheckCheckbox() {
        CheckBox cb = v.findViewById(R.id.checkBox);
        cb.setChecked(false);
    }
}
