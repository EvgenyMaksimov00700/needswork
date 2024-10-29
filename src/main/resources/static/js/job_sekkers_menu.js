let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
window.Telegram.WebApp.expand();
}

catch(error) {clientID = 159619887}
console.log(clientID)

let employerId;
function support(){
window.Telegram.WebApp.openTelegramLink(`https://t.me/Cherchent`); // URL с ID чата
                window.Telegram.WebApp.close();
                }
}