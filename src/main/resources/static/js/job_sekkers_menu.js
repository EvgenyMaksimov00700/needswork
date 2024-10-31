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


document.addEventListener('DOMContentLoaded', function(){
     fetch( `/videoCv/user/${clientID}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {

                    throw new Error(`Ошибка HTTP: ${response.status}`); // Бросаем ошибку, если ответ не в порядке
                }
                return response.json();

            }).then(data => {
            const vacancies=document.getElementById("resume-buttons")
            data.forEach (videoCv => {

            const vacancy_url = "window.location.href='/employer/vacancy/description?id=" + videoCv.video_message + "'";
            const element= `<div class="resume-button" onclick=${vacancy_url}>
                                    ${videoCv.name}
                                </div>`
                                 vacancies.innerHTML+=element})
            })
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