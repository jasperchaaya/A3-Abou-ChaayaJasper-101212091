import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestsGameScenario2Test {

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
    public void testScenario2() throws InterruptedException {

        //Click scenario 2 button
        System.out.println("**Clicking Scenario 2**");
        questsGamePage.clickScenario2();
        pause(2);
        //Verify the game started
        assertTrue(questsGamePage.getGameStatus().contains("Starting "), "Game did not start as expected.");
        pause(2);

        //Verify that Scenario 1 setup completes
        assertTrue(questsGamePage.getGameStatus().contains("SUCCESS"), "Scenario 2 setup did not complete as expected.");
        pause(2);

        //Play the game
        System.out.println("*Clicking Draw Button*");
        questsGamePage.clickDraw();
        pause(2);

        System.out.println("*Player 1 Clicking Yes Button to set up quest*");
        questsGamePage.clickYes();
        pause(2);
        assertTrue(questsGamePage.getGameStatus().contains("Quest Accepted"), "Quest was not accepted as expected.");
        pause(2);

        //Setup quest by player 1
        //Select multiple cards by their values
        System.out.println("*Selecting cards for Player 1 to setup stage*");
        questsGamePage.selectMultipleCards(Arrays.asList("F 5", "F 10", "H 10", "A 15"));
                //"F 5", "F 5", "D 5", "F 10", "H 10", "F 10", "A 15"));
        pause(2);
        System.out.println("*Clicking set up quest*");
        questsGamePage.clickSetStage();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //do attacks
        //player 2 attacking
        pause(3);
        System.out.println("*Selecting cards for Player 2 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList("H 10", "S 10","H 10", "S 10", "S 10", "A 15"));
        pause(3);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //player 3 attacking
        System.out.println("*skipping attack for player 3*");
        pause(2);
        questsGamePage.clickSkipAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //player 4 attacking
        System.out.println("*Selecting cards for Player 4 to attack*");
        pause(10);
        questsGamePage.selectMultipleCards(Arrays.asList("H 10", "S 10","H 10", "S 10"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();


        //Verify the results for quest 1
        pause(3);

        //Asserting shields
        System.out.println("**checking if player 1 has 0 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 1 Shields: 0"), "Player 1 did not have 0 shields");

        System.out.println("**checking if player 2 has 4 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 2 Shields: 4"), "Player 2 did not have 4 shields");

        System.out.println("**checking if player 3 has 0 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 3 Shields: 0"), "Player 3 did not have 0 shields");

        System.out.println("**checking if player 4 has 4 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 4 Shields: 4"), "Player 4 did not have 4 shields");

        //Asserting winners
        System.out.println("**checking if player 4 has won the quest*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest Winner: Player 4"), "Player 4 did not win quest.");

        System.out.println("**checking if player 4 has won the quest*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest Winner: Player 2"), "Player 2 did not win quest.");
        //Asserting cards

        System.out.println("*Checking if player 1 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 1 Number of Cards (12): F 5,D 5,F 5,F 10,H 10,F 10,F 15,F 15,A 15,F 15,F 15,L 20"),"Player 1 did not have the correct cards.");

        System.out.println("*Checking if player 2 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 2 Number of Cards (12): D 5,D 5,S 10,F 10,F 10,L 20,L 20,E 30,F 30,F 40,F 40,F 50"),"Player 2 did not have the correct cards.");

        System.out.println("*Checking if player 3 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 3 Number of Cards (12): F 5,F 5,F 5,F 5,D 5,D 5,D 5,H 10,H 10,H 10,H 10,H 10"),"Player 3 did not have the correct cards.");

        System.out.println("*Checking if player 4 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 4 Number of Cards (12): F 5,H 10,S 10,A 15,F 15,F 15,L 20,L 20,F 20,E 30,F 30,F 50"),"Player 4 did not have the correct cards.");


        System.out.println("**Clicking Draw Button**");
        questsGamePage.clickDraw();
        pause(2);

        System.out.println("**Player 2 Clicking no to decline set up**");
        questsGamePage.clickNo();
        pause(2);

        System.out.println("**Player 3 Clicking yes to accept set up quest");
        questsGamePage.clickYes();
        pause(1);

        System.out.println("*Selecting cards for Player 3 to setup stage*");
        questsGamePage.selectMultipleCards(Arrays.asList("F 5", "F 5", "F 5"));
        pause(2);
        System.out.println("*Clicking set up quest*");
        questsGamePage.clickSetStage();
        pause(2);
        questsGamePage.clickDisplayAllHands();


        //player 4 attacking
        System.out.println("*Selecting cards for Player 4 to attack*");
        pause(10);  //trim is getting rid of E 30
        questsGamePage.selectMultipleCards(Arrays.asList("H 10", "S 10","H 10", "E 30"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //player 1 declining to participate
        System.out.println("**Player 1 not participating**");
        questsGamePage.clickSkipAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //player 2 attacking
        System.out.println("*Selecting cards for Player 2 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList("D 5", "S 10","H 10", "A 15", "E 30"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //Asserting shields
        System.out.println("**checking if player 1 has 0 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 1 Shields: 0"), "Player 1 did not have 0 shields");

        System.out.println("**checking if player 2 has 7 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 2 Shields: 7"), "Player 2 did not have 7 shields");

        System.out.println("**checking if player 3 has 0 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 3 Shields: 0"), "Player 3 did not have 0 shields");

        System.out.println("**checking if player 4 has 7 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 4 Shields: 7"), "Player 4 did not have 7 shields");

        //Asserting winners
        System.out.println("**checking if player 4 has won the quest*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest Winner: Player 2"), "Player 2 did not win quest.");

        System.out.println("**checking if player 4 has won the quest*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest Winner: Player 4"), "Player 4 did not win quest.");

        //Asserting winners
        System.out.println("**checking if player 4 has won the game*");
        assertTrue(questsGamePage.getGameStatus().contains("Game Winner: Player 2"), "Player 2 did not win quest.");

        System.out.println("**checking if player 4 has won the game*");
        assertTrue(questsGamePage.getGameStatus().contains("Game Winner: Player 4"), "Player 4 did not win quest.");

        //Asserting cards
        System.out.println("*Checking if player 1 has the correct cards*");
//        String[] myArray = questsGamePage.getAllPlayerHandText().split("\n");
//
//        String[] hand1 = myArray[1].replace("Player 1: ","").split(",");
//        System.out.println(Arrays.toString(hand1));

        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 1 Number of Cards (12): F 5,D 5,F 5,F 10,H 10,F 10,F 15,F 15,A 15,F 15,F 15,L 20"),"Player 1 did not have the correct cards.");

        System.out.println("*Checking if player 2 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 2 Number of Cards (12): D 5,S 10,F 10,L 20,L 20,F 20,F 25,F 25,F 30,F 40,F 40,F 50"),"Player 2 did not have the correct cards.");

        System.out.println("*Checking if player 3 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 3 Number of Cards (12): F 5,D 5,D 5,D 5,D 5,D 5,H 10,H 10,H 10,H 10,H 10,F 30"),"Player 3 did not have the correct cards.");

        System.out.println("*Checking if player 4 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 4 Number of Cards (12): F 5,S 10,F 15,F 15,L 20,L 20,F 20,F 20,F 20,F 20,F 30,F 50"),"Player 4 did not have the correct cards.");

    }

//    @AfterEach
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }


}
