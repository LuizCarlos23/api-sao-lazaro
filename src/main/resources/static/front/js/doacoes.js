let baseUrl = "http://localhost:8080"
const WarehouseTable = {FOOD: 1, PET_FOOD: 2, MEDICINE: 3, CLEANING_MATERIAL: 4}
const DonationType = { GENERAL: 1, MONETARY: 2 }
const MonetaryDonation = 5
const VisibleTable = WarehouseTable.FOOD
const AnimalSpecieNameTranslate = { "DOG": "Cachorro", "CAT": "Gato" }
const AnimalAgeRangeNameTranslate = { "ADULT": "Adulto", "YOUNG": "Jovem" }
const AnimalSizeNameTranslate = { "SMALL": "Pequeno", "MEDIUM": "Médio", "LARGE": "Grande" }
const MedicineTypeNameTranslate = { "Pill": "Comprimido", "Liquid": "Líquido", "Injectable": "Injetável", "Spray": "Spray", "Ointment": "Pomada" }
const DonationTypeNameTranslate = { "FOOD": "Alimento", "PET_FOOD": "Ração", "MEDICINE": "Medicamento", "CLEANING_MATERIAL": "Material de Limpeza"}
const DonationMonetaryTypeTranslate = { "PIX": "PIX", "CARD": "Cartão", "MONEY": "Dinheiro" }
const EnumAnimalSpecie = { "DOG": 0, "CAT": 1}
const EnumAnimalAgeRange = { "ADULT": 0, "YOUNG": 1}
const EnumAnimalSize = { "SMALL": 0, "MEDIUM": 1, "LARGE": 2 }
const EnumMedicineType = { "Pill": 0, "Liquid": 1, "Injectable": 2, "Spray": 3, "Ointment": 4 }

let globalItemsData = []

$(document).ready(() => {
    renderTable(DonationType.GENERAL)
})

$("#btnPesquisar").on("click", async () => {
    let donationType = Number($("#donationType").val())  
    if (!donationType) return
    renderTable(donationType)
})

$("#btnRegisterFoodDonations").on("click", async () => {
    let data = {}
    
    data.name = $("#inputNameFoodDonations").val()
    data.quantity = Number($("#inputFoodQuantityDonations").val())

    let result = await registerDonations(data, WarehouseTable.FOOD)
    if (result) {
        $('#foodDonationsModal').modal('hide');
        $('#formFoodDonations').trigger('reset');
        renderTable(DonationType.GENERAL)
    }
})

$("#btnRegisterCleaningMaterialDonations").on("click", async () => {
    let data = {}
    
    data.name = $("#inputNameCleaningMaterialDonations").val()
    data.quantity = Number($("#inputCleaningMaterialQuantityDonations").val())

    let result = await registerDonations(data, WarehouseTable.CLEANING_MATERIAL)
    if (result) {
        $('#cleaningMaterialDonationsModal').modal('hide');
        $('#formCleaningMaterialDonations').trigger('reset');
        renderTable(DonationType.GENERAL)
    }
})

$("#btnRegisterPetFoodDonations").on("click", async () => {
    let data = {}    

    data.name = $("#inputNamePetFoodDonations").val()
    data.specie = $("#selectAnimalSpeciePetFoodDonations").val()
    data.ageRange = $("#selectAnimalAgeRangePetFoodDonations").val()
    data.animalSize = $("#selectAnimalSizePetFoodDonations").val()
    data.quantityKg = Number($("#inputQuantityPetFoodDonations").val()).toFixed(2)

    let result = await registerDonations(data, WarehouseTable.PET_FOOD)
    if (result) {
        $('#petFoodDonationsModal').modal('hide');
        $('#formPetFoodDonations').trigger('reset');
        renderTable()
    }
})

$("#btnMedicineDonations").on("click", async () => {
    let data = {}

    data.name = $("#inputMedicineNameDonations").val()
    data.type = Number($("#selectMedicineTypeDonations").val())
    data.quantity = Number($("#inputMedicineQuantityDonations").val()).toFixed(2)

    let result = await registerDonations(data, WarehouseTable.MEDICINE)
    if (result) {
        $('#medicineDonationsModal').modal('hide');
        $('#formMedicineDonations').trigger('reset');
        renderTable(DonationType.GENERAL)
    }
})

$("#btnRegisterMonetaryDonations").on("click", async () => {
    let data = {}

    data.type = Number($("#inputTypeMonetary").val())
    data.value = Number($("#inputValueMonetary").val()).toFixed(2)
    data.date = $("#inputDateMonetary").val()

    let result = await registerDonations(data, MonetaryDonation)
    if (result) {
        $('#monetaryDonationsModal').modal('hide');
        $('#formMonataryDonations').trigger('reset');
        renderTable(DonationType.MONETARY)
    }
})

$("#specificDonationType").on("change", (element) => {
    let option =  Number($(element.currentTarget).val())

    switch (option) {
        case WarehouseTable.FOOD:
            $('#foodDonationsModal').modal('show');
            break;
        case WarehouseTable.PET_FOOD:
            $('#petFoodDonationsModal').modal('show');
            break;
        case WarehouseTable.MEDICINE:
            $('#medicineDonationsModal').modal('show');
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            $('#cleaningMaterialDonationsModal').modal('show');
            break;
        case MonetaryDonation:
            $('#monetaryDonationsModal').modal('show');
            break;
    }

    $(element.currentTarget).val(0)
})

async function renderTable(donationType) {
    $("#info").html("")

    let donationItems = await getDonationsItems(donationType)
    globalItemsData = donationItems

    if (donationItems?.length == 0 || !donationItems) {
        $("#info").html("<span class='d-flex justify-content-center p-4'>Nenhum item</span>")
        return
    } 

    switch (donationType) {
        case DonationType.GENERAL:
            $("#tBodyGeneralDonationsItems").html("")
            showTable(DonationType.GENERAL)
            donationItems?.forEach(item => { addItemInTableGeneralDonations(item) })
            break;
        case DonationType.MONETARY:
            $("#tBodyMonetaryDonationsItems").html("")
            showTable(DonationType.MONETARY)
            donationItems?.forEach(item => { addItemInTableMonetaryDonations(item) })
            break;
    }
}
    

async function getDonationsItems(donationType) {
    let donationItems = null
    let url = `${baseUrl}`

    switch (donationType) {
        case DonationType.GENERAL:
            url += "/general_donation/" 
            break;
        case DonationType.MONETARY:
            url += "/monetary_donation/" 
            break;
    }

    await fetch(url)
        .then(result => {
            if (result.status == 200) {
                return result.json()
            } else {
                alert("Ocorreu um error ao buscar os dados das doações!")
            }        
        })
        .then(result => { donationItems = result })
        .catch(err => {
            console.log("Ocorreu um error", err)
            alert("Ocorreu um error interno ao buscar os dados das doações!")
        })

    return donationItems
}

function addItemInTableGeneralDonations(item) {
    let row = `<tr> 
        <td class='align-middle'>${item.id}</td>
        <td class='align-middle'>${item.name}</td>
        <td class='align-middle'>${DonationTypeNameTranslate[item.type]}</td>
        <td class='align-middle'>${AnimalSizeNameTranslate[item.petfoodAnimalSize] ?? ""}</td>
        <td class='align-middle'>${AnimalAgeRangeNameTranslate[item.petfoodAgeRange] ?? ""}</td>
        <td class='align-middle'>${AnimalSpecieNameTranslate[item.petfoodSpecie] ?? ""}</td>
        <td class='align-middle'>${MedicineTypeNameTranslate[item.medicineType] ?? ""}</td>
        <td class='align-middle'>${item.date}</td>
        <td class='align-middle'>${item.quantity}</td>
    </tr>`

    document.getElementById("tBodyGeneralDonationsItems").innerHTML += row
}

function addItemInTableMonetaryDonations(item) {
    let row = `<tr> 
        <td class='align-middle'>${item.id}</td>
        <td class='align-middle'>${DonationMonetaryTypeTranslate[item.type]}</td>
        <td class='align-middle'>${item.date}</td>
        <td class='align-middle'>${Number(item.value).toFixed(2)}</td>
    </tr>`

    document.getElementById("tBodyMonetaryDonationsItems").innerHTML += row
}


async function registerDonations(data, tableOption) {
    let url = `${baseUrl}/general_donation`
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
        case MonetaryDonation:
            url = `${baseUrl}/monetary_donation/`
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
            alert("Ocorreu um erro ao registrar a doação! Verifique os campos.")
        }
    }).catch(err => {
        console.log(err)
        alert("Ocorreu um error interno ao registrar a doação!")
    })

    return registered
}

function showTable(donationType) {
    switch (donationType) {
        case DonationType.GENERAL:
            hideTables([$("#tableMonetaryDonationsItems")])
            $("#tableGeneralDonationsItems").removeClass("d-none")
            break;
        case DonationType.MONETARY:
            hideTables([$("#tableGeneralDonationsItems")])
            $("#tableMonetaryDonationsItems").removeClass("d-none")
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