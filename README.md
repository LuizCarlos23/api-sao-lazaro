# Sistema de Controle de Estoque e Cadastro de Animais - Abrigo São Lázaro

## Descrição

Este repositório contém o código-fonte de um sistema em desenvolvimento, desenvolvido em Java, para o controle de estoque e cadastro de animais no Abrigo São Lázaro. O sistema foi projetado para gerenciar informações sobre alimentos, rações, medicamentos, materiais de limpeza e animais do abrigo.

## Funcionalidades Principais

- **Controle de Estoque:**
  - Alimentos
  - Rações
  - Medicamentos
  - Materiais de Limpeza
  - Doações Monetárias

- **Cadastro de Animais:**
  - Inclusão, visualização, edição e exclusão de animais
  - Registro de óbito ou adoção de animais

## Lógica de Inserção de Dados

Para o controle de estoque, a inserção de dados é realizada através de duas tabelas principais:
1. **Doações Gerais:** Para registrar itens doados ao abrigo.
2. **Compras:** Para registrar itens adquiridos pelo abrigo.

Por exemplo, ao adicionar uma nova ração ao estoque, a ração pode ser registrada como uma doação ou uma compra, seguindo a mesma lógica para outros tipos de itens no estoque.

Doações Monetárias são registradas separadamente para fins de transparência e controle financeiro.

## Cadastro de Animais

O sistema permite o cadastro de animais, incluindo informações como espécie, raça, idade, entre outros. Além disso, é possível registrar quando um animal é adotado ou vem a óbito:
- Óbito e adoção, os dados do animal são mantidos na tabela de cadastro, e as informações sobre o óbito ou de adoção são registradas em outra tabela.

## Documentação das Rotas da API

### Visão Geral
Esta documentação descreve as rotas disponíveis na API do sistema.

### Animais

| Nome                          | Método | URL                           | Descrição                            | Body                                                                                  |
|-------------------------------|--------|-------------------------------|--------------------------------------|---------------------------------------------------------------------------------------|
| Listar Animais Registrados    | GET    | `/animal/`                    | Retorna uma lista de todos os animais registrados | N/A                                                                                   |
| Listar Animais No Abrigo      | GET    | `/animal/shelter`             | Retorna uma lista de animais no abrigo | N/A                                                                                   |
| Listar Animais Adotados       | GET    | `/animal/adopteds`            | Retorna uma lista de animais adotados | N/A                                                                                   |
| Listar Animais Falecido       | GET    | `/animal/deceaseds`           | Retorna uma lista de animais falecidos | N/A                                                                                   |
| Registrar Animal              | POST   | `/animal/`                    | Registra um novo animal              | `{ "entranceDate": string, "race": string, "location": string, "anamnesis": string }` |
| Registrar Adoção Animal       | POST   | `/animal/adopte`              | Registra a adoção de um animal       | `{ "id": integer, "adopterName": string, "adopterNumber": string, "adopterCpf": string }` |
| Registrar Óbito Animal        | POST   | `/animal/decease`             | Registra o óbito de um animal        | `{ "id": integer, "reason": string }`                                                  |
| Editar Animal                 | PUT    | `/animal/{id}`                   | Edita um animal específico           | `{ "id": integer, "entranceDate": string, "race": string, "local": string, "anamnesis": string }` |
| Remover Animal                | DELETE | `/animal/{id}`                   | Remove um animal específico          | N/A                                                                                   |

### Compras

| Nome                          | Método | URL                               | Descrição                     | Body                                                                                                                                                                                                                                               |
|-------------------------------|--------|-----------------------------------|-------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Listar Compras                | GET    | `/shopping/`                      | Retorna uma lista de todas as compras realizadas | N/A                                                                                                                                                                                                                                                |
| Registrar Compra Comida       | POST   | `/shopping/food`                  | Registra uma nova compra de comida     | ```json { "name": string, "quantity": integer, "value": float } ```                                                                                                                                                                          |
| Registrar Compra Ração        | POST   | `/shopping/pet_food`              | Registra uma nova compra de ração para animais    | ```json { "petfoodSpecie": integer, "name": "Dog Food", "quantity": integer, "petfoodAgeRange": integer, "petfoodAnimalSize": integer, "value": float } ```                                                                                                                |
| Registrar Compra Remédio      | POST   | `/shopping/medicine`              | Registra uma nova compra de remédio   | ```json { "name": string, "quantity": integer, "medicineType": string } ```                                                                                                                                                                  |
| Registrar Compra Material de Limpeza | POST   | `/shopping/cleaning_material`     | Registra uma nova compra de material de limpeza | ```json { "name": string, "quantity": integer, "value": float } ```                                                                                                                                                                          |

### Doações Gerais

| Nome                          | Método | URL                               | Descrição                     | Body                                                                                                                                                                                                                                               |
|-------------------------------|--------|-----------------------------------|-------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Listar Doações                | GET    | `/general_donation/`              | Retorna uma lista de todas as doações realizadas | N/A                                                                                                                                                                                                                                                |
| Registrar Doação Comida       | POST   | `/general_donation/food`          | Registra uma nova doação de comida     | ```json { "name": string, "quantity": integer } ```                                                                                                                                                                                            |
| Registrar Doação Ração        | POST   | `/general_donation/pet_food`      | Registra uma nova doação de ração para animais    | ```json { "specie": integer, "name": string, "quantityKg": integer, "ageRange": integer, "animalSize": integer } ```                                                                                                                     |
| Registrar Doação Remédio      | POST   | `/general_donation/medicine`      | Registra uma nova doação de remédio   | ```json { "type": integer, "name": string, "quantity": integer } ```                                                                                                                                                                         |
| Registrar Doação Material de Limpeza | POST   | `/general_donation/cleaning_material` | Registra uma nova doação de material de limpeza | ```json { "name": string, "quantity": integer } ```                                                                                                                                                                                            |

### Doações Monetárias

| Nome                          | Método | URL                               | Descrição                     | Body                                                                                  |
|-------------------------------|--------|-----------------------------------|-------------------------------|---------------------------------------------------------------------------------------|
| Listar Doações                | GET    | `/monetary_donation/`             | Retorna uma lista de todas as doações monetárias  | N/A                                                                                   |
| Registrar Doação Monetária    | POST   | `/monetary_donation/`             | Registra uma nova doação monetária  | `{ "type": integer, "date": string, "value": integer }`                                       |


### Alimentos

| Nome             | Método | URL                    | Descrição                            | Body                                                                                                                                           |
|------------------|--------|------------------------|--------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| Listar Rações    | GET    | `/food/`               | Retorna uma lista de todas as rações | N/A                                                                                                                                            |
| Editar Ração     | PUT    | `/food/{id}`              | Edita uma ração específica           | ```json { "name": string, "quantity": integer } ```                                                                                        |
| Baixa Ração      | PUT    | `/food/writeoff`       | Dá baixa em uma ração específica     | ```json { "id": integer, "quantity": integer } ```                                                                                         |
| Remover Ração    | DELETE | `/food/{id}`              | Remove uma ração específica          | N/A                                                                                                                                            |

### Rações

| Nome             | Método | URL                    | Descrição                            | Body                                                                                  |
|------------------|--------|------------------------|--------------------------------------|---------------------------------------------------------------------------------------|
| Listar Rações    | GET    | `/pet_food/`           | Retorna uma lista de todas as rações | N/A                                                                                   |
| Editar Ração     | PUT    | `/pet_food/{id}`          | Edita uma ração específica           | `{ "specie": integer, "name": string, "quantityKg": integer, "ageRange": integer, "animalSize": integer }` |
| Baixa Ração      | PUT    | `/pet_food/writeoff`   | Dá baixa em uma ração específica     | `{ "id": integer, "quantity": integer }`                                                      |
| Remover Ração    | DELETE | `/pet_food/{id}`          | Remove uma ração específica          | N/A                                                                                   |

### Medicamentos

| Nome                | Método | URL                        | Descrição                            | Body                                                                                  |
|---------------------|--------|----------------------------|--------------------------------------|---------------------------------------------------------------------------------------|
| Listar Medicamentos | GET    | `/medicine/`               | Retorna uma lista de todos os medicamentos | N/A                                                                                   |
| Editar Medicamento  | PUT    | `/medicine/{id}`              | Edita um medicamento específico          | `{ "type": integer, "name": string, "quantity": integer }`                                    |
| Baixa Medicamento   | PUT    | `/medicine/writeoff`       | Dá baixa em um medicamento específico    | `{ "id": integer, "quantity": integer }`                                                      |
| Remover Medicamento | DELETE | `/medicine/{id}`              | Remove um medicamento específico         | N/A                                                                                   |

### Materiais de Limpeza

| Nome                          | Método | URL                              | Descrição                            | Body                                                                                  |
|-------------------------------|--------|----------------------------------|--------------------------------------|---------------------------------------------------------------------------------------|
| Listar Materiais de Limpeza  | GET    | `/cleaningmaterial/`            | Retorna uma lista de todos os materiais de limpeza | N/A                                                                                   |
| Editar Materiais de Limpeza  | PUT    | `/cleaningmaterial/{id}`           | Edita um material de limpeza específico          | `{ "name": string, "quantity": integer }`                                                 |
| Baixa Materiais de Limpeza   | PUT    | `/cleaningmaterial/writeoff`    | Dá baixa em um material de limpeza específico    | `{ "id": integer, "quantity": integer }`                                                      |
| Remover Materiais de Limpeza | DELETE | `/cleaningmaterial/{id}`           | Remove um material de limpeza específico         | N/A                                                                                   |


## Esquema do Banco de Dados

### registered_animals

| Coluna    | Tipo     | Descrição                     |
|-----------|----------|-------------------------------|
| id        | SERIAL   | Chave primária                |
| entry_date| DATE     | Data de entrada               |
| race      | VARCHAR(100) | Raça do animal                |
| anamnesis | TEXT     | Anamnese                      |

### animals_in_shelters

| Coluna               | Tipo     | Descrição                                 |
|----------------------|----------|-------------------------------------------|
| id                   | SERIAL   | Chave primária                            |
| registered_animal_id | INT      | Referência para registered_animals(id)    |
| location             | VARCHAR(100) | Localização do abrigo                       |

### deceased_animals

| Coluna               | Tipo     | Descrição                                 |
|----------------------|----------|-------------------------------------------|
| id                   | SERIAL   | Chave primária                            |
| registered_animal_id | INT      | Referência para registered_animals(id)    |
| reason               | VARCHAR(100) | Razão do óbito                              |
| date                 | DATE     | Data do óbito                             |

### adopted_animals

| Coluna               | Tipo     | Descrição                                 |
|----------------------|----------|-------------------------------------------|
| id                   | SERIAL   | Chave primária                            |
| registered_animal_id | INT      | Referência para registered_animals(id)    |
| adoption_date        | DATE     | Data da adoção                            |
| adopter_name         | VARCHAR(100) | Nome do adotante                           |
| adopter_number       | VARCHAR(20)  | Número de contato do adotante              |
| adopter_cpf          | VARCHAR(14)  | CPF do adotante                            |

### shopping

| Coluna             | Tipo        | Descrição                     |
|--------------------|-------------|-------------------------------|
| id                 | SERIAL      | Chave primária                |
| type               | INT         | Tipo de item (Alimento, Ração, Medicamento, Material de limpeza)                  |
| name               | VARCHAR(100)| Nome do item                  |
| quantity           | NUMERIC     | Quantidade                    |
| petfood_animal_size| INT         | Tamanho do animal (para ração)|
| petfood_age_range  | INT         | Faixa etária (para ração)     |
| petfood_specie     | INT         | Espécie do animal (para ração)|
| medicine_type      | INT         | Tipo de medicamento (Comprimido, Líquido, Injetável, Spray, Pomada)           |
| value              | NUMERIC     | Valor                         |
| date               | DATE        | Data                          |

### general_donations

| Coluna             | Tipo        | Descrição                     |
|--------------------|-------------|-------------------------------|
| id                 | SERIAL      | Chave primária                |
| type               | INT         | Tipo de item (Alimento, Ração, Medicamento, Material de limpeza)                  |
| name               | VARCHAR(100)| Nome do item                  |
| quantity           | NUMERIC     | Quantidade                    |
| petfood_animal_size| INT         | Tamanho do animal (para ração)|
| petfood_age_range  | INT         | Faixa etária (para ração)     |
| petfood_specie     | INT         | Espécie do animal (para ração)|
| medicine_type      | INT         | Tipo de medicamento (Comprimido, Líquido, Injetável, Spray, Pomada)           |
| date               | DATE        | Data da doação                |

### monetary_donations

| Coluna    | Tipo        | Descrição          |
|-----------|-------------|--------------------|
| id        | SERIAL      | Chave primária     |
| value     | NUMERIC     | Valor da doação    |
| type      | INT         | Tipo de doação (Pix, Cédula, Cartão)     |
| date      | DATE        | Data da doação     |


### warehouse_foods

| Coluna    | Tipo        | Descrição             |
|-----------|-------------|-----------------------|
| id        | SERIAL      | Chave primária        |
| name      | VARCHAR(100)| Nome do alimento      |
| quantity  | NUMERIC     | Quantidade            |

### warehouse_pet_foods

| Coluna        | Tipo        | Descrição             |
|---------------|-------------|-----------------------|
| id            | SERIAL      | Chave primária        |
| specie        | INTEGER     | Espécie do animal     |
| name          | VARCHAR(100)| Nome do alimento      |
| quantity_kg   | NUMERIC     | Quantidade em kg      |
| age_range     | INTEGER     | Faixa etária do animal (Jovem, Adulto)|
| animal_size   | INTEGER     | Tamanho do animal (Pequeno, Médio, Grande)     |

### warehouse_medicines

| Coluna    | Tipo        | Descrição             |
|-----------|-------------|-----------------------|
| id        | SERIAL      | Chave primária        |
| name      | VARCHAR(100)| Nome do medicamento   |
| type      | INTEGER     | Tipo de medicamento (Comprimido, Líquido, Injetável, Spary, Pomada)    |
| quantity  | NUMERIC     | Quantidade            |

### warehouse_cleaning_materials

| Coluna    | Tipo        | Descrição                  |
|-----------|-------------|----------------------------|
| id        | SERIAL      | Chave primária             |
| name      | VARCHAR(100)| Nome do material de limpeza|
| quantity  | NUMERIC     | Quantidade                 |