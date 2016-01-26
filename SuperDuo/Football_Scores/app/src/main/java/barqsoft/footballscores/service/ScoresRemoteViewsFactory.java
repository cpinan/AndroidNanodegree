package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.models.ScoreModel;
import barqsoft.footballscores.receivers.ScoresWidgetProvider;

/**
 * @author Carlos Piñan
 * @source http://developer.android.com/guide/topics/appwidgets/index.html
 * @source http://stackoverflow.com/questions/21379949/how-to-load-items-in-android-homescreen-listview-widget
 */
public class ScoresRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<ScoreModel> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;

    public ScoresRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        updateElements();
    }

    @Override
    public void onDataSetChanged() {
        updateElements();
    }

    private void updateElements() {
        MyFetchService myFetchService = new MyFetchService();
        myFetchService.getData();
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.BASE_CONTENT_URI,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            mWidgetItems.add(new ScoreModel(cursor));
        }
    }

    @Override
    public void onDestroy() {
        mWidgetItems.clear();
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        ScoreModel scoreModel = mWidgetItems.get(position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.scores_list_item);
        rv.setTextViewText(R.id.home_name, scoreModel.getHomeName());
        rv.setTextViewText(R.id.away_name, scoreModel.getAwayName());
        rv.setTextViewText(R.id.data_textview, scoreModel.getDate());
        rv.setTextViewText(R.id.score_textview, scoreModel.getScore());
        rv.setImageViewResource(R.id.home_crest, scoreModel.getHomeCrestImageResource());
        rv.setImageViewResource(R.id.away_crest, scoreModel.getAwayCrestImageResource());

        Bundle extras = new Bundle();
        extras.putInt(ScoresWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.container, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
