package inspector.ded.ajman.ajmaninspector;

import java.math.BigInteger;
import java.util.UUID;

public class Utility {

    public static UUID getUUID (){
        String s = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
        String s2 = s.replace("-", "");
        return new UUID(
                new BigInteger(s2.substring(0, 16), 16).longValue(),
                new BigInteger(s2.substring(16), 16).longValue());
    }

}
