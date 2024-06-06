let baseUrl = "http://localhost:8080"
const WarehouseTable = {FOOD: 1, PET_FOOD: 2, MEDICINE: 3, CLEANING_MATERIAL: 4}
const VisibleTable = WarehouseTable.FOOD
const AnimalSpecieNameTranslate = { "DOG": "Cachorro", "CAT": "Gato" }
const AnimalAgeRangeNameTranslate = { "ADULT": "Adulto", "YOUNG": "Jovem" }
const AnimalSizeNameTranslate = { "SMALL": "Pequeno", "MEDIUM": "Médio", "LARGE": "Grande" }
const MedicineTypeNameTranslate = { "Pill": "Comprimido", "Liquid": "Líquido", "Injectable": "Injetável", "Spray": "Spray", "Ointment": "Pomada" }
const ShoppingTypeNameTranslate = { "FOOD": "Alimento", "PET_FOOD": "Ração", "MEDICINE": "Medicamento", "CLEANING_MATERIAL": "Material de Limpeza"}
const EnumAnimalSpecie = { "DOG": 0, "CAT": 1}
const EnumAnimalAgeRange = { "ADULT": 0, "YOUNG": 1}
const EnumAnimalSize = { "SMALL": 0, "MEDIUM": 1, "LARGE": 2 }
const EnumMedicineType = { "Pill": 0, "Liquid": 1, "Injectable": 2, "Spray": 3, "Ointment": 4 }

let globalItemsData = []

$(document).ready(() => {
    renderTable()
})

$("#btnRegisterFoodShopping").on("click", async () => {
    let data = {}
    
    data.name = $("#inputNameFoodShopping").val()
    data.quantity = Number($("#inputFoodQuantityShopping").val())
    data.value = Number($("#inputFoodValueShopping").val())

    let result = await registerShopping(data, WarehouseTable.FOOD)
    if (result) {
        $('#foodShoppingModal').modal('hide');
        $('#formFoodShopping').trigger('reset');
        renderTable()
    }
})

$("#btnRegisterCleaningMaterialShopping").on("click", async () => {
    let data = {}
    
    data.name = $("#inputNameCleaningMaterialShopping").val()
    data.quantity = Number($("#inputCleaningMaterialQuantityShopping").val())
    data.value = Number($("#inputCleaningMaterialValueShopping").val())

    let result = await registerShopping(data, WarehouseTable.CLEANING_MATERIAL)
    if (result) {
        $('#cleaningMaterialShoppingModal').modal('hide');
        $('#formCleaningMaterialShopping').trigger('reset');
        renderTable()
    }
})

$("#btnRegisterPetFoodShopping").on("click", async () => {
    let data = {}    

    data.name = $("#inputNamePetFoodShopping").val()
    data.petfoodSpecie = $("#selectAnimalSpeciePetFoodShopping").val()
    data.petfoodAgeRange = $("#selectAnimalAgeRangePetFoodShopping").val()
    data.petfoodAnimalSize = $("#selectAnimalSizePetFoodShopping").val()
    data.quantity = Number($("#inputQuantityPetFoodShopping").val()).toFixed(2)
    data.value = Number($("#inputValuePetFoodShopping").val()).toFixed(2)

    let result = await registerShopping(data, WarehouseTable.PET_FOOD)
    if (result) {
        $('#petFoodShoppingModal').modal('hide');
        $('#formPetFoodShopping').trigger('reset');
        renderTable()
    }
})

$("#btnMedicineShopping").on("click", async () => {
    let data = {}

    data.name = $("#inputMedicineNameShopping").val()
    data.medicineType = Number($("#selectMedicineTypeShopping").val())
    data.quantity = Number($("#inputMedicineQuantityShopping").val()).toFixed(2)
    data.value = Number($("#inputMedicineValueShopping").val()).toFixed(2)

    let result = await registerShopping(data, WarehouseTable.MEDICINE)
    if (result) {
        $('#medicineShoppingModal').modal('hide');
        $('#formMedicineShopping').trigger('reset');
        renderTable()
    }
})

$("#shoppingType").on("change", (element) => {
    let option =  Number($(element.currentTarget).val())

    switch (option) {
        case WarehouseTable.FOOD:
            $('#foodShoppingModal').modal('show');
            break;
        case WarehouseTable.PET_FOOD:
            $('#petFoodShoppingModal').modal('show');
            break;
        case WarehouseTable.MEDICINE:
            $('#medicineShoppingModal').modal('show');
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            $('#cleaningMaterialShoppingModal').modal('show');
            break;
    }

    $(element.currentTarget).val(0)
})

async function renderTable() {
    $("#info").html("")
    $("#tBodyShoppingItems").html("")

    let shoppingItems = await getShoppingItems()
    globalItemsData = shoppingItems

    if (shoppingItems?.length == 0 || !shoppingItems) {
        $("#info").html("<span class='d-flex justify-content-center p-4'>Nenhum item</span>")
        return
    } 

    shoppingItems?.forEach(item => { addItemInTable(item) })
}

async function getShoppingItems() {
    let shoppingItems = null
    let url = `${baseUrl}/shopping/`

    await fetch(url)
        .then(result => {
            if (result.status == 200) {
                return result.json()
            } else {
                alert("Ocorreu um error ao buscar os dados das compras!")
            }        
        })
        .then(result => { shoppingItems = result })
        .catch(err => {
            console.log("Ocorreu um error", err)
            alert("Ocorreu um error interno ao buscar os dados das compras!")
        })

    return shoppingItems
}

function addItemInTable(item) {
    let row = `<tr> 
        <td class='align-middle py-3'>${item.id}</td>
        <td class='align-middle py-3'>${item.name}</td>
        <td class='align-middle py-3 text-center'>${ShoppingTypeNameTranslate[item.type]}</td>
        <td class='align-middle py-3 text-center'>${AnimalSizeNameTranslate[item.petfoodAnimalSize] ?? ""}</td>
        <td class='align-middle py-3 text-center'>${AnimalAgeRangeNameTranslate[item.petfoodAgeRange] ?? ""}</td>
        <td class='align-middle py-3 text-center'>${AnimalSpecieNameTranslate[item.petfoodSpecie] ?? ""}</td>
        <td class='align-middle py-3 text-center'>${MedicineTypeNameTranslate[item.medicineType] ?? ""}</td>
        <td class='align-middle py-3 text-center'>${item.date}</td>
        <td class='align-middle py-3 text-center'>${item.quantity}</td>
        <td class='align-middle py-3'>${Number(item.value).toFixed(2)}</td>
    </tr>`

    document.getElementById("tBodyShoppingItems").innerHTML += row
}


async function registerShopping(data, tableOption) {
    let url = `${baseUrl}/shopping`
    let registered = false

    switch (tableOption) {
        case WarehouseTable.FOOD:
            url += "/food"
            break;
        case WarehouseTable.PET_FOOD:
            url += "/pet_food"
            break;
        case WarehouseTable.MEDICINE:
            url += "/medicine"
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            url += "/cleaning_material"
            break;
    }

    await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    }).then(response => {
        if (response.status == 201) {
            registered = true
        } else {
            alert("Ocorreu um erro ao registrar a compra! Verifique os campos.")
        }
    }).catch(err => {
        console.log(err)
        alert("Ocorreu um error interno ao registrar a compra!")
    })

    return registered
}