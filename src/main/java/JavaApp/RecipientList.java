package JavaApp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientList {
    private String email;
    private  String first_name;
    private String last_name;
}
