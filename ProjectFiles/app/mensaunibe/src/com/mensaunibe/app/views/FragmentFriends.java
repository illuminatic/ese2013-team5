package com.mensaunibe.app.views;

import java.util.ArrayList;
import java.util.HashMap;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh.OnRefreshListener;

import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * Fragment that appears in the "content_frame", shows friends
 */
public class FragmentFriends extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentFriends.class.getSimpleName();
	
	private Controller mController;
	private DataHandler mDataHandler;
	@SuppressWarnings("unused")
	private MensaList mMensaList;
	
	private SimpleAdapter mAdapter;
	private ProgressBar mProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = Controller.getController();
		this.mDataHandler = Controller.getDataHandler();
		this.mMensaList = mDataHandler.getMensaList();
		
		View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
		
		// set up the progress bar
        mProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
        // add nice color to the progress bar
        mProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);

		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final CustomListViewPullToRefresh listview = (CustomListViewPullToRefresh) rootView.findViewById(R.id.friendslist);
		
		// disable scrolling when list is refreshing
		listview.setLockScrollWhileRefreshing(false);

		// override the default strings
		listview.setTextPullToRefresh("Ziehen f�r Update");
		listview.setTextReleaseToRefresh("Loslassen f�r Update");
		listview.setTextRefreshing("Lade Daten...");

		// set the onRefreshListener for the pull down listview
		listview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// code to refresh the list contents goes here

				// async webrequest
				//adapter.loadData();
				
				// call listView.onRefreshComplete() when the loading is done.

				// For demo purposes, the code will pause here to
				// force a delay when invoking the refresh
				listview.postDelayed(new Runnable() {
					@Override
					public void run() {
						
						listview.onRefreshComplete("Freunde neu geladen");
					}
				}, 2000);
			}
		});

		// Fetch the string array from resouce arrays.xml > mensalist
		// String[] friendslist =
		// getResources().getStringArray(R.array.friendlist);
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		final String[][] friends = {
				{ "Friend Name", "Some information, maybe location?" },
				{ "Friend Name Blub", "Some information, maybe location?" },
				{ "Friend Name Bli", "Some information, maybe location?" },
				{ "Friend Name Ga", "Some information, maybe location?" },
				{ "Friend Name Da", "Some information, maybe location?" } };

		// Creating an array adapter to store the list of countries
		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(inflater.getContext(),
		// R.layout.list_item_1line, friendslist);
		HashMap<String, String> item;
		for (int i = 0; i < friends.length; i++) {
			item = new HashMap<String, String>();
			item.put("line1", friends[i][0]);
			item.put("line2", friends[i][1]);
			list.add(item);
		}

		mAdapter = new SimpleAdapter(mController, list,
				R.layout.list_friendlist_item, new String[] { "line1",
						"line2" }, new int[] { R.id.line1, R.id.line2 });

		// setting the adapter for the ListView
		listview.setAdapter(mAdapter);

		Toast toast = Toast.makeText(mController,
				"Hier werden alle Freunde angezeigt", Toast.LENGTH_LONG);
		toast.show();
		return rootView;
	}
}
