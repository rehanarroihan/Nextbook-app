package id.sch.smktelkom_mlg.nextbook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.nextbook.Model.Card;
import id.sch.smktelkom_mlg.nextbook.R;

/**
 * Created by Rehan on 20/11/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private ArrayList<Card> cardList;
    private Context context;

    public CardAdapter(Context context, ArrayList<Card> cardList) {
        this.cardList = cardList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.tvTitle.setText(card.getCard_name());
        holder.tvDesc.setText(card.getCard_desc());
    }

    @Override
    public int getItemCount() {
        if (cardList != null) {
            return cardList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.card_title);
            tvDesc = itemView.findViewById(R.id.card_desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
