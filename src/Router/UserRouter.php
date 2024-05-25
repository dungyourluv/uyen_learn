<?php 

namespace TodoApi\Router;

use DunnServer\Router\Router;
use TodoApi\Controller\AddUserCtl;
use TodoApi\Controller\LoginCtl;
use TodoApi\Controller\RegisterUserCtl;
use TodoApi\Filter\AuthFilter;
use TodoApi\Filter\IsAdminFilter;

class UserRouter extends Router {
    function __construct() {
        parent::__construct("/users");
        $this->addRoute("/register", new RegisterUserCtl());
        $this->addRoute("/login", new LoginCtl());
        $this->addRoute("/add", new AddUserCtl());

        $this->addFilter("/add", new AuthFilter(), new IsAdminFilter());
    }
}