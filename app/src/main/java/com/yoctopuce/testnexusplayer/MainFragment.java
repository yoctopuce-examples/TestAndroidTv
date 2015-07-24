/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.yoctopuce.testnexusplayer;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BrowseFragment
{
    private static final String TAG = "MainFragment";

    RelayPresenter _relayPresenter;
    private RelayUpdateReceiver _updateReceiver;
    private ArrayObjectAdapter _listRowAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setupEventListeners();

        _updateReceiver = new RelayUpdateReceiver()
        {

            @Override
            public void onRelayUpdate(String serial, String functionId, String name, boolean ison)
            {
                //mRowsAdapter.notifyArrayItemRangeChanged();
                Relay relay = new Relay(serial,functionId, name, ison);
                _listRowAdapter.add(relay);

            }
        };

        YoctoIntentService.requestRelays(getActivity());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().registerReceiver(_updateReceiver, new IntentFilter(RelayUpdateReceiver.ACTION_RELAY_STATE));
    }

    @Override
    public void onPause()
    {
        getActivity().unregisterReceiver(_updateReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private void loadRows()
    {
        //List<Movie> list = MovieList.setupMovies();
        List<Relay> list = new ArrayList<>();

        ArrayObjectAdapter mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        _relayPresenter = new RelayPresenter();

        _listRowAdapter = new ArrayObjectAdapter(_relayPresenter);
        for (Relay r : list) {
            _listRowAdapter.add(r);
        }
        mRowsAdapter.add(new ListRow(_listRowAdapter));

        setAdapter(mRowsAdapter);

    }

    private void prepareBackgroundManager()
    {

        BackgroundManager backgroundManager = BackgroundManager.getInstance(getActivity());
        backgroundManager.attach(getActivity().getWindow());
        DisplayMetrics mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements()
    {
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_DISABLED);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        // set search icon color
    }

    private void setupEventListeners()
    {
        setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener
    {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row)
        {

            if (item instanceof Relay) {
                Relay relay = (Relay) item;
                relay.setIson(!relay.ison());
                YoctoIntentService.setRelayState(getActivity(), relay.getHwid(), relay.ison());
                int i = _listRowAdapter.indexOf(relay);
                _listRowAdapter.notifyArrayItemRangeChanged(i,1);
            }
        }
    }
}




