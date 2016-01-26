package barqsoft.footballscores.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * @author Carlos Piñan
 */
public class ScoresWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScoresRemoteViewsFactory(getApplicationContext(), intent);
    }
}
