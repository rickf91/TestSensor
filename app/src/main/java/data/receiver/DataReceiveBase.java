package data.receiver;

import android.app.Activity;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;


/**
 * Created by richard on 16/9/20.
 */
public abstract class DataReceiveBase extends Observable implements IEnvDataReceiver, Runnable {

    private Queue<EnvData> mEnvDataQue = null;
    private Thread mWorkdingThread     = null;
    private final int THREAD_WAIT_TIME_OUT = 2000;
    protected Activity mRefActivity    = null;

    DataReceiveBase(Activity activity) {
        mEnvDataQue  = new LinkedList<>();
        assert(null != mEnvDataQue);
        mEnvDataQue.clear();

        mWorkdingThread = new Thread(this);
        assert(null != mWorkdingThread);

        mRefActivity = activity;
    }
    @Override
    protected void finalize() throws Throwable {
        mEnvDataQue.clear();
    }

    @Override
    public void run() {
        taskReceiveData();
    }

    @Override
    public boolean beginReceiveData() {
        mWorkdingThread.start();
        return beginTask();
    }

    @Override
    public boolean endReceiveData() {
        boolean bRtn = true;
        try {
            mWorkdingThread.join(THREAD_WAIT_TIME_OUT);
        }catch(InterruptedException e) {
            e.printStackTrace();
            bRtn = false;
        }
        bRtn = endTask();
        return bRtn;
    }

    @Override
    public boolean getData(EnvData sensorData) {
        notifyObservers(sensorData);
    }



    abstract protected void taskReceiveData();
    abstract protected boolean beginTask();
    abstract protected boolean endTask();
}
