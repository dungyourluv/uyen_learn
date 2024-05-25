<?php 

namespace TodoApi\Controller;

use DunnServer\MVC\Controller;
use DunnServer\Utils\DunnMap;

class WelcomeCtl extends Controller {
    function doGet($req, $res)
    {
        $map = new DunnMap();
        $map->set('message', 'Welcome to DunnServer');
        $map->set("desciription", "This is a simple API server built with DunnServer");
        $map->set("author", "Dunn");
        $res->send($map);
    }
}