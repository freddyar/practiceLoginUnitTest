import authentication.Authentication;
import authentication.CredentialsService;
import authentication.PermissionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * @author freddyar
 */
public class AuthenticationMockTest {

    Authentication authentication;
    CredentialsService credentialsServiceMock = Mockito.mock(CredentialsService.class);
    PermissionService permissionServiceMock = Mockito.mock(PermissionService.class);

    @BeforeEach
    public void setup() {
        authentication = new Authentication();

        Mockito.when(credentialsServiceMock.isValidCredential("admin", "admin")).thenReturn(true);
        Mockito.when(credentialsServiceMock.isValidCredential("admin", "pass")).thenReturn(false);

        Mockito.when(permissionServiceMock.getPermission("admin")).thenReturn("C,R,U,D");

        authentication.setCredentialsService(credentialsServiceMock);
        authentication.setPermissionService(permissionServiceMock);
    }

    @AfterEach
    public void cleanUp() {}

    @Test
    public void verifyValidUser() {
        String expectedResult = "user authenticated successfully with permission: [C,R,U,D]";
        String obtainedResult = authentication.login("admin", "admin");
        Assertions.assertEquals(expectedResult, obtainedResult, "ERROR LOGIN!");
    }

    @Test
    public void verifyInvalidUser() {
        String expectedResult = "user or password incorrect";
        String obtainedResult = authentication.login("admin", "admin1");
        Assertions.assertEquals(expectedResult, obtainedResult, "ERROR LOGIN!");
    }
}
