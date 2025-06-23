let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
if (window.history.length > 1) {
        window.Telegram.WebApp.BackButton.show();
        window.Telegram.WebApp.BackButton.onClick(() => {
            window.history.back();
        });
    }
window.Telegram.WebApp.expand();
  function isDesktop() {
        const userAgent = navigator.userAgent.toLowerCase();
        return userAgent.includes("windows") || userAgent.includes("macintosh") || userAgent.includes("linux");
    }
    console.log(isDesktop());
    if (!isDesktop()) {
        window.Telegram.WebApp.requestFullscreen();
    }
}

catch(error) {clientID = 159619887}
console.log(clientID)

let employerId;
function support(){
window.Telegram.WebApp.openTelegramLink(`https://t.me/Cherchent`); // URL с ID чата
                window.Telegram.WebApp.close();
                }

document.addEventListener('DOMContentLoaded', function(){
     fetch( `/employer/user/${clientID}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {
                console.log ("fuck off")
                    window.location.href = '/employer/reg/'
                    throw new Error(`Ошибка HTTP: ${response.status}`); // Бросаем ошибку, если ответ не в порядке
                }
                return response.json();
            })
            .then(data => {employerId = data.employer_id;})
});
function delete_employer() {
if (confirm("Вы действительно хотите удалить Ваш аккаунт?")){
fetch( `/employer/${employerId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {

                    throw new Error(`Ошибка HTTP: ${response.status}`);
                }
                return response.json();

            }).then(data => {window.Telegram.WebApp.openTelegramLink(`https://t.me/tworker_ru_bot?start=start`);
                             window.Telegram.WebApp.close();})

}
}