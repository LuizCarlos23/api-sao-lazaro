// obter data
const hoje = new Date();
const dia = hoje.getDate();

const meses = ["janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"];
const mes = meses[hoje.getMonth()];

const texto = `Hoje é dia ${dia} de ${mes}.`;
document.getElementById("dataAtual").innerText = texto;

// abrir sidebar
document.getElementById("btn_bar").addEventListener("click", function() {
    document.getElementById("mySidebar").classList.toggle("show");
});

let baseUrl = "http://localhost:8080"
const animalsTable = { SHELTER: 1, REGISTERED: 2, ADOPTED: 3, DECEASE: 4 };
const WarehouseTable = {FOOD: 1, PET_FOOD: 2, MEDICINE: 3, CLEANING_MATERIAL: 4}
const AnimalSpecieNameTranslate = { "DOG": "Cachorro", "CAT": "Gato" }
const AnimalAgeRangeNameTranslate = { "ADULT": "Adulto", "YOUNG": "Jovem" }
const AnimalSizeNameTranslate = { "SMALL": "Pequeno", "MEDIUM": "Médio", "LARGE": "Grande" }
const MedicineTypeNameTranslate = { "Pill": "Comprimido", "Liquid": "Líquido", "Injectable": "Injetável", "Spray": "Spray", "Ointment": "Pomada" }

$(document).ready(() => {
    renderWarehouseTable(WarehouseTable.FOOD)
})

$(document).ready(() => {
    renderAnimalTable(animalsTable.SHELTER);
});

$("#btnPesquisar").on("click", async () => {
    let warehouseType = Number($("#warehouseType").val())
    if (warehouseType == 0) return
    renderWarehouseTable(warehouseType)
})

$("#btnPesquisarAnimal").on("click", async () => {
    let animalStatus = Number($("#animalStatus").val());
    if (animalStatus == 0) return;
    renderAnimalTable(animalStatus);
});

async function renderWarehouseTable(tableOption) {
    $("#info").html("")
    $("#tBodyFood").html("")
    $("#tBodyPetFood").html("")
    $("#tBodyMedicine").html("")
    $("#tBodyCleaningMaterial").html("")

    let warehouseItems = await getWarehouse(tableOption)

    if (warehouseItems?.length == 0 || !warehouseItems) {
        $("#info").html("<span class='d-flex justify-content-center p-4'>Nenhum item</span>")
        return
    }

    switch (tableOption) {
        case WarehouseTable.FOOD:
            warehouseItems.forEach(item => { addFoodInTable(item) })
            break;
        case WarehouseTable.PET_FOOD:
            warehouseItems.forEach(item => { addPetFoodInTable(item) })
            break;
        case WarehouseTable.MEDICINE:
            warehouseItems.forEach(item => { addMedicineInTable(item) })
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            warehouseItems.forEach(item => { addCleaningMaterialInTable(item) })
            break;
    }

    $("#warehouseType").val(tableOption)
    showWarehouseTable(tableOption)
}

async function renderAnimalTable(tableOption) {
    $("#infoAnimal").html("");
    $("#tBodyAnimalsInShelter").html("");
    $("#tBodyAnimalsRegistered").html("");
    $("#tBodyAnimalsAdopeteds").html("");
    $("#tBodyAnimalsDeceases").html("");

    let animals = await getAnimals(tableOption);

    if (animals?.length == 0 || !animals) {
        $("#infoAnimal").html("<span class='d-flex justify-content-center p-4'>Nenhum animal</span>");
        return;
    }

    switch (tableOption) {
        case animalsTable.SHELTER:
            animals.forEach(animal => { addAnimalInTableShelter(animal); });
            break;
        case animalsTable.REGISTERED:
            animals.forEach(animal => { addAnimalInTableRegistered(animal); });
            break;
        case animalsTable.ADOPTED:
            animals.forEach(animal => { addAnimalInTableAdopeteds(animal); });
            break;
        case animalsTable.DECEASE:
            animals.forEach(animal => { addAnimalInTableDeceases(animal); });
            break;
    }

    $("#animalStatus").val(tableOption);
    showAnimalTable(tableOption);
}

async function getWarehouse(tableOption) {
    let warehouseItems = null
    let url = `${baseUrl}`

    switch (tableOption) {
        case WarehouseTable.PET_FOOD:
            url += "/pet_food/"
            break;
        case WarehouseTable.MEDICINE:
            url += "/medicine/"
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            url += "/cleaningmaterial/"
            break;
        default:
            url += "/food/"
            break;
    }

    await fetch(url)
        .then(result => {
            if (result.status == 200) {
                return result.json()
            } else {
                alert("Ocorreu um error ao buscar os itens!")
            }
        })
        .then(result => { warehouseItems = result })
        .catch(err => {
            console.log("Ocorreu um error", err)
            alert("Ocorreu um error ao buscar os itens!")
        })

    return warehouseItems
}

async function getAnimals(tableOption) {
    let animals = null;
    let url = `${baseUrl}/animal/`;

    switch (tableOption) {
        case animalsTable.SHELTER:
            url += "shelter";
            break;
        case animalsTable.ADOPTED:
            url += "adopteds";
            break;
        case animalsTable.DECEASE:
            url += "deceaseds";
            break;
    }

    await fetch(url)
        .then(result => {
            if (result.status == 200) {
                return result.json();
            } else {
                alert("Ocorreu um error ao buscar os animais!");
            }        
        })
        .then(result => { animals = result; })
        .catch(err => {
            console.log("Ocorreu um error", err);
            alert("Ocorreu um error ao buscar os animais!");
        });

    return animals;
}

function addFoodInTable(item) {
    let row = `<tr>
        <td class='align-middle'>${item.id}</td>
        <td class='align-middle'>${item.name}</td>
        <td class='align-middle'>${item.quantity}</td>
    </tr>`

    document.getElementById("tBodyFood").innerHTML += row
}

function addPetFoodInTable(item) {
    let row = `<tr>
        <td class='align-middle'>${item.id}</td>
        <td class='align-middle'>${item.name}</td>
        <td class='align-middle'>${AnimalSpecieNameTranslate[item.specie]}</td>
        <td class='align-middle'>${AnimalAgeRangeNameTranslate[item.ageRange]}</td>
        <td class='align-middle'>${AnimalSizeNameTranslate[item.animalSize]}</td>
        <td class='align-middle'>${Number(item.quantityKg).toFixed(2)}</td>
    </tr>`

    document.getElementById("tBodyPetFood").innerHTML += row
}

function addAnimalInTableShelter(animal) {
    let row = `<tr>
        <td class='align-middle py-3'>${animal.registeredAnimal.entryDate}</td>
        <td class='align-middle'>${animal.registeredAnimal.race}</td>
        <td class='align-middle'>${animal.location}</td>
        <td class='align-middle'>${animal.registeredAnimal.anamnesis}</td>
        <td class='align-middle justify-content-end pe-5'>
            <select class="form-select form-control" aria-label="Ações" onChange="animalShelterActions(this, ${animal.registeredAnimal.id})">
                <option value="0" selected disabled>Ações</option>
                <option value="1">Adotado</option>
                <option value="2">Falecido</option>
            </select>
        </td>
    </tr>`;

    document.getElementById("tBodyAnimalsInShelter").innerHTML += row;
}

function addAnimalInTableRegistered(animal) {
    let row = `<tr>
        <td class='align-middle py-3'>${animal.entryDate}</td>
        <td class='align-middle'>${animal.race}</td>
        <td class='align-middle'>${animal.anamnesis}</td>
        <td class='align-middle justify-content-end pe-5'>
            <select class="form-select form-control" aria-label="Ações" onChange="animalRegisteredActions(this, ${animal.id})">
                <option value="0" selected disabled>Ações</option>
                <option value="1">Remover</option>
            </select>
        </td>
    </tr>`;

    document.getElementById("tBodyAnimalsRegistered").innerHTML += row;
}

function addAnimalInTableAdopeteds(animal) {
    let element = document.createElement("input");
    element.value = animal.adopterCpf;
    $(element).mask("000.000.000-00");
    let adopterCpfFormated = $(element).val();
    element.value = animal.adopterNumber;
    $(element).mask("(00) 00000-0000");
    let adopterNumberFormated = $(element).val();

    let row = `<tr>
        <td class='align-middle py-3'>${animal.registeredAnimal.entryDate}</td>
        <td class='align-middle'>${animal.registeredAnimal.race}</td>
        <td class='align-middle'>${animal.registeredAnimal.anamnesis}</td>
        <td class='align-middle'>${animal.adoptionDate}</td>
        <td class='align-middle'>${animal.adopterName}</td>
        <td class='align-middle'>${adopterNumberFormated}</td>
        <td class='align-middle'>${adopterCpfFormated}</td>
    </tr>`;

    document.getElementById("tBodyAnimalsAdopeteds").innerHTML += row;
}

function addAnimalInTableDeceases(animal) {
    let row = `<tr>
        <td class='align-middle py-3'>${animal.registeredAnimal.entryDate}</td>
        <td class='align-middle'>${animal.registeredAnimal.race}</td>
        <td class='align-middle'>${animal.registeredAnimal.anamnesis}</td>
        <td class='align-middle'>${animal.deceaseDate}</td>
        <td class='align-middle'>${animal.reason}</td>
    </tr>`;

    document.getElementById("tBodyAnimalsDeceases").innerHTML += row;
}

function showWarehouseTable(tableOption) {
    hideWarehouseTables();

    switch (tableOption) {
        case WarehouseTable.FOOD:
            $("#tableFood").removeClass("d-none");
            break;
        case WarehouseTable.PET_FOOD:
            $("#tablePetFood").removeClass("d-none");
            break;
        case WarehouseTable.MEDICINE:
            $("#tableMedicine").removeClass("d-none");
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            $("#tableCleaningMaterial").removeClass("d-none");
            break;
    }
}

function hideWarehouseTables() {
    $("#tableFood").addClass("d-none");
    $("#tablePetFood").addClass("d-none");
    $("#tableMedicine").addClass("d-none");
    $("#tableCleaningMaterial").addClass("d-none");
}

function showAnimalTable(tableOption) {
    hideAnimalTables();

    switch (tableOption) {
        case animalsTable.SHELTER:
            $("#tableAnimalsInShelter").removeClass("d-none");
            break;
        case animalsTable.REGISTERED:
            $("#tableAnimalsRegistered").removeClass("d-none");
            break;
        case animalsTable.ADOPTED:
            $("#tableAnimalsAdopeteds").removeClass("d-none");
            break;
        case animalsTable.DECEASE:
            $("#tableAnimalsDeceases").removeClass("d-none");
            break;
    }
}

function hideAnimalTables() {
    $("#tableAnimalsInShelter").addClass("d-none");
    $("#tableAnimalsRegistered").addClass("d-none");
    $("#tableAnimalsAdopeteds").addClass("d-none");
    $("#tableAnimalsDeceases").addClass("d-none");
}

function addMedicineInTable(item) {
    let row = `<tr>
        <td class='align-middle'>${item.id}</td>
        <td class='align-middle'>${item.name}</td>
        <td class='align-middle'>${MedicineTypeNameTranslate[item.type]}</td>
        <td class='align-middle'>${item.quantity}</td>
    </tr>`

    document.getElementById("tBodyMedicine").innerHTML += row
}

function addCleaningMaterialInTable(item) {
    let row = `<tr>
        <td class='align-middle'>${item.id}</td>
        <td class='align-middle'>${item.name}</td>
        <td class='align-middle'>${item.quantity}</td>
    </tr>`

    document.getElementById("tBodyCleaningMaterial").innerHTML += row
}