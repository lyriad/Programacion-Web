<!DOCTYPE html>
<html lang="en">
<head>
    <title>Users</title>
    <#include "partials/header_info.ftl" >
</head>
<body>
    <#include "partials/navbar.ftl" >
    <#include "partials/messages.ftl" >
    <div class="container-fluid px-0">
        <div class="content-wrapper d-flex align-items-start px-0 ">
            <div class="row w-100 mx-0">
                <div class="col-lg-6 mx-auto">
                    <div class="text-left py-2">
                        <h3 class="text-white">Give user administrator privileges</h3>
                        <form method="POST" action="/admins">
                            <div class="input-group mt-3">
                                <input name="username" type="text" class="form-control" placeholder="Username">
                                <div class="input-group-append">
                                    <button class="btn btn-primary" type="submit">Make admin!</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="content-wrapper d-flex align-items-start px-0 ">
            <div class="row w-100 mx-0">
                <div class="col-lg-10 mx-auto">
                    <div class="text-left py-5">
                        <h3>Admins</h3>
                        <table class="table">
                            <thead class="thead-dark">
                            <tr>
                                <th scope="col">Username</th>
                                <th scope="col">Name</th>
                                <th scope="col">Email</th>
                                <th scope="col">Register date</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list admins as admin>
                                <tr>
                                    <th>${admin.username}</th>
                                    <th>${admin.name}</th>
                                    <th>${admin.email}</th>
                                    <th>${admin.registerDate.time?date}</th>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#include "partials/general_scripts.ftl" >
    </body>
</html>