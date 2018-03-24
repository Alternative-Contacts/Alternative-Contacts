package com.github.hocceruser.altcontacts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactActivity extends Activity {

	private EditText edittextname;
	private EditText edittextnumber;

	private double index = 0;

	private ArrayList<HashMap<String, Object>> contactsMapsList = new ArrayList<>();

	private SharedPreferences contactsList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		initialize();
		initializeLogic();
	}

	private void  initialize() {
		Button buttoncall = (Button) findViewById(R.id.buttoncall);
		edittextname = (EditText) findViewById(R.id.edittextname);
		edittextnumber = (EditText) findViewById(R.id.edittextnumber);
		Button buttondel = (Button) findViewById(R.id.buttondel);
		Button buttonOK = (Button) findViewById(R.id.buttonOK);

		contactsList = getSharedPreferences("contactsList", Activity.MODE_PRIVATE);


		buttonOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) { 
				finish();
			}
		});
		buttondel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) { 
				edittextname.setText("");
				edittextnumber.setText("");
				finish();
			}
		});
		buttoncall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				Intent intentCall = new Intent();
				intentCall.setAction(Intent.ACTION_DIAL);
				intentCall.setData(Uri.parse("tel:" + edittextnumber.getText().toString()));
				startActivity(intentCall);
			}
		});

	}

	private void  initializeLogic() {
		index = Double.parseDouble(getIntent().getStringExtra("index"));
		contactsMapsList = new Gson().fromJson(contactsList.getString("MapList", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		edittextname.setText(contactsMapsList.get((int)index).get("name").toString());
		edittextnumber.setText(contactsMapsList.get((int)index).get("number").toString());
	}

	@Override
	public void onPause() {
		super.onPause();
				if ((edittextnumber.getText().toString().length() > 0) || (edittextname.getText().toString().length() > 0)) {
					contactsMapsList.get((int)index).put("name", edittextname.getText().toString());
					contactsMapsList.get((int)index).put("number", edittextnumber.getText().toString());
				/*	//TODO remove -- debugging
					if (edittextname.getText().toString().equals("test123")){
						throw new RuntimeException("test123");
					}*/
				}
				else {
					_delete();
				}
				contactsList.edit().putString("MapList", new Gson().toJson(contactsMapsList)).apply();
	}

	private void _delete () {
		contactsMapsList.remove((int)(index));
		edittextname.setText("");
		edittextnumber.setText("");
	}

}
