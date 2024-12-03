const apiBaseUrl = "http://127.0.0.1:8080";
const sessionId = Math.floor(Math.random() * 100);

async function startGame() {
    try {
        appendStat("Starting Quest Game...\n");
        const response = await fetch(`${apiBaseUrl}/start?sessionId=${sessionId}`, { method: "POST" });
        const result = await response.text();
        appendStat(result);

        document.getElementById("start-game").disabled = true;
        document.getElementById("reset-game").disabled = false;
        document.getElementById("draw-btn").disabled = false;
        document.getElementById("yes-btn").disabled = false;
        document.getElementById("no-btn").disabled = false;

        getPlayerHand();
        getShields();
    } catch (error) {
        console.error("Error in startGame: ", error);
    }
}

async function yes() {
    try {
        document.getElementById("yes-btn").disabled = true;
        document.getElementById("no-btn").disabled = true;
        appendStat("\nQuest Accepted");
        const response = await fetch(`${apiBaseUrl}/yes?sessionId=${sessionId}`, { method: "POST" });
        const result = await response.text();
        if (result) {
            const hand = result.split(",");
            displayHand(hand);
            getCurrentPlayer(true);
            appendStat("\nSelect the cards to set stage");
        }
    } catch (error) {
        console.error("Error when yes pressed: ", error);
    }
}

async function no() {
    try {
        const response = await fetch(`${apiBaseUrl}/no?sessionId=${sessionId}`, { method: "POST" });
        const result = await response.text();
        console.log("Stand Response:", result);
        appendStat(result);
    } catch (error) {
        console.error("Error when no pressed: ", error);
    }
}

async function resetGame() {
    try {
        reset();
        const response = await fetch(`${apiBaseUrl}/reset?sessionId=${sessionId}`, { method: "DELETE" });
        const result = await response.text();
    } catch (error) {
        console.error("Error in resetGame:", error);
    }
}

// Enable buttons when an item is selected
document.getElementById("cards").addEventListener("change", () => {
    const select = document.getElementById("cards");
    if (select.selectedOptions.length > 0) {
        document.getElementById("setStage-btn").disabled = false;
        document.getElementById("playAttack-btn").disabled = false;
    }
});

// Handle Set Stage button
async function setStage() {
    const selectedCards = getSelectedCards();
    appendStat(`\nSetting stage with: ${selectedCards.join(", ")}`);
    document.getElementById("setStage-btn").disabled = true;
    document.getElementById("playAttack-btn").disabled = true;
    document.getElementById("skipAttack-btn").disabled = false;


    const select = document.getElementById("cards");
    let indexes = Array.from(select.selectedOptions).map(option => option.index);
    //call the controller setStage to set stage and will return hand of next player
    let selected = indexes.join(",");
    const response = await fetch(`${apiBaseUrl}/setStage?sessionId=${sessionId}&selected=${selected}`, { method: "POST" });
    const result = await response.text();
    if (result) {
        const hand = result.split("|")[1].split(",");
        displayHand(hand);
        getCurrentPlayer(false);
        appendStat("\n"+result.split("|")[0]+", select cards to attack");
    }
}

// Handle Play Attack button
async function playAttack() {
//    debugger;
    const selectedCards = getSelectedCards();
    appendStat(`\nPlaying attack with: ${selectedCards.join(", ")}`);
    document.getElementById("setStage-btn").disabled = true;
    document.getElementById("playAttack-btn").disabled = true;

    const select = document.getElementById("cards");
    let indexes = Array.from(select.selectedOptions).map(option => option.index);

    //call the controller plat attack
    let selected = indexes.join(",");
    const response = await fetch(`${apiBaseUrl}/playAttack?sessionId=${sessionId}&selected=${selected}`, { method: "POST" });
    const result = await response.text();
    if (result) {
        if(result.includes("|")){
            const info = result.split("|");
            appendStat('\n'+info[0]);
            const hand = info[1].split(",");
            getShields();
            getCurrentPlayer(false);
            displayHand(hand);
            document.getElementById("player").innerText = player + " Hand";
        }else{
            appendStat('\n'+result+'\nGAME OVER');
            getShields();
        }

    }
}

// Get selected cards from the select element
function getSelectedCards() {
    const select = document.getElementById("cards");
    return Array.from(select.selectedOptions).map(option => option.value);
}

window.onload = () => { reset(); };

function reset() {
    document.getElementById("game-status").innerText = "";
    document.getElementById("start-game").disabled = false;  // Start Game button enabled
    document.getElementById("reset-game").disabled = true;   // Reset Game button disabled
    document.getElementById("draw-btn").disabled = true;     // draw button disabled
    document.getElementById("yes-btn").disabled = true;      // Yes button disabled
    document.getElementById("no-btn").disabled = true;       // No button disabled
    document.getElementById("setStage-btn").disabled = true; // Set Stage button disabled
    document.getElementById("playAttack-btn").disabled = true; // Play Attack button disabled
    document.getElementById("cards").innerHTML = "";         // Clear card options
    document.getElementById("shields").innerHTML = "";
}

async function getCurrentPlayer(setStageOwner){
    const response = await fetch(`${apiBaseUrl}/getCurrentPlayer?sessionId=${sessionId}`, { method: "POST" });
    const result = await response.text();
    document.getElementById("player").innerText = result + " Hand";
    if(setStageOwner){
        document.getElementById("StageOwner").value = result;
        console.log(document.getElementById("StageOwner").value);
    }
}

//draw event card
async function draw() {
    try {
        const response = await fetch(`${apiBaseUrl}/drawEventCard?sessionId=${sessionId}`, { method: "POST" });
        const result = await response.text();
        if (result) {
            appendStat(`\nDrawn Event Card: ${result}`);
            if(result.startsWith("Plague")){
                appendStat("\nPlayer loses 2 shields");
                await updatePlayerShields(-2);
                endTurn();
            }else if(result.startsWith("Queen")){
                appendStat("\nPlayer gets 2 Adventure Cards");
                const response = await fetch(`${apiBaseUrl}/handleEventCard?sessionId=${sessionId}&cardType=${result}`, { method: "POST" });
                const ret = await response.text();
                console.log("Queen's favor card pulled");
                endTurn();
            }else if(result.startsWith("Prosperity")){
                appendStat("\nAll players get 2 Adventure Cards");
                const response = await fetch(`${apiBaseUrl}/handleEventCard?sessionId=${sessionId}&cardType=${result}`, { method: "POST" });
                const ret = await response.text();
                console.log("Prosperity card pulled");
                endTurn();
            }else if(result.startsWith("Q ")){
                appendStat("\nDo you wish to sponsor? Yes/No");
                document.getElementById("yes-btn").disabled = false;
                document.getElementById("no-btn").disabled = false;

            }

        }
    } catch (error) {
        console.error("Error drawing event card:", error);
        appendStat(`\nError: ${error.message}`);
    }
}

function appendStat(msg){
    let objDiv = document.getElementById("game-status");
    objDiv.innerText += msg;
    objDiv.scrollTop = objDiv.scrollHeight;
}

async function getPlayerHand(){
    const response = await fetch(`${apiBaseUrl}/getCurrentPlayerHand?sessionId=${sessionId}`, { method: "POST" });
    const result = await response.text();
    if (result) {
        const hand = result.split(",");
        displayHand(hand);
        getCurrentPlayer(false);
    }
}

function displayHand(hand){
    const select = document.getElementById("cards");
    select.innerHTML = ""; // Clear existing options
    for (let i = 0; i < hand.length; i++) {
        const option = document.createElement("option");
        option.text = hand[i];
        option.value = hand[i];
        select.appendChild(option);
    }
}

async function getShields() {
    try {
        const response = await fetch(`${apiBaseUrl}/getPlayersShields?sessionId=${sessionId}`, { method: "POST" });
        if (!response.ok) {
            throw new Error(await response.text());
        }
        const shields = await response.text();
        document.getElementById("shields").innerHTML = shields;
    } catch (error) {
        console.error("Error fetching player shields:", error);
    }
}

async function updatePlayerShields(shieldCount) {
    //debugger;
    try {
        const response = await fetch(`${apiBaseUrl}/updatePlayerShields?sessionId=${sessionId}&shieldCount=${shieldCount}`, { method: "POST" });
        if (!response.ok) {
            throw new Error(await response.text());
        }
        const shields = await response.text();
        document.getElementById("shields").innerHTML = shields;
    }catch (error) {
        console.error("Error fetching player shields:", error);
    }
}

async function endTurn() {
    try {
        const response = await fetch(`${apiBaseUrl}/endTurn?sessionId=${sessionId}`, { method: "POST" });
        const nextPlayerName = await response.text();
        if(nextPlayerName){
            appendStat("\n" + nextPlayerName + "'s turn");
        }
    }catch (error) {
        console.error("Error fetching player shields:", error);
    }
}

async function skipAttack() {
    document.getElementById("setStage-btn").disabled = true;
    document.getElementById("playAttack-btn").disabled = true;

    //call the controller skip turn
    const response = await fetch(`${apiBaseUrl}/skipAttack?sessionId=${sessionId}`, { method: "POST" });
    const result = await response.text();
    if (result) {
        if(result.includes("|")){
            const info = result.split("|");
            appendStat('\n'+info[0]);
            const hand = info[1].split(",");
            getShields();
            getCurrentPlayer(false);
            displayHand(hand);
            document.getElementById("player").innerText = player + " Hand";
        }else{
            appendStat('\n'+result+'\nGAME OVER');
            getShields();
        }

    }
}

//for testing
async function displayAllHands() {
    const handsDiv = document.getElementById("all-player-hands");

    handsDiv.style.display = "block";
    const response = await fetch(`${apiBaseUrl}/displayAllHands?sessionId=${sessionId}`, { method: "POST" });
    const result = await response.text();
    if(result){
        handsDiv.innerHTML = result;
    }

}

async function runScenario1() {
    try {
        appendStat("Starting Scenario 1 game setup...\n");
        const response = await fetch(`${apiBaseUrl}/setUpScenario1?sessionId=${sessionId}`, { method: "POST" });
        const result = await response.text();
        appendStat(result);

        //Enable relevant buttons
        document.getElementById("start-game").disabled = true;
        document.getElementById("reset-game").disabled = false;
        document.getElementById("draw-btn").disabled = false;
        document.getElementById("yes-btn").disabled = false;
        document.getElementById("no-btn").disabled = false;
        document.getElementById("setStage-btn").disabled = false;
        document.getElementById("playAttack-btn").disabled = false;
        document.getElementById("skipAttack-btn").disabled = false;


        getPlayerHand();
        getShields();

        // Start periodic check after scenario1 button is clicked
        setInterval(() => {
            if (document.getElementById("start-game").disabled) {
                document.getElementById("setStage-btn").disabled = false;
                document.getElementById("playAttack-btn").disabled = false;
                document.getElementById("allHands-btn").disabled = false;

            }
        }, 500); // Check every 500ms
    } catch (error) {
        console.error("Error in runScenario1: ", error);
    }
}

async function runScenario2() {
    try {
        appendStat("Starting Scenario 2 game setup...\n");
        const response = await fetch(`${apiBaseUrl}/setUpScenario2?sessionId=${sessionId}`, { method: "POST" });
        const result = await response.text();
        appendStat(result);

        //Enable relevant buttons
        document.getElementById("start-game").disabled = true;
        document.getElementById("reset-game").disabled = false;
        document.getElementById("draw-btn").disabled = false;
        document.getElementById("yes-btn").disabled = false;
        document.getElementById("no-btn").disabled = false;
        document.getElementById("setStage-btn").disabled = false;
        document.getElementById("playAttack-btn").disabled = false;
        document.getElementById("skipAttack-btn").disabled = false;


        getPlayerHand();
        getShields();

        // Start periodic check after scenario1 button is clicked
        setInterval(() => {
            if (document.getElementById("start-game").disabled) {
                document.getElementById("setStage-btn").disabled = false;
                document.getElementById("playAttack-btn").disabled = false;
                document.getElementById("allHands-btn").disabled = false;

            }
        }, 500); // Check every 500ms
    } catch (error) {
        console.error("Error in runScenario2: ", error);
    }
}

async function runScenario3() {
    try {
        appendStat("Starting Scenario 3 game setup...\n");
        const response = await fetch(`${apiBaseUrl}/setUpScenario3?sessionId=${sessionId}`, { method: "POST" });
        const result = await response.text();
        appendStat(result);

        //Enable relevant buttons
        document.getElementById("start-game").disabled = true;
        document.getElementById("reset-game").disabled = false;
        document.getElementById("draw-btn").disabled = false;
        document.getElementById("yes-btn").disabled = false;
        document.getElementById("no-btn").disabled = false;
        document.getElementById("setStage-btn").disabled = false;
        document.getElementById("playAttack-btn").disabled = false;
        document.getElementById("skipAttack-btn").disabled = false;


        getPlayerHand();
        getShields();

        // Start periodic check after scenario1 button is clicked
        setInterval(() => {
            if (document.getElementById("start-game").disabled) {
                document.getElementById("setStage-btn").disabled = false;
                document.getElementById("playAttack-btn").disabled = false;
                document.getElementById("allHands-btn").disabled = false;

            }
        }, 500); // Check every 500ms
    } catch (error) {
        console.error("Error in runScenario3: ", error);
    }
}

async function runScenario4() {
    try {
        appendStat("Starting Scenario 4 game setup...\n");
        const response = await fetch(`${apiBaseUrl}/setUpScenario4?sessionId=${sessionId}`, { method: "POST" });
        const result = await response.text();
        appendStat(result);

        //Enable relevant buttons
        document.getElementById("start-game").disabled = true;
        document.getElementById("reset-game").disabled = false;
        document.getElementById("draw-btn").disabled = false;
        document.getElementById("yes-btn").disabled = false;
        document.getElementById("no-btn").disabled = false;
        document.getElementById("setStage-btn").disabled = false;
        document.getElementById("playAttack-btn").disabled = false;
        document.getElementById("skipAttack-btn").disabled = false;


        getPlayerHand();
        getShields();

        // Start periodic check after scenario1 button is clicked
        setInterval(() => {
            if (document.getElementById("start-game").disabled) {
                document.getElementById("setStage-btn").disabled = false;
                document.getElementById("playAttack-btn").disabled = false;
                document.getElementById("allHands-btn").disabled = false;

            }
        }, 500); // Check every 500ms
    } catch (error) {
        console.error("Error in runScenario4: ", error);
    }
}


//async function endQuest() {
//    try {
//        const response = await fetch(`${apiBaseUrl}/endQuest?sessionId=${sessionId}`, { method: "POST" });
//        const result = await response.text();
//        if(result){
//            appendStat(result.split("|")[0]);
//            appendStat("\n" + result.split("|")[1] + "'s turn");
//        }
//    }catch (error) {
//        console.error("Error fetching player shields:", error);
//    }
//}