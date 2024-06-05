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

$("#btnRegisterWriteOff").on("click", async () => {
    let data = {}
    let tableOption = Number($("#writeOffTableOption").val())
    
    data.id = Number($("#writeOffItemId").val())
    data.quantity = $("#inputWriteOffQuantity").val()

    let result = await registerWarehouseItemWriteOff(data.id, data.quantity, tableOption)
    if (result) {
        $('#writeOffModal').modal('hide');
        $('#formWriteOff').trigger('reset');
        renderTable(tableOption)
    }
})

$("#btnRegisterGenericEdit").on("click", async () => {
    let data = {}
    let tableOption = Number($("#tableOptionEdit").val())
    
    let id = Number($("#itemIdEdit").val())
    data.name = $("#inputNameItemEdit").val()
    data.quantity = Number($("#inputQuantityItemEdit").val())

    let result = await regiterGenericEdit(id, data, tableOption)
    if (result) {
        $('#genericEditModal').modal('hide');
        $('#formGenericEdit').trigger('reset');
        renderTable(tableOption)
    }
})

$("#btnRegisterPetFoodEdit").on("click", async () => {
    let data = {}    
    let id = Number($("#petFoodIdEdit").val())

    data.name = $("#inputNamePetFoodEdit").val()
    data.specie = $("#selectAnimalSpeciePetFoodEdit").val()
    data.ageRange = $("#selectAnimalAgeRangePetFoodEdit").val()
    data.animalSize = $("#selectAnimalSizePetFoodEdit").val()
    data.quantityKg = Number($("#inputQuantityPetFoodEdit").val()).toFixed(2)

    let result = await regiterGenericEdit(id, data, WarehouseTable.PET_FOOD)
    if (result) {
        $('#petFoodEditModal').modal('hide');
        $('#formPetFoodEdit').trigger('reset');
        renderTable(WarehouseTable.PET_FOOD)
    }
})

$("#btnMedicineEdit").on("click", async () => {
    let data = {}    
    let id = Number($("#medicineIdEdit").val())

    data.name = $("#inputMedicineNameEdit").val()
    data.type = $("#selectMedicineTypeEdit").val()
    data.quantity = Number($("#inputMedicineQuantityEdit").val()).toFixed(2)

    let result = await regiterGenericEdit(id, data, WarehouseTable.MEDICINE)
    if (result) {
        $('#medicineEditModal').modal('hide');
        $('#formMedicineEdit').trigger('reset');
        renderTable(WarehouseTable.MEDICINE)
    }
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
                alert("Ocorreu um error ao buscar os animais!")
            }        
        })
        .then(result => { warehouseItems = result })
        .catch(err => {
            console.log("Ocorreu um error", err)
            alert("Ocorreu um error ao buscar os animais!")
        })

    return warehouseItems
}

async function registerWarehouseItemWriteOff(id, quantity, tableOption){
    let url = `${baseUrl}`
    let updated = false
    switch (tableOption) {
        case WarehouseTable.FOOD:
            url += "/food/writeoff"
            break;
        case WarehouseTable.PET_FOOD:
            url += "/pet_food/writeoff"
            break;
        case WarehouseTable.MEDICINE:
            url += "/medicine/writeoff"
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            url += "/cleaningmaterial/writeoff"
            break;
    }

    await fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({id, quantity})
    })
        .then(result => {
            if (result.status == 200) {
                updated = true
            } else {
                alert("Ocorreu um error ao realizar a baixa do item! Verifique os campos.")
            }        
        })
        .then(result => { warehouseItems = result })
        .catch(err => {
            console.log("Ocorreu um error", err)
            alert("Ocorreu um error interno ao realizar a baixa do item!")
        })

    return updated
}

async function regiterGenericEdit(id, data, tableOption){
    let url = `${baseUrl}/`
    let updated = false
    switch (tableOption) {
        case WarehouseTable.FOOD:
            url += "food/"
            break;
        case WarehouseTable.PET_FOOD:
            url += "pet_food/"
            break;
        case WarehouseTable.MEDICINE:
            url += "medicine/"
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            url += "cleaningmaterial/"
            break;
    }

    await fetch(url + id, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(result => {
            if (result.status == 200) {
                updated = true
            } else {
                alert("Ocorreu um error ao realizar a edição do item! Verifique os campos.")
            }        
        })
        .then(result => { warehouseItems = result })
        .catch(err => {
            console.log("Ocorreu um error", err)
            alert("Ocorreu um error interno ao realizar a edição do item!")
        })

    return updated
}

async function removeWarehouseItem(id, tableOption) {
    let url = `${baseUrl}/`
    let removed = false
    switch (tableOption) {
        case WarehouseTable.FOOD:
            url += "food/"
            break;
        case WarehouseTable.PET_FOOD:
            url += "pet_food/"
            break;
        case WarehouseTable.MEDICINE:
            url += "medicine/"
            break;
        case WarehouseTable.CLEANING_MATERIAL:
            url += "cleaningmaterial/"
            break;
    }

    await fetch(url + id, {
        method: "DELETE"
    })
        .then(result => {
            if (result.status == 200) {
                removed = true
            } else {
                alert("Ocorreu um error ao realizar a remoção do item! Atualize a página e tente novamente.")
            }        
        })
        .then(result => { warehouseItems = result })
        .catch(err => {
            console.log("Ocorreu um error", err)
            alert("Ocorreu um error interno ao realizar a remoção do item!")
        })

    return removed
}

function addFoodInTable(item) {
    let row = `<tr> 
        <td class='align-middle'>${item.id}</td>
        <td class='align-middle'>${item.name}</td>
        <td class='align-middle'>${item.quantity}</td>
        <td class='align-middle justify-content-end pe-5'>
            <select class="form-select form-control" aria-label="Ações" onChange="foodActions(this, ${item.id})">
                <option value="0" selected disabled>Ações</option>
                <option value="1" >Editar</option>
                <option value="2" >Baixa</option>
                <option value="3" >Remover</option>
            </select>
        </td>
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
        <td class='align-middle justify-content-end pe-5'>
            <select class="form-select form-control" aria-label="Ações" onChange="petFoodActions(this, ${item.id})">
                <option value="0" selected disabled>Ações</option>
                <option value="1" >Editar</option>
                <option value="2" >Baixa</option>
                <option value="3" >Remover</option>
            </select>
        </td>
    </tr>`

    document.getElementById("tBodyPetFood").innerHTML += row
}

function addMedicineInTable(item) {
    let row = `<tr> 
        <td class='align-middle'>${item.id}</td>
        <td class='align-middle'>${item.name}</td>
        <td class='align-middle'>${MedicineTypeNameTranslate[item.type]}</td>
        <td class='align-middle'>${item.quantity}</td>
        <td class='align-middle justify-content-end pe-5'>
            <select class="form-select form-control" aria-label="Ações" onChange="medicineActions(this, ${item.id})">
                <option value="0" selected disabled>Ações</option>
                <option value="1" >Editar</option>
                <option value="2" >Baixa</option>
                <option value="3" >Remover</option>
            </select>
        </td>
    </tr>`

    document.getElementById("tBodyMedicine").innerHTML += row
}

function addCleaningMaterialInTable(item) {
    let row = `<tr> 
        <td class='align-middle'>${item.id}</td>
        <td class='align-middle'>${item.name}</td>
        <td class='align-middle'>${item.quantity}</td>
        <td class='align-middle justify-content-end pe-5'>
            <select class="form-select form-control" aria-label="Ações" onChange="cleaningMaterialActions(this, ${item.id})">
                <option value="0" selected disabled>Ações</option>
                <option value="1" >Editar</option>
                <option value="2" >Baixa</option>
                <option value="3" >Remover</option>
            </select>
        </td>
    </tr>`

    document.getElementById("tBodyCleaningMaterial").innerHTML += row
}

function foodActions(element, id) {
    let option = $(element).val()
    if (option == 0 || !option) return

    switch (option) {
        case "1":
            openGenericEditModal(id, WarehouseTable.FOOD)
            break;
        case "2":
            openWriteOffModal(id, WarehouseTable.FOOD)
            break;
        case "3": 
            openConfirmAlert(id, WarehouseTable.FOOD)
            break;

    }

    $(element).val(0)
}

function petFoodActions(element, id) {
    let option = $(element).val()
    if (option == 0 || !option) return

    switch (option) {
        case "1":
            openPetFoodEditModal(id)
            break;
        case "2":
            openWriteOffModal(id, WarehouseTable.PET_FOOD)
            break;
        case "3": 
            openConfirmAlert(id, WarehouseTable.PET_FOOD)
            break;
    }

    $(element).val(0)
}

function medicineActions(element, id) {
    let option = $(element).val()
    if (option == 0 || !option) return

    switch (option) {
        case "1":
            openMedicineEditModal(id)
            break;
        case "2":
            openWriteOffModal(id, WarehouseTable.MEDICINE)
            break;
        case "3": 
            openConfirmAlert(id, WarehouseTable.MEDICINE)
            break;
    }

    $(element).val(0)
}

function cleaningMaterialActions(element, id) {
    let option = $(element).val()
    if (option == 0 || !option) return

    switch (option) {
        case "1":
            openGenericEditModal(id, WarehouseTable.CLEANING_MATERIAL)
            break;
        case "2":
            openWriteOffModal(id, WarehouseTable.CLEANING_MATERIAL)
            break;
        case "3": 
            openConfirmAlert(id, WarehouseTable.CLEANING_MATERIAL)
            break;

    }

    $(element).val(0)
}

function openWriteOffModal(id, tableOption){
    let data = globalItemsData.filter(x => x.id == id)
    if (data?.length == 0) return
    data = data[0]

    $("#writeOffItemId").val(id)
    $("#writeOffTableOption").val(tableOption)
    $("#inputWriteOffNameItem").val(data?.name)
    $("#inputWriteOffCurrentQuantity").val(data?.quantity || data?.quantityKg)
    $('#writeOffModal').modal('show');
}

function openGenericEditModal(id, tableOption) {
    let data = globalItemsData.filter(x => x.id == id)
    if (data?.length == 0) return
    data = data[0]

    $("#itemIdEdit").val(id)
    $("#tableOptionEdit").val(tableOption)
    $("#inputNameItemEdit").val(data?.name)
    $("#inputQuantityItemEdit").val(data?.quantity)
    $('#genericEditModal').modal('show');
}

function openPetFoodEditModal(id) {
    let data = globalItemsData.filter(x => x.id == id)
    if (data?.length == 0) return
    data = data[0]

    $("#petFoodIdEdit").val(id)
    $("#inputNamePetFoodEdit").val(data?.name)
    $("#selectAnimalSpeciePetFoodEdit").val(EnumAnimalSpecie[data?.specie])
    $("#selectAnimalAgeRangePetFoodEdit").val(EnumAnimalAgeRange[data?.ageRange])
    $("#selectAnimalSizePetFoodEdit").val(EnumAnimalSize[data?.animalSize])
    $("#inputQuantityPetFoodEdit").val(Number(data?.quantityKg).toFixed(2))
    $('#petFoodEditModal').modal('show');
}

function openMedicineEditModal(id) {
    let data = globalItemsData.filter(x => x.id == id)
    if (data?.length == 0) return
    data = data[0]

    $("#medicineIdEdit").val(id)
    $("#inputMedicineNameEdit").val(data?.name)
    $("#selectMedicineTypeEdit").val(EnumMedicineType[data?.type])
    $("#inputMedicineQuantityEdit").val(data?.quantity)
    $('#medicineEditModal').modal('show');
}

function openConfirmAlert(id, tableOption) {
    if (confirm("Todos os dados desse item será removido. Tem certeza disso? ")) {
        removeWarehouseItem(id, tableOption).then(result => result ? renderTable(tableOption) : "")
    }
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
