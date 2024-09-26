package mate.academy.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import mate.academy.bookstore.validation.FieldMatch;

@Data
@FieldMatch(firstField = "password", repeatedField = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, max = 30)
    private String password;
    @NotBlank
    @Size(min = 6, max = 30)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min = 10, max = 150)
    private String shippingAddress;
}
