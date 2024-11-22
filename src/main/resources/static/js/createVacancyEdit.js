let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
}
catch(error) {clientID = 159619887}
console.log(clientID)
document.addEventListener('touchstart', function(event) {
            const activeElement = document.activeElement;

            if (activeElement && (activeElement.tagName === 'INPUT' || activeElement.tagName === 'TEXTAREA')) {
                // Проверяем, что нажали вне поля ввода
                if (!activeElement.contains(event.target)) {
                    activeElement.blur();
                }
            }
        });

const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const vacancyId = params.get('id');
function back () {
console.log(vacancyId);
window.location.href=`/employer/vacancy/description?id=${vacancyId}`
}

function updateSelect (selectId, value, text) {
    const selectElement = document.getElementById(selectId);
    let optionExists = false;

    // Проверяем, есть ли уже option с таким значением
    for (let i = 0; i < selectElement.options.length; i++) {
        if (selectElement.options[i].text === text) {
            selectElement.selectedIndex = i; // Если найдено, выбираем его
            optionExists = true;
            break;
        }
    }

    // Если option с таким значением не существует, добавляем его
    if (!optionExists) {
        const newOption = new Option(text, value); // Создаем новый элемент option
        selectElement.add(newOption); // Добавляем его в select
        selectElement.selectedIndex = selectElement.options.length - 1; // Выбираем добавленный элемент
    }
}

function setVacancyData (){
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
        document.getElementById("name").value = data.position;
        updateSelect('industry-select', data.industry.id, data.industry.name);
        updateSelect('workSchedule', data.workSchedule, data.workSchedule);
           // Всего 5 строк
    document.getElementById("city-name").value = data.city; // для для всех остальных



         document.getElementById("textarea").value = data.responsibility; // для для всех остальных
         document.getElementById("address").value = data.address;
         if (data.fromSalary!=null) {
         document.getElementById("salaryfrom").value = data.fromSalary;
         }
         if (data.toSalary!=null) {
         document.getElementById("salaryto").value = data.toSalary;
         }
         if (data.distantWork==true) {
         document.getElementById("remoteWork").checked = true;}
         if (data.exp=="Нет опыта") {
         document.getElementById("no-experience").style.backgroundColor='#333333';
         }
         if (data.exp=="от 1 года до 3 лет") {
         document.getElementById("1-3").style.backgroundColor='#333333';
         }
         if (data.exp=="от 3 до 5 лет") {
         document.getElementById("3-5").style.backgroundColor='#333333';
         }
         if (data.exp=="Более 5 лет") {
         document.getElementById("5+").style.backgroundColor='#333333';
         }

    });
}

document.addEventListener('DOMContentLoaded', function(){
    industrySelect()
    populateCitySelect()
    setVacancyData()
});
// Функция для заполнения выпадающего списка
function populateCitySelect() {
   fetch('/vacancy/city')
      .then(response => {
      if (!response.ok) {
           throw new Error(`Ошибка HTTP: ${response.status}`);
      }
      return response.json();
      })
      .then(russianCities => {
          console.log(russianCities);
          const citySelect = document.getElementById('city-select');
           russianCities.forEach(city => {
             const option = document.createElement('option');
             option.value = city;
             option.text = city;
             citySelect.appendChild(option);
           });
      })
}
function industrySelect() {
   fetch('/industry/showall')
      .then(response => {
      if (!response.ok) {
           throw new Error(`Ошибка HTTP: ${response.status}`);
      }
      return response.json();
      })
      .then(industries => {
          const industrySelect = document.getElementById('industry-select');
           industries.forEach(industry => {
             const option = document.createElement('option');
             option.value = industry.id;
             option.text = industry.name;
             industrySelect.appendChild(option);
           });
      })
}


function chooseWorkExperience(button){

    const no_exp = document.getElementById('no-experience');
    const exp_1_3 = document.getElementById('1-3');
    const exp_3_5 = document.getElementById('3-5');
    const exp_5 = document.getElementById('5+');
    no_exp.style.backgroundColor='green';
    exp_1_3.style.backgroundColor='green';
    exp_3_5.style.backgroundColor='green';
    exp_5.style.backgroundColor='green';
    button.style.backgroundColor='#333333';
}





function submit() {
   const name = document.getElementById("name").value;
   const industryId = document.getElementById("industry-select").value;
   const city = document.getElementById("city-name").value;
   const textarea = document.getElementById("textarea").value;
   const address = document.getElementById("address").value;
   let exp = "";
   const no_exp = document.getElementById('no-experience');
   const exp_1_3 = document.getElementById('1-3');
   const exp_3_5 = document.getElementById('3-5');
   const exp_5 = document.getElementById('5+');
   if  (no_exp.style.backgroundColor=="" && exp_1_3.style.backgroundColor==""
   && exp_3_5.style.backgroundColor=="" && exp_5.style.backgroundColor==""  ){exp = ""}

   else if (no_exp.style.backgroundColor!='green') {
   console.log(no_exp.style.backgroundColor)
   exp=no_exp.textContent;
   } else if (exp_1_3.style.backgroundColor!='green') {
   exp=exp_1_3.textContent;}

   else if (exp_3_5.style.backgroundColor!='green') {
   exp=exp_3_5.textContent;}
   else if (exp_5.style.backgroundColor!='green') {
   exp=exp_5.textContent;}

   const salaryfrom = document.getElementById("salaryfrom").value;
   const salaryto = document.getElementById("salaryto").value;
   fetch( `/employer/user/${clientID}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка HTTP: ${response.status}`); // Бросаем ошибку, если ответ не в порядке
            }
            return response.json();
        }).then(employer=>{
        console.log(employer)
        const selectElement = document.getElementById("workSchedule");
        let unfield = [];
        if(name=="") {
            unfield.push ("название вакансии")
        }
        if(industryId=="Укажите отрасль") {
            unfield.push ("сфера деятельности")
        }
        if(city=="Укажите город") {
            unfield.push ("город")
        }
        if(textarea=="") {
            unfield.push ("описание вакансии")
        }
        if(address=="") {
            unfield.push ("адрес")
        }
        if(salaryfrom=="" || salaryto=="") {
            unfield.push ("зарплата")
        }
        if(selectElement.options[selectElement.selectedIndex].text=="Тип занятости") {
            unfield.push ("график работы")
        }
        if(exp == ""){
            unfield.push ("опыт работы")}




             const data = {
                   position: name,
                   industry_id: industryId,
                   city: city,
                   responsibility: textarea,
                   address: address,
                   exp: exp,
                   fromSalary: salaryfrom,
                   toSalary: salaryto,
                   distantWork:document.getElementById("remoteWork").checked,
                   workSchedule: selectElement.options[selectElement.selectedIndex].text,
                   employer_id: employer.employer_id,
             };
              console.log(data);
              if(unfield.length>0) {
                         alert("Пожалуйста, заполните поля: " + unfield.join(", "));
                         return;
                     }

             fetch('/vacancy/'+vacancyId, {
                    method: 'PUT',
                    headers: {
                      'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                }) .then(response => {
                       if (!response.ok) {
                            throw new Error(`Ошибка HTTP: ${response.status}`);
                       }
                       return response.json();
                }).then(data => {
                     alert ("Вакансия успешно обновлена")
                     console.log(data);
                window.location.href='/employer/vacancy/description?id=' + vacancyId;


                })
        })
   }