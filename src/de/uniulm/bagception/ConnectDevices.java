package de.uniulm.bagception;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class ConnectDevices extends Thread {

	public static final String BT_UUID = "1bcc9340-2c29-11e3-8224-0800200c9a66";
	private final BluetoothSocket bluetoothSocket;
	private final BluetoothDevice bluetoothDevice;
	public BluetoothAdapter bluetoothAdapter;

	public ConnectDevices(BluetoothDevice device) {

		// tmp socket, because bluetoothSocket ist final
		BluetoothSocket tmpSocker = null;
		bluetoothDevice = device;
		bluetoothAdapter.getDefaultAdapter();
		try {
			// MY_UUID is the app's UUID string, also used by the server code
			tmpSocker = device.createRfcommSocketToServiceRecord(UUID.fromString(BT_UUID));
		} catch (IOException e) {
		}
		bluetoothSocket = tmpSocker;
	}
	
    public void run() {
        // Cancel discovery because it will slow down the connection
        //bluetoothAdapter.cancelDiscovery();
        
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            bluetoothSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
        manageConnectedSocket(bluetoothSocket);
    }
 
    private void manageConnectedSocket(BluetoothSocket bluetoothSocket2) {
		// TODO Auto-generated method stub
		
	}

	/** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
        	bluetoothSocket.close();
        } catch (IOException e) { }
    }

}
