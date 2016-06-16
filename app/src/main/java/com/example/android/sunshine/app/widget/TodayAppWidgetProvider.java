package com.example.android.sunshine.app.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.android.sunshine.app.sync.SunshineSyncAdapter;

public class TodayAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "TodayAppWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate() called with: " + "context = [" + context + "], appWidgetManager = [" + appWidgetManager + "], appWidgetIds = [" + appWidgetIds + "]");
        Log.d(TAG, "onUpdate: starting widget service");
        context.startService(new Intent(context, TodayWidgetService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.d(TAG, "onAppWidgetOptionsChanged() called with: " + "context = [" + context + "], appWidgetManager = [" + appWidgetManager + "], appWidgetId = [" + appWidgetId + "], newOptions = [" + newOptions + "]");
        context.startService(new Intent(context, TodayWidgetService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() called with: " + "context = [" + context + "], intent = [" + intent + "]");
        super.onReceive(context, intent);
        if (intent.getAction().equals(SunshineSyncAdapter.ACTION_DATA_UPDATED)) {
            context.startService(new Intent(context, TodayWidgetService.class));
        }
    }
}
