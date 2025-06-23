let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
if (window.history.length > 1) {
    window.Telegram.WebApp.BackButton.show();
    window.Telegram.WebApp.BackButton.onClick(() => {
    window.history.back();
});
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



document.addEventListener('touchstart', function(event) {
            const activeElement = document.activeElement;

            if (activeElement && (activeElement.tagName === 'INPUT' || activeElement.tagName === 'TEXTAREA')) {
                // Проверяем, что нажали вне поля ввода
                if (!activeElement.contains(event.target)) {
                    activeElement.blur();
                }
            }
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
             option.value = city.name;
             option.text = city.name;
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
             option.text = industry.category;
             industrySelect.appendChild(option);
           });
      })
}
function windowsLoad(){
industrySelect()
populateCitySelect()
}



function chooseWorkExperience(button){

    const no_exp = document.getElementById('no-experience');
    const exp_1_3 = document.getElementById('1-3');
    const exp_3_5 = document.getElementById('3-5');
    const exp_5 = document.getElementById('5+');
    no_exp.style.backgroundColor='#006699';
    exp_1_3.style.backgroundColor='#006699';
    exp_3_5.style.backgroundColor='#006699';
    exp_5.style.backgroundColor='#006699';
    button.style.backgroundColor='#333333';
}

// Вызываем функцию после загрузки страницы
window.onload = windowsLoad;




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

   else if (no_exp.style.backgroundColor!='#006699') {
   console.log(no_exp.style.backgroundColor)
   exp=no_exp.textContent;
   } else if (exp_1_3.style.backgroundColor!='#006699') {
   exp=exp_1_3.textContent;}

   else if (exp_3_5.style.backgroundColor!='#006699') {
   exp=exp_3_5.textContent;}
   else if (exp_5.style.backgroundColor!='#006699') {
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

             fetch('/vacancy', {
                    method: 'POST',
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
                     alert ("Вакансия успешно создана")
                     console.log(data);
                window.location.href="/employer/lk/";


                })
        })
   }