<?php 

namespace TodoApi\Controller;

use DunnServer\MVC\Controller;
use TodoApi\Model\UserModel;

class RegisterUserCtl extends Controller {
    function doPost($req, $res)
    {
        try {
            $user = UserModel::fromRequest($req);
            $user->register();
            $res->send($user->toResponse());
        } catch (\Throwable $th) {
            $res->status($th->getCode())->send($th);
        }
    }
}