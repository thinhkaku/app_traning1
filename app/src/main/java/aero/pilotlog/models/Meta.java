package aero.pilotlog.models;

/**
 * Created by phuc.dd on 05/03/2018.
 */

public class Meta<T> {
    private T properties;
    public Meta(T properties) {
        this.properties = properties;
    }

}

/*public class Meta
{
    public int data { get; set; }
    public int beta { get; set; }
}

public class RootObject
{
    public Meta meta { get; set; }
}*/

