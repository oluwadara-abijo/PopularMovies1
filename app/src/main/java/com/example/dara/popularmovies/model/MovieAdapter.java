package com.example.dara.popularmovies.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dara.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    //List object that holds movie data
    private List<Movie> mMovieData;

    //Constructor
    public MovieAdapter(List<Movie> data, ItemClickListener clickListener) {
        mMovieData = data;
        mItemClickListener = clickListener;
    }

    private final ItemClickListener mItemClickListener;

    //Interface for click handling
    public interface ItemClickListener {
        void onItemClickListener (Movie movie);
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layout = R.layout.item_movie;
        boolean attachToParent = false;
        View view = LayoutInflater.from(context).inflate(layout, viewGroup, attachToParent);
        MovieAdapterViewHolder viewHolder = new MovieAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        Movie currentMovie = mMovieData.get(position);

        Picasso.get()
                .load(currentMovie.getPosterUrl())
                .placeholder(R.drawable.poster_sample)
                .error(R.drawable.poster_error)
                .into(holder.mPosterImageView);

    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    //ViewHolder inner class
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mPosterImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = itemView.findViewById(R.id.image_view_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mItemClickListener.onItemClickListener(mMovieData.get(position));
        }
    }
}
