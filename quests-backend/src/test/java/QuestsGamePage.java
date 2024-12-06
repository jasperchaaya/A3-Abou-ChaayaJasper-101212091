import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestsGamePage {

    private WebDriver driver;
    private final WebDriverWait wait;

    //Constructor
    public QuestsGamePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
    }

    //Buttons
    By startBtn = By.id("start-game");
    private final By resetBtn = By.id("reset-game");
    private final By drawBtn = By.id("draw-btn");
    private final By yesBtn = By.id("yes-btn");
    private final By noBtn = By.id("no-btn");
    private final By setStageBtn = By.id("setStage-btn");
    private final By playAttackBtn = By.id("playAttack-btn");
    private final By skipAttackBtn = By.id("skipAttack-btn");

    //Game status
    private final By gameStatus = By.id("game-status");
    //Player Shields
    private final By playerShields = By.id("shields");

    //Player Hand
    private final By playerHandLabel = By.id("player");
    private final By playerHandDropdown = By.id("cards");

    //Scenarios and testing
    private final By scenario1Btn = By.id("scenario1-btn");
    private final By scenario2Btn = By.id("scenario2-btn");
    private final By scenario3Btn = By.id("scenario3-btn");
    private final By scenario4Btn = By.id("scenario4-btn");
    private final By allHandsBtn = By.id("allHands-btn");
    private final By allHandsText = By.id("all-player-hands");


    //Methods to interact with elements

    public void clickStartGame() {
        wait.until(ExpectedConditions.elementToBeClickable(startBtn)).click();
    }

    public void clickResetGame() {
        wait.until(ExpectedConditions.elementToBeClickable(resetBtn)).click();
    }

    public void clickDraw() {
        wait.until(ExpectedConditions.elementToBeClickable(drawBtn)).click();
    }

    public void clickYes() {
        wait.until(ExpectedConditions.elementToBeClickable(yesBtn)).click();
    }

    public void clickNo() {
        wait.until(ExpectedConditions.elementToBeClickable(noBtn)).click();
    }

    public void clickSetStage() {
        wait.until(ExpectedConditions.elementToBeClickable(setStageBtn)).click();
    }

    public void clickPlayAttack() {
        wait.until(ExpectedConditions.elementToBeClickable(playAttackBtn)).click();
    }

    public void clickSkipAttack() {
        wait.until(ExpectedConditions.elementToBeClickable(skipAttackBtn)).click();
    }

    public void clickScenario1() {
        wait.until(ExpectedConditions.elementToBeClickable(scenario1Btn)).click();
    }

    public void clickScenario2() {
        wait.until(ExpectedConditions.elementToBeClickable(scenario2Btn)).click();
    }

    public void clickScenario3() {
        wait.until(ExpectedConditions.elementToBeClickable(scenario3Btn)).click();
    }

    public void clickScenario4() {
        wait.until(ExpectedConditions.elementToBeClickable(scenario4Btn)).click();
    }
    public void clickDisplayAllHands() {
        wait.until(ExpectedConditions.elementToBeClickable(allHandsBtn)).click();
    }

    //select multiple cards by values
    public void selectMultipleCards(List<String> cards) {
        WebElement dropdownElement = driver.findElement(playerHandDropdown);

        Map<String, Integer> cardCount = new HashMap<>();
        for (String card : cards) {
            cardCount.put(card, cardCount.getOrDefault(card, 0) + 1);
        }

        List<WebElement> options = dropdownElement.findElements(By.tagName("option")); // Locate all options
        for (Map.Entry<String, Integer> entry : cardCount.entrySet()) {
            String cardText = entry.getKey();
            int occurrences = entry.getValue();

            int selectedCount = 0;
            for (WebElement option : options) {
                if (option.getText().equals(cardText) && !option.isSelected()) {
                    //Use JavaScript to select the option
                    ((JavascriptExecutor) driver).executeScript("arguments[0].selected = true;", option);
                    selectedCount++;

                    if (selectedCount == occurrences) {
                        break;
                    }
                }
            }
        }
    }

    //select multiple cards by index of select
    public void selectMultipleCardsByIndex(List<Integer> indices) {
        WebElement dropdownElement = driver.findElement(playerHandDropdown);
        List<WebElement> options = dropdownElement.findElements(By.tagName("option"));

        for (Integer index : indices) {
            if (index >= 0 && index < options.size()) {
                WebElement option = options.get(index);
                if (!option.isSelected()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].selected = true;", option);
                }
            } else {
                System.out.println("Index " + index + " is out of bounds for the options list.");
            }
        }
    }





    //methods to get game status or player hand
    public String getGameStatus() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(gameStatus)).getText();
    }

    public String getPlayerHandText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(playerHandLabel)).getText();
    }

    public String getAllPlayerHandText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(allHandsText)).getText();
    }

    public int getPlayerHandSize() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(playerHandDropdown))
                .findElements(By.tagName("option"))
                .size();
    }

    public String getPlayerShields() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(playerShields)).getText();
    }


}
