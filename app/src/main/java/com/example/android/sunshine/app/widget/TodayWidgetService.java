package com.example.android.sunshine.app.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;

import java.util.Locale;

public class TodayWidgetService extends IntentService {
    private static final String TAG = "TodayWidgetService";

    public TodayWidgetService() {
        super("TodayWidgetService");
    }

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP
    };

    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DESC = 1;
    public static final int COL_WEATHER_MAX_TEMP = 2;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent() called with: " + "intent = [" + intent + "]");
        String location = Utility.getPreferredLocation(this);
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                location, System.currentTimeMillis());
        Cursor cursor = getContentResolver().query(weatherForLocationUri, FORECAST_COLUMNS, null,
                null, WeatherContract.WeatherEntry.COLUMN_DATE + " ASC");

        if (cursor == null) {
            return;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }

        int weatherId = cursor.getInt(COL_WEATHER_ID);
        double high = cursor.getDouble(COL_WEATHER_MAX_TEMP);
        String description = cursor.getString(COL_WEATHER_DESC);

        System.out.println(weatherId);
        System.out.println(high);
        System.out.println(description);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, TodayAppWidgetProvider.class));

        for (int id : appWidgetIds) {
            RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_today_small);
            views.setImageViewResource(R.id.image, Utility.getArtResourceForWeatherCondition(weatherId));
            views.setTextViewText(R.id.temp, Utility.formatTemperature(this, high));

            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(id, views);
        }

        cursor.close();
    }
}
