let clientID = window.Telegram.WebApp.initDataUnsafe.user.id;


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
function windowsLoad(){
industrySelect()
populateCitySelect()
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
    button.style.backgroundColor='#20805E';
}

// Вызываем функцию после загрузки страницы
window.onload = windowsLoad;




function submit() {
   const name = document.getElementById("name").value;
   const industryId = document.getElementById("industry-select").value;
   const city = document.getElementById("city-select").value;
   const textarea = document.getElementById("textarea").value;
   const address = document.getElementById("address").value;
   let exp;
   const no_exp = document.getElementById('no-experience');
   const exp_1_3 = document.getElementById('1-3');
   const exp_3_5 = document.getElementById('3-5');
   const exp_5 = document.getElementById('5+');
   if (no_exp.style.backgroundColor!='green') {
   exp=no_exp.textContent;
   } else if (exp_1_3.style.backgroundColor!='green') {
   exp=exp_1_3.textContent;}
   else if (exp_3_5.style.backgroundColor!='green') {
   exp=exp_3_5.textContent;}
   else if (exp_5.style.backgroundColor!='green') {
   exp=exp_5.textContent;}
   const salaryfrom = document.getElementById("salaryfrom").value;
   const salaryfrom = document.getElementById("salaryto").value;
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
                   workSchedule: document.getElementById("workSchedule").text,
                   employer_id: employer.id,
                 };
             console.log(data);
             fetch('/vacancy', {
                        method: 'POST',
                        headers: {
                          'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(data)
                      })
                   .then(response => {
                       if (!response.ok) {
                            throw new Error(`Ошибка HTTP: ${response.status}`);
                       }
                       return response.json();
                   })
                   .then(data => {
                       console.log(data);

                   })
              })
        }
}