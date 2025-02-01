let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
    function isDesktop() {
        const userAgent = navigator.userAgent.toLowerCase();
        return userAgent.includes("windows") || userAgent.includes("macintosh") || userAgent.includes("linux");
    }
    console.log(isDesktop());
    if (!isDesktop()) {
        document.getElementById("screen").style.marginTop = "110px";
        window.Telegram.WebApp.requestFullscreen();
    }

window.Telegram.WebApp.expand();}
catch(error) {clientID = 159619887}
console.log(clientID)