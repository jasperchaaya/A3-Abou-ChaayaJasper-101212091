import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestsGameScenario3Test {

    private WebDriver driver;
    private QuestsGamePage questsGamePage;



    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Jasper\\Documents\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        questsGamePage = new QuestsGamePage(driver);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-application-cache");

        driver.get("http://127.0.0.1:8081/quests-frontend/");
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='75%';");

    }

    //Private helper for pauses
    private void pause(int t) throws InterruptedException {
        Thread.sleep(1000L * t);
    }

    @Test
    public void testScenario3() throws InterruptedException {

        //Click scenario 2 button
        System.out.println("***Clicking Scenario 3***");
        questsGamePage.clickScenario3();
        pause(2);
        //Verify the game started
        assertTrue(questsGamePage.getGameStatus().contains("Starting "), "Game did not start as expected.");
        pause(2);

        //Verify that Scenario 1 setup completes
        assertTrue(questsGamePage.getGameStatus().contains("Success"), "Scenario 3 setup did not complete as expected.");
        pause(2);
        //Play the game
        System.out.println("*Clicking Draw Button*");
        questsGamePage.clickDraw();
        pause(2);

        System.out.println("*Player 1 Clicking No Button*");
        questsGamePage.clickNo();

        System.out.println("*Player 2 Clicking Yes Button to set up quest*");
        questsGamePage.clickYes();
        pause(2);
        assertTrue(questsGamePage.getGameStatus().contains("Quest Accepted"), "Quest was not accepted as expected.");
        pause(2);

        //Aetup quest by player 2
        //Select multiple cards by their values
        System.out.println("*Selecting cards for Player 2 to setup stage*");
        questsGamePage.selectMultipleCards(Arrays.asList("F 5", "F 10", "F 15", "F 30"));
        pause(2);
        System.out.println("*Clicking set up quest*");
        questsGamePage.clickSetStage();
        pause(2);

        //do attacks
        //player 3 attacking
        System.out.println("*Selecting cards for Player 3 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList("S 10", "S 10", "S 20"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);

        //player 4 attacking
        System.out.println("*Selecting cards for Player 4 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList("S 10", "S 10", "S 20"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);

        //player 1 attacking
        System.out.println("*Selecting cards for Player 1 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList("H 10"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);


        //Verify the quest results
        pause(3);

        System.out.println("*Checking if quest ended*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest ended"), "Quest did not end.");

        //Asserting shields
        System.out.println("*Checking if player 1 has 0 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 1 Shields: 0"), "Player 1 did not have 0 shields");

        System.out.println("*Checking if player 2 has 0 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 2 Shields: 0"), "Player 2 did not have 0 shields");

        System.out.println("*Checking if player 3 has 0 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 3 Shields: 0"), "Player 3 did not have 0 shields");

        System.out.println("*Checking if player 4 has 4 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 4 Shields: 4"), "Player 4 did not have 4 shields");

        //Asserting winners
        System.out.println("*Checking if player 4 has won the quest*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest Winner: Player 4"), "Player 4 did not win quest.");

        //Asserting cards TODO MAKE DIV TO DISPLAY ALL PLAYER CARDS AND A METHOD THAT GETS THEM
        System.out.println("*Checking if player 1 has the correct cards*");
        assertTrue(questsGamePage.getGameStatus().contains("F5,F10,F15,F15,F15,H10,A15,A15,L20"),
                "Player 1 did not have the correct cards.");

        System.out.println("*Checking if player 2 has 12 adventure cards*");
        assertTrue(questsGamePage.getGameStatus().contains("todo check 12"),
                "Player 2 did not have 12 adventure cards.");

        System.out.println("*Checking if player 3 has the correct cards*");
        assertTrue(questsGamePage.getGameStatus().contains("F5,F5,F15,F30,S10"),
                "Player 3 did not have the correct cards.");

        System.out.println("*Checking if player 3 has the correct cards*");
        assertTrue(questsGamePage.getGameStatus().contains("F15,F15,F40"),
                "Player 3 did not have the correct cards.");

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


}
