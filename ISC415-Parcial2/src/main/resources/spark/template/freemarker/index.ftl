<!DOCTYPE html>
<html lang="en">
<head>
    <title>Home</title>
    <#include "partials/header_info.ftl" >
    <script src="/js/qrcode.min.js"></script>
</head>

<body>
    <#include "partials/navbar.ftl" >
    <#include "partials/messages.ftl" >
    <div class="container-fluid px-0">
        <div class="content-wrapper d-flex align-items-start px-0 bg-primary">
            <div class="row w-100 mx-0">
                <div class="col-lg-6 mx-auto">
                    <div class="text-left py-5">
                        <h3 class="text-white">Shorten your links!</h3>
                        <form method="POST" action="/create">
                            <div class="input-group mt-3">
                                <input name="url" type="text" class="form-control" placeholder="Enter a long URL"/>
                                <div class="input-group-append">
                                    <button class="btn btn-dark" type="submit">Shorten</button>
                                </div>
                            </div>
                        </form>
                        <p class="text-white">You must register to see charts on your links</p>
                    </div>
                </div>
            </div>
        </div>

        <#if currentUser?? || urls?size &gt; 0>
        <div class="content-wrapper d-flex align-items-start px-0 mt-4">
            <div class="row w-100 mx-0">
                <div class="col-lg-10 mx-auto">
                    <h2>Your links</h2>
                    <hr>
                    <table class="table">
                        <thead class="thead-dark">
                            <tr>
                                <th scope="col">Original URL</th>
                                <th scope="col">Shortened URL</th>
                                <th scope="col">Owner</th>
                                <th scope="col">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <#list urls as url>
                            <tr>
                                <#if url.originalUrl?length < 100  >
                                    <th scope="col"><a href="${url.originalUrl}">${url.originalUrl}</a></th>
                                <#else>
                                    <th scope="col"><a href="${url.originalUrl}">${url.originalUrl[0..100]}...</a></th>
                                </#if>
                                <th scope="col"><a href="${url.shortenedUrl}">${url.shortenedUrl}</a></th>
                                <th scope="col"><#if url.owner??>${url.owner.username}<#else>Anonymous</#if></th>
                                <th scope="col" class="d-flex">
                                    <button class="img-thumbnail" data-toggle="modal" data-target="#modal-${url.shortenedUrl[url.shortenedUrl?last_index_of("/") + 1]}">
                                        <img alt="view" src="/icons/view-icon.svg" width="24" height="24"/>
                                    </button>
                                    <#if currentUser??>
                                        <form method="POST" action="/delete">
                                            <input name="url" class="d-none" value="${url.originalUrl}" readonly />
                                            <button class="img-thumbnail" type="submit"><img alt="Delete" src="/icons/remove.svg" width="24" height="24"/> </button>
                                        </form>
                                    </#if>
                                </th>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                    <hr>
                    <#if currentUser?? >
                    <ul class="pagination justify-content-center mb-4">
                        <ul class="pagination">
                            <#list 1 ..<pages as page_number>
                                <li class="page-item"><a class="page-link" href="/?page=${page_number}">${page_number}</a></li>
                            </#list>
                        </ul>
                    </ul>
                    </#if>
                </div>
            </div>
        </div>
        </#if>

        <#list urls as url>
            <div class="modal fade" id="modal-${url.shortenedUrl[url.shortenedUrl?last_index_of("/") + 1]}">
                <div class="modal-dialog">
                    <div class="modal-content">

                        <div class="modal-header">
                            <h4 class="modal-title">${url.shortenedUrl}</h4>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>

                        <div class="modal-body text-center">
                            <p>Scan this code to redirect to the link</p>
                            <div class="d-flex justify-content-center mb-3" id="qr-${url.shortenedUrl[url.shortenedUrl?last_index_of("/") + 1]}"></div>
                            <hr>
                            <p>Click <a href="/stats/${url.shortenedUrl[url.shortenedUrl?last_index_of("/") + 1]}">here</a> to see statistics about your link</p>
                        </div>
                        <script type="text/javascript">
                            new QRCode(document.getElementById("qr-${url.shortenedUrl[url.shortenedUrl?last_index_of("/") + 1]}"), "${domain}/r/${url.shortenedUrl[url.shortenedUrl?last_index_of("/") + 1]}");
                            new QRCode("test", {
                                text: "/r/${url.shortenedUrl[url.shortenedUrl?last_index_of("/") + 1]}",
                                width: 128,
                                height: 128,
                                colorDark : "#000000",
                                colorLight : "#ffffff",
                                correctLevel : QRCode.CorrectLevel.H
                            });
                        </script>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </#list>

        <div class="content-wrapper d-flex align-items-start px-0 py-3">
            <div class="row w-100 mx-0">
                <div class="col-md-2"></div>
                <div class="col-md-2 mb-2">
                    <div class="d-flex m-auto">
                        <img src="/icons/material-link.svg" />
                        <h3 class="ml-1">Shorten</h3>
                    </div>
                    Shorten your URL so it's ready to be shared everywhere
                </div>
                <div class="col-md-1"></div>
                <div class="col-md-2 mb-2">
                    <div class="d-flex m-auto">
                        <img src="/icons/material-trending.svg" />
                        <h3 class="ml-1">Track</h3>
                    </div>
                    Analytics help you know where your clicks are coming from
                </div>
                <div class="col-md-1"></div>
                <div class="col-md-2 mb-2">
                    <div class="d-flex m-auto">
                        <img src="/icons/material-people.svg" />
                        <h3 class="ml-1">Learn</h3>
                    </div>
                    Understand and visualize your audience
                </div>
                <div class="col-md-2"></div>
            </div>
        </div>
    </div>
    <#include "partials/general_scripts.ftl" >
    </body>
</html>