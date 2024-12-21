import me.dariansandru.domain.Admin;
import me.dariansandru.domain.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UserTest {
    @Test
    public void testPlayerFields(){
        System.out.println("Testing Player Fields:");
        Player player = new Player();

        player.setUsername("TestUsername");
        player.setPassword("TestPassword");
        player.setEmail("TestEmail");
        assert(player.getUsername().equals("TestUsername"));
        assert(player.getPassword().equals("TestPassword"));
        assert(player.getEmail().equals("TestEmail"));
        System.out.println("Test 1 passed! Fields are set correctly.");

        player.setWins(3);
        player.setLosses(1);
        player.setWinLossRatio();
        assert(player.getWins() == 3);
        assert(player.getLosses() == 1);
        assert((int)player.getWinLossRatio() == 75);
        System.out.println("Test 2 passed! Win/Loss ratio works correctly for non-zero values.");

        player.setWins(0);
        player.setLosses(0);
        player.setWinLossRatio();
        assert((int)player.getWinLossRatio() == 0);
        System.out.println("Test 3 passed! Win/Loss ratio works correctly for zero values.");
    }

    @Test
    public void testAdminFields(){
        System.out.println("Testing Admin Fields:");
        Admin admin = new Admin();

        admin.setUsername("TestUsername");
        admin.setPassword("TestPassword");
        admin.setEmail("TestEmail");
        assert(admin.getUsername().equals("TestUsername"));
        assert(admin.getPassword().equals("TestPassword"));
        assert(admin.getEmail().equals("TestEmail"));
        System.out.println("Test 1 passed! Fields are set correctly.");

        List<String> permissions = new ArrayList<>();
        permissions.add("Ban Permission");
        permissions.add("Modify Rating Permission");
        permissions.add("Report Hacker Permission");
        admin.setPermissions(permissions);
        assert(admin.getPermissions().get(0).equals("Ban Permission"));
        assert(admin.getPermissions().get(1).equals("Modify Rating Permission"));
        assert(admin.getPermissions().get(2).equals("Report Hacker Permission"));
        System.out.println("Test 2 passed! Permissions are set correctly.");
    }
}
