package id.sch.smktelkom_mlg.nextbook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.sch.smktelkom_mlg.nextbook.Model.Comment;
import id.sch.smktelkom_mlg.nextbook.R;

/**
 * Created by Rehan on 29/12/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ICommentAdapter mICommentAdapter;
    private ArrayList<Comment> commentList;
    private Context context;

    public CommentAdapter(Context context, ArrayList<Comment> commentList) {
        this.commentList = commentList;
        this.context = context;
        this.mICommentAdapter = (ICommentAdapter) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post_comment, parent, false);
        CommentAdapter.ViewHolder vh = new CommentAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        Glide.with(context).load(comment.getPict()).into(holder.ivUser);
        holder.tvComment.setText(Html.fromHtml("<b>" + comment.getDspname() + "</b> " + comment.getComment()));
        holder.tvCreated.setText(comment.getCreateds());
        if (Prefs.getString("uid", null).equals(comment.getUid())) {
            holder.btDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (commentList != null) {
            return commentList.size();
        }
        return 0;
    }

    public interface ICommentAdapter {
        void doDelete(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewCmntUser)
        CircleImageView ivUser;
        @BindView(R.id.textViewUsrCmnt)
        TextView tvComment;
        @BindView(R.id.textViewDateCmnt)
        TextView tvCreated;
        @BindView(R.id.buttonDelete)
        TextView btDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mICommentAdapter.doDelete(getAdapterPosition());
                }
            });
        }
    }
}
