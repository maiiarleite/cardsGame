async function start() {	
    $("#alert").hide();
    $("#winning").hide();
	$("#start").hide();
	
    startGame();
    
	$("#textCards").show();
	$("#loading").show();
}
			
async function startGame() {
    return fetch('/api/cards/start')
        .then(responseObject => {
            	if (!responseObject.ok) {
					throw new Error('Error sending data');
				}
				return responseObject.text()})
        .then(data => {			
            JSON.parse(data).forEach(function(response, index) {				
    	    	let responseAux = JSON.parse(response);
                let player = getPlayerCards(responseAux);

                $(`#player${index + 1} img`).each(function(i) {
                    if (player && player[i]) {
                        $(this).attr('src', player[i].image);
                    }
                });
            });
            
			$("#textCards").hide();
			$("#textWinner").show();
			
			checkWinner(data);			
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function getPlayerCards(response) {	
	let qtdPlayer = 4;
	
    for (let i = 0; i < qtdPlayer; i++) {		
        let player = response.piles[`player${i + 1}`];
        
        if (player && player.cards) {
            return player.cards;
        }
    }

    return null;
}
	
async function checkWinner(gameData) {	
    setTimeout(function() {
        fetch('/api/cards/winningPlayer', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({data : JSON.parse(gameData)})
            })
            .then(response => {
            	if (!response.ok) {
					throw new Error('Error sending data');
				}
				return response.text()})
            .then(data => {
                $("#loading").hide();
				$("#textWinner").hide();   	        
	        	
                let response = JSON.parse(data);
                let winner = '';   
        	    
    	        for (let i = 0; i < response.length; i++) {
					winner = winner + response[i].name + ' ⋯⋯⋯⋯⋯⋯ ' + response[i].point + '\n';
					
					var winners = {
				        deckId: response[i].deckId,
				        name: response[i].name,
				        point: response[i].point,
				        date: response[i].date,
				    };
				
    	        	saveWinners(winners);
    	        }
    	            	        
	        	document.getElementById("winning").innerText = winner.toUpperCase();
    	        $("#checkWinner, #alert, #winning, #start").show();
    	        
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }, 3000);
}

async function saveWinners(winnerData) {
	fetch('/api/cards/save', {
	  method: 'POST',
	  headers: {'Content-Type': 'application/json'},
	  body: JSON.stringify(winnerData)
	})
	.then(response => {
	  if (!response.ok) {
	    throw new Error('Error sending data.');
	  }
	  return response.json();
	});
}


function updateWinnerMessage(message) {
    document.getElementById("winning").innerText = message;
}

function showWinnerMessage(message) {
    var winnerContainer = document.getElementById("checkWinner");
    winnerContainer.style.display = "block";
    updateWinnerMessage(message);
}