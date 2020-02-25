// const getJsonUsers = "/adminPage/json-users";
// const getJsonClients = "/adminPage/json-clients";
const token = $('#_csrf').attr('content');
const header = $('#_csrf_header').attr('content');

let userIdToDelete;
let clientIdToDelete;
let rowIndexToDelete;

$.ajaxSetup({
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-CSRF-TOKEN': token
    }
});

function setRowIndexAndUserId(row, id) {
    userIdToDelete = id;
    rowIndexToDelete = row.parentNode.parentNode.rowIndex;
}

function setRowIndexAndClientId(row, id) {
    clientIdToDelete = id;
    rowIndexToDelete = row.parentNode.parentNode.rowIndex;
}

function closeModal(nameOfTheModal) {
    $(nameOfTheModal).modal('toggle');
}

function deleteEntity() {

    let deleteUserUrl = '/adminPage/json-users/delete/' + userIdToDelete;

    $.ajax({
        url: deleteUserUrl,
        type: 'DELETE',
        success: function () {

            let table = $("#user-table");
            table[0].deleteRow(rowIndexToDelete);

            $('#alert-messages').append(
                "<div class='alert alert-success alert-dismissible fade show' role='alert'>" +
                "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>" +
                "<span aria-hidden='true'>&times;</span> </button>" +
                "<strong>Başarılı!</strong> Kullanıcı silindi!!!" +
                "</div>"
            );
            closeModal('#deleteUserModal');
            userIdToDelete = "";
            rowIndexToDelete = "";
        }
    });
}

function deleteClient() {

    let deleteClientUrl = '/adminPage/json-clients/delete/' + clientIdToDelete;

    $.ajax({
        url: deleteClientUrl,
        type: 'DELETE',
        success: function () {

            let table = $("#client-table");
            table[0].deleteRow(rowIndexToDelete);

            $('#alert-messages').append(
                "<div class='alert alert-success alert-dismissible fade show' role='alert'>" +
                "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>" +
                "<span aria-hidden='true'>&times;</span> </button>" +
                "<strong>Başarılı!</strong> Müşteri silindi!!!" +
                "</div>"
            );
            closeModal('#deleteClientModal');
            clientIdToDelete = "";
            rowIndexToDelete = "";
        }
    });
}

function searchUserByProperty() {
    let selectedProperty = $("#search-user-dropdown option:selected").text();
    let value = $("#searchUserBar").val();

    if (value != null && value !== "") {
        window.location.href = "/adminPage/users?usersProperty=" + selectedProperty + "&propertyValue=" + value;
    } else {
        window.location.href = "/adminPage/users";
    }
}

function searchClientByProperty() {
    let selectedProperty = $("#search-client-dropdown option:selected").text();
    let value = $("#searchClientBar").val();

    if (value != null && value !== "") {
        window.location.href = "/adminPage/clients?clientsProperty=" + selectedProperty + "&propertyValue=" + value;
    } else {
        window.location.href = "/adminPage/clients";
    }
}

function searchReceiptByProperty() {
    let selectedProperty = $("#search-receipt-dropdown option:selected").text();
    let value = $("#searchReceiptBar").val();

    if (value != null && value !== "") {
        if (selectedProperty == "Müşteri Adı") {
            window.location.href = "/adminPage/receipts/clientname/" + value;
        } else if (selectedProperty == "Müşteri Soyadı") {
            window.location.href = "/adminPage/receipts/clientsurname/" + value;
        }
    } else {
        window.location.href = "/adminPage/receipts";
    }
}
