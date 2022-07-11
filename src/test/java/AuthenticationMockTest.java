import authentication.Authentication;
import authentication.CredentialsService;
import authentication.PermissionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
        Mockito.when(credentialsServiceMock.isValidCredential("creator", "creator")).thenReturn(true);
        Mockito.when(credentialsServiceMock.isValidCredential("reader", "reader")).thenReturn(true);
        Mockito.when(credentialsServiceMock.isValidCredential("updater", "updater")).thenReturn(true);
        Mockito.when(credentialsServiceMock.isValidCredential("deleter", "deleter")).thenReturn(true);
        Mockito.when(credentialsServiceMock.isValidCredential("publisher", "publisher")).thenReturn(true);
        Mockito.when(credentialsServiceMock.isValidCredential("moderator", "moderator")).thenReturn(true);
        Mockito.when(credentialsServiceMock.isValidCredential("invalid", "invalid")).thenReturn(false);

        Mockito.when(permissionServiceMock.getPermission("admin")).thenReturn("C,R,U,D");
        Mockito.when(permissionServiceMock.getPermission("creator")).thenReturn("C");
        Mockito.when(permissionServiceMock.getPermission("reader")).thenReturn("R");
        Mockito.when(permissionServiceMock.getPermission("updater")).thenReturn("U");
        Mockito.when(permissionServiceMock.getPermission("deleter")).thenReturn("D");
        Mockito.when(permissionServiceMock.getPermission("publisher")).thenReturn("C,R,U");
        Mockito.when(permissionServiceMock.getPermission("moderator")).thenReturn("U,D");

        authentication.setCredentialsService(credentialsServiceMock);
        authentication.setPermissionService(permissionServiceMock);
    }

    @AfterEach
    public void cleanUp() {}

    @ParameterizedTest
    @CsvSource({
        "admin,admin,'C,R,U,D'",
        "creator,creator,C",
        "reader,reader,R",
        "updater,updater,U",
        "deleter,deleter,D",
        "publisher,publisher,'C,R,U'",
        "moderator,moderator,'U,D'",
    })
    public void verifyValidUser(String userName, String password, String permissions) {
        String expectedResult = "user authenticated successfully with permission: [" + permissions + "]";
        String obtainedResult = authentication.login(userName, password);
        Assertions.assertEquals(expectedResult, obtainedResult, "ERROR valid user!");
    }

    @Test
    public void verifyInvalidUser() {
        String expectedResult = "user or password incorrect";
        String obtainedResult = authentication.login("invalid", "invalid");
        Assertions.assertEquals(expectedResult, obtainedResult, "ERROR Invalid user!");
    }
}
