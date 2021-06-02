<%@ page language="java" session="false" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="zh">

<head>
<tag:basePath pageContext="${pageContext}"></tag:basePath>
	<!--
    <script async src = "//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js" >
    </script>
    <script>
    (adsbygoogle = window.adsbygoogle || []).push({
        google_ad_client: "ca-pub-8550836177608334",
        enable_page_level_ads: true
    });
    </script>
	-->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="author" content="viggo" />
    <title>WebStack.cc - 程序员网址导航</title>
    <meta name="keywords" content="UI设计,UI设计素材,设计导航,网址导航,设计资源,创意导航,创意网站导航,程序员网址大全,设计素材大全,程序员导航,UI设计资源,优秀UI设计欣赏,程序员导航,程序员网址大全,程序员网址导航,产品经理网址导航,交互程序员网址导航,www.webstack.cc">
    <meta name="description" content="WebStack - 收集国内外优秀设计网站、UI设计资源网站、灵感创意网站、素材资源网站，定时更新分享优质产品设计书签。www.webstack.cc">
    <link rel="shortcut icon" href="../assets/images/favicon.png">
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Arimo:400,700,400italic">
    <link rel="stylesheet" href="../assets/css/fonts/linecons/css/linecons.css">
    <link rel="stylesheet" href="../assets/css/fonts/fontawesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="../assets/css/bootstrap.css">
    <link rel="stylesheet" href="../assets/css/xenon-core.css">
    <link rel="stylesheet" href="../assets/css/xenon-components.css">
    <link rel="stylesheet" href="../assets/css/xenon-skins.css">
    <link rel="stylesheet" href="../assets/css/nav.css">
    
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!-- / FB Open Graph -->
    <meta property="og:type" content="article">
    <meta property="og:url" content="http://www.webstack.cc/">
    <meta property="og:title" content="WebStack - 收集国内外优秀设计网站、UI设计资源网站、灵感创意网站、素材资源网站，定时更新分享优质产品设计书签。www.webstack.cc">
    <meta property="og:description" content="UI设计,UI设计素材,设计导航,网址导航,设计资源,创意导航,创意网站导航,程序员网址大全,设计素材大全,程序员导航,UI设计资源,优秀UI设计欣赏,程序员导航,程序员网址大全,程序员网址导航,产品经理网址导航,交互程序员网址导航,www.webstack.cc">
    <meta property="og:image" content="http://webstack.cc/assets/images/webstack_banner_cn.png">
    <meta property="og:site_name" content="WebStack - 收集国内外优秀设计网站、UI设计资源网站、灵感创意网站、素材资源网站，定时更新分享优质产品设计书签。www.webstack.cc">
    <!-- / Twitter Cards -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="WebStack - 收集国内外优秀设计网站、UI设计资源网站、灵感创意网站、素材资源网站，定时更新分享优质产品设计书签。www.webstack.cc">
    <meta name="twitter:description" content="UI设计,UI设计素材,设计导航,网址导航,设计资源,创意导航,创意网站导航,程序员网址大全,设计素材大全,程序员导航,UI设计资源,优秀UI设计欣赏,程序员导航,程序员网址大全,程序员网址导航,产品经理网址导航,交互程序员网址导航,www.webstack.cc">
    <meta name="twitter:image" content="http://www.webstack.cc/assets/images/webstack_banner_cn.png">
</head>

<body class="page-body">
    <!-- skin-white -->
    <div class="page-container">
        <div class="sidebar-menu toggle-others fixed">
            <div class="sidebar-menu-inner">
                <header class="logo-env">
                    <!-- logo -->
                    <div class="logo">
                        <a href="../../search/" class="logo-expanded">
                            <img src="../assets/images/logo@2x.png" width="100%" alt="" />
                        </a>
                        <a href="index.html" class="logo-collapsed">
                            <img src="../assets/images/logo-collapsed@2x.png" width="40" alt="" />
                        </a>
                    </div>
                    <div class="mobile-menu-toggle visible-xs">
                        <a href="#" data-toggle="user-info-menu">
                            <i class="linecons-cog"></i>
                        </a>
                        <a href="#" data-toggle="mobile-menu">
                            <i class="fa-bars"></i>
                        </a>
                    </div>
                </header>
                <ul id="main-menu" class="main-menu">
                    <li>
                        <a href="#">
                            <i class="glyphicon glyphicon-globe"></i>
                            <span class="title">常用站点</span>
                        </a>
                        <ul id="public_fav">
                            <li>
		                        <a href="#" class="smooth">
		                            <i class="glyphicon glyphicon-plus"></i> 
		                            <span class="title" data-toggle="modal" onclick="toEditFav();">新建收藏夹</span>
		                        </a>
		                    </li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">
                            <i class="linecons-star"></i>
                            <span class="title">我的收藏</span>
                        </a>
                        <ul id="my_fav">
                            <li>
		                        <a  class="smooth">
		                            <i class="glyphicon glyphicon-plus"></i> 
		                            <span class="title" data-toggle="modal" onclick="toEditFav();">新建收藏夹</span>
		                        </a>
		                    </li>
                        </ul>
                    </li>
                     <li>
                        <a href="#">
                            <i ></i>
                            <span class="title">更多</span>
                        </a>
                        <ul >
                            <li>
		                        <a href="#" class="smooth">
		                            <i class=""></i>
		                            <span class="title" id="webCollectTool">网页搜集工具</span>
		                        </a>
		                    </li>
                        </ul>
                    </li>
                    <div class="submit-tag">
                        <a href="about.html">
                            <i class="linecons-heart"></i>
                            <span class="tooltip-blue">关于本站</span>
                            <span class="label label-Primary pull-right hidden-collapsed">♥︎</span>
                        </a>
                    </div>
                </ul>
            </div>
        </div>
        <div class="main-content">
            <nav class="navbar user-info-navbar" role="navigation">
                <!-- User Info, Notifications and Menu Bar -->
                <!-- Left links for user info navbar -->
                <ul class="user-info-menu left-links list-inline list-unstyled">
                    <li class="hidden-sm hidden-xs">
                        <a href="#" data-toggle="sidebar">
                            <i class="fa-bars"></i>
                        </a>
                    </li>
                    <li class="dropdown hover-line language-switcher">
                        <a href="../cn/index.html" class="dropdown-toggle" data-toggle="dropdown">
                            <img src="../assets/images/flags/flag-cn.png" alt="flag-cn" /> Chinese
                        </a>
                        <ul class="dropdown-menu languages">
                            <li>
                                <a href="../en/index.html">
                                    <img src="../assets/images/flags/flag-us.png" alt="flag-us" /> English
                                </a>
                            </li>
                            <li class="active">
                                <a href="../cn/index.html">
                                    <img src="../assets/images/flags/flag-cn.png" alt="flag-cn" /> Chinese
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul> 
<!--                 <a href="https://github.com/WebStackPage/WebStackPage.github.io" target="_blank"><img style="position: absolute; top: 0; right: 0; border: 0;" src="https://s3.amazonaws.com/github/ribbons/forkme_right_darkblue_121621.png" alt="Fork me on GitHub"></a> -->
            </nav>
            
            <div id="favList"></div>
            
            <br />
            
            <!-- Main Footer -->
            <!-- Choose between footer styles: "footer-type-1" or "footer-type-2" -->
            <!-- Add class "sticky" to  always stick the footer to the end of page (if page contents is small) -->
            <!-- Or class "fixed" to  always fix the footer to the end of page -->
            <footer class="main-footer sticky footer-type-1">
                <div class="footer-inner">
                    <!-- Add your copyright text here -->
                    <div class="footer-text">
                        &copy; 2017
                        <a href="../cn/about.html"><strong>WebStack</strong></a> design by <a href="http://viggoz.com" target="_blank"><strong>Viggo</strong></a>
                        <!--  - Purchase for only <strong>23$</strong> -->
                    </div>
                    <!-- Go to Top Link, just add rel="go-top" to any link to add this functionality -->
                    <div class="go-up">
                        <a href="#" rel="go-top">
                            <i class="fa-angle-up"></i>
                        </a>
                    </div>
                </div>
            </footer>
        </div>
    </div>



 
<div id="htmlTpl">
</div>

<!-- <script src="../assets/js/jquery-1.11.1.min.js"></script> -->
<script type="text/javascript" src="/ResourceWeb/admin/Scripts/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/ResourceWeb/admin/Scripts/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/ResourceWeb/admin/Scripts/kkdUtil.js"></script>
<script type="text/javascript" src="/ResourceWeb/admin/Scripts/kkdUi.js"></script>
<script type="text/javascript" src="/ResourceWeb/admin/Scripts/common.js"></script>
<script type="text/javascript" src="./templete.js"></script>

    <!-- Bottom Scripts -->
    <script src="../assets/js/bootstrap.min.js"></script>
    <script src="../assets/js/TweenMax.min.js"></script>
    <script src="../assets/js/resizeable.js"></script>
    <script src="../assets/js/joinable.js"></script>
    <script src="../assets/js/xenon-api.js"></script>
    <script src="../assets/js/xenon-toggles.js"></script>
<!--     JavaScripts initializations and stuff -->
    <script src="../assets/js/xenon-custom.js"></script>
    <script src="./tpl.js"></script>
    <script src="./index.js"></script>
</body>

</html>
