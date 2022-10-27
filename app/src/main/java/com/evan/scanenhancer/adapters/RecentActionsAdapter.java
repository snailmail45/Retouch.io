package com.evan.scanenhancer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.evan.scanenhancer.R;
import com.evan.scanenhancer.data.model.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentActionsAdapter extends RecyclerView.Adapter<RecentActionsAdapter.ViewHolder> {

    public interface RecentActionItemsCallback {
        void onItemClicked(Result result);
    }

    private final RecentActionItemsCallback callback;
    private final List<Result> list;

    public RecentActionsAdapter(List<Result> list, RecentActionItemsCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecentActionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_recent_action, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentActionsAdapter.ViewHolder holder, int i) {

        String url = list.get(i).getData().getUrl();
        Glide.with(holder.itemView.getContext())
                .load(url)
                .transform(
                        new CenterCrop(),
                        new GranularRoundedCorners(20, 20, 20, 20)
                )
                .into(holder.ivPreview);


        holder.tvDate.setText(list.get(i).getFormattedDate());
        holder.tvActionType.setText(String.valueOf(list.get(i).getActionType()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_preview)
        ImageView ivPreview;

        @BindView(R.id.tv_action_type)
        TextView tvActionType;

        @BindView(R.id.tv_date)
        TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            callback.onItemClicked(list.get(position));
        }
    }
}
