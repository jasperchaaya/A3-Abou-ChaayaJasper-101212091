
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        QuestsGameScenario1Test.class,
        QuestsGameScenario2Test.class,
        QuestsGameScenario3Test.class,
        QuestsGameScenario4Test.class

})
public class RunSeleniumTestSuite {}

