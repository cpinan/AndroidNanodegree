package com.carlospinan.popularmovies.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.helpers.APIHelper;
import com.carlospinan.popularmovies.models.MovieModel;

import java.util.List;

/**
 * @author Carlos Piñan
 */
public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.ViewHolder> {

    public interface PopularMoviesListener {
        void onMovieClicked(View view, MovieModel movieModel, String transitionNameId);
    }

    private List<MovieModel> movieModelList;
    private LayoutInflater inflater;
    private PopularMoviesListener listener;

    public PopularMoviesAdapter(Context context, List<MovieModel> movieModelList) {
        this.movieModelList = movieModelList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_popularmovies, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MovieModel movieModel = movieModelList.get(position);
        if (movieModel != null && movieModel.getPosterPath() != null) {
            ImageView popularMovieImageView = holder.popularMovieImageView;
            String imagePath = APIHelper.get().getImagePath(APIHelper.IMAGE_SIZE.W342, movieModel.getPosterPath());
            APIHelper.get().loadImage(popularMovieImageView, imagePath);
            final String transitionNameId = String.format(popularMovieImageView.getContext().getString(R.string.transitionNameId), position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popularMovieImageView.setTransitionName(transitionNameId);
            }
            if (listener != null) {
                popularMovieImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onMovieClicked(v, movieModel, transitionNameId);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return movieModelList.size();
    }

    public void add(List<MovieModel> movieModelList) {
        if (!movieModelList.isEmpty()) {
            this.movieModelList.addAll(movieModelList);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        this.movieModelList.clear();
        notifyDataSetChanged();
    }

    public List<MovieModel> getMovies() {
        return movieModelList;
    }

    public void setListener(PopularMoviesListener listener) {
        this.listener = listener;
    }

    // View holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView popularMovieImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            popularMovieImageView = (ImageView) itemView.findViewById(R.id.popularMovieImageView);
        }
    }
}
