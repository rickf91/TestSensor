package data.receiver;

import android.app.Activity;

/**
 * Created by richard on 16/9/20.
 */
public class DataReceiveWiFI extends DataReceiveBase {

    DataReceiveWiFI(Activity activity) {
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
        return true;
    }

    @Override
    protected boolean endTask() {
        return true;
    }

    //private void openWiFi
}
