import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "test";
        String encodedPassword = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("Encoded: " + encodedPassword);
        
        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("Matches: " + matches);
    }
}