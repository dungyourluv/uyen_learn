<?php 

namespace TodoApi\Router;

use DunnServer\Middlewares\HandleFileFilter;
use DunnServer\Router\Router;
use TodoApi\Controller\TaskCtl;
use TodoApi\Controller\UploadTaskCtl;
use TodoApi\Filter\AuthFilter;

class PostRouter extends Router {
    function __construct() {
        parent::__construct("/tasks");
        $this->addRoute("", new TaskCtl());
        $this->addRoute("/upload", new UploadTaskCtl());
        $this->addFilter("/upload", new AuthFilter(), new HandleFileFilter($_SERVER['DOCUMENT_ROOT']."/uploads", "/uploads"));
        $this->addFilter("", new AuthFilter());
    }
}