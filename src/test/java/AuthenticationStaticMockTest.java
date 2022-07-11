import authenticationStatic.Authentication;
import authenticationStatic.CredentialsStaticService;
import authenticationStatic.PermissionStaticService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author freddyar
 */
public class AuthenticationStaticMockTest {

    Authentication authentication;
    MockedStatic<CredentialsStaticService> credentialsMocked;
    MockedStatic<PermissionStaticService> permissionsMocked;

    @BeforeEach
    public void setup() {
        authentication = new Authentication();
        credentialsMocked = Mockito.mockStatic(CredentialsStaticService.class);
        permissionsMocked = Mockito.mockStatic(PermissionStaticService.class);

        credentialsMocked.when(() -> CredentialsStaticService.isValidCredential("admin", "admin")).thenReturn(true);
        credentialsMocked.when(() -> CredentialsStaticService.isValidCredential("creator", "creator")).thenReturn(true);
        credentialsMocked.when(() -> CredentialsStaticService.isValidCredential("reader", "reader")).thenReturn(true);
        credentialsMocked.when(() -> CredentialsStaticService.isValidCredential("updater", "updater")).thenReturn(true);
        credentialsMocked.when(() -> CredentialsStaticService.isValidCredential("deleter", "deleter")).thenReturn(true);
        credentialsMocked.when(() -> CredentialsStaticService.isValidCredential("publisher", "publisher")).thenReturn(true);
        credentialsMocked.when(() -> CredentialsStaticService.isValidCredential("moderator", "moderator")).thenReturn(true);
        credentialsMocked.when(() -> CredentialsStaticService.isValidCredential("invalid", "invalid")).thenReturn(false);

        permissionsMocked.when(() -> PermissionStaticService.getPermission("admin")).thenReturn("C,R,U,D");
        permissionsMocked.when(() -> PermissionStaticService.getPermission("creator")).thenReturn("C");
        permissionsMocked.when(() -> PermissionStaticService.getPermission("reader")).thenReturn("R");
        permissionsMocked.when(() -> PermissionStaticService.getPermission("updater")).thenReturn("U");
        permissionsMocked.when(() -> PermissionStaticService.getPermission("deleter")).thenReturn("D");
        permissionsMocked.when(() -> PermissionStaticService.getPermission("publisher")).thenReturn("C,R,U");
        permissionsMocked.when(() -> PermissionStaticService.getPermission("moderator")).thenReturn("U,D");
    }

    @AfterEach
    public void cleanUp() {
        credentialsMocked.close();
        permissionsMocked.close();
    }

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
