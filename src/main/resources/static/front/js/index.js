// obter data
    const hoje = new Date();
    const dia = hoje.getDate();

    const meses = ["janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"];
    const mes = meses[hoje.getMonth()];

    const texto = `Hoje é dia ${dia} de ${mes}.`;
    document.getElementById("dataAtual").innerText = texto;

document.getElementById("btn_bar").addEventListener("click", function() {
    document.getElementById("mySidebar").classList.toggle("show");
});

let baseUrl = "http://localhost:8080"
const WarehouseTable = {FOOD: 1, PET_FOOD: 2, MEDICINE: 3, CLEANING_MATERIAL: 4}
const VisibleTable = WarehouseTable.FOOD
const AnimalSpecieNameTranslate = { "DOG": "Cachorro", "CAT": "Gato" }
const AnimalAgeRangeNameTranslate = { "ADULT": "Adulto", "YOUNG": "Jovem" }
const AnimalSizeNameTranslate = { "SMALL": "Pequeno", "MEDIUM": "Médio", "LARGE": "Grande" }
const MedicineTypeNameTranslate = { "Pill": "Comprimido", "Liquid": "Líquido", "Injectable": "Injetável", "Spray": "Spray", "Ointment": "Pomada" }
const EnumAnimalSpecie = { "DOG": 0, "CAT": 1}
const EnumAnimalAgeRange = { "ADULT": 0, "YOUNG": 1}
const EnumAnimalSize = { "SMALL": 0, "MEDIUM": 1, "LARGE": 2 }
const EnumMedicineType = { "Pill": 0, "Liquid": 1, "Injectable": 2, "Spray": 3, "Ointment": 4 }

let globalItemsData = []

$(document).ready(() => {
    renderTable(WarehouseTable.FOOD)
})

$("#btnPesquisar").on("click", async () => {
    let warehouseType = Number($("#warehouseType").val())
    if (warehouseType == 0) return
    renderTable(warehouseType)
})

async function renderTable(tableOption) {
    $("#info").html("")
    $("#tBodyFood").html("")
    $("#tBodyPetFood").html("")
    $("#tBodyMedicine").html("")
    $("#tBodyCleaningMaterial").html("")

    let warehouseItems = await getWarehouse(tableOption)
    globalItemsData = warehouseItems

    if (warehouseItems?.length == 0 || !warehouseItems) {
        $("#info").html("<span class='d-flex justify-content-center p-4'>Nenhum item</span>")
        return
    }

    warehouseItems?.forEach(item => { (item) })

    switch (tableOption) {
        case WarehouseTable.FOOD:
            warehouseItems?.forEach(animal => { addFoodInTable(animal) })
            break;
        case WarehouseTable.PET_FOOD:
            warehouseItems?.forEach(item => { addPetFoodInTable(item) })
            break;
        case WarehouseTable.MEDICINE:
            warehouseItems?.forEach(item => { addMedicineInTable(item) })
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            warehouseItems?.forEach(item => { addCleaningMaterialInTable(item) })
            break;
    }

    $("#warehouseType").val(tableOption)
    showTable(tableOption)
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

function showTable(tableOption) {
    switch (tableOption) {
        case WarehouseTable.FOOD:
            hideTables([$("#tablePetFood"), $("#tableMedicine"), $("#tableCleaningMaterial")])
            $("#tableFood").removeClass("d-none")
            break;
        case WarehouseTable.PET_FOOD:
            hideTables([$("#tableFood"), $("#tableMedicine"), $("#tableCleaningMaterial")])
            $("#tablePetFood").removeClass("d-none")
            break;
        case WarehouseTable.MEDICINE:
            hideTables([$("#tableFood"), $("#tableCleaningMaterial"), $("#tablePetFood")])
            $("#tableMedicine").removeClass("d-none")
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            hideTables([$("#tableFood"), $("#tablePetFood"), $("#tableMedicine")])
            $("#tableCleaningMaterial").removeClass("d-none")
            break;
    }
}

function hideTables(tables) {
    tables.forEach(element => {
        if (!element.hasClass("d-none")){
            element.addClass("d-none")
        }
    });
}
