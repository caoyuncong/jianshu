<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button class="navbar-toggle" data-toggle="collapse" data-target="#nav">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="" class="navbar-brand">简书</a>
        </div>
        <div id="nav" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active" id="index"><a href="">首页</a></li>
                <li><a href="">下载 App</a></li>
            </ul>
            <form action="" class="navbar-form navbar-left" method="post">
                <div class="input-group">
                    <input type="text" class="form-control" name="keywords" placeholder="搜索">
                    <div class="input-group-btn">
                        <button class="btn btn-default">
                            <i class="glyphicon glyphicon-search"></i>
                        </button>
                    </div>
                </div>
            </form>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown">
                        Aa
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="set">
                            <img src="static/image/moon_fill.svg" alt="" width="15">夜间模式
                            <input type="checkbox" name="night-mode">
                        </li>
                        <li class="divider"></li>
                        <li></li>
                    </ul>
                </li>
                <li><a href="">登录</a></li>
                <li>
                    <button id="sign-up" class="btn btn-default navbar-btn">注册</button>
                </li>
                <li>
                    <button id="write-note" class="btn btn-danger navbar-btn"><i class="glyphicon glyphicon-pencil"></i>
                        写文章
                    </button>
                </li>
            </ul>
        </div>
    </div>
</nav>


