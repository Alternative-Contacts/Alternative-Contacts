/*******************************************************************************
 *
 *                              Alternative Contacts
 *                        Contact: hocceruser-github@gmx.de
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see http://www.gnu.org/licenses/ .
 *
 ******************************************************************************/


package com.github.hocceruser.altcontacts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

	private ListView listview;

	private HashMap<String, Object> newContact = new HashMap<>();

	private ArrayList<HashMap<String, Object>> contactsMapsList = new ArrayList<>();
	private ArrayList<String> contactsNamesList = new ArrayList<String>();

	private SharedPreferences contactsList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initialize();
		initializeLogic();
	}

	private void initialize() {
		Button buttonADD = (Button) findViewById(R.id.buttonADD);
		listview = (ListView) findViewById(R.id.listview);

		contactsList = getSharedPreferences("contactsList", Activity.MODE_PRIVATE);


		buttonADD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {

				Intent contactIntent = new Intent();
				contactIntent.setClass(getApplicationContext(), ContactActivity.class);
				newContact.clear();
				newContact.put("name", "");
				newContact.put("number", "");
				contactsMapsList.add(newContact);
				_save();
				contactIntent.putExtra("index", String.valueOf((contactsMapsList.size() - 1)));
				startActivity(contactIntent);
			}
		});
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView _parent, View _view, final int _position, long _id) {

				Intent contactIntent = new Intent();
				contactIntent.setClass(getApplicationContext(), ContactActivity.class);
				contactIntent.putExtra("index", String.valueOf(_position));
				startActivity(contactIntent);
			}
		});

	}

	private void initializeLogic() {
		if (!contactsList.getString("filled", "").equals("1")) {
			contactsList.edit().putString("MapList", new Gson().toJson(contactsMapsList)).apply();
			contactsList.edit().putString("filled", "1").apply();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		contactsMapsList.clear();
		contactsMapsList = new Gson().fromJson(contactsList.getString("MapList", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
		}.getType());
		contactsNamesList.clear();
		double i = 0;
		for (int _repeat15 = 0; _repeat15 < (contactsMapsList.size()); _repeat15++) {
			if (i < contactsMapsList.size()) {
				contactsNamesList.add(contactsMapsList.get((int) i).get("name").toString());
			}
			i++;
		}
		listview.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, contactsNamesList));
	}

	private void _save() {
		contactsList.edit().putString("MapList", new Gson().toJson(contactsMapsList)).apply();
	}

}
