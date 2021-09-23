package org.torproject.android.ui.hiddenservices.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.torproject.android.R;
import org.torproject.android.ui.hiddenservices.providers.CookieContentProvider;

public class ClientCookiesAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public ClientCookiesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final Context mContext = context;
        int id = cursor.getInt(cursor.getColumnIndex(CookieContentProvider.ClientCookie._ID));
        final String where = CookieContentProvider.ClientCookie._ID + "=" + id;

        TextView domain = (TextView) view.findViewById(R.id.cookie_onion);
        domain.setText(cursor.getString(cursor.getColumnIndex(CookieContentProvider.ClientCookie.DOMAIN)));

        Switch enabled = (Switch) view.findViewById(R.id.cookie_switch);
        /* ********OpenRefactory Warning********
		 Possible null pointer Dereference!
		 Path: 
			File: ClientCookiesAdapter.java, Line: 38
				Switch enabled=(Switch)view.findViewById(R.id.cookie_switch);
				Variable enabled is assigned from a library method call which may return null.
			File: ClientCookiesAdapter.java, Line: 39
				enabled.setChecked(cursor.getInt(cursor.getColumnIndex(CookieContentProvider.ClientCookie.ENABLED)) == 1);
				enabled is referenced in method invocation.
		*/
		enabled.setChecked(
                cursor.getInt(cursor.getColumnIndex(CookieContentProvider.ClientCookie.ENABLED)) == 1
        );

        enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentResolver resolver = mContext.getContentResolver();
                ContentValues fields = new ContentValues();
                fields.put(CookieContentProvider.ClientCookie.ENABLED, isChecked);
                resolver.update(
                        CookieContentProvider.CONTENT_URI, fields, where, null
                );

                Toast.makeText(
                        mContext, R.string.please_restart_Orbot_to_enable_the_changes, Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.layout_client_cookie_list_item, parent, false);
    }
}
