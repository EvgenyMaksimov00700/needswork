let clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
//let clientID = 159619887
console.log(clientID)
document.addEventListener('DOMContentLoaded', function(){
     fetch( `/vacancy/user/${clientID}`, {
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
            const vacancies=document.getElementById("vacancies")
            data.forEach (vacancy => {
            let salary="";
            if (vacancy.fromSalary!=null) {
                salary+=`от ${vacancy.fromSalary}`;
            }
            if (vacancy.toSalary!=null) {
                salary+=` до ${vacancy.toSalary}`;
            }
            if (salary=="") {
                salary="Не указано";
            }
            const vacancy_url = "window.location.href='/employer/vacancy/description?id=" + vacancy.id + "'";
            const element= `<button class="vacancy" onclick=${vacancy_url}>
                                    ${vacancy.position}<br>${salary}<br>${vacancy.city}<br>${vacancy.employer.name}
                                </button>`
                                 vacancies.innerHTML+=element})
            })
});