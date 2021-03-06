package com.materight.streamcorn.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.materight.streamcorn.R;
import com.materight.streamcorn.adapters.base.OnItemClickListener;
import com.materight.streamcorn.scrapers.models.MediaInterface;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter<T extends MediaInterface> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_MEDIA = 1;
    public static final int VIEW_PROGRESS = 0;

    private List<T> mediaList;

    private OnItemClickListener<T> onItemClickListener;

    private RequestManager glide;

    public MediaAdapter(RequestManager glide) {
        this.mediaList = new ArrayList<>();
        this.glide = glide;
        mediaList.add(null);
    }

    public void clearMedia() {
        mediaList.clear();
        mediaList.add(null);
        notifyDataSetChanged();
    }

    public void addMedia(List<T> mediaList) {
        addMedia(mediaList, true);
    }

    public void addMedia(List<T> mediaList, boolean stillLoading) {
        int lastItemPosition = this.mediaList.size() - 1;
        //  Delete progress
        this.mediaList.remove(lastItemPosition);
        this.notifyItemRemoved(lastItemPosition);

        this.mediaList.addAll(mediaList);
        if (stillLoading) {
            this.mediaList.add(null);
        }
        this.notifyItemRangeInserted(lastItemPosition, mediaList.size());
    }

    public void setProgressMessage(String message) {
        notifyItemChanged(mediaList.size() - 1, message);
    }

    public boolean isEmpty() {
        return mediaList.isEmpty();
    }

    @Override
    public int getItemViewType(int position) {
        return mediaList.get(position) != null ? VIEW_MEDIA : VIEW_PROGRESS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_MEDIA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false);
            viewHolder = new MediaViewHolder(view);
            viewHolder.itemView.setOnClickListener(v -> {
                onItemClickListener.onItemClick(v, mediaList.get(viewHolder.getAdapterPosition()));
            });
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaViewHolder) {
            T media = mediaList.get(position);
            ((MediaViewHolder) holder).title.setText(media.getTitle());
            glide.load(media.getImageUrl())
                    .apply(new RequestOptions()
                            .error(R.drawable.media_poster))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(((MediaViewHolder) holder).imageView);
        } else if (holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) holder).setProgress();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (holder instanceof ProgressViewHolder && payloads.size() > 0 && payloads.get(0) instanceof String) {
            ((ProgressViewHolder) holder).setMessage((String) payloads.get(0));
        } else {
            onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView title;
        ImageView imageView;

        MediaViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView text;

        ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            text = itemView.findViewById(R.id.text);
        }

        void setMessage(String message) {
            text.setText(message);
            text.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        void setProgress() {
            text.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}




