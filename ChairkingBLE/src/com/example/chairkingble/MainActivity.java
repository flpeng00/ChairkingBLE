package com.example.chairkingble;

import java.util.StringTokenizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends BlunoLibrary {
	
	private Button btnScan;
	private TextView txtFSR[] = new TextView[4];
	private int[] intFSR = new int[16];
	private boolean isPacking = false;
	private String buffer = null;
	private int i = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		onCreateProcess();
		serialBegin(115200);
		txtFSR[0] = (TextView)findViewById(R.id.textView1);
		txtFSR[1] = (TextView)findViewById(R.id.textView2);
		txtFSR[2] = (TextView)findViewById(R.id.textView3);
		txtFSR[3] = (TextView)findViewById(R.id.textView4);
		btnScan = (Button)findViewById(R.id.btn_scan);
		btnScan.setOnClickListener(	new OnClickListener(){
			@Override
			public void onClick(View v) {
			
			buttonScanOnClickProcess();	
			}
		});
		
	}
	
	
	@Override
	protected void onResume() {
		onResumeProcess();
		super.onResume();
	}

	@Override
	protected void onPause() {
		onPauseProcess();
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		onStopProcess();
		super.onStop();
	}


	@Override
	protected void onDestroy() {
		onDestroyProcess();		
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onConectionStateChange(
			connectionStateEnum connectionState) {
		switch (connectionState) {											//Four connection state
		case isConnected:
			btnScan.setText("Connected");
			break;
		case isConnecting:
			btnScan.setText("Connecting");
			break;
		case isToScan:
			btnScan.setText("Scan");
			break;
		case isScanning:
			btnScan.setText("Scanning");
			break;
		case isDisconnecting:
			btnScan.setText("isDisconnecting");
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onSerialReceived(String theString) {
		Log.e("MainActivity", theString);
		StringTokenizer st = new StringTokenizer(theString, "/");
		String cmd = null;
		int temp = -1;
		
		while(st.hasMoreTokens()){
			cmd = st.nextToken();
			if(cmd.equals("S")){
				isPacking = true;
				buffer = "";
			}else if(cmd.equals("E")){
				StringTokenizer st2 = new StringTokenizer(buffer, ":");
				Log.w("MainActivity", buffer);
				i = 0;
				while(st2.hasMoreTokens()){
					cmd = st2.nextToken();
					intFSR[i] = Integer.parseInt(cmd);
					i++;
				}
				if(i==16) refreshTexts();
				isPacking = false;
			} else{
				if(isPacking)
					buffer += cmd;
			}
		}
	}
	
	public void refreshTexts(){
		for(int i=0;i<4;i++){
			txtFSR[i].setText(intFSR[i*4] + "\t\t\t\t\t\t\t" + intFSR[i*4+1] 
					+ "\t\t\t\t\t\t\t" + intFSR[i*4+2] + "\t\t\t\t\t\t\t" + intFSR[i*4+3]);
		}
	}
}
