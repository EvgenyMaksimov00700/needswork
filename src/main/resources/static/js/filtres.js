let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;}
catch(error) {clientID = 159619887}
console.log(clientID)
function vacancy_back() {
window.location.href=`/vacancy/menu?city=${encodeURIComponent(city)}&industry=${encodeURIComponent(industry)}`}
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
          const citySelect = document.getElementById('city-list');
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
          const industrySelect = document.getElementById('industry');
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
companySelect()
}

window.onload = windowsLoad;
function companySelect() {
   fetch('/employer/showall')
      .then(response => {
      if (!response.ok) {
           throw new Error(`Ошибка HTTP: ${response.status}`);
      }
      return response.json();
      })
      .then(employers => {
          const companySelect = document.getElementById('company');
          const employersSet = new Set();
           employers.forEach(employer => {
           if (employer.name != "")
           {employersSet.add(employer.name);
           }});
           employersSet.forEach(employer => {
           const option = document.createElement('option');
           option.value = employer;
           option.text = employer;
           companySelect.appendChild(option);
           });
      })

}
function chooseWorkExperience(button){
if (button.style.backgroundColor=="rgb(0, 136, 204)") {
    button.style.backgroundColor='#333333';
}
else {
    button.style.backgroundColor='#0088cc';}




}
