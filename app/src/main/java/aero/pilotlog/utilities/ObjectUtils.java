package aero.pilotlog.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by tuan.na on 9/16/2015.
 */
public class ObjectUtils {

    public static Object deepCopy(Object o) throws Exception{
        //Serialization of object
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(o);

        //De-serialization of object
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);

        //Verify that object is not corrupt

        //validateNameParts(fName);
        //validateNameParts(lName);

        return in.readObject();
    }
}
