
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestsGameScenario1Test {

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
    public void testScenario1() throws InterruptedException {

        //waiting for page to load
        pause(3);

        //Click "Scenario 1" button to setup the scenario
        System.out.println("*Clicking Scenario 1*");
        questsGamePage.clickScenario1();

        pause(2);
        //Verify the game started
        assertTrue(questsGamePage.getGameStatus().contains("Starting"), "Game did not start as expected.");

        //Verify that Scenario 1 setup completes
        assertTrue(questsGamePage.getGameStatus().contains("SUCCESS"), "Scenario 1 setup did not complete as expected.");
        pause(2);

        //displaying all hands
        questsGamePage.clickDisplayAllHands();
        pause(2);

        //Play the game
        System.out.println("*Clicking Draw Button*");
        questsGamePage.clickDraw();
        pause(3);

        System.out.println("*Player 1 Clicking No Button*");
        questsGamePage.clickNo();
        pause(3);

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
        questsGamePage.selectMultipleCardsByIndex(Arrays.asList(1, 2, 4));
        pause(3);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);
        questsGamePage.clickDisplayAllHands();


        //player 4 attacking
        System.out.println("*Selecting cards for Player 4 to attack*");
//        questsGamePage.selectMultipleCards(Arrays.asList("S 10", "S 10", "S 20"));
        questsGamePage.selectMultipleCardsByIndex(Arrays.asList(3, 4, 6, 9));
        pause(4);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(3);

        questsGamePage.clickDisplayAllHands();
        pause(3);

        //player 1 attacking
        System.out.println("*Selecting cards for Player 1 to attack*");
        questsGamePage.selectMultipleCards(List.of("H 10"));
        pause(4);
        System.out.println("*Clicking Play Attack*");
        questsGamePage.clickPlayAttack();
        pause(2);


        //Verify the quest results
        pause(3);
        questsGamePage.clickDisplayAllHands();

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
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 1: F 5,F 10,F 10,F 15,F 15,A 15,A 15,L 20,L 20,F 30,F 30"),
                "Player 1 did not have the correct cards.");

        System.out.println("*Checking if player 2 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 2: F 5,F 5,F 10,S 10,H 10,H 10,F 15,F 20,L 20,F 20,L 20,F 30,F 60"),
                "Player 2 did not have the correct cards.");

        System.out.println("*Checking if player 3 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 3: F 5,F 5,S 10,H 10,F 10,F 15,F 15,L 20,F 30,F 40,F 50"),
                "Player 3 did not have the correct cards.");

        System.out.println("*Checking if player 4 has the correct cards*");
        assertTrue(questsGamePage.getAllPlayerHandText().contains("Player 4: F 10,H 10,F 15,F 15,A 15,L 20,F 40,F 50,F 70"),
                "Player 4 did not have the correct cards.");
    }

//    @AfterEach
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }


}
