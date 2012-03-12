package com.pj.app.youragewidget.ui;


import com.pj.app.youragewidget.R;
import com.pj.app.youragewidget.R.drawable;
import com.pj.app.youragewidget.R.id;
import com.pj.app.youragewidget.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class InfoActivity extends Activity implements View.OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.main_info);
		
		Button ok = (Button) findViewById(R.id.ok);
		ok.setOnClickListener(this);
	}

	
	
	@Override
	protected void onPause() {
		View mainLayout = findViewById(R.id.main_layout);
		mainLayout.setVisibility(View.GONE);
		super.onPause();
	}



	@Override
	protected void onResume() {
		View mainLayout = findViewById(R.id.main_layout);
		mainLayout.setVisibility(View.VISIBLE);
		super.onResume();
	}



	@Override
	public void onClick(View v) {
		moveTaskToBack(true);
	}
}
