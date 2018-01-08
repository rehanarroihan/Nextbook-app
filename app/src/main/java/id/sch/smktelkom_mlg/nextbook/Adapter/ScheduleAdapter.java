package id.sch.smktelkom_mlg.nextbook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.nextbook.Model.Schedule;
import id.sch.smktelkom_mlg.nextbook.R;

/**
 * Created by Rehan on 29/12/2017.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private ArrayList<Schedule> scheduleList;
    private Context context;

    public ScheduleAdapter(Context context, ArrayList<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_class_schedule, parent, false);
        ScheduleAdapter.ViewHolder vh = new ScheduleAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.tvStart.setText(schedule.getStart());
        holder.tvEnd.setText(schedule.getEnd());
        holder.tvLesson.setText(schedule.getLesson());
        holder.tvTeacher.setText(schedule.getTeacher());
    }

    @Override
    public int getItemCount() {
        if (scheduleList != null) {
            return scheduleList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStart, tvEnd, tvLesson, tvTeacher;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStart = itemView.findViewById(R.id.textViewStart);
            tvEnd = itemView.findViewById(R.id.textViewEnd);
            tvLesson = itemView.findViewById(R.id.textViewLesson);
            tvTeacher = itemView.findViewById(R.id.textViewTeacher);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
