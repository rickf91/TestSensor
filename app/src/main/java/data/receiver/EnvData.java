package data.receiver;

/**
 * Created by richard on 16/9/20.
 */
public class EnvData {
    //Gyo Data
    private double mGyoX;
    private double mGyoY;
    private double mGyoZ;

    public void getGyoData(Double GyoX, Double GyoY, Double GyoZ) {
        GyoX = mGyoX;
        GyoY = mGyoY;
        GyoZ = mGyoZ;
    }

    public void setGyoData(double gyoX, double gyoY, double gyoZ) {
        mGyoX = gyoX;
        mGyoY = gyoY;
        mGyoZ = gyoZ;
    }

    //Gensor
    private double mGX;
    private double mGY;
    private double mGZ;

    public void getGSensorData(Double GX, Double GY, Double GZ) {
        GX = mGX;
        GY = mGY;
        GZ = mGZ;
    }
    public void setGSensorData(double gX, double gY, double gZ) {
        mGX = gX;
        mGY = gY;
        mGZ = gZ;
    }

    //Pressure
    private double mPressData;

    public double getPressData() {
        return mPressData;
    }

    public void setPressData(double PressData) {
        mPressData = PressData;
    }

    //Magnet
    private double mMagn;

    public double getMagnData() {
        return mMagn;
    }

    public void setMagnData(double PressData) {
        mMagn = PressData;
    }



    //Temperature
    private double mTemp;

    public double getTempData() {
        return mTemp;
    }

    public void setTempData(double TempData) {
        mTemp = TempData;
    }

    //Lighting
    private double mLight;

    public double getLightingData() {
        return mLight;
    }

    public void setLightData(double LightData) {
        mLight = LightData;
    }

    //EnvNoise
    private double mEnvNos;

    public double getEnvNos() {
        return mEnvNos;
    }

    public void setmEnvNos(double EnvNos) {
        mEnvNos = EnvNos;
    }

    //Humid
    private double mHumid;

    public double getHumidData() {
        return mHumid;
    }

    public void setHumidData(double HumidData) {
        mHumid = HumidData;
    }
}
