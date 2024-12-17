let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
window.Telegram.WebApp.requestFullscreen();
window.Telegram.WebApp.expand();
}

catch(error) {clientID = 159619887}
console.log(clientID)

const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const vacancyId = params.get('id');
function edit_vacancy () {
window.location.href=`/employer/vacancy/edit?id=${vacancyId}`}
let employerUserId;

function vacancy_responses() {
window.location.href=`/employer/responses7/show?id=${vacancyId}`
}

function closeModal() {
    document.getElementById('resumeModal').style.display = 'none';
}

window.onclick = function(event) {
    const modal = document.getElementById('resumeModal');
    if (event.target === modal) {
        closeModal();
    }
}


function delete_vacancy() {
if (confirm("Вы действительно хотите удалить вакансию?")){
fetch( `/vacancy/${vacancyId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {

                    throw new Error(`Ошибка HTTP: ${response.status}`);
                }
                return response.json();

            }).then(data => {window.location.href="/employer/my_vacancy7/show"})

}
}
document.addEventListener('DOMContentLoaded', function(){
     fetch( `/vacancy/${vacancyId}`, {
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
                document.getElementById("position").innerHTML = data.position;
                document.getElementById("employer_name").innerHTML = "<b>Компания: </b>" + data.employer.name;
                document.getElementById("city").innerHTML = "<b>Город: </b>" + data.city;
                document.getElementById("address").innerHTML = "<b>Адрес: </b>" + data.address;
                document.getElementById("salary").innerHTML += "<b>Зарплата: </b>";
                if (data.fromSalary!=null) {
                document.getElementById("salary").innerHTML += "от " + data.fromSalary + " ";
                }
                if (data.toSalary!=null) {
                document.getElementById("salary").innerHTML += "до " + data.toSalary + " ";
                }
                if (data.fromSalary==null && data.toSalary==null) {
                document.getElementById("salary").innerHTML += "не указана";
                }
                else {
                document.getElementById("salary").innerHTML += "руб";}
                document.getElementById("exp").innerHTML = "<b>Требуемый опыт работы: </b>" + data.exp;
                document.getElementById("workSchedule").innerHTML = "<b>График работы: </b>" + data.workSchedule;
                if (data.distantWork==true) {
                document.getElementById("workSchedule").innerHTML += ", возможно удаленно";}
                const paragraphs = data.responsibility.split(/\n\s*\n/);
                const outputDiv = document.getElementById("responsibility");
                paragraphs.forEach(paragraph => {
                    if (paragraph.trim() !== "") {
                        const p = document.createElement('p');
                        p.classList.add('paragraph');
                        p.textContent = paragraph.trim();
                        outputDiv.appendChild(p);
                    }
                });

                document.getElementById("create_date").innerHTML = "<b>Дата публикации: </b>" + data.createdDateTime;
            });

});

