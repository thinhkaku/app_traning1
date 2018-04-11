package aero.pilotlog.interfaces;

/**
 * Created by tuan.pv on 2015/08/28.
 */
public interface IAsyncTaskCallback {
    void start();

    void doWork();

    void updateUI();

    void end();
}
