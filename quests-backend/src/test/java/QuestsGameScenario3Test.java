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
        assertTrue(questsGamePage.getGameStatus().contains("SUCCESS"), "Scenario 3 setup did not complete as expected.");
        pause(2);
        //Play the game
        System.out.println("*Clicking Draw Button*");
        questsGamePage.clickDraw();
        pause(2);

        questsGamePage.clickDisplayAllHands();

        System.out.println("*Player 1 Clicking Yes Button to set up quest*");
        questsGamePage.clickYes();
        pause(2);
        assertTrue(questsGamePage.getGameStatus().contains("Quest Accepted"), "Quest was not accepted as expected.");
        pause(2);

        //Aetup quest by player 1
        //Select multiple cards by their values
        System.out.println("*Selecting cards for Player 1 to setup stage*");
        questsGamePage.selectMultipleCards(Arrays.asList("F 5", "F 10", "F 15", "F 20"));
        pause(2);
        System.out.println("*Clicking set up quest*");
        questsGamePage.clickSetStage();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //do attacks
        //player 2 attacking
        System.out.println("*Selecting cards for Player 2 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList("S 10", "H 10", "A 15", "L 20"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //player 3 attacking
        System.out.println("*Selecting cards for Player 3 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList("S 10", "H 10", "A 15", "L 20"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //player 4 attacking
        System.out.println("*Selecting cards for Player 4 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList("S 10", "H 10", "A 15", "L 20"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //Verify the quest results
        pause(10);

        System.out.println("*Checking if quest ended*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest ended"), "Quest did not end.");

        //Asserting shields
        System.out.println("*Checking if player 1 has 0 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 1 Shields: 0"), "Player 1 did not have 0 shields");

        System.out.println("*Checking if player 2 has 4 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 2 Shields: 4"), "Player 2 did not have 4 shields");

        System.out.println("*Checking if player 3 has 4 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 3 Shields: 4"), "Player 3 did not have 4 shields");

        System.out.println("*Checking if player 4 has 4 shields*");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 4 Shields: 4"), "Player 4 did not have 4 shields");

        //Asserting winners
        System.out.println("*Checking if player 2 has won the quest*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest Winner: Player 2"), "Player 2 did not win quest.");

        System.out.println("*Checking if player 3 has won the quest*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest Winner: Player 3"), "Player 3 did not win quest.");

        System.out.println("*Checking if player 4 has won the quest*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest Winner: Player 4"), "Player 4 did not win quest.");

        //Asserting cards

        System.out.println("*Checking if player 1 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 1 Number of Cards (12): F 5,D 5,D 5,D 5,D 5,F 5,F 5,F 10,F 10,F 10,F 15,F 20"),"Player 1 did not have the correct cards.");

        System.out.println("*Checking if player 2 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 2 Number of Cards (12): F 5,H 10,S 10,S 10,F 10,A 15,F 15,L 20,F 20,F 25,F 30,E 30"),"Player 2 did not have the correct cards.");

        System.out.println("*Checking if player 3 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 3 Number of Cards (12): F 5,F 5,H 10,S 10,S 10,F 10,A 15,L 20,F 25,F 25,F 30,E 30"),"Player 3 did not have the correct cards.");

        System.out.println("*Checking if player 4 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 4 Number of Cards (12): F 5,H 10,S 10,S 10,F 10,A 15,L 20,F 20,F 25,F 30,E 30,F 70"),"Player 4 did not have the correct cards.");


        //player 2 draws a plague card
        questsGamePage.clickDraw();
        pause(3);
        System.out.println("***Checking if player 2 lost 2 cards from the plague card");
        assertTrue(questsGamePage.getPlayerShields().contains("Player 2 Shields: 2"), "Player 2 did not have 2 shields");
        questsGamePage.clickDisplayAllHands();

        //player 3 draws a prosperity card
        questsGamePage.clickDraw();
        pause(3);
        System.out.println("***Checking if player 3 got a prosperity card");
        assertTrue(questsGamePage.getGameStatus().contains("Prosperity"), "Player 3 did not get a Prosperity card");
        questsGamePage.clickDisplayAllHands();


        //player 4 draws a queens favor card
        questsGamePage.clickDraw();
        pause(3);
        System.out.println("***Checking if player 4 got a Queen's Favor card");
        assertTrue(questsGamePage.getGameStatus().contains("Queen's Favor"), "Player 4 did not get a Queen's Favor card");
        questsGamePage.clickDisplayAllHands();

        //player 1 draws a Q 3
        questsGamePage.clickDraw();
        pause(3);
        System.out.println("***Player 1 accepting quest of Q 3 ***");
        questsGamePage.clickYes();

        System.out.println("*Selecting cards for Player 1 to setup stage*");
        questsGamePage.selectMultipleCards(Arrays.asList("F 15", "D 5", "F 20"));
        pause(2);
        System.out.println("*Clicking set up quest*");
        questsGamePage.clickSetStage();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //do second quest attacks
        //player 2 attacking
        System.out.println("*Selecting cards for Player 2 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList( "H 10", "A 15", "L 20"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //player 3 attacking
        System.out.println("*Selecting cards for Player 3 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList("A 15", "S 10", "E 30"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //player 4 attacking
        System.out.println("*Selecting cards for Player 4 to attack*");
        questsGamePage.selectMultipleCards(Arrays.asList("S 10"));
        pause(2);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();

        //Verify the quest results
        pause(3);

        System.out.println("*Checking if quest ended*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest ended"), "Quest did not end.");

        //Asserting cards

        System.out.println("*Checking if player 1 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 1 Number of Cards (12): D 5,D 5,F 5,F 5,F 10,F 10,F 10,S 10,S 10,F 15,F 15,F 40"),"Player 1 did not have the correct cards.");

        System.out.println("*Checking if player 2 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 2 Number of Cards (12): D 5,D 5,S 10,F 10,F 15,F 15,F 15,F 20,F 25,F 30,E 30,F 30"),"Player 2 did not have the correct cards.");

        System.out.println("*Checking if player 3 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 3 Number of Cards (12): F 5,S 10,F 10,H 10,A 15,A 15,F 25,F 25,F 25,F 25,F 25,F 30"),"Player 3 did not have the correct cards.");

        System.out.println("*Checking if player 4 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 4 Number of Cards (12): H 10,S 10,A 15,A 15,L 20,F 20,F 25,F 30,E 30,F 40,F 50,F 70"),"Player 4 did not have the correct cards.");

        //Verify the quest winners
        System.out.println("*Checking if player 2 has won the quest*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest Winner: Player 2"), "Player 2 did not win quest.");

        System.out.println("*Checking if player 3 has won the quest*");
        assertTrue(questsGamePage.getGameStatus().contains("Quest Winner: Player 3"), "Player 3 did not win quest.");

        //Verifying game winner
        System.out.println("*Checking if player 3 has won the game*");
        assertTrue(questsGamePage.getGameStatus().contains("Game Winner: Player 3"), "Player 3 did not win the game.");


    }

//    @AfterEach
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }


}
