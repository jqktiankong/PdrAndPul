package com.jqk.pdrandpul;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jqk.pdrandpullibrary.view.PdrAndPulView;

import java.util.List;

/**
 * Created by YASCN on 2017/6/21.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> stringList;

    private Context context;

    private PdrAndPulView pdrAndPulView;

    public MyAdapter(List<String> stringList, PdrAndPulView pdrAndPulView) {
        this.stringList = stringList;
        this.pdrAndPulView = pdrAndPulView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder((LinearLayout) view);
        viewHolder.tv = (TextView) view.findViewById(R.id.textView);
        viewHolder.cardView = (CardView) view.findViewById(R.id.cardView);
//        viewHolder.rippleView = (RippleView) view.findViewById(R.id.rippleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv.setText(stringList.get(position));

//        holder.rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
//            @Override
//            public void onComplete(RippleView rippleView) {
//                Toast.makeText(context, "rippleView被点了 + " + position, Toast.LENGTH_SHORT).show();
//            }
//        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdrAndPulView.isLoading()) {
                    Toast.makeText(context, "正在更新数据", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "cardView被点了 + " + position, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout item;
        private TextView tv;
        private CardView cardView;
//        private RippleView rippleView;

        public ViewHolder(LinearLayout itemView) {
            super(itemView);
            item = itemView;
        }
    }
}
