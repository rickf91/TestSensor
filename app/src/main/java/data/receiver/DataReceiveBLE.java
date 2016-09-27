package data.receiver;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by richard on 16/9/21.
 */
public class DataReceiveBLE extends DataReceiveBase {

    // Initializes Bluetooth adapter.
    final BluetoothManager bluetoothManager =
            (BluetoothManager) mRefActivity.getSystemService(Context.BLUETOOTH_SERVICE);;
    BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
    final int REQUEST_ENABLE_BT = 1;

    DataReceiveBLE(Activity activity) {
        super(activity);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected void taskReceiveData() {

    }

    @Override
    protected boolean beginTask() {
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mRefActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        return true;

    }

    @Override
    protected boolean endTask() {
        return true;
    }
}
