package ru.tech_mail.evolution;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static ru.tech_mail.evolution.content_provider.EvolutionDatabaseContract.Technologies;

/**
 * Created by Ivan on 05.04.2015.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {
    private Cursor mCursor;

    public TabsAdapter(FragmentManager fm, Cursor cursor) {
        super(fm);
        mCursor = cursor;
    }

    @Override
    public Fragment getItem(int position) {
        if (mCursor!=null && mCursor.moveToPosition(position)) {
            long id = mCursor.getLong(mCursor.getColumnIndex(Technologies._ID));
            return TechnologyFragment.newInstance(id);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return mCursor!=null?mCursor.getCount():0;
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }


    public static class TechnologyFragment extends Fragment {
        private static final String ARG_TECHNOLOGY_ID = "ARG_TECHNOLOGY_ID";
        private long technologyId;


        public TechnologyFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            technologyId = getArguments().getLong(ARG_TECHNOLOGY_ID, 0);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.tab_item_technology, container, false);
            Uri technologyUri = ContentUris.withAppendedId(Technologies.CONTENT_URI, technologyId);
            String title = "";
            String info = "";
            String picture = "";
            Cursor cursor = getActivity().getContentResolver().query(technologyUri, Technologies.ALL_COLUMNS_PROJECTION, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                title = cursor.getString(cursor.getColumnIndex(Technologies.COLUMN_TITLE));
                info = cursor.getString(cursor.getColumnIndex(Technologies.COLUMN_INFO));
                picture = cursor.getString(cursor.getColumnIndex(Technologies.COLUMN_PICTURE));
            }

            ((TextView)rootView.findViewById(R.id.text_view_title)).setText(title);
            Picasso.with(getActivity()).load(picture).into((ImageView)rootView.findViewById(R.id.image_view_picture));
            ((TextView)rootView.findViewById(R.id.text_view_info)).setText(info);
            return rootView;
        }
        public static TechnologyFragment newInstance(long technologyId) {
            Bundle arguments = new Bundle();
            arguments.putLong(TechnologyFragment.ARG_TECHNOLOGY_ID, technologyId);
            TechnologyFragment fragment = new TechnologyFragment();
            fragment.setArguments(arguments);
            return fragment;
        }
    }
}
