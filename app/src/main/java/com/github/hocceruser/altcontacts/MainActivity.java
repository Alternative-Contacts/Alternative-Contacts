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

	private ListView listview5;

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

	private void  initialize() {
		Button button6 = (Button) findViewById(R.id.button6);
		listview5 = (ListView) findViewById(R.id.listview5);

		contactsList = getSharedPreferences("contactsList", Activity.MODE_PRIVATE);



		button6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {

				Intent contactIntent = new Intent();
				contactIntent.setClass(getApplicationContext(), ContactActivity.class);
				newContact.clear();
				newContact.put("name", "");
				newContact.put("number", "");
				contactsMapsList.add(newContact);
				_save();
				contactIntent.putExtra("index", String.valueOf((long)(contactsMapsList.size() - 1)));
				startActivity(contactIntent);
			}
		});
		listview5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView _parent, View _view, final int _position, long _id) {

				Intent contactIntent = new Intent();
				contactIntent.setClass(getApplicationContext(), ContactActivity.class);
				contactIntent.putExtra("index", String.valueOf((long)(contactsMapsList.size() - 1)));
				startActivity(contactIntent);
			}
		});

	}

	private void  initializeLogic() {
		if (!contactsList.getString("filled", "").equals("1")) {
			contactsList.edit().putString("MapList", new Gson().toJson(contactsMapsList)).apply();
			contactsList.edit().putString("filled", "1").apply();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
				contactsMapsList.clear();
				contactsMapsList = new Gson().fromJson(contactsList.getString("MapList", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				contactsNamesList.clear();
		double i = 0;
				for(int _repeat15 = 0; _repeat15 < (contactsMapsList.size()); _repeat15++) {
					if (i < contactsMapsList.size()) {
						contactsNamesList.add(contactsMapsList.get((int) i).get("name").toString());
					}
					i++;
				}
				listview5.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, contactsNamesList));
	}

	private void _save () {
		contactsList.edit().putString("MapList", new Gson().toJson(contactsMapsList)).apply();
	}

}
