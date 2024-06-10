let baseUrl = "http://localhost:8080"
const animalsTable = {SHELTER: 1, REGISTERED: 2, ADOPTED: 3, DECEASE: 4}
const visibleTable = animalsTable.SHELTER

$(document).ready(() => {
    renderTable(animalsTable.SHELTER)
})

$("#btnPesquisar").on("click", async () => {
    let animalStatus = Number($("#animalStatus").val())  
    if (animalStatus == 0) return
    renderTable(animalStatus)
})

$("#btnRegisterAnimal").on("click", async () => {
    let data = {}
    data.entranceDate = $("#inputEntranceDate").val()
    data.race = $("#inputRace").val()
    data.location = $("#inputLocation").val()
    data.anamnesis = $("#inputAnamnesis").val()

    let result = await registerAnimal(data)
    if (result) {
        $('#registerAnimalModal').modal('hide');
        $("#formRegister").trigger('reset');
        renderTable(animalsTable.SHELTER)
    }
})

$("#btnAdopteAnimal").on("click", async () => {
    let adoptionData = {}
    adopedAnimalId = $("#adopedAnimalId").val()

    if (!adopedAnimalId) {
        alert("Animal não registrado")        
        return
    }

    adoptionData.id = adopedAnimalId
    adoptionData.adopterName = $("#inputAdopterName").val()
    adoptionData.adopterNumber = $("#inputAdopterNumber").cleanVal()
    adoptionData.adopterCpf = $("#inputAdopterCpf").cleanVal()

    let result = await registerAdoption(adoptionData)
    if (result) {
        $("#adopedAnimalId").val("")
        $('#adopteAnimalModal').modal('hide');
        $("#formRegisterAdoption").trigger('reset');
        renderTable(animalsTable.ADOPTED)
    }
})

$("#btnDeceaseAnimal").on("click", async () => {
    let deceaseData = {}
    deceasedAnimalId = $("#deceasedAnimalId").val()

    if (!deceasedAnimalId) {
        alert("Animal não registrado")        
        return
    }

    deceaseData.id = deceasedAnimalId
    deceaseData.reason = $("#inputDeceaseReason").val()

    if (deceaseData.reason?.trim() == "") {
        alert("Escreve o motivo do óbito!")
        return
    }

    let result = await registerDecease(deceaseData)
    if (result) {
        $("#deceasedAnimalId").val("")
        $('#deceaseAnimalModal').modal('hide');
        $("#formRegisterDecease").trigger('reset');
        renderTable(animalsTable.DECEASE)
    }
})

async function renderTable(tableOption) {
    $("#info").html("")
    $("#tBodyAnimalsInShelter").html("")
    $("#tBodyAnimalsRegistered").html("")
    $("#tBodyAnimalsAdopeteds").html("")
    $("#tBodyAnimalsDeceases").html("")

    let animals = await getAnimals(tableOption)

    if (animals?.length == 0 || !animals) {
        $("#info").html("<span class='d-flex justify-content-center p-4'>Nenhum animal</span>")
        return
    } 

    switch (tableOption) {
        case animalsTable.SHELTER:
            animals?.forEach(animal => { addAnimalInTableShelter(animal) })
            break;
        case animalsTable.REGISTERED:
            animals?.forEach(animal => { addAnimalInTableRegistered(animal) })
            break;
        case animalsTable.ADOPTED:
            animals?.forEach(animal => { addAnimalInTableAdopeteds(animal) })
            break;
    
        case animalsTable.DECEASE:
            animals?.forEach(animal => { addAnimalInTableDeceases(animal) })
            break;
    }

    $("#animalStatus").val(tableOption)
    showTable(tableOption)
}

async function getAnimals(tableOption) {
    let animals = null
    let url = `${baseUrl}/animal/`

    switch (tableOption) {
        case animalsTable.SHELTER:
            url += "shelter"
            break;
        case animalsTable.ADOPTED:
            url += "adopteds"
            break;
        case animalsTable.DECEASE:
            url += "deceaseds"
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
        .then(result => { animals = result })
        .catch(err => {
            console.log("Ocorreu um error", err)
            alert("Ocorreu um error ao buscar os animais!")
        })

    return animals
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
                <option value="1" >Adotado</option>
                <option value="2" >Falecido</option>
            </select>
        
        </td>
    </tr>`

    document.getElementById("tBodyAnimalsInShelter").innerHTML += row
}

function addAnimalInTableRegistered(animal) {
    let row = `<tr> 
        <td class='align-middle py-3'>${animal.entryDate}</td>
        <td class='align-middle'>${animal.race}</td>
        <td class='align-middle'>${animal.anamnesis}</td>
        <td class='align-middle justify-content-end pe-5'>
            <select class="form-select form-control" aria-label="Ações" onChange="animalRegisteredActions(this, ${animal.id})" >
                <option value="0" selected disabled>Ações</option>
                <option value="1">Remover</option>
            </select>
        </td>
    </tr>`

    document.getElementById("tBodyAnimalsRegistered").innerHTML += row
}

function addAnimalInTableAdopeteds(animal) {
    let element = document.createElement("input")
    element.value = animal.adopterCpf
    $(element).mask("000.000.000-00")
    adopterCpfFormated = $(element).val()
    element.value = animal.adopterNumber
    $(element).mask("(00) 00000-0000")
    adopterNumberFormated = $(element).val()


    let row = `<tr> 
        <td class='align-middle py-3'>${animal.registeredAnimal.entryDate}</td>
        <td class='align-middle'>${animal.registeredAnimal.race}</td>
        <td class='align-middle'>${animal.registeredAnimal.anamnesis}</td>
        <td class='align-middle'>${animal.adoptionDate}</td>
        <td class='align-middle'>${animal.adopterName}</td>
        <td class='align-middle'>${adopterNumberFormated}</td>
        <td class='align-middle'>${adopterCpfFormated}</td>
    </tr>`

    document.getElementById("tBodyAnimalsAdopeteds").innerHTML += row
}

function addAnimalInTableDeceases(animal) {
    let row = `<tr> 
        <td class='align-middle py-3'>${animal.registeredAnimal.entryDate}</td>
        <td class='align-middle'>${animal.registeredAnimal.race}</td>
        <td class='align-middle'>${animal.registeredAnimal.anamnesis}</td>
        <td class='align-middle'>${animal.date}</td>
        <td class='align-middle'>${animal.reason}</td>
    </tr>`

    document.getElementById("tBodyAnimalsDeceases").innerHTML += row
}

function showTable(tableOption) {
    switch (tableOption) {
        case animalsTable.SHELTER:
            hideTables([$("#tableAnimalsRegistered"), $("#tableAnimalsAdopeteds"), $("#tableAnimalsDeceases")])
            $("#tableAnimalsInShelter").removeClass("d-none")
            break;
        case animalsTable.REGISTERED:
            hideTables([$("#tableAnimalsInShelter"), $("#tableAnimalsAdopeteds"), $("#tableAnimalsDeceases")])
            $("#tableAnimalsRegistered").removeClass("d-none")
            break;
        case animalsTable.ADOPTED:
            hideTables([$("#tableAnimalsRegistered"), $("#tableAnimalsInShelter"), $("#tableAnimalsDeceases")])
            $("#tableAnimalsAdopeteds").removeClass("d-none")
            break;
        case animalsTable.DECEASE:
            hideTables([$("#tableAnimalsInShelter"), $("#tableAnimalsAdopeteds"), $("#tableAnimalsRegistered")])
            $("#tableAnimalsDeceases").removeClass("d-none")
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

async function registerAnimal(animal) {
    let url = `${baseUrl}/animal/`
    let registered = false

    await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(animal)
    }).then(response => {
        if (response.status == 201) {
            registered = true
        } else {
            alert("Ocorreu um erro ao registrar o animal! Verifique os campos")
        }
    }).catch(err => {
        console.log(err)
        alert("Ocorreu um error interno ao registrar o animal!")
    })

    return registered
}

async function registerAdoption(data) {
    let url = `${baseUrl}/animal/adopte`
    let registered = false

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
            alert("Ocorreu um erro ao registrar a adoção! Verifique os campos")
        }
    }).catch(err => {
        console.log(err)
        alert("Ocorreu um error interno ao registrar a adoção!")
    })

    return registered
}

async function registerDecease(data) {
    let url = `${baseUrl}/animal/decease`
    let registered = false

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
            alert("Ocorreu um erro ao registrar o óbito! Verifique os campos")
        }
    }).catch(err => {
        console.log(err)
        alert("Ocorreu um error interno ao registrar o óbito!")
    })

    return registered
}

async function removeAnimal(id) {
    let url = `${baseUrl}/animal/${id}`
    let removed = false

    await fetch(url, {
        method: "DELETE"
    }).then(response => {
        if (response.status == 200) {
            removed = true
        } else {
            alert("Ocorreu um erro ao remover o animal do registro!")
        }
    }).catch(err => {
        console.log(err)
        alert("Ocorreu um error interno ao remover o animal do registro!")
    })

    return removed
}


function animalShelterActions(event, id) {
    let option = $(event).val()
    if (option == 0 || !option) return

    switch (option){
        case "1":
            openAdopteAnimalModal(id)
            break;
        case "2":
            openDeceaseAnimalModal(id)
            break;
    }

    $(event).val(0)
}

function animalRegisteredActions(event, id) {
    let option = $(event).val()
    if (option == 0 || !option) return

    switch (option){
        case "1":
            showMessageConfirmRemoveAnimal(id)
            break;
    }

    $(event).val(0)
}

function openAdopteAnimalModal(id) {
    $("#adopedAnimalId").val(id)
    $('#adopteAnimalModal').modal('show');
}

function openDeceaseAnimalModal(id) {
    $("#deceasedAnimalId").val(id)
    $('#deceaseAnimalModal').modal('show');
}

async function showMessageConfirmRemoveAnimal(id){
    if (confirm("Todos os registros desse animal seram removidos. Tem certeza que deseja fazer isso?")) {
        await removeAnimal(id)
        renderTable(animalsTable.REGISTERED)
    }
}

document.getElementById("btn_bar").addEventListener("click", function() {
    document.getElementById("mySidebar").classList.toggle("show");
});

    // document.getElementById("btn_bar").addEventListener("click", function() {
    //     // Adiciona ou remove a classe 'collapsed' à barra lateral
    //     document.getElementById("mySidebar").classList.toggle("collapsed");
    //     document.getElementById("btn_bar").classList.toggle("collapsed");
    // });