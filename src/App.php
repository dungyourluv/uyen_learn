<?php

namespace TodoApi;

use DunnServer\DunnServer;
use DunnServer\MVC\Controller;
use DunnServer\Router\Router;
use TodoApi\Controller\WelcomeCtl;
use TodoApi\Router\PostRouter;
use TodoApi\Router\UserRouter;

class App {
    static function main() {
        $server = new DunnServer();
        $server->addRoute("/", new WelcomeCtl());

        $apiRouter = new Router("/api/v1");
        $apiRouter->useRouter(new PostRouter());
        $apiRouter->useRouter(new UserRouter());
        $server->useRouter($apiRouter);

        $server->addRoute("/*", new class extends Controller {
            function doGet($req, $res)
            {
                $res->status(404)->send("Not Found");
            }

            function doPost($req, $res)
            {
                $res->status(404)->send("Not Found");
            }

            function doPut($req, $res)
            {
                $res->status(404)->send("Not Found");
            }

            function doDelete($req, $res)
            {
                $res->status(404)->send("Not Found");
            }
        });

        $server->run();
    }
}