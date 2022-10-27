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
import com.evan.scanenhancer.data.model.FilterItem;
import com.evan.scanenhancer.util.SpacingUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    public interface FilterAdapterCallback {
        void onItemClick(FilterItem filterItem);
    }

    private static final int CARD_CORNERS_VALUE_DP = 16;
    private int cardRadiusPixels;
    private final List<FilterItem> list;
    private final FilterAdapterCallback callback;

    public FilterAdapter(List<FilterItem> list, FilterAdapterCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        cardRadiusPixels = SpacingUtils.convertIntToDP(recyclerView.getContext(), CARD_CORNERS_VALUE_DP);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.viewholder_home_action, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = (int) (parent.getWidth() * 0.25);
        //Do not set layoutParams.height. It will prevent centering
        itemView.setLayoutParams(layoutParams);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.tvName.setText(list.get(i).getName());
        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(list.get(i).getPreview())
                .transform(
                        new CenterCrop(),
                        new GranularRoundedCorners(
                                cardRadiusPixels,
                                cardRadiusPixels,
                                cardRadiusPixels,
                                cardRadiusPixels
                        )
                )
                .into(holder.ivLocation);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_location)
        ImageView ivLocation;

        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callback.onItemClick(list.get(getAbsoluteAdapterPosition()));
        }
    }
}
