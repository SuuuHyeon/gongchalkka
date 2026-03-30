import io.jsonwebtoken.io.Decoders;
public class TestJJWT {
    public static void main(String[] args) {
        String key = "PXSpGsJilJtzw019jG4DoPLujs1RoRTwSWhf2lGoINE";
        try {
            byte[] decoded = Decoders.BASE64.decode(key);
            System.out.println("JJWT Decoded length: " + decoded.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
