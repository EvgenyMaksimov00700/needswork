

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
    button.style.backgroundColor='yellow';
}

// Вызываем функцию после загрузки страницы
window.onload = windowsLoad;

