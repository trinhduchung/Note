package com.sd.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferencesManager
{

    private Context _context;
    private SharedPreferences _sharedPreferences;
    private Editor _editor;

    public SharedPreferencesManager(Context context)
    {
        _context = context;
        _sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(_context);
    }

    public SharedPreferences getPrefs()
    {
        return _sharedPreferences;
    }
}