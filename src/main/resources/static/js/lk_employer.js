let clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
window.Telegram.WebApp.expand();
let employerId;

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