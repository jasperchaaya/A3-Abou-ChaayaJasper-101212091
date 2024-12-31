To run:

Open 2 terminals:

First terminal is used to run the back-end
navigate to ...\A3-Abou-ChaayaJasper-101212091\quests-backend 
and run
mvn spring-boot:run

Second terminal is used to run front-end
in ...\A3-Abou-ChaayaJasper-101212091
run
npx http-server

Go to http://127.0.0.1:8081/quests-frontend/


To run Selenium tests:
Repeat the above steps
Ensure you have the latest Chromedriver installed
Ensure you have Selenium installed

In \A3-Abou-ChaayaJasper-101212091\quests-backend\src\test\java
Modify these 4 classes to point to the Chromedriver on your computer
  Update this line "System.setProperty(..." 
  
  QuestsGameScenario1Test.class
  QuestsGameScenario2Test.class
  QuestsGameScenario3Test.class
  QuestsGameScenario4Test.class

Run any of the above 4 tests or run the RunSeleniumTestSuite to run all 4 at once.
