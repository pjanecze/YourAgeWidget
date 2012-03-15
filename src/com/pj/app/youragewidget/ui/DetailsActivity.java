package com.pj.app.youragewidget.ui;

import com.pj.app.youragewidget.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class DetailsActivity extends FragmentActivity implements OnItemSelectedListener, OnClickListener{

	Spinner spFragments;
	
	Button btnOk;
	
	int selectedFragment = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_details);
		
		btnOk = (Button) findViewById(R.id.ok);
		btnOk.setOnClickListener(this);
		
		spFragments = (Spinner) findViewById(R.id.sp_fragments);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, 
				getResources().getStringArray(R.array.fragments));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spFragments.setAdapter(adapter);
		spFragments.setOnItemSelectedListener(this);
		
		changeFragment();
	}


	private void changeFragment() {
		Fragment fragment;
		if(selectedFragment == 0) {
			fragment = new DetailsInfoFragment();
		} else {
			fragment = new DetailsSettingsFragment();
		}
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_content, fragment);
		transaction.commit();
	}
	
	@Override
	public void onClick(View v) {
		finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {
		if(pos != selectedFragment) {
			selectedFragment = pos;
			
			changeFragment();
			
		}
	}

	


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//nothing
	}


	public void makeBackgroundTransparent(boolean isTransparent) {
		View view = findViewById(R.id.main_layout);
		if(isTransparent) {
			view.setBackgroundColor(getResources().getColor(R.color.dialog_background_transparent));
		} else {
			view.setBackgroundColor(getResources().getColor(R.color.dialog_background));
		}
	}
	
}
