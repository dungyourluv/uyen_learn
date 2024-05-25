<?php 

namespace TodoApi\Controller;

use DunnServer\MVC\Controller;
use TodoApi\Model\UserModel;

class AddUserCtl extends Controller {
    function doPost($req, $res)
    {
        $isAdmin = $req->getBody("isAdmin") ?? false;

        try {
            $user = UserModel::fromRequest($req);
            $user->isAdmin = $isAdmin;
            $user->register();
            $res->send($user->toResponse());
        } catch (\Throwable $th) {
            $res->status($th->getCode())->send($th);
        }
    }
}