<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.List"%>
    <%@ page import="com.rebel.youtube.repository.PersonRepository"%>
        <%@ page import="com.rebel.youtube.model.PersonModel"%>
    <% 
    
    PersonRepository personRepository = new PersonRepository();
    List<PersonModel> personModels =  personRepository.read();%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>JSP in Action- crudInMacos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.1.9/dist/sweetalert2.all.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
    <!-- icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.6.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.1.9/dist/sweetalert2.min.css">
</head>
<body>
    <header>
        <nav class="navbar navbar-expand-sm navbar-toggleable-sm navbar-light bg-white border-bottom box-shadow mb-3">
            <div class="container">
                <a class="navbar-brand">crudInMacos</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target=".navbar-collapse" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="navbar-collapse collapse d-sm-inline-flex justify-content-between">
                    <ul class="navbar-nav flex-grow-1">
                        <li class="nav-item">
                            <a class="nav-link text-dark" href="index.jsp">Home</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </header>
    <div class="container">
        <main role="main" class="pb-3">
  
<div class="card">
    <div class="card-body">
        <label>
            <input type="text" class="form-control" id="search" placeholder="Search Here" />
        </label>
    </div>
    <div class="card-footer">
        <button class="btn btn-info" onclick="searchRecord()">
            <i class="bi bi-search"></i>
        </button>
    </div>
</div>
<table class="table table-striped">
    <thead>
        <tr>
            <th>
                <button type="button" onclick="refreshMe()" class="btn btn-primary">
                    <i class="bi bi-tropical-storm"></i>
                </button>

            </th>
            <th><input type="text" id="name" placeholder="Name" class="form-control" /></th>
            <th><input type="text" id="age" placeholder="Age" class="form-control" /> </th>
            <th>
                <button type="button" class="btn btn-primary" onclick="createRecord()">Create </button>


            </th>
        </tr>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Age</th>
            <th>Command</th>
        </tr>
    </thead>
    <tbody id="tbody">
       <% for(int i=0; i < personModels.size();i++){
       		String personId = String.valueOf(personModels.get(i).getPersonId());
       		%>
            <tr id="<%=personId %>-personId">
                <td scope="row"><%=personId %> </td>
                <td scope="row">
                    <input type="text" id="<%=personId %>-name" placeholder="Name" value="<%=personModels.get(i).getName()%>" class="form-control" />
                </td>
                <td scope="row">
                    <input type="text" id="<%=personId %>-age" placeholder="Age" class="form-control" value="<%=personModels.get(i).getAge()%>" style="text-align: right">

                </td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateRecord(<%=personId %>)"><i class="bi bi-file-diff"></i> UPDATE</button>
                    <button type="button" class="btn btn-danger" onclick="deleteRecord(<%=personId %>)"><i class="bi bi-trash"></i> DELETE</button>
                </td>
            </tr>
        <% } %>
    </tbody>
</table>
<script>var url = "http://localhost:8080/youtube/api";  // same like those postman address before
    const tbody = $("#tbody");

    function refreshMe() {
        readRecord();
        $("#search").val("");
    }

    function emptyTemplate() {
        return "" +
            "<tr>" +
            "<td colspan='4'>It's lonely here</td>" +
            "</tr>";
    }

    function template(personId, name, age) {
        return "" +
            "  <tr id=\"" + personId + "-personId\">" +
            "        <td>" + personId + "</td>" +
            "        <td><input type=\"text\" class=\"form-control\" id=\"" + personId + "-name\" value=\"" + name + "\">" +
            "        </td>" +
            "        <td><input type=\"text\" class=\"form-control\" id=\"" + personId + "-age\" value=\"" + age + "\" style=\"text-align: right\">" +
            "        </td>" +
            "" +
            "        <td>" +
            "          <div class=\"btn-group\">" +
            "            <button type=\"button\" onclick=\"updateRecord('" + personId + "')\" class=\"btn btn-warning\">" +
            "              <i class=\"bi bi-file-diff\"></i>" +
            "              UPDATE" +
            "            </button>" +
            "            <button type=\"button\" onclick=\"deleteRecord('" + personId + "')\" class=\"btn btn-danger\">" +
            "              <i class=\"bi bi-trash\"></i>" +
            "              DELETE" +
            "            </button>" +
            "          </div>" +
            "        </td>" +
            "      </tr>" +
            "";
    }

    function createRecord() {
        // since jquery out of the box better use it ? angular ? react ? vue
        // we don't know and don't care much
        const name = $("#name");
        const age = $("#age");
        $.ajax({
            type: "post",
            url: url,
            contentType: "application/x-www-form-urlencoded",
            data: {
                mode: "create",
                name: name.val(),
                age: age.val()
            }, beforeSend() {
                console.log("Waiiit waiit");
            }, success: function (response) {
            	console.log(response);
                const data = response;
                if (data.status) {
                    const tbodyTemplate = template(data.lastInsertId, name.val(), age.val());
                    tbody.prepend(tbodyTemplate);
                    Swal.fire({
                        title: 'Success!',
                        text: 'You create a record',
                        icon: 'success',
                        confirmButtonText: 'Cool'
                    })
                    name.val("");
                    age.val("");
                } else {
                    console.log("something wrong");
                }
            }, failure: function (xhr) {
                console.log(xhr.status);
            }
        })
    }
    function readRecord() {
        $.ajax({
            type: "post",
            url: url,
            contentType: "application/x-www-form-urlencoded",
            data: {
                mode: "read"
            }, beforeSend() {
                console.log("Waiiit waiit");
            }, success: function (response) {
                const data = response;
                if (data.status) {
                    if (data.data.length > 0) {
                        let templateStringBuilder = "";
                        for (let i = 0; i < data.data.length; i++) {
                            templateStringBuilder += template(data.data[i].personId, data.data[i].name, data.data[i].age);
                        }
                        tbody.html("").html(templateStringBuilder);
                    } else {
                        tbody.html("").html(emptyTemplate());
                    }
                } else {
                    console.log("something wrong");
                }
            }, failure: function (xhr) {
                console.log(xhr.status);
            }
        })
    }
    function searchRecord() {
        $.ajax({
            type: "post",
            url: url,
            contentType: "application/x-www-form-urlencoded",
            data: {
                mode: "search",
                search: $("#search").val()
            }, beforeSend() {
                console.log("Waiiit waiit");
            }, success: function (response) {
                const data = response;
                if (data.status) {
                    console.log(data.data);

                    if (typeof data.data !== "undefined") {
                        if (data.data.length > 0) {
                            let templateStringBuilder = "";
                            for (let i = 0; i < data.data.length; i++) {
                                templateStringBuilder += template(data.data[i].personId, data.data[i].name, data.data[i].age);
                            }
                            tbody.html("").html(templateStringBuilder);
                        }
                        console.log(data.data);
                        if(data.data.length ==0){
                        	tbody.html("").html(emptyTemplate());
                        }
                    } else {
                        tbody.html("").html(emptyTemplate());
                    }
                } else {
                    console.log("something wrong");
                }
            }, failure: function (xhr) {
                console.log(xhr.status);
            }
        })
    }
    function updateRecord(personId) {
        $.ajax({
            type: "post",
            url: url,
            contentType: "application/x-www-form-urlencoded",
            data: {
                mode: "update",
                name: $("#" + personId + "-name").val(),
                age: $("#" + personId + "-age").val(),
                personId: personId
            }, beforeSend() {
                console.log("Waiiit waiit");
            }, success: function (response) {
                const data = response;
                if (data.status) {
                    Swal.fire({
                        title: 'System!',
                        text: 'You updated the record',
                        icon: 'info'
                    })
                } else {
                    console.log("something wrong");
                }
            }, failure: function (xhr) {
                console.log(xhr.status);
            }
        })
    }
    function deleteRecord(personId) {
        const formData = new FormData();
        formData.append("mode", "delete");
        formData.append("personId", personId);

        Swal.fire({
            title: 'System!',
            text: 'Want to delete the record?',
            icon: 'warning',
            confirmButtonText: 'Yes, I am sure!',
            showCancelButton: true,
            cancelButtonText: "No, cancel it!",
        }).then(function (result) {
            if (result.value) {

                $.ajax({
                    type: "post",
                    url: url,
                    contentType: "application/x-www-form-urlencoded",
                    data: {
                        mode: "delete",
                        personId: personId
                    }, beforeSend() {
                        console.log("Waiiit waiit");
                    }, success: function (response) {
                        const data = response;
                        if (data.status) {
                            $("#" + personId + "-personId").remove();
                            Swal.fire(
                                "Deleted!",
                                "Your file has been deleted.",
                                "success"
                            )
                        } else {
                            console.log("something wrong");
                        }
                    }, failure: function (xhr) {
                        console.log(xhr.status);
                    }
                })
            } else if (result.dismiss === "cancel") {
                Swal.fire(
                    "Cancelled",
                    "Haiya , be safe la .. sui ",
                    "error"
                )
            }
        });

    }</script>
        </main>
    </div>

    <footer class="border-top footer text-muted">
        <div class="container">
            &copy; 2021 - CRUD In macos
        </div>
    </footer>


</body>
</html>

