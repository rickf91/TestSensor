package data.receiver;

/**
 * Created by richard on 16/9/20.
 */
public interface IEnvDataReceiver {
    public boolean beginReceiveData();
    public boolean endReceiveData();
    public boolean getData(EnvData sensorData);
}



