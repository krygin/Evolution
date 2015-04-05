package ru.tech_mail.evolution;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tech_mail.evolution.content_provider.EvolutionDatabaseContract;

/**
 * Created by Ivan on 05.04.2015.
 */
public class TabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_CURRENT_ITEM_POSITION = "ARG_CURRENT_ITEM_POSITION";
    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;
    private int mPosition;


    public TabFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        mViewPager = (ViewPager)rootView.findViewById(R.id.view_pager);
        mTabsAdapter = new TabsAdapter(getFragmentManager(), null);
        mViewPager.setAdapter(mTabsAdapter);
        mPosition = getArguments().getInt(ARG_CURRENT_ITEM_POSITION);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EvolutionDatabaseContract.Technologies.CONTENT_URI, EvolutionDatabaseContract.Technologies.ID_COLUMN_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTabsAdapter.swapCursor(data);
        mTabsAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mPosition, false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTabsAdapter.swapCursor(null);
    }

    public static TabFragment newInstance(int id) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_CURRENT_ITEM_POSITION, id);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
}
