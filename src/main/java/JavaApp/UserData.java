package JavaApp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private String name;
    private String surname;
    private String occupation;
    private String position;
    private String phone;
    private String email;
}
