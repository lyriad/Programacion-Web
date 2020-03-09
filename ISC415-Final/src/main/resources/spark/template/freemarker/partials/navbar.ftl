<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand nav-title" href="/">shortener</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
            <#if currentUser?? && currentUser.admin>
            <li class="nav-item">
                <a class="nav-link" href="/admins">Admins</a>
            </li>
            </#if>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <#if currentUser??>
            <li class="nav-item">
                <a class="nav-link active">Hello, ${currentUser.name}</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/logout">Logout</a>
            </li>
            <#else>
            <li class="dropdown order-1 mr-2">
                <button type="button" id="dropdownMenu1" data-toggle="dropdown" class="btn btn-outline-secondary text-white dropdown-toggle">Login <span class="caret"></span></button>
                <ul class="dropdown-menu dropdown-menu-right mt-2">
                    <li class="px-3 py-2">
                        <form class="form" method="POST" action="/login">
                            <div class="form-group">
                                <input name="username" placeholder="Username" class="form-control form-control" type="text" required="">
                            </div>
                            <div class="form-group">
                                <input name="password" placeholder="Password" class="form-control form-control" type="password" required="">
                            </div>
                            <div class="custom-control custom-checkbox my-2">
                                <input name="remember" id="remember" type="checkbox" class="custom-control-input" />
                                <label class="custom-control-label" for="remember">Remember me</label>
                            </div>
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary btn-block">Login</button>
                            </div>
                            <div class="form-group text-center">
                                <small><a href="#">Forgot password?</a></small>
                            </div>
                        </form>
                    </li>
                </ul>
            </li>
            <li class="dropdown order-1">
                <button type="button" id="dropdownMenu1" data-toggle="dropdown" class="btn btn-primary dropdown-toggle">Register <span class="caret"></span></button>
                <ul class="dropdown-menu dropdown-menu-right mt-2">
                    <li class="px-3 py-2">
                        <form class="form" method="POST" action="/users/register">
                            <div class="form-group">
                                <input name="username" placeholder="Username" class="form-control form-control" type="text" required="">
                            </div>
                            <div class="form-group">
                                <input name="name" placeholder="Name" class="form-control form-control" type="text" required="">
                            </div>
                            <div class="form-group">
                                <input name="email" placeholder="Email" class="form-control form-control" type="text" required="">
                            </div>
                            <div class="form-group">
                                <input name="password" placeholder="Password" class="form-control form-control" type="password" required="">
                            </div>
                            <div class="form-group">
                                <input name="confirm-password" placeholder="Confirm password" class="form-control form-control" type="password" required="">
                            </div>
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary btn-block">Register</button>
                            </div>
                        </form>
                    </li>
                </ul>
            </li>
            </#if>
        </ul>
    </div>
</nav>