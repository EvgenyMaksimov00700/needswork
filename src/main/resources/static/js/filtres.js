let clientID;
try {
    clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
} catch (error) {
    clientID = 159619887;
}
console.log(clientID);

const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const city = params.get('city');
const industry = params.get('industry');
const company = params.get('company');
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

document.addEventListener('DOMContentLoaded', function () {
    windowsLoad();

    if (city != null) {
        document.getElementById('city').value = city;
    }
});

function vacancy_back() {
    window.location.href = `/vacancy/menu?${existParams.toString()}`;
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
            console.log(russianCities);
            const citySelect = document.getElementById('city-list');
            russianCities.forEach(city => {
                const option = document.createElement('option');
                option.value = city;
                option.text = city;
                citySelect.appendChild(option);
            });
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
                option.text = industry.name;
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

// Функция для выполнения загрузки данных
function windowsLoad() {
    industrySelect();
    populateCitySelect();
    companySelect();
}

function chooseWorkExperience(button) {
    console.log(button.style.backgroundColor);
    if (button.style.backgroundColor == "rgb(0, 136, 204)" || button.style.backgroundColor == "") {
        button.style.backgroundColor = '#333333';
    } else {
        button.style.backgroundColor = '#0088cc';
    }
}
