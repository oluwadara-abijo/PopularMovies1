package com.example.dara.popularmovies.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.database.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    //List object that holds movie data
    private List<Movie> mMovies;

    private final ItemClickListener mItemClickListener;

    //Constructor
    public MovieAdapter(List<Movie> data, ItemClickListener clickListener) {
        mMovies = data;
        mItemClickListener = clickListener;
    }

    //Interface for click handling
    public interface ItemClickListener {
        void onItemClickListener (Movie movie);
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layout = R.layout.item_movie;
        View view = LayoutInflater.from(context).inflate(layout, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        Movie currentMovie = mMovies.get(position);

        Picasso.get()
                .load(currentMovie.getPosterUrl())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.poster_error)
                .into(holder.mPosterImageView);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    /**
     * When data changes, this method updates the list of favourite movies
     * and notifies the adapter to use the new values on it
     */
    public void setFavouriteMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    //ViewHolder inner class
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView mPosterImageView;

        MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = itemView.findViewById(R.id.image_view_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mItemClickListener.onItemClickListener(mMovies.get(position));
        }
    }
}
