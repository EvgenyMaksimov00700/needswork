let clientID;
try {
    clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
    function isDesktop() {
            const userAgent = navigator.userAgent.toLowerCase();
            return userAgent.includes("windows") || userAgent.includes("macintosh") || userAgent.includes("linux");
        }
        console.log(isDesktop());
        if (!isDesktop()) {
        document.querySelector(".container").style.marginTop = "90px";
            window.Telegram.WebApp.requestFullscreen();
        }
    }


 catch (error) {
    clientID = 159619887;
}
console.log(clientID);

const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const city = params.get('city');
const industry = params.get('industry');
const company = params.get('company');
const position = params.get('position');
const salary = params.get('salary');
const exp = params.get('exp');
const bus = params.get('bus');
const time = params.get('time');
console.log(city);

const existParams = new URLSearchParams();
const encodeExistParams = new URLSearchParams();

if (city) {
    existParams.append('city', city);
    encodeExistParams.append('city', encodeURIComponent(city));
}
if (industry) {
    existParams.append('industry', industry);
    encodeExistParams.append('industry', encodeURIComponent(industry));
}
if (company) {
    existParams.append('company', company);
    encodeExistParams.append('company', encodeURIComponent(company));
}
if (position) {
    existParams.append('position', position);
    encodeExistParams.append('position', encodeURIComponent(position));
}
if (salary) {
    existParams.append('salary', salary);
    encodeExistParams.append('salary', encodeURIComponent(salary));
}
if (exp) {
    existParams.append('exp', exp);
    encodeExistParams.append('exp', encodeURIComponent(exp));
}
if (bus) {
    existParams.append('bus', bus);
    encodeExistParams.append('bus', encodeURIComponent(bus));
}
if (time) {
    existParams.append('time', time);
    encodeExistParams.append('time', encodeURIComponent(time));
}

document.addEventListener('DOMContentLoaded', function () {
    windowsLoad();

    if (city != null) {
        document.getElementById('city-search').value = city;
    }
    if (industry  != null) {
        document.getElementById('industry').value = industry;
        fetch(`/industry/${industry}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка при получении отрасли');
                }
                return response.json();
            })
            .then(data => {
                if (data && data.name) {
                    document.getElementById('industry-search').value = data.category;
                }
            })
            .catch(error => {
                console.error('Ошибка:', error);
            });
    }
    if (company!=null) {
    document.getElementById('company').value = company;
    }
    if (position != null) {
    document.getElementById('position').value = position;
    }
    if (salary!= null) {
    document.getElementById('income').value = salary;
    }
    if (exp!= null) {
    let expsButton = document.querySelectorAll('.exp');
    const exps = exp.split(',');
    expsButton.forEach(button => {
        if (exps.includes(button.textContent)) {
            button.style.backgroundColor = '#333333';
        }
    });
    }
    if (bus!= null) {
    let workScheduleButton = document.querySelectorAll('.bus');
        const buss = bus.split(',');
        workScheduleButton.forEach(button => {
            if (buss.includes(button.textContent)) {
                button.style.backgroundColor = '#333333';
            }
        });

    }
    if (time!= null) {
    let timeButton = document.querySelectorAll('.time');
        const times = time.split(',');
        timeButton.forEach(button => {
            if (times.includes(button.textContent)) {
                button.style.backgroundColor = '#333333';
            }
        });

    }
});

function vacancy_back() {
    window.location.href = `/vacancy/menu?${existParams.toString()}`;
}

function apply() {
const currentParams = new URLSearchParams();
const currentCity = document.getElementById("city-search").value;
const currentCompany = document.getElementById("company").value;
const currentIndustry = document.getElementById("industry").value;
const currentPosition = document.getElementById("position").value;
const currentSalary = document.getElementById("income").value;
if (currentCity) {
    currentParams.append('city', currentCity);
}
if (currentPosition) {
    currentParams.append('position', currentPosition);
}
if (currentIndustry) {
    currentParams.append('industry', currentIndustry);

}
if (currentCompany) {
    currentParams.append('company', currentCompany);

   }
if (currentSalary) {
    currentParams.append('salary', currentSalary);
        }
    let currentExps = "";
    const exps = document.querySelectorAll(".exp");
    exps.forEach(button =>{
    if (button.style.backgroundColor != "rgb(0, 136, 204)" && button.style.backgroundColor != "") {
    currentExps += button.textContent +",";
    }
    })
    if (currentExps) {
        currentParams.append('exp', currentExps.slice(0,-1));
        }

    let currentBuss = "";
    const buss = document.querySelectorAll(".bus");
    buss.forEach(button =>{
    if (button.style.backgroundColor != "rgb(0, 136, 204)" && button.style.backgroundColor != "") {
    currentBuss += button.textContent +",";
    }
    })

    if (currentBuss) {
        currentParams.append('bus', currentBuss.slice(0,-1));
        }


    let currentTime = "";
    const time = document.querySelectorAll(".time");
    time.forEach(button =>{
    if (button.style.backgroundColor != "rgb(0, 136, 204)" && button.style.backgroundColor != "") {
    currentTime += button.textContent +",";
    }
    })
    if (currentTime) {
        currentParams.append('time', currentTime.slice(0,-1));
        }




    window.location.href = `/vacancy/menu?${currentParams.toString()}`;
}

function chooseTime(button){

  const time = document.querySelectorAll(".time");
  if (button.style.backgroundColor == "rgb(0, 136, 204)" || button.style.backgroundColor == "")  {
  time.forEach(b =>{ b.style.backgroundColor = "#0088cc"});
  button.style.backgroundColor='#333333';
  }
  else {
  time.forEach(b =>{ b.style.backgroundColor = "#0088cc"});
  }
}

document.addEventListener('touchstart', function (event) {
    const activeElement = document.activeElement;

    if (activeElement && (activeElement.tagName === 'INPUT' || activeElement.tagName === 'TEXTAREA')) {
        // Проверяем, что нажали вне поля ввода
        if (!activeElement.contains(event.target)) {
            activeElement.blur();
        }
    }
});

// Функция для заполнения выпадающего списка городов
function populateCitySelect() {
    fetch('/vacancy/city')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка HTTP: ${response.status}`);
            }
            return response.json();
        })
        .then(russianCities => {

            const citySelect = document.getElementById('city');
            russianCities.forEach(city => {
                const option = document.createElement('option');
                option.value = city.name;
                option.text = city.name;
                citySelect.appendChild(option);
            });
            if (city != null) {
                                    const cityOption = Array.from(citySelect.options).find(option => option.value === city);
                                    if (cityOption) {
                                        citySelect.value = city;
                                    }
                                }
        })


        .catch(error => console.error('Ошибка загрузки городов:', error));
}

// Функция для заполнения выпадающего списка отраслей
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
                option.text = industry.category;
                industrySelect.appendChild(option);
            });

            // Установим выбранное значение после загрузки опций
            if (industry != null) {
                const industryOption = Array.from(industrySelect.options).find(option => option.value === industry);
                if (industryOption) {
                    industrySelect.value = industry;
                }
            }
        })
        .catch(error => console.error('Ошибка загрузки отраслей:', error));
}

// Функция для заполнения выпадающего списка компаний
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
                if (employer.name != "") {
                    employersSet.add(employer.name);
                }
            });
            employersSet.forEach(employer => {
                const option = document.createElement('option');
                option.value = employer;
                option.text = employer;
                companySelect.appendChild(option);
            });

            // Установим выбранное значение после загрузки опций

            if (company != null) {
                const companyOption = Array.from(companySelect.options).find(option => option.value === company);


                if (companyOption) {
                    companySelect.value = company;
                }
            }
        })
        .catch(error => console.error('Ошибка загрузки компаний:', error));
}
function initSearchableSelect(wrapper) {
  const select = wrapper.querySelector('select');
  const input  = wrapper.querySelector('input');
  const list   = wrapper.querySelector('.options-container');

  // Сохраняем исходные опции (обновляется при каждом вызове)
  function getOpts() {
    return Array.from(select.options).map(o => ({
      value: o.value,
      label: o.text
    }));
  }

  // Рендерим отфильтрованные варианты
  function showOptions() {
    const term = input.value.trim().toLowerCase();
    const opts = getOpts();
    list.innerHTML = '';
    opts
      .filter(o => o.label.toLowerCase().includes(term))
      .forEach(o => {
        const div = document.createElement('div');
        div.className = 'option';
        div.textContent = o.label;
        div.dataset.value = o.value;
        list.appendChild(div);
      });
    list.style.display = list.children.length ? 'block' : 'none';
  }

  // События
  input.addEventListener('input', showOptions);
  input.addEventListener('focus', showOptions);

  // При клике на вариант: установить select и input, скрыть список
  list.addEventListener('click', e => {
    if (!e.target.classList.contains('option')) return;
    const { value } = e.target.dataset;
    const { label } = getOpts().find(o => o.value === value);
    select.value = value;
    input.value = label;
    list.style.display = 'none';
  });

  // Клик вне компонента — скрыть список
  document.addEventListener('click', e => {
    if (!wrapper.contains(e.target)) {
      list.style.display = 'none';
    }
  });

  // Если в URL был уже выбран город — показать его в поле поиска
  const pre = select.value;
  if (pre) {
    const match = getOpts().find(o => o.value === pre);
    if (match) input.value = match.label;
  }
}


// Функция для выполнения загрузки данных
function windowsLoad() {
    industrySelect();
    populateCitySelect();
   // companySelect();
   setTimeout(() => {
       document.querySelectorAll('.searchable-select')
         .forEach(initSearchableSelect);
     }, 300);
}

function chooseWorkExperience(button) {
    console.log(button.style.backgroundColor);
    if (button.style.backgroundColor == "rgb(0, 136, 204)" || button.style.backgroundColor == "") {
        button.style.backgroundColor = '#333333';
    } else {
        button.style.backgroundColor = '#0088cc';
    }
}
