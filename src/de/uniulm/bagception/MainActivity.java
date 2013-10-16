package de.uniulm.bagception;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final int REQUEST_ENABLE_BT = 1;
	public BluetoothAdapter bluetoothAdapter;
	BluetoothManager bluetoothManager;
	BluetoothDevice device;
	ListView listview;
	ArrayAdapter arrayAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listview = (ListView) findViewById(R.id.foundDevices);
		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
		listview.setAdapter(arrayAdapter);
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
		    // Device does not support Bluetooth
		}
		
		if (!bluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        arrayAdapter.add(device.getName() + "\n" + device.getAddress());
		    }
		}
		
		listview.setClickable(true);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		  @Override
		  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

		    Object o = listview.getItemAtPosition(position);
		    ConnectDevices connectDevices = new ConnectDevices(device);
		    connectDevices.start();
		    //Toast.makeText(getBaseContext(), (CharSequence) o, Toast.LENGTH_LONG).show();
		  }
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
		arrayAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mReceiver);
	}
	
	public void onScanClick(View view){
		//listview.setAdapter(null);
		bluetoothAdapter.startDiscovery();
	}	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	
	final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Add the name and address to an array adapter to show in a ListView
	            arrayAdapter.add(device.getName() + "\n" + device.getAddress());
	        }
	    }
	};
}
