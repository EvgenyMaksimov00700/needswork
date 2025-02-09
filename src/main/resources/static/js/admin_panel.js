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

document.addEventListener('DOMContentLoaded', function(){
     fetch( `/admin/info/total`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {

                    throw new Error(`Ошибка HTTP: ${response.status}`);
                }
                return response.json();

            }).then(data => {
                console.log (data);
document.getElementById('vacancies').textContent = data.vacancies;
document.getElementById('responses').textContent = data.responses;
document.getElementById('videoResponses').textContent = data.videoResponses;
document.getElementById('textResponses').textContent = data.textResponses;
document.getElementById('withoutResponses').textContent = data.withoutResponses;
document.getElementById('jobSeekers').textContent = data.jobSeekers;
document.getElementById('employers').textContent = data.employers;
document.getElementById('payments').textContent = data.payments;

;
            });
setTimeout(() => {
                const loadingOverlay = document.getElementById("loading");
            loadingOverlay.style.display = "none";
            }, 1000);
});