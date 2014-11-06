package com.example.googlemapproto;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainPasswordChecking extends Activity{

	TextView textViewWaitMeesage;
	Button buttonStart;
	int count = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_password_checking);

		textViewWaitMeesage = (TextView)findViewById(R.id.textViewWaitMeesage);
		buttonStart			= (Button)findViewById(R.id.buttonStart);
	}

	public void startNow(View v) 
	{

		new CountDownTimer(10000, 1000) {

			public void onTick(long millisUntilFinished) 
			{
				buttonStart.setEnabled(false);
				textViewWaitMeesage.setVisibility(View.VISIBLE);
				textViewWaitMeesage.setText("Please wait for " + count);
				count++;
			}

			public void onFinish() 
			{
				buttonStart.setEnabled(true);
				textViewWaitMeesage.setText("");
				textViewWaitMeesage.setVisibility(View.INVISIBLE);
				count = 1;
			}
		}.start();
	}
}
