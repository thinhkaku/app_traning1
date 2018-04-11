package aero.pilotlog.models;

/**
 * Created by phuc.dd on 05/03/2018.
 */

public class TableUpload<T> {
    public T meta;

    public TableUpload(T meta) {
        this.meta = meta;
    }
}
