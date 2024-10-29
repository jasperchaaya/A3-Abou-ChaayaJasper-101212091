import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome = true,
        dryRun = false,
        plugin = {"pretty"},
        glue = "game.quests",
        features = "src/main/test/resources")
public class RunCucumberTest {

}
