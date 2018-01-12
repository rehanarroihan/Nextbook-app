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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.sch.smktelkom_mlg.nextbook.Model.Post;
import id.sch.smktelkom_mlg.nextbook.R;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

/**
 * Created by Rehan on 29/12/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    IPostAdapter mIPostAdapter;
    private ArrayList<Post> mList;
    private Context context;

    public PostAdapter(Context context, ArrayList<Post> postList, IPostAdapter listener) {
        this.mList = postList;
        this.context = context;
        this.mIPostAdapter = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_class_post, parent, false);
        PostAdapter.ViewHolder vh = new PostAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mList.get(position);
        holder.tvUser.setText(post.getDspname());
        if (post.getLesson().equals(null)) {
            holder.tvLesson.setText("Other");
        } else {
            holder.tvLesson.setText(post.getLesson());
        }
        holder.tvCreate.setText(post.getCreate());
        holder.tvContent.setText(post.getContent());
        holder.tvComment.setText(String.valueOf(post.getComment()) + " Komentar");
        if (post.getPict().equals("N")) {
            holder.ivPt.setVisibility(View.GONE);
        } else {
            holder.ivPt.setVisibility(View.VISIBLE);
        }
        if (post.getDoc().equals("N")) {
            holder.ivFile.setVisibility(View.GONE);
        } else {
            holder.ivFile.setVisibility(View.VISIBLE);
        }
        if (!post.getProv().equals("email")) {
            Glide.with(context)
                    .load(post.getPicts())
                    .into(holder.ivUser);
        } else {
            Glide.with(context)
                    .load(Config.ImageURL + "2.0/img/user/" + post.getPict())
                    .into(holder.ivUser);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public interface IPostAdapter {
        void doClick(int post);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewPostUser)
        CircleImageView ivUser;
        @BindView(R.id.textViewPostUser)
        TextView tvUser;
        @BindView(R.id.textViewPostLesson)
        TextView tvLesson;
        @BindView(R.id.textViewPostCreate)
        TextView tvCreate;
        @BindView(R.id.textViewContent)
        TextView tvContent;
        @BindView(R.id.textViewKomen)
        TextView tvComment;
        @BindView(R.id.iconFile)
        ImageView ivFile;
        @BindView(R.id.iconPhoto)
        ImageView ivPt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIPostAdapter.doClick(getAdapterPosition());
                }
            });
        }
    }
}
