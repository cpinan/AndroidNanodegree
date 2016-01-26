package barqsoft.footballscores.receivers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;

/**
 * @author Carlos Piñan
 * @source http://developer.android.com/guide/practices/ui_guidelines/widget_design.html#anatomy_determining_size
 * @source http://www.androidauthority.com/create-simple-android-widget-608975/
 * @source http://developer.android.com/guide/topics/appwidgets/index.html
 * @source http://stackoverflow.com/questions/2471875/processing-more-than-one-button-click-at-android-widget
 */
public class ScoreWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context, ScoreWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_score);
            String defaultText = context.getString(R.string.football_scores_informative_widget);
            remoteViews.setTextViewText(R.id.updateTextView, defaultText + ": " + String.valueOf(System.currentTimeMillis()));

            Intent intent = new Intent(context, ScoreWidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.updateTextView, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

}
