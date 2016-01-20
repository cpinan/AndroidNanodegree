package com.carlospinan.popularmovies.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.carlospinan.popularmovies.BuildConfig;
import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.providers.APIProvider;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.squareup.picasso.Picasso;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * @author Carlos Piñan
 */
public class APIHelper {

    public enum IMAGE_SIZE {
        W92,
        W154,
        W185,
        W342,
        W500,
        W780,
        ORIGINAL,
        RECOMMENDED
    }

    private static final boolean USE_PICASSO = false;
    public static final String ENDPOINT_URL = BuildConfig.BASE_URL;
    public static final String BASE_IMAGE_URL = BuildConfig.IMAGE_URL;
    public static final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    private static APIHelper instance;

    private APIProvider provider;

    private APIHelper() {
        /* IGNORED */
    }

    public static APIHelper get() {
        if (instance == null) {
            instance = new APIHelper();
        }
        return instance;
    }

    public APIProvider getRetrofitService() {
        if (provider == null) {
            OkHttpClient client = OkHttpSingleton.getOkHttpClient();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            provider = retrofit.create(APIProvider.class);
        }
        return provider;
    }

    public String getImagePath(String image) {
        return getImagePath(IMAGE_SIZE.RECOMMENDED, image);
    }

    public String getImagePath(IMAGE_SIZE size, String image) {
        String widthString;
        switch (size) {
            case W92:
            case W154:
            case W185:
            case W342:
            case W500:
            case W780:
            case ORIGINAL:
                widthString = size.toString().toLowerCase();
                break;
            default:
                widthString = "w185";
        }
        if (!image.startsWith("/")) {
            image = "/" + image;
        }
        return String.format(BASE_IMAGE_URL, widthString, image);
    }

    public void loadImage(ImageView imageView, String path) {
        final Context context = imageView.getContext();
        /*
        There is a bug using Picasso with RecyclerView. I'm working on this issue but with glide is working well.
         */
        if (USE_PICASSO) {
            Picasso.with(context).
                    load(path).
                    placeholder(R.color.colorPrimaryDark).
                    error(R.drawable.placeholder_error).
                    into(imageView);
        } else {
            Glide.with(context).
                    load(path).
                    placeholder(R.color.colorPrimaryDark).
                    error(R.drawable.placeholder_error).
                    into(imageView);
        }
    }

    public String getVideoPath(String site, String key) {
        String url = site;
        if (site.equalsIgnoreCase("youtube")) {
            url = String.format("https://www.youtube.com/watch?v=%s", key);
        }
        return url;
    }

    public void loadVideo(Context context, String site, String key) {
        if (site != null && key != null) {
            String url = getVideoPath(site, key);
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                intent.setData(Uri.parse(url));
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose)));
            }
        }
    }

}
