let clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
window.Telegram.WebApp.expand();

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
});