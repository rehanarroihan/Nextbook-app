package id.sch.smktelkom_mlg.nextbook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.nextbook.Model.Member;
import id.sch.smktelkom_mlg.nextbook.R;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

/**
 * Created by Rehan on 29/12/2017.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private ArrayList<Member> memberList;
    private Context context;

    public MemberAdapter(Context context, ArrayList<Member> memberList) {
        this.memberList = memberList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_class_member, parent, false);
        MemberAdapter.ViewHolder vh = new MemberAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Member member = memberList.get(position);
        holder.tvName.setText(member.getName());
        holder.tvEmail.setText(member.getEmail());
        if (!member.getProv().equals("email")) {
            Glide.with(context)
                    .load(member.getPps())
                    .into(holder.ivMember);
        } else {
            Glide.with(context)
                    .load(Config.ImageURL + "2.0/img/user/" + member.getPp())
                    .into(holder.ivMember);
        }
    }

    @Override
    public int getItemCount() {
        if (memberList != null) {
            return memberList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail;
        ImageView ivMember;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewNameMember);
            tvEmail = itemView.findViewById(R.id.textViewEmailMember);
            ivMember = itemView.findViewById(R.id.imageViewMember);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
